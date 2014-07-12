package org.cytoscape.tiedie.internal.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cytoscape.model.CyNode;

/**
 *
 * @author SrikanthB
 */


public class TieDieUtil {

    public TieDieUtil(){
    
    }
    
    public static double findLinkerCutoff(List<CyNode> nodeList, Set<CyNode> upstreamnodeheatSet, Set<CyNode> downstreamnodeheatSet,HeatVector upstreamheatVectorDiffused,HeatVector downstreamheatVectorDiffused, double sizeFactor){
        double linker_cutoff;
        if(downstreamnodeheatSet == null){
            linker_cutoff = findLinkerCutoffSingle(nodeList, upstreamnodeheatSet, upstreamheatVectorDiffused, sizeFactor);
        }
           
        else {
            linker_cutoff = findLinkerCutoffMulti(nodeList, upstreamnodeheatSet, downstreamnodeheatSet, upstreamheatVectorDiffused, downstreamheatVectorDiffused, sizeFactor);
        }
         
        return linker_cutoff;
    }
    
    
    
    
    
    public static double findLinkerCutoffSingle(List<CyNode> nodeList, Set<CyNode> upstreamnodeheatSet, HeatVector upstreamheatVectorDiffused, double sizeFactor) {
        double linker_cutoff=0;
        double target_size;
        double EPSILON = 0.0001;
        
        target_size = (sizeFactor)*(upstreamnodeheatSet.size());
        Map nodeDiffusedScoreMap, nodeDiffusedScoreMapSorted;
        Set diffusedNodeSet;
        nodeDiffusedScoreMap = Kernel.getnodeDiffusedScoreMap(upstreamheatVectorDiffused, nodeList);
        
        nodeDiffusedScoreMapSorted = MapUtil.sortByValue(nodeDiffusedScoreMap);
        
        /*
        Iterate over the sorted map(cuurentnode, currentvalue)
        {
            cutoff = currentvalue+Epsilon;
            add the 1st element of the map to the set DiffusedNodeSet; 
            if(size(diffusedNodeSet)-size(NodeSet)) > target_size
            break;
            
        }
        return cutoff;
        */
        
        
        return linker_cutoff;
    }
    
    
    
    public static double findLinkerCutoffMulti(List<CyNode> nodeList, Set<CyNode> upstreamnodeheatSet, Set<CyNode> downstreamnodeheatSet, HeatVector upstreamheatVectorDiffused, HeatVector downstreamheatVectorDiffused, double sizeFactor) {
        double linker_cutoff=0;
        
        
        
        
        
        
        return linker_cutoff;
    }

    
    
   
    public static List<CyNode> filterLinkers(HeatVector upstreamheatVectorDiffused, HeatVector downstreamheatVectorDiffused, double linker_cutoff){
        List<CyNode> filteredNodeList = null;
    
    
        return filteredNodeList;
    }
    
    
    
    
   
    
    
      
}
