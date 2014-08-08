package org.cytoscape.tiedie.internal.tests;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.cytoscape.model.CyNode;

/**
 *
 * @author SrikanthB
 */

public class Block {
    LinkedList<CyNode> nodesInThisBlock; 
    List<CyNode> shuffledList;
    Map<CyNode, CyNode> permutedMap;
    
    private int size;
    
    public Block(){
    }
    
    public int getSize(){
        return size;
    }
    
    public void add(CyNode node){
        nodesInThisBlock.add(node);
        shuffledList.add(node);
        size++;
    }
    
    public void permuteBlock(){
        int i;
        shuffle();
        
        for(i=0; i<nodesInThisBlock.size(); i++){
            permutedMap.put(nodesInThisBlock.get(0), shuffledList.get(0));
        }
        
        
    }
    
    
    public void shuffle(){
        Collections.shuffle(shuffledList);
    }


}
