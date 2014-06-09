package org.cytoscape.tiedie.internal.logic;

import Jama.Matrix;
import java.util.List;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;
import org.cytoscape.view.model.CyNetworkView;


/**
 * @author SrikanthB
 * 
 * 
 * 
 * 
 */


public class TieDieLogicThread extends Thread {
    
    public CyNetwork currentnetwork;
    public CyNetworkView currentnetworkview;
    
    public TieDieLogicThread(CyNetwork currentnetwork, CyNetworkView currentnetworkview) {
        this.currentnetwork = currentnetwork;
        this.currentnetworkview = currentnetworkview;
    }

    @Override
    public void run(){
        
        List<CyNode> nodeList = currentnetwork.getNodeList();
        int totalnodecount = nodeList.size();
        CyTable edgeTable = currentnetwork.getDefaultEdgeTable();
        CyTable nodeTable = currentnetwork.getDefaultNodeTable();
        
        double[][] adjacencyMatrixOfNetwork = Kernel.createAdjMatrix(currentnetwork, nodeList, edgeTable, totalnodecount);
        double[][] degreeMatrixOfNetwork = Kernel.createDegMatrix(currentnetwork, nodeList, totalnodecount);
        double[][] laplacianMatrixOfNetwork = Kernel.createLapMatrix(adjacencyMatrixOfNetwork, degreeMatrixOfNetwork, totalnodecount);
        double[][] diffusionKernel = Kernel.createRequiredExponential(laplacianMatrixOfNetwork);
        
        
        Matrix upstreamheatVector = new Matrix(1, totalnodecount);
        Matrix downstreamheatVector = new Matrix(1, totalnodecount);
        Matrix upstreamheatVectorDiffused = new Matrix(1, totalnodecount);
        Matrix downstreamheatVectorDiffused = new Matrix(1, totalnodecount);
        
        int counter = 0;
        int flag = 0;
        String columnName;
        int upstreamheat, downstreamheat;
        
        for(CyNode root : nodeList){
           
            CyRow row = nodeTable.getRow(root.getSUID());
            columnName = "upstreamheat";
            if(row.get(columnName,Integer.class)!=null){
                upstreamheat = row.get(columnName,Integer.class);
                upstreamheatVector.set(0,counter,upstreamheat);
            }
            counter++;
            
            columnName = "downstreamheat";
             if(row.get(columnName,Integer.class)!=null){
                downstreamheat = row.get(columnName,Integer.class);
                downstreamheatVector.set(0,flag,downstreamheat);
            }
             flag++;
        }
        
        upstreamheatVectorDiffused = Kernel.diffuse(upstreamheatVector, diffusionKernel);
        downstreamheatVectorDiffused = Kernel.diffuse(downstreamheatVector, diffusionKernel); 
       
       
        
    }
    
     
    
}
