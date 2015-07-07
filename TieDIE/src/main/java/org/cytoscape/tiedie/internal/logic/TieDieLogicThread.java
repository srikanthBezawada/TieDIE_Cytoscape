package org.cytoscape.tiedie.internal.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;
import org.cytoscape.model.subnetwork.CyRootNetwork;
import org.cytoscape.model.subnetwork.CySubNetwork;
import org.cytoscape.view.model.CyNetworkView;

import org.cytoscape.tiedie.internal.CyActivator;
import static org.cytoscape.tiedie.internal.visuals.UpdateSubNetView.updateView;




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
        nodeList = new LinkedList();
        this.nodeList = this.currentnetwork.getNodeList(); 
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
        upnodeScoreMapDiffused = getDiffusedMap(upstreamheatVectorDiffused);
        downnodeScoreMapDiffused = getDiffusedMap(downstreamheatVectorDiffused);
        
        extractSubnetwork();
        
        
      
        
    } 
    
    public void extractSubnetwork(){
        sizeFactor = 1;
        linker_cutoff = TieDieUtil.findLinkerCutoff(upstreamheatVector.getnodeHeatSet(), downstreamheatVector.getnodeHeatSet(), upnodeScoreMapDiffused, downnodeScoreMapDiffused, sizeFactor);
        System.out.println("linker_cutoff"+linker_cutoff);
        // nodeList is the extra parameter to existing tiedie  https://github.com/epaull/TieDIE/blob/master/lib/tiedie_util.py#L336
        
        linkers_nodeScoreMap = TieDieUtil.findLinkersMap(upnodeScoreMapDiffused, downnodeScoreMapDiffused);
        // calling "z" function here according to literature
        filtered_linkersNodeScoreMap = TieDieUtil.findFilteredLinkersMap(linkers_nodeScoreMap, linker_cutoff);
        createExtractedSubnetwork();
    }
    public void createExtractedSubnetwork(){
        Set<CyNode> linkerNodes = filtered_linkersNodeScoreMap.keySet();
        List<CyNode> nodes = currentnetwork.getNodeList();
        List<CyNode> newnodes = new ArrayList<CyNode>();
        List<CyEdge> edges = currentnetwork.getEdgeList();
        List<CyEdge> newedges = new ArrayList<CyEdge>();
        
        for(Object currentnode : upstreamheatVector.getnodeHeatSet()){
            newnodes.add((CyNode) currentnode);
        }
        for(Object currentnode : downstreamheatVector.getnodeHeatSet()){
            newnodes.add((CyNode) currentnode);
        }
        for(Object currentnode : linkerNodes){
            newnodes.add((CyNode) currentnode);
        }
        
        for(CyEdge currentedge : edges){
            CyNode node1 = currentedge.getSource();
            CyNode node2 = currentedge.getTarget();
            if(newnodes.contains(node1) && newnodes.contains(node2)){
                newedges.add(currentedge);
            }
        }
        System.out.println("nodes size"+newnodes.size());
        System.out.println("edges size"+newedges.size());
        //Set<CyNode> s = new HashSet<CyNode>(newnodes);
        //System.out.println("nodesetsize"+s.size());
                
        CyRootNetwork root = ((CySubNetwork)currentnetwork).getRootNetwork();
        CyNetwork TieDIEsubNetwork = root.addSubNetwork(newnodes, newedges);
        TieDIEsubNetwork.getRow(TieDIEsubNetwork).set(CyNetwork.NAME, "TieDIE subnetwork");
        CyNetworkManager networkManager = CyActivator.networkManager;
        networkManager.addNetwork(TieDIEsubNetwork);
        CyNetworkView tiedieView = CyActivator.networkViewFactory.createNetworkView(TieDIEsubNetwork);
        CyActivator.networkViewManager.addNetworkView(tiedieView);
        updateView(tiedieView, "grid");
    }
    
   

    
    
    public Map getDiffusedMap(DiffusedHeatVector diffusedVector){
        Map diffScoreMap = new HashMap<CyNode, Double>();
        int count=0;
        double diffScr;
        for(CyNode root: nodeList){
            diffScr = diffusedVector.getVectorOfScores().get(0, count);
            diffScoreMap.put(root, diffScr);
            count++; 
        }
        return diffScoreMap;
    }
    
    
}
