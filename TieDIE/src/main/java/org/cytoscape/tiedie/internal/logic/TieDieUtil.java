package org.cytoscape.tiedie.internal.logic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.cytoscape.model.CyNode;

import static org.cytoscape.tiedie.internal.logic.Kernel.getnodeDiffusedScoreMap;

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
        Set<CyNode> diffused_node_set;
        
        nodeDiffusedScoreMap = Kernel.getnodeDiffusedScoreMap(upstreamheatVectorDiffused, nodeList);
        nodeDiffusedScoreMapSorted = MapUtil.sortByValue(nodeDiffusedScoreMap);
        
        Iterator<Map.Entry<CyNode, Double>> iterator = nodeDiffusedScoreMapSorted.entrySet().iterator() ;
        diffused_node_set = new HashSet<CyNode>();

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
        double EPSILON = 0.0001;
        
        Set<CyNode> upstreamdiffused_node_set , downstreamdiffused_node_set, filteredNodeSet;// get this filtered node set
        Map filteredNodeScoreMap;
        Map upstreamnodeDiffusedScoreMap,downstreamnodeDiffusedScoreMap;
        Map upstreamnodeDiffusedScoreMapSorted, downstreamnodeDiffusedScoreMapSorted;
        
        upstreamnodeDiffusedScoreMap = Kernel.getnodeDiffusedScoreMap(upstreamheatVectorDiffused, nodeList);
        downstreamnodeDiffusedScoreMap = Kernel.getnodeDiffusedScoreMap(downstreamheatVectorDiffused, nodeList);
        upstreamnodeDiffusedScoreMapSorted = MapUtil.sortByValue(upstreamnodeDiffusedScoreMap);
        downstreamnodeDiffusedScoreMapSorted = MapUtil.sortByValue(downstreamnodeDiffusedScoreMap);
        
        filteredNodeScoreMap = findLinkersMap(nodeList, upstreamheatVectorDiffused, downstreamheatVectorDiffused ,1);
        
        
        return linker_cutoff;
    }

    
    
   
    public static Map findLinkersMap(List<CyNode> nodeList, HeatVector upstreamheatVectorDiffused, HeatVector downstreamheatVectorDiffused, double linker_cutoff){
        // This is the z function according to literature
        Map filteredNodeScoreMap;
        double min_heat,x,y;
        
        if(downstreamheatVectorDiffused == null){
            filteredNodeScoreMap = getnodeDiffusedScoreMap(upstreamheatVectorDiffused, nodeList);
            return filteredNodeScoreMap;
        }
        filteredNodeScoreMap = new HashMap<CyNode,Double>();
        for(CyNode root : upstreamheatVectorDiffused.nodeHeatSet)
        {
            if(downstreamheatVectorDiffused.nodeHeatSet.contains(root)==false)
            continue;
            x = (Double)getnodeDiffusedScoreMap(upstreamheatVectorDiffused, nodeList).get(root);
            y = (Double)getnodeDiffusedScoreMap(downstreamheatVectorDiffused, nodeList).get(root);
            min_heat = Math.min(x,y);
            filteredNodeScoreMap.put(root,min_heat);
         
        
        }
        return filteredNodeScoreMap;
    }
    
   
    public static Map findFilteredLinkersMap(){
        return null;
    
    }
    
    
   
    
    
    
    
    
    
    
    
    
    
}
