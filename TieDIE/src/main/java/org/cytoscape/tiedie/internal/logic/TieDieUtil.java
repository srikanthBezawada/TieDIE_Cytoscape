package org.cytoscape.tiedie.internal.logic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.cytoscape.model.CyNode;


/**
 *
 * @author SrikanthB
 * "TieDieUtil.java"  will have all utility functions used by "TieDieLogicThread.java"
 */


public class TieDieUtil {
      //upstreamnodesets, downstreamnodesets, diffusedupstream1, diffuseddownstream1, sizefactor=1
      public static double findLinkerCutoff(Set<CyNode> upstreamnodeheatSet, Set<CyNode> downstreamnodeSet, Map upnodeScoreMapDiffused, Map downnodeScoreMapDiffused, double sizeFactor){
          double linker_cutoff;
          
          if(downnodeScoreMapDiffused == null) // hotnet algorithm
          {
              linker_cutoff = findLinkerCutoffSingle(upstreamnodeheatSet, upnodeScoreMapDiffused, sizeFactor);
          }
          else
          {
              linker_cutoff = findLinkerCutoffMulti(upstreamnodeheatSet, downstreamnodeSet, upnodeScoreMapDiffused, downnodeScoreMapDiffused, sizeFactor);
          }
          
          return linker_cutoff;
      } 
    
    
      public static double findLinkerCutoffSingle(Set<CyNode> upstreamnodeSet, Map upnodeScoreMapDiffused, double sizeFactor) {
          double linker_cutoff=0.0;
          double targetSize;
          double EPSILON = 0.0001;
          Map nodeDiffusedScoreMapSorted;
          Set<CyNode> diffusedSet;
      
          targetSize = (sizeFactor)*(upstreamnodeSet.size());
          nodeDiffusedScoreMapSorted = MapUtil.sortByValue(upnodeScoreMapDiffused);
          diffusedSet = new HashSet<CyNode>();
          
          Iterator<Map.Entry<CyNode, Double>> iterator = nodeDiffusedScoreMapSorted.entrySet().iterator() ;
          while(iterator.hasNext()){
              Entry<CyNode, Double> entry = iterator.next();
              linker_cutoff = entry.getValue()+EPSILON ;
              diffusedSet.add(entry.getKey());
              if(difference(diffusedSet, upstreamnodeSet).size() > targetSize)
              break;
          }
          return linker_cutoff;
   
      }
    
    
       public static double findLinkerCutoffMulti(Set<CyNode> upstreamnodeSet, Set<CyNode> downstreamnodeSet, Map upScoreMapDiffused, Map downScoreMapDiffused, double sizeFactor){
           if(sizeFactor == 0){
               return 1000000;
           }
           double linker_cutoff=0;
           double EPSILON = 0.0001;
           double h, size_frac;
           Map upScoreMapDiffusedSorted,downScoreMapDiffusedSorted;
           //First linkers are found and then are filtered linkers
           Map linkers_nodeScoreMapSorted,linkers_nodeScoreMap,filtered_linkersNodeScoreMapSorted,filtered_linkersNodeScoreMap;
           upScoreMapDiffusedSorted = MapUtil.sortByValue(upScoreMapDiffused);
           downScoreMapDiffusedSorted = MapUtil.sortByValue(downScoreMapDiffused);
           
           linkers_nodeScoreMap = findLinkersMap(upScoreMapDiffused, downScoreMapDiffused);
           filtered_linkersNodeScoreMap = findFilteredLinkersMap(linkers_nodeScoreMap, 1);// cutoff = 1
           
           linkers_nodeScoreMapSorted = MapUtil.sortByValue(linkers_nodeScoreMap);
           filtered_linkersNodeScoreMapSorted = MapUtil.sortByValue(filtered_linkersNodeScoreMap);
           
           Iterator<Map.Entry<CyNode, Double>> iterator = linkers_nodeScoreMapSorted.entrySet().iterator() ;
           while(iterator.hasNext()){
               Entry<CyNode, Double> entry = iterator.next();
               h = entry.getValue();
               linker_cutoff = h-EPSILON;
               size_frac = scoreLinkers(upScoreMapDiffused,upScoreMapDiffusedSorted,downScoreMapDiffused,downScoreMapDiffusedSorted,upstreamnodeSet,downstreamnodeSet, linker_cutoff , sizeFactor);
               if(size_frac > 1){ // reached the desired size
                   return linker_cutoff;
               }
           }
           return linker_cutoff;
           
       }
       
  
       public static double scoreLinkers(Map upScoreMapDiffused,Map upScoreMapDiffusedSorted,Map downScoreMapDiffused,Map downScoreMapDiffusedSorted,Set<CyNode> upstreamnodeSet,Set<CyNode> downstreamnodeSet,double linker_cutoff, double  sizeFactor){
           Set<CyNode> f1 = null, f2 = null;
           f1 = new HashSet<CyNode>();
           f2 = new HashSet<CyNode>();
           double score, size_frac; //what to do with score ?
           
           Iterator<Map.Entry<CyNode, Double>> iterator1 = upScoreMapDiffusedSorted.entrySet().iterator() ;
           while(iterator1.hasNext()){
               Entry<CyNode, Double> entry = iterator1.next();  
               if(entry.getValue() < linker_cutoff)
               break;
               f1.add(entry.getKey());  
           }
       
           Iterator<Map.Entry<CyNode, Double>> iterator2 = downScoreMapDiffusedSorted.entrySet().iterator() ;
           while(iterator2.hasNext()){
               Entry<CyNode, Double> entry = iterator2.next();  
               if(entry.getValue() < linker_cutoff)
               break;
               f2.add(entry.getKey());  
           }
           
           Set<CyNode> union = union(f1, f2);
           Set<CyNode> intersection = intersection(f1, f2);
           Set<CyNode> temp = difference(intersection, upstreamnodeSet);
           Set<CyNode> connecting = difference(temp, downstreamnodeSet);
           
           score = connecting.size()/(float)union.size();
           size_frac = (connecting.size())/(float)((union(upstreamnodeSet, downstreamnodeSet)).size())/(float)sizeFactor;
                   
           return size_frac;
       }
       
       
       /*
          "Z function" is used to combine score vectors for two input sets according to literature
          "filterLinkers" is the "Z function" of TieDIE python implementation & is done in 2 functions here
            1. findLinkerMap returns all linker nodes,scores
            2. findFilteredMap returns all linker nodes whose scores > cutoff
       */
       
       public static Map findLinkersMap(Map upnodeScoreMapDiffused, Map downnodeScoreMapDiffused){
           Map<CyNode, Double> linkers_nodeScoreMap;
           double min_heat,x,y;
           
           linkers_nodeScoreMap = new HashMap<CyNode,Double>();
           if(downnodeScoreMapDiffused == null){
               return upnodeScoreMapDiffused;
           }
           Set<CyNode> diffUpset = upnodeScoreMapDiffused.keySet();
           //Set<CyNode> diffDownset = upnodeScoreMapDiffused.keySet();
           
           for(CyNode node : diffUpset){
               if(downnodeScoreMapDiffused.containsKey(node)){
               // node makes the cut
                   x = (Double)upnodeScoreMapDiffused.get(node);
                   y = (Double)downnodeScoreMapDiffused.get(node);
                   min_heat = Math.min(x,y);
                   linkers_nodeScoreMap.put(node, min_heat);
               }
           }
      
           return linkers_nodeScoreMap;
       }
       
       public static Map findFilteredLinkersMap(Map linkers_nodeScoreMap, double cutoff){// see why not linker_
           Map<CyNode, Double> filtered_linkersNodeScoreMap = new HashMap<CyNode, Double>();
           Iterator<Map.Entry<CyNode, Double>> iterator = linkers_nodeScoreMap.entrySet().iterator() ;
        
           while(iterator.hasNext()){
               Entry<CyNode, Double> entry = iterator.next();
               if(entry.getValue() > cutoff){
                   filtered_linkersNodeScoreMap.put(entry.getKey(), entry.getValue());
               }
           }
           return filtered_linkersNodeScoreMap;
       }
  
        public static <T> Set<T> difference(Set<T> setA, Set<T> setB) {
            Set<T> tmp = new HashSet<T>(setA);
            tmp.removeAll(setB);
            return tmp;
        }
        public static <T> Set<T> union(Set<T> setA, Set<T> setB) {
            Set<T> tmp = new HashSet<T>(setA);
            tmp.addAll(setB);
            return tmp;
        }

        public static <T> Set<T> intersection(Set<T> setA, Set<T> setB) {
            Set<T> tmp = new HashSet<T>();
            for (T x : setA)
            if (setB.contains(x))
                tmp.add(x);
            return tmp;    
        }
       
       
}
