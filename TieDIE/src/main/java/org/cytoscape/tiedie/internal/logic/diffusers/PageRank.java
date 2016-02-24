package org.cytoscape.tiedie.internal.logic.diffusers;

import gov.sandia.cognition.math.matrix.Matrix;
import gov.sandia.cognition.math.matrix.MatrixFactory;
import gov.sandia.cognition.math.matrix.Vector;
import gov.sandia.cognition.math.matrix.VectorFactory;
import gov.sandia.cognition.math.matrix.decomposition.EigenvectorPowerIteration;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.tiedie.internal.logic.DiffusedHeatVector;
import org.cytoscape.tiedie.internal.logic.HeatVector;

public class PageRank {
    private static final double DAMPINGF = 0.85;
    
    public static DiffusedHeatVector diffuse(HeatVector inputV, CyNetwork network){
        double[] heatArray = inputV.heatArray;
        Vector input = (Vector)VectorFactory.getDefault().copyValues(heatArray);
        Matrix dirAdjMat = MatrixFactory.getDefault().copyArray(MatrixUtil.createAdjMatrix(network));
        Vector output = estimateEigenvector(input, dirAdjMat, DAMPINGF, EigenvectorPowerIteration.DEFAULT_STOPPING_THRESHOLD , EigenvectorPowerIteration.DEFAULT_MAXIMUM_ITERATIONS);
        Jama.Matrix m = new Jama.Matrix(1, heatArray.length);
        int counter=0;
        for(double d : output.toArray()){
            m.set(0, counter, d);
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
