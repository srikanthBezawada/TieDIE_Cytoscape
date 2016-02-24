package org.cytoscape.tiedie.internal.logic.diffusers;

import Jama.Matrix;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.tiedie.internal.logic.DiffusedHeatVector;
import org.cytoscape.tiedie.internal.logic.HeatVector;
import org.jblas.DoubleMatrix;
import org.jblas.MatrixFunctions;

public class Kernel {
    private static final double t = 0.1;
    
    public static DiffusedHeatVector diffuse(HeatVector inputV, CyNetwork network){
        Matrix diffusedVectorMatrix; 
        Matrix diffKernelMat = new Matrix(createRequiredExponential(network));
        diffusedVectorMatrix = inputV.getheatVectorOfScores().times(diffKernelMat);
        
        return new DiffusedHeatVector(diffusedVectorMatrix);
    }
    
    public static double[][] createRequiredExponential(CyNetwork network){
        double[][] adjM = MatrixUtil.createAdjMatrix(network);
        double[][] dMat = MatrixUtil.createDegMatrix(network);
        Matrix L = MatrixUtil.createLapMatrix(adjM, dMat);
        double[][] minusOftL = (L.timesEquals(-t)).getArrayCopy();
        DoubleMatrix diffKernelMat = MatrixFunctions.expm(new DoubleMatrix(minusOftL));
        return diffKernelMat.toArray2();
    }
    
}
