package org.cytoscape.tiedie.internal.tests;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.tiedie.internal.logic.MapUtil;

/*
   @author SrikanthB

   Encapsulates the permutation logic for an input heat set. Permutes
   Node scores with other nodes of similar network degree by sorting
   all nodes by degree, and binning them into blocks of a set size. 
   Permutations are done only within blocks, so that the degree distribution
   of input nodes is preserved  

*/

public class Permuter {
    Map nodeDegreeMap, nodeDegreeMapSorted, permutedMap;
    CyNetwork currentnetwork;
    List<CyNode> nodeList;
    int totalnodecount;
    private static int BLOCK_SIZE = 10;
    
    Permuter(CyNetwork network){
        this.currentnetwork = network;
        this.nodeList = currentnetwork.getNodeList();
        this.totalnodecount = nodeList.size();
        nodeDegreeMap = new HashMap<CyNode, Integer>();
        for(CyNode currentNode: nodeList){
            List<CyNode> myNeighbourList = currentnetwork.getNeighborList(currentNode, CyEdge.Type.ANY);
            int currentNodeDegree = myNeighbourList.size();
            nodeDegreeMap.put(currentNode, currentNodeDegree);
            nodeDegreeMapSorted = MapUtil.sortByValue(nodeDegreeMap);
        }
        
    }
    
    
    public void permuteOne(){
        int numOfBlocks;
        int i=0;
        
        if(totalnodecount%BLOCK_SIZE == 0){
            numOfBlocks = totalnodecount/BLOCK_SIZE;
        }
        else{
            numOfBlocks = (totalnodecount/BLOCK_SIZE)+1 ;
        }
        Block[] blockArray = new Block[numOfBlocks];
  
        Iterator<Map.Entry<CyNode, Integer>> iterator = nodeDegreeMapSorted.entrySet().iterator() ;
        while(iterator.hasNext()){
            Map.Entry<CyNode, Integer> entry = iterator.next();
            blockArray[i].add(entry.getKey());
            if(blockArray[i].getSize() == BLOCK_SIZE){
                blockArray[i].permuteBlock();
                
                
                //validate(blockArray[i]);
                i++;
            }
            
        }
    
    }
    
    public void permute(){
    
    
    
    }
    
    /*
    public void validate(Block b){
        for(int i=0; i<b.getSize(); i++){
            if(b.nodesInThisBlock.get(i)) {
            } else {
            }}
    }
    */
   
    
    
}