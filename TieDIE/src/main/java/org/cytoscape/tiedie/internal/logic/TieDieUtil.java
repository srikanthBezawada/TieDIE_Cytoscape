package org.cytoscape.tiedie.internal.logic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
        
        Iterator<Map.Entry<CyNode, Double>> iterator = nodeDiffusedScoreMapSorted.entrySet().iterator() ;
        Set<CyNode> diffused_node_set = new HashSet<CyNode>();

        while(iterator.hasNext()){
            Entry<CyNode, Double> entry = iterator.next();
            linker_cutoff = entry.getValue()+EPSILON ;
            diffused_node_set.add(entry.getKey());
            if((upstreamnodeheatSet.size())-(diffused_node_set.size()) > target_size)
            break;
        }
        
        return linker_cutoff;
        
        /**
        Set<Map.Entry<CyNode, Double>> entrySet = nodeDiffusedScoreMapSorted.entrySet();
        Set<CyNode> diffused_node_set = new HashSet<CyNode>();
             
        for (Entry entry : entrySet) {
            linker_cutoff = (Double)entry.getValue()+EPSILON ;
            diffused_node_set.add((CyNode)entry.getKey());
            if((upstreamnodeheatSet.size())-(diffused_node_set.size()) > target_size)
            break;
        } 
        return linker_cutoff;
        */
        
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
