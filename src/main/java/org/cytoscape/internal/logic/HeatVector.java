package org.cytoscape.tiedie.internal.logic;

import Jama.Matrix;
import java.util.List;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;

/**
 *
 * @author SrikanthB
 */
public class HeatVector {
    
    Matrix heatVectorOfScores;
    int numOfColumns;
    
    public HeatVector(int numOfColumns){
        this.numOfColumns = numOfColumns;
        this.heatVectorOfScores = new Matrix(1, numOfColumns);
    }
    
    public HeatVector(Matrix rowVector){
        this.heatVectorOfScores = rowVector;
        this.numOfColumns = rowVector.getColumnDimension();
    }
    
    public HeatVector extractHeatVector(String columnName, List<CyNode> nodeList,CyTable nodeTable){
        int counter=0;
        double heatscore;
        
        for(CyNode root : nodeList){
            CyRow row = nodeTable.getRow(root.getSUID());
            if(row.get(columnName,Double.class)!=null){
                heatscore = row.get(columnName,Double.class);
                heatVectorOfScores.set(0,counter,heatscore);
            }
            counter++;
        }   
        
        HeatVector inputRowVector = new HeatVector(heatVectorOfScores);
        return inputRowVector;
    }
    
  

}
