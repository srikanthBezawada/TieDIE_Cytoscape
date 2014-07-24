package org.cytoscape.tiedie.internal.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;

/*
   @author SrikanthB

   Encapsulates the permutation logic for an input heat set. Permutes
   Node scores with other nodes of similar network degree by sorting
   all nodes by degree, and binning them into blocks of a set size. 
   Permutations are done only within blocks, so that the degree distribution
   of input nodes is preserved  

*/

public class Permuter {
    Map nodeDegreeMap, nodeDegreeMapSorted;
    CyNetwork currentnetwork;
    List<CyNode> nodeList;
    private static final int BLOCK_SIZE = 10;
    
    Permuter(CyNetwork network){
        this.currentnetwork = network;
        this.nodeList = currentnetwork.getNodeList();
        nodeDegreeMap = new HashMap<CyNode, Integer>();
        for(CyNode currentNode: nodeList){
            List<CyNode> myNeighbourList = currentnetwork.getNeighborList(currentNode, CyEdge.Type.ANY);
            int currentNodeDegree = myNeighbourList.size();
            nodeDegreeMap.put(currentNode, currentNodeDegree);
            nodeDegreeMapSorted = MapUtil.sortByValue(nodeDegreeMap);
        }
       
    }


    public Map permuteBlock(){
        return null;
    
    
    }

    public void permuteOne(){
    
    
    }
    
    public void permute(){
    
    
    
    }
    
}