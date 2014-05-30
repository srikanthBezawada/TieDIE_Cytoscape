package org.cytoscape.tiedie.internal.logic;

import Jama.Matrix;
import org.jblas.DoubleMatrix;
import org.jblas.MatrixFunctions;


import java.util.List;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;
import org.cytoscape.view.model.CyNetworkView;



/**
 * @author SrikanthB
 * 
 * L=(D-A)
 * K = exponential(-t*L)
 * 
 * 
 * 
 * D is degree matrix and has only diagonal elements
 * A is adjacency matrix
 * L is graph laplacian  
 * K is heat diffusion kernel of the graph which is a matrix again
 */


public class KernelThread extends Thread {

    public CyNetwork currentnetwork;
    public CyNetworkView currentnetworkview;

    public KernelThread(CyNetwork currentnetwork, CyNetworkView currentnetworkview) {
        this.currentnetwork = currentnetwork;
        this.currentnetworkview = currentnetworkview;
    }

    @Override
    public void run() {
        
        double t;
        t = 0.01;
        
        List<CyNode> nodeList = currentnetwork.getNodeList();
        int totalnodecount = nodeList.size();
        CyTable edgeTable = currentnetwork.getDefaultEdgeTable();
        CyTable nodeTable = currentnetwork.getDefaultNodeTable();

        double[][] adjacencyMatrixOfNetwork = createAdjMatrix(currentnetwork, nodeList, edgeTable, totalnodecount);
        double[][] degreeMatrixOfNetwork = createDegMatrix(currentnetwork, nodeList, totalnodecount);
        double[][] laplacianMatrixOfNetwork = createLapMatrix(adjacencyMatrixOfNetwork, degreeMatrixOfNetwork, totalnodecount);
        double[][] diffusionKernel = createRequiredExponential(laplacianMatrixOfNetwork, t);
        
        
 
        
    }

    public static double[][] createAdjMatrix(CyNetwork currentnetwork, List<CyNode> nodeList, CyTable edgeTable, int totalnodecount) {
        //make an adjacencymatrix for the current network
        double[][] adjacencyMatrixOfNetwork = new double[totalnodecount][totalnodecount];
        int k = 0;
        for (CyNode root : nodeList) {
            List<CyNode> neighbors = currentnetwork.getNeighborList(root, CyEdge.Type.OUTGOING);
            for (CyNode neighbor : neighbors) {
                List<CyEdge> edges = currentnetwork.getConnectingEdgeList(root, neighbor, CyEdge.Type.DIRECTED);
                if (edges.size() > 0) {
                    CyRow row = edgeTable.getRow(edges.get(0).getSUID());
                    try {
                        adjacencyMatrixOfNetwork[k][nodeList.indexOf(neighbor)] = Double.parseDouble(row.get(CyEdge.INTERACTION, String.class));
                    } catch (Exception ex) {
                    }
                }
            }
            k++;
        }
        return adjacencyMatrixOfNetwork;
    }

    public static double[][] createDegMatrix(CyNetwork currentnetwork, List<CyNode> nodeList, int totalnodecount) {

        double[][] degreeMatrixOfNetwork = new double[totalnodecount][totalnodecount];

        int k = 0;
        for (CyNode root : nodeList) {
            List<CyNode> myNeighbourList = currentnetwork.getNeighborList(root, CyEdge.Type.ANY);
            int myNodeDegree = myNeighbourList.size();
            degreeMatrixOfNetwork[k][k] = myNodeDegree;
            k++;
        }

        return degreeMatrixOfNetwork;
    }

    
    
    
    public static double[][] createLapMatrix(double[][] adjacencyMatrixOfNetwork, double[][] degreeMatrixOfNetwork, int totalnodecount) {

        double[][] laplacianMatrixOfNetwork;
        Matrix D = new Matrix(degreeMatrixOfNetwork);
        Matrix A = new Matrix(adjacencyMatrixOfNetwork);
        Matrix L = D.minus(A);
        laplacianMatrixOfNetwork = L.getArrayCopy();
        
        return laplacianMatrixOfNetwork;
    }

    
    public static double[][] createRequiredExponential(double[][] laplacianMatrixOfNetwork, double t){
        
        double[][] minusOftL;
        double[][] diffusionKernel;
        DoubleMatrix diffusionKernelMatrix;
        
        Matrix C = new Matrix(laplacianMatrixOfNetwork);
        C = C.timesEquals(-t);  //  (-t)*L
        minusOftL = C.getArrayCopy();
        
        diffusionKernelMatrix = new DoubleMatrix(minusOftL);
        diffusionKernelMatrix = MatrixFunctions.expm(diffusionKernelMatrix); // exponentiation 
        diffusionKernel = diffusionKernelMatrix.toArray2();
        
        return diffusionKernel;
    
    }
    
    
    
    
 
}
