package org.cytoscape.tiedie.internal.logic.diffusers;

import Jama.Matrix;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;

public class MatrixUtil {
    
    public static double[][] createAdjMatrix(CyNetwork network){
        List<CyNode> nodeList = network.getNodeList();
        CyTable edgeTable = network.getDefaultEdgeTable();
        int size = nodeList.size();
        double[][] adjM = new double[size][size];
        CyRow row;
        
        int k = 0;
        for (CyNode root : nodeList) {
            List<CyNode> neighbors = network.getNeighborList(root, CyEdge.Type.ANY);
            for (CyNode neighbor : neighbors) {
                List<CyEdge> edges = network.getConnectingEdgeList(root, neighbor, CyEdge.Type.ANY);
                if (edges.size() > 0) {
                    row = edgeTable.getRow(edges.get(0).getSUID());
                    try {
                        adjM[k][nodeList.indexOf(neighbor)] = 1;
                    } catch (Exception ex) {
                    }
                }
            }
            k++;
        }
        return adjM;
    }
    
    public static double[][] createDegMatrix(CyNetwork network){
        List<CyNode> nodeList = network.getNodeList();
        int size = nodeList.size();
        double[][] dMat = new double[size][size];
        int k = 0;
        for (CyNode root : nodeList) {
            List<CyNode> myOutNeighbourList = network.getNeighborList(root, CyEdge.Type.OUTGOING);
            List<CyNode> myInNeighbourList = network.getNeighborList(root, CyEdge.Type.INCOMING);
            Set<CyNode> myNeighbourSet =  new HashSet<CyNode>();
            for(CyNode a:myOutNeighbourList){ // update code for self loops
                myNeighbourSet.add(a);
            }
            for(CyNode a:myInNeighbourList){
                myNeighbourSet.add(a);
            }
            int myNodeDegree = myNeighbourSet.size();
            dMat[k][k] = myNodeDegree;
            k++;
        }
        return dMat;
    }    
    
    public static Matrix createLapMatrix(double[][] adjM, double[][] dMat){
        Matrix D = new Matrix(dMat);
        Matrix A = new Matrix(adjM);
        Matrix L = D.minus(A);
        return L;
    }
    
    
    
}
