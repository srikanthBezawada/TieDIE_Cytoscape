package org.cytoscape.tiedie.internal.logic;

import Jama.Matrix;

import gov.sandia.cognition.math.matrix.MatrixFactory;
import gov.sandia.cognition.math.matrix.Vector;
import gov.sandia.cognition.math.matrix.VectorFactory;
import gov.sandia.cognition.math.matrix.decomposition.EigenvectorPowerIteration;

/**
 *
 * @author SrikanthB
 */
public class DiffusedHeatVector {
    private Matrix heatVectorOfScores;
    private int numOfColumns;
    
    public DiffusedHeatVector(int numOfColumns) {
        this.numOfColumns = numOfColumns;
        this.heatVectorOfScores = new Matrix(1, numOfColumns); 
        // Matrix(int m, int n) 
        //  Construct an m-by-n matrix of zeros.
    }

    public DiffusedHeatVector(Matrix rowVector) {
        this.heatVectorOfScores = rowVector;
        this.numOfColumns = rowVector.getColumnDimension();
    }
    
    public Matrix getVectorOfScores(){
        return heatVectorOfScores;
    }
    
    public DiffusedHeatVector extractDiffusedHeatVector(HeatVector inputVector, Kernel heatDiffusionKernel){
        return heatDiffusionKernel.diffuse(inputVector);
    }    
    
    public DiffusedHeatVector extractDiffusedHeatVector(HeatVector inputVector, double[][] directedAdj){
        double[] heatArray = inputVector.heatArray;
        //Vector input = (Vector)VectorFactory.getDefault().copyValues(heatArray);
        Vector input = (Vector)VectorFactory.getDefault().copyValues(heatArray);
        gov.sandia.cognition.math.matrix.Matrix dirAdjMat;  //Matrix(directedAdj)
        dirAdjMat = MatrixFactory.getDefault().copyArray(directedAdj);
        Vector output = estimateEigenvector(input, dirAdjMat, 0.85, EigenvectorPowerIteration.DEFAULT_STOPPING_THRESHOLD , EigenvectorPowerIteration.DEFAULT_MAXIMUM_ITERATIONS);
        Matrix m = new Matrix(1, heatArray.length);
        int counter=0;
        //System.out.println("output ->"+output.isUnitVector());
        //System.out.println("output.toArray().length -> "+output.toArray().length);
        for(double d : output.toArray()){
            m.set(0, counter, d);
            //System.out.println(d+ "\t");
            counter++;
        }
        return new DiffusedHeatVector(m);
    }
    
    public static Vector estimateEigenvector(
        final Vector initial,
        final gov.sandia.cognition.math.matrix.Matrix A,
        final double alpha,
        final double stoppingThreshold,
        final int maxIterations )
    {
        Vector v = initial.unitVector();

        double normChange;
        int iteration = 0;
        boolean keepGoing = true;
        while (keepGoing)
        {
            final Vector vPrevious = v;
            v = A.times( v );
            v.scaleEquals(alpha);
            v.plusEquals(initial.scale(1.0 - alpha));
            normChange = v.minus( vPrevious ).norm2();
            iteration++;

            if ((normChange <= stoppingThreshold) || (iteration >= maxIterations))
            {
                keepGoing = false;
            }
        }

        return v;
    
    }
}