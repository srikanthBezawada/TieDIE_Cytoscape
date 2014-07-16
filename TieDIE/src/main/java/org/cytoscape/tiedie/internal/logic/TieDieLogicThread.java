package org.cytoscape.tiedie.internal.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;
import org.cytoscape.view.model.CyNetworkView;


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
     
    public TieDieLogicThread(CyNetwork currentnetwork, CyNetworkView currentnetworkview) {
        this.currentnetwork = currentnetwork;
        this.currentnetworkview = currentnetworkview;
        this.nodeList = currentnetwork.getNodeList(); 
        this.totalnodecount = nodeList.size();
        this.edgeTable = currentnetwork.getDefaultEdgeTable();
        this.nodeTable = currentnetwork.getDefaultNodeTable();
    }

    @Override
    public void run(){
      
        Kernel heatDiffusionKernel = new Kernel(currentnetwork);
        double[][] diffusionKernel = heatDiffusionKernel.createRequiredExponential();
        
        /*
        Create upstreamheatVector, downstreamheatVector for 2-way diffusion
        "Extract them using extractHeatVector"
        */
        
        HeatVector upstreamheatVector = new HeatVector(totalnodecount);
        upstreamheatVector = upstreamheatVector.extractHeatVector("upstreamheat",nodeList,nodeTable);
        HeatVector downstreamheatVector = new HeatVector(totalnodecount);
        downstreamheatVector = downstreamheatVector.extractHeatVector("downstreamheat",nodeList,nodeTable);
        
        // Get the diffused heat vectors which spread all over the network
        DiffusedHeatVector upstreamheatVectorDiffused = new DiffusedHeatVector(totalnodecount);
        upstreamheatVectorDiffused =  upstreamheatVectorDiffused.extractDiffusedHeatVector(upstreamheatVector, heatDiffusionKernel);
        DiffusedHeatVector downstreamheatVectorDiffused = new DiffusedHeatVector(totalnodecount);
        downstreamheatVectorDiffused = downstreamheatVectorDiffused.extractDiffusedHeatVector(downstreamheatVector, heatDiffusionKernel);
        
        // Extract the maps with only inital sets and their diffused values
        Map upnodeScoreMapDiffused = getDiffusedMap("upstreamheat", upstreamheatVector.nodeHeatSet, upstreamheatVectorDiffused);
        Map downnodeScoreMapDiffused = getDiffusedMap("downstreamheat", downstreamheatVector.nodeHeatSet, downstreamheatVectorDiffused);
        
        sizeFactor = 1;
        linker_cutoff = TieDieUtil.findLinkerCutoff(nodeList,upstreamheatVector.getnodeHeatSet(), downstreamheatVector.getnodeHeatSet(), upnodeScoreMapDiffused, downnodeScoreMapDiffused, sizeFactor);
        // nodeList is the extra parameter to existing tiedie  https://github.com/epaull/TieDIE/blob/master/lib/tiedie_util.py#L336
        
        
      
        
    } 
    
    public Map getDiffusedMap(String columnName, Set<CyNode> nodeHeatSet ,DiffusedHeatVector diffusedVector ){
        Map sameSetScoreDiffused = new HashMap<CyNode, Double>();
        int count = 0;
        double diffusedScore;
        
        for (CyNode root : nodeList) { // nodeList is always accessed in a same order
            CyRow row = nodeTable.getRow(root.getSUID());
            if (row.get(columnName, Double.class) != null) {
                diffusedScore = diffusedVector.heatVectorOfScores.get(0, count);
                sameSetScoreDiffused.put(root, diffusedScore);
            }

            count++;
        }
        return sameSetScoreDiffused;
    } 
    
}
