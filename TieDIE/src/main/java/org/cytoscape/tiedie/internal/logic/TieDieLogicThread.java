package org.cytoscape.tiedie.internal.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.cytoscape.model.CyEdge;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;
import org.cytoscape.view.model.CyNetworkView;

import org.cytoscape.tiedie.internal.CyActivator;


/**
 * @author SrikanthB
  
  TieDieLogicThread.java  keeps the ball rolling according to our execution flow and 
  this will be called from start button of GUI. 
 */


public class TieDieLogicThread extends Thread {
    
    /*
        About instance variables :
    
    */
    double sizeFactor, linker_cutoff;
    
    public CyNetwork currentnetwork;
    public CyNetworkView currentnetworkview;
    public int totalnodecount;
    List<CyNode> nodeList;
    CyTable nodeTable, edgeTable;
    
    private double[][] adjacencyMatrixOfNetwork;
    Kernel heatDiffusionKernel;
    HeatVector upstreamheatVector, downstreamheatVector;
    DiffusedHeatVector upstreamheatVectorDiffused, downstreamheatVectorDiffused;
    Map upnodeScoreMapDiffused, downnodeScoreMapDiffused;
    Map linkers_nodeScoreMap, filtered_linkersNodeScoreMap;
    
    public TieDieLogicThread(CyNetwork currentnetwork, CyNetworkView currentnetworkview) {
        this.currentnetwork = currentnetwork;
        this.currentnetworkview = currentnetworkview;
        this.nodeList = currentnetwork.getNodeList(); 
        this.totalnodecount = nodeList.size();
        this.edgeTable = currentnetwork.getDefaultEdgeTable();
        this.nodeTable = currentnetwork.getDefaultNodeTable();
        
        upstreamheatVector = new HeatVector(totalnodecount);
        downstreamheatVector = new HeatVector(totalnodecount);
        upstreamheatVectorDiffused = new DiffusedHeatVector(totalnodecount);
        downstreamheatVectorDiffused = new DiffusedHeatVector(totalnodecount);
    }

    @Override
    public void run(){
      
        heatDiffusionKernel = new Kernel(currentnetwork);
        
        /*
        Create upstreamheatVector, downstreamheatVector for 2-way diffusion
        "Extract them using extractHeatVector"
        */
        upstreamheatVector = upstreamheatVector.extractHeatVector("upstreamheat",nodeList,nodeTable);
        downstreamheatVector = downstreamheatVector.extractHeatVector("downstreamheat",nodeList,nodeTable);
        // Get the diffused heat vectors which spread all over the network
        upstreamheatVectorDiffused =  upstreamheatVectorDiffused.extractDiffusedHeatVector(upstreamheatVector, heatDiffusionKernel);
        downstreamheatVectorDiffused = downstreamheatVectorDiffused.extractDiffusedHeatVector(downstreamheatVector, heatDiffusionKernel);
        
        // Extracting the maps with only inital sets and their diffused values below
        upnodeScoreMapDiffused = getDiffusedMap("upstreamheat", upstreamheatVectorDiffused);
        downnodeScoreMapDiffused = getDiffusedMap("downstreamheat", downstreamheatVectorDiffused);
        
        extractSubnetwork();
        
        
      
        
    } 
    
    public void extractSubnetwork(){
        sizeFactor = 1;
        linker_cutoff = TieDieUtil.findLinkerCutoff(nodeList,upstreamheatVector.getnodeHeatSet(), downstreamheatVector.getnodeHeatSet(), upnodeScoreMapDiffused, downnodeScoreMapDiffused, sizeFactor);
        // nodeList is the extra parameter to existing tiedie  https://github.com/epaull/TieDIE/blob/master/lib/tiedie_util.py#L336
        
        linkers_nodeScoreMap = TieDieUtil.findLinkersMap(nodeList, upstreamheatVector.getnodeHeatSet(), downstreamheatVector.getnodeHeatSet(), upnodeScoreMapDiffused, downnodeScoreMapDiffused);
        // calling "z" function here according to literature
        filtered_linkersNodeScoreMap = TieDieUtil.findFilteredLinkersMap(linkers_nodeScoreMap, linker_cutoff);
        createExtractedSubnetwork();
    }
    
    public void createExtractedSubnetwork(){
        int subnodeCount;
        CyNetwork TieDIEsubNetwork;
        CyNetworkFactory networkFactory;
        List<CyNode> newnodeList;
        double [][] adjacencyMatrixOfNewNetwork;
        
        networkFactory = CyActivator.networkFactory;  // To get a reference of CyNetworkFactory at CyActivator class of the App
        TieDIEsubNetwork = networkFactory.createNetwork(); //Network factory creates new network in control panel
        
        TieDIEsubNetwork.getRow(TieDIEsubNetwork).set(CyNetwork.NAME, "TieDIE subnetwork");    // Set name for network
        
        
        newnodeList = new ArrayList<CyNode>(totalnodecount);
        for (int i = 0; i < nodeList.size(); i++) {
            newnodeList.add(TieDIEsubNetwork.addNode()); // Add all the nodes
        }
        // Set name for new nodes
        for (int i = 0; i < nodeList.size(); i++) {
            TieDIEsubNetwork.getRow(newnodeList.get(i)).set(CyNetwork.NAME, nodeTable.getRow(nodeList.get(i).getSUID()).get(CyNetwork.NAME, String.class));
        }
             //add edges
        for (int i = 0; i < totalnodecount; i++) {
            for (int j = i + 1; j < totalnodecount; j++) {
                double maxi = Math.max(adjacencyMatrixOfNetwork[i][j], adjacencyMatrixOfNetwork[j][i]);
                if (maxi > 0.0) {
                    CyEdge root = TieDIEsubNetwork.addEdge(newnodeList.get(i), newnodeList.get(j), true);
                    CyRow row = TieDIEsubNetwork.getDefaultEdgeTable().getRow(root.getSUID());
                    row.set(CyEdge.INTERACTION, "" + maxi);
                }
            }
        }
          
        adjacencyMatrixOfNewNetwork = new double[totalnodecount][totalnodecount];
        for (int i = 0; i < totalnodecount; i++) {
            for (int j = 0; j < totalnodecount; j++) {
                adjacencyMatrixOfNewNetwork[i][j] = adjacencyMatrixOfNetwork[i][j];
            }
        }
 
        subnodeCount = (upstreamheatVector.getnodeCount()) + (upstreamheatVector.getnodeCount())+ (MapUtil.count(filtered_linkersNodeScoreMap));
        // Add the network to Cytoscape
        CyNetworkManager networkManager = CyActivator.networkManager;
        networkManager.addNetwork(TieDIEsubNetwork);
     
        //Add view to cyto
        //CyNetworkView myView = CyActivator.networkViewFactory.createNetworkView(SpanningTree);
        //CyActivator.networkViewManager.addNetworkView(myView);
        
        
        
        
    }
    
    
    public Map getDiffusedMap(String columnName,DiffusedHeatVector diffusedVector ){
        Map sameSetScoreDiffused = new HashMap<CyNode, Double>();
        int count = 0;
        double diffusedScore;
        
        for (CyNode root : nodeList) { // nodeList is always accessed in a same order
            CyRow row = nodeTable.getRow(root.getSUID());
            if (row.get(columnName, Double.class) != null) {
                diffusedScore = diffusedVector.getVectorOfScores().get(0, count);
                sameSetScoreDiffused.put(root, diffusedScore);
            }

            count++;
        }
        return sameSetScoreDiffused;
        // This map contains only <nodes, heatscore> corresponding to that columnName only
    } 
    
}
