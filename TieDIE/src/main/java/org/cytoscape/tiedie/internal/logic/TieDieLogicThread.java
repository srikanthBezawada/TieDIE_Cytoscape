package org.cytoscape.tiedie.internal.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
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
import org.cytoscape.model.subnetwork.CyRootNetwork;
import org.cytoscape.model.subnetwork.CySubNetwork;
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
        CyRootNetwork root = ((CySubNetwork)currentnetwork).getRootNetwork();
        List<CyNode> oldnodes = currentnetwork.getNodeList();
        List<CyNode> nodes = currentnetwork.getNodeList();
        List<CyEdge> edges = currentnetwork.getEdgeList();
        
        for(CyNode currentnode : oldnodes){
            if(upstreamheatVector.getnodeHeatSet().contains(currentnode)||downstreamheatVector.getnodeHeatSet().contains(currentnode)||filtered_linkersNodeScoreMap.containsKey(currentnode)){
                continue;
            }
            nodes.remove(currentnode);
            // Remove the nodes you want removed from the list
            // Adjust your edges
        }
        /*
        CyRootNetwork root = ((CySubNetwork)network).getRootNetwork();
        List<CyNode> nodes = network.getNodeList();
        List<CyEdge> edges = network.getEdgeList();
        // adjust nodes, edges
        CyNetwork newNetwork = root.addSubNetwork(nodes, edges);
        */
        
        subnodeCount = (upstreamheatVector.getnodeCount()) + (downstreamheatVector.getnodeCount())+ (MapUtil.count(filtered_linkersNodeScoreMap));
        if(subnodeCount == nodes.size()){
            System.out.println("nodes.size()"+nodes.size());
            int x =  subnodeCount;
            System.out.println("calculations are correct, subnodecount"+ x);
        }
        
        CyNetwork TieDIEsubNetwork = root.addSubNetwork(nodes, edges);
        TieDIEsubNetwork.getRow(TieDIEsubNetwork).set(CyNetwork.NAME, "TieDIE subnetwork");
        
        //CyNetworkView tiedieView = CyActivator.networkViewFactory.createNetworkView(TieDIEsubNetwork);
        //CyActivator.networkViewManager.addNetworkView(tiedieView);
        
        //CyNetworkView myView = CyActivator.networkViewFactory.createNetworkView(TieDIEsubNetwork);
        //CyActivator.networkViewManager.addNetworkView(myView);
     
       
        
    }
    
    
    public Map getDiffusedMap(String columnName, DiffusedHeatVector diffusedVector ){
        Map sameSetScoreDiffused = new HashMap<CyNode, Double>();
        int count = 0;
        Number diffusedScore = null;
        
        for (CyNode root : nodeList) { // nodeList is always accessed in a same order
            /*CyRow row = nodeTable.getRow(root.getSUID());
            if (row.get(columnName, Double.class) != null) {
                diffusedScore = diffusedVector.getVectorOfScores().get(0, count);
                sameSetScoreDiffused.put(root, diffusedScore);
            }*/
            
            CyRow row = nodeTable.getRow(root.getSUID());
            if (nodeTable.getColumn(columnName).getType() == Double.class) {
                diffusedScore = row.get(columnName, Double.class);
            }
            else if (nodeTable.getColumn(columnName).getType() == Integer.class) {
                diffusedScore = row.get(columnName, Integer.class);
            } 
            else if (nodeTable.getColumn(columnName).getType() == Long.class) {
                diffusedScore = row.get(columnName, Long.class);
            }
            else if (nodeTable.getColumn(columnName).getType() == String.class){
                try{
                    diffusedScore = Double.parseDouble(row.get(columnName, String.class));
                    
                }
                catch(NumberFormatException e){
                    System.out.println("Column with name "+columnName+" has unsupported format. Should contain only numbers");
                }
                catch(NullPointerException e){
                     System.out.println("String is null");
                }
                
            }
            if(diffusedScore == null)
            continue;
            
            diffusedScore = diffusedVector.getVectorOfScores().get(0, count);
            sameSetScoreDiffused.put(root, diffusedScore);
    
            count++;
        }
        return sameSetScoreDiffused;
        // This map contains only <nodes, heatscore> corresponding to that columnName only
    } 
    
}
