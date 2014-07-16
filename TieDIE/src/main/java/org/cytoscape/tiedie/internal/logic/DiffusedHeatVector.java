package org.cytoscape.tiedie.internal.logic;

import Jama.Matrix;

/**
 *
 * @author SrikanthB
 */
public class DiffusedHeatVector {
    Matrix heatVectorOfScores;
    int numOfColumns;
    
    public DiffusedHeatVector(int numOfColumns) {
        this.numOfColumns = numOfColumns;
        this.heatVectorOfScores = new Matrix(1, numOfColumns);
    }

    public DiffusedHeatVector(Matrix rowVector) {
        this.heatVectorOfScores = rowVector;
        this.numOfColumns = rowVector.getColumnDimension();
    }
    
    public DiffusedHeatVector extractDiffusedHeatVector(HeatVector inputVector, double[][] diffusionKernel){
        return Kernel.diffuse(inputVector, diffusionKernel);
    }
    
    
    
    
    
    
    
}
