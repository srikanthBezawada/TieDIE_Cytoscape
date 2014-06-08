package org.cytoscape.tiedie.internal.logic;

import java.util.List;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
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

    public void run(){
        
        List<CyNode> nodeList = currentnetwork.getNodeList();
        int totalnodecount = nodeList.size();
        CyTable edgeTable = currentnetwork.getDefaultEdgeTable();
        CyTable nodeTable = currentnetwork.getDefaultNodeTable();
        
        double[][] adjacencyMatrixOfNetwork = Kernel.createAdjMatrix(currentnetwork, nodeList, edgeTable, totalnodecount);
        double[][] degreeMatrixOfNetwork = Kernel.createDegMatrix(currentnetwork, nodeList, totalnodecount);
        double[][] laplacianMatrixOfNetwork = Kernel.createLapMatrix(adjacencyMatrixOfNetwork, degreeMatrixOfNetwork, totalnodecount);
        double[][] diffusionKernel = Kernel.createRequiredExponential(laplacianMatrixOfNetwork);
        
    
    
    
    
    }
    
     
    
}
