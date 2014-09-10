package org.cytoscape.tiedie.internal.logic;

import Jama.Matrix;
import java.util.HashMap;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.List;
import java.util.Map;

import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;

/**
 *
 * @author SrikanthB
 */

public class HeatVector {   
    /*
        About instance variables :
    
        1. Each HeatVector has a vector of scores which is a rowmatrix "Matrix heatVectorOfScores"
        2. "nodeHeatSet" is the corresponding set of nodes for the respective heat vector.
            
    */    
    
    private Matrix heatVectorOfScores;
    private int numOfColumns;
    private Set<CyNode> nodeHeatSet; 
    private Map<CyNode, Double> nodeScoreMap;
    private int nodeCount;
    
    //HeatValue and Score are the same
   
    public HeatVector(int numOfColumns) {
        this.numOfColumns = numOfColumns;
        this.heatVectorOfScores = new Matrix(1, numOfColumns);
        // Matrix(int m, int n) 
        //  Construct an m-by-n matrix of zeros.
    }

    public HeatVector(Matrix rowVector) {
        this.heatVectorOfScores = rowVector;
        this.numOfColumns = rowVector.getColumnDimension();
    }
    
    // Getter methods start here
    public Set getnodeHeatSet(){
        return nodeHeatSet;
        
    }
    
    public Matrix getheatVectorOfScores() {
        return heatVectorOfScores;
    }
    
    public int getnodeCount(){
        return nodeCount;
    }
    // Getter methods end here
   
    /*
     About methods :
    
        1. "extractHeatVector" extracts the required heat vector from "nodeTable" by taking "columnName" as input
        2. For a two way diffusion, column names are supposed to be "upstreamheat" , "downstreamheat"  from the 
           input files
        3. "extractHeatVector" also sets "nodeHeatSet" 
    */
    
    public HeatVector extractHeatVector(String columnName, List<CyNode> nodeList, CyTable nodeTable) {
        int counter = 0;
        Number heatscore = null;
        nodeHeatSet = new LinkedHashSet<CyNode>();  
        
        nodeScoreMap = new HashMap<CyNode, Double>();
        for (CyNode root : nodeList) { // nodeList is always accessed in a same order
            CyRow row = nodeTable.getRow(root.getSUID());
            
            if (nodeTable.getColumn(columnName).getType() == Double.class) {
                heatscore = row.get(columnName, Double.class);
            }
            else if (nodeTable.getColumn(columnName).getType() == Integer.class) {
                heatscore = row.get(columnName, Integer.class);
            } 
            else if (nodeTable.getColumn(columnName).getType() == Long.class) {
                heatscore = row.get(columnName, Long.class);
            }
            else if (nodeTable.getColumn(columnName).getType() == String.class){
                try{
                    heatscore = Double.parseDouble(row.get(columnName, String.class));
                    
                }
                catch(NumberFormatException e){
                    System.out.println("Column with name "+columnName+" has unsupported format. Should contain only numbers");
                }
                catch(NullPointerException e){
                     System.out.println("String is null");
                }
                
            }
            if(heatscore == null)
            continue;
            
            heatVectorOfScores.set(0, counter, heatscore.doubleValue()); // set() method of Jama library
            nodeHeatSet.add(root);  // put all the nodes corresponding to that column in nodeHeatSet
            nodeScoreMap.put(root, heatscore.doubleValue());
            nodeCount++;
            
            counter++;
        }

        return this;
    }
    
}
