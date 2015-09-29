package org.cytoscape.tiedie.internal.logic;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;
import org.cytoscape.model.subnetwork.CyRootNetwork;
import org.cytoscape.model.subnetwork.CySubNetwork;
import org.cytoscape.view.model.CyNetworkView;

import org.cytoscape.tiedie.internal.CyActivator;
import static org.cytoscape.tiedie.internal.visuals.UpdateSubNetView.updateView;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.view.presentation.property.NodeShapeVisualProperty;




/**
 * @author SrikanthB
  
  TieDieLogicThread.java  keeps the ball rolling according to our execution flow and 
  this will be called from start button of GUI. 
 */


public class TieDieLogicThread extends Thread {
    
    /*
        About instance variables :
    
    */
    double sizeFactor, linker_cutoff;
    String upstreamColumn, downstreamColumn;
    
    public CyNetwork currentnetwork;
    public CyNetworkView currentnetworkview;
    public int totalnodecount;
    List<CyNode> nodeList;
    CyTable nodeTable, edgeTable;
    boolean isKernel;
    
    private double[][] adjacencyMatrixOfNetwork;
    Kernel heatDiffusionKernel;
    HeatVector upstreamheatVector, downstreamheatVector;
    DiffusedHeatVector upstreamheatVectorDiffused, downstreamheatVectorDiffused;
    Map upnodeScoreMapDiffused, downnodeScoreMapDiffused;
    Map linkers_nodeScoreMap, filtered_linkersNodeScoreMap;
    
    public TieDieLogicThread(CyNetwork currentnetwork, CyNetworkView currentnetworkview, String upstreamColumn, String downstreamColumn, boolean isKernel) {
        this.currentnetwork = currentnetwork;
        this.currentnetworkview = currentnetworkview;
        //nodeList = new LinkedList();
        this.nodeList = this.currentnetwork.getNodeList(); 
        this.totalnodecount = nodeList.size();
        this.edgeTable = currentnetwork.getDefaultEdgeTable();
        this.nodeTable = currentnetwork.getDefaultNodeTable();
        this.upstreamColumn = upstreamColumn;
        this.downstreamColumn = downstreamColumn;
        this.isKernel = isKernel;
        upstreamheatVector = new HeatVector(totalnodecount);
        downstreamheatVector = new HeatVector(totalnodecount);
        upstreamheatVectorDiffused = new DiffusedHeatVector(totalnodecount);
        downstreamheatVectorDiffused = new DiffusedHeatVector(totalnodecount);
    }

    @Override
    public void run(){
        System.out.println("Start---");
        heatDiffusionKernel = new Kernel(currentnetwork);
        if(isKernel){
            /*
            Create upstreamheatVector, downstreamheatVector for 2-way diffusion
            "Extract them using extractHeatVector"
            */
            if(upstreamColumn == null){ // change this, no need
                upstreamColumn = "upstreamheat";
            }
            if(downstreamColumn == null){
                downstreamColumn = "downstreamheat";
            }
            upstreamheatVector = upstreamheatVector.extractHeatVector(upstreamColumn, nodeList, nodeTable);
            downstreamheatVector = downstreamheatVector.extractHeatVector(downstreamColumn, nodeList, nodeTable);
            // Get the diffused heat vectors which spread all over the network
            upstreamheatVectorDiffused =  upstreamheatVectorDiffused.extractDiffusedHeatVector(upstreamheatVector, heatDiffusionKernel);
            downstreamheatVectorDiffused = downstreamheatVectorDiffused.extractDiffusedHeatVector(downstreamheatVector, heatDiffusionKernel);
        
        } else{//pagerank
            /*
            Create upstreamheatVector, downstreamheatVector for 2-way diffusion
            "Extract them using extractHeatVector"
            */
            if(upstreamColumn == null){
                upstreamColumn = "upstreamheat";
            }
            if(downstreamColumn == null){
                downstreamColumn = "downstreamheat";
            }
            upstreamheatVector = upstreamheatVector.extractHeatVector(upstreamColumn, nodeList, nodeTable);
            downstreamheatVector = downstreamheatVector.extractHeatVector(downstreamColumn, nodeList, nodeTable);
            // Get the diffused heat vectors which spread all over the network
            double[][] directedAdj = heatDiffusionKernel.getadjacencyMatrixOfNetwork();
            upstreamheatVectorDiffused =  upstreamheatVectorDiffused.extractDiffusedHeatVector(upstreamheatVector, directedAdj);
            downstreamheatVectorDiffused = downstreamheatVectorDiffused.extractDiffusedHeatVector(downstreamheatVector, directedAdj);
        
        }
        
        
        
        
        // Extracting the maps of node vs diffused heat
        upnodeScoreMapDiffused = getDiffusedMap(upstreamheatVectorDiffused);
        downnodeScoreMapDiffused = getDiffusedMap(downstreamheatVectorDiffused);
        
        extractSubnetwork();
        
        
      
        
    } 
    
    public void extractSubnetwork(){
        if(isKernel){
            sizeFactor = 1;
        }else{
            sizeFactor = 2.5;//  2.47 no   2.5 yes
        }
        System.out.println("sizefactor"+sizeFactor);
        
        for(Object o : upstreamheatVector.getnodeHeatSet()){
            CyNode c = (CyNode)o;
            if(downnodeScoreMapDiffused.containsKey(c)){
                downnodeScoreMapDiffused.remove(c);
            }
        }
                
        for(Object o : downstreamheatVector.getnodeHeatSet()){
            CyNode c = (CyNode)o;
            if(upnodeScoreMapDiffused.containsKey(c)){
                upnodeScoreMapDiffused.remove(c);
            }
        }
        
        
        System.out.println("upnodeScoreMapDiffused.size()"+upnodeScoreMapDiffused.size());
        System.out.println("downnodeScoreMapDiffused.size()"+downnodeScoreMapDiffused.size());
        
        linker_cutoff = TieDieUtil.findLinkerCutoff(upstreamheatVector.getnodeHeatSet(), downstreamheatVector.getnodeHeatSet(), upnodeScoreMapDiffused, downnodeScoreMapDiffused, sizeFactor);
        System.out.println("linker_cutoff"+linker_cutoff);
        // nodeList is the extra parameter to existing tiedie  https://github.com/epaull/TieDIE/blob/master/lib/tiedie_util.py#L336
        
        linkers_nodeScoreMap = TieDieUtil.findLinkersMap(upnodeScoreMapDiffused, downnodeScoreMapDiffused);
        // calling "z" function here according to literature
        filtered_linkersNodeScoreMap = TieDieUtil.findFilteredLinkersMap(linkers_nodeScoreMap, linker_cutoff);
        createExtractedSubnetwork();
    }
    
    
    public void createExtractedSubnetwork(){
        Set<CyNode> linkerNodes = filtered_linkersNodeScoreMap.keySet();
        List<CyNode> nodes = currentnetwork.getNodeList();
        List<CyNode> newnodes = new ArrayList<CyNode>();
        List<CyEdge> edges = currentnetwork.getEdgeList();
        List<CyEdge> newedges = new ArrayList<CyEdge>();
        
        for(Object currentnode : upstreamheatVector.getnodeHeatSet()){
            newnodes.add((CyNode) currentnode);
        }
        for(Object currentnode : downstreamheatVector.getnodeHeatSet()){
            newnodes.add((CyNode) currentnode);
        }
        for(Object currentnode : linkerNodes){
            newnodes.add((CyNode) currentnode);
        }
        
        for(CyEdge currentedge : edges){
            CyNode node1 = currentedge.getSource();
            CyNode node2 = currentedge.getTarget();
            if(newnodes.contains(node1) && newnodes.contains(node2)){
                newedges.add(currentedge);
            }
        }
        System.out.println("nodes size"+newnodes.size());
        System.out.println("edges size"+newedges.size());
        // SOME LINKERS ARE SHOWN FROM SOURCE, TARGET.. verify the findLinker code
        Set<CyNode> s = new HashSet<CyNode>(newnodes);
        System.out.println("nodesetsize"+s.size());
                
        CyRootNetwork root = ((CySubNetwork)currentnetwork).getRootNetwork();
        CyNetwork TieDIEsubNetwork = root.addSubNetwork(newnodes, newedges);
        TieDIEsubNetwork.getRow(TieDIEsubNetwork).set(CyNetwork.NAME, "TieDIE subnetwork");
        CyNetworkManager networkManager = CyActivator.networkManager;
        networkManager.addNetwork(TieDIEsubNetwork);
        CyNetworkView tiedieView = CyActivator.networkViewFactory.createNetworkView(TieDIEsubNetwork);
        CyActivator.networkViewManager.addNetworkView(tiedieView);
        updateView(tiedieView, "grid");
        CyActivator.getCyEventHelper().flushPayloadEvents();
        
        for(Object currentnode : linkerNodes){
            tiedieView.getNodeView((CyNode)currentnode).setVisualProperty(BasicVisualLexicon.NODE_FILL_COLOR, Color.ORANGE);
            //tiedieView.getNodeView((CyNode)currentnode).setVisualProperty(BasicVisualLexicon.NODE_BORDER_PAINT, Color.ORANGE);
            //tiedieView.getNodeView((CyNode)currentnode).setVisualProperty(BasicVisualLexicon.NODE_BORDER_WIDTH, 10.0);
            currentnetworkview.getNodeView((CyNode)currentnode).setVisualProperty(BasicVisualLexicon.NODE_SHAPE, NodeShapeVisualProperty.OCTAGON);
            currentnetworkview.getNodeView((CyNode)currentnode).setVisualProperty(BasicVisualLexicon.NODE_BORDER_PAINT, Color.ORANGE);
            currentnetworkview.getNodeView((CyNode)currentnode).setVisualProperty(BasicVisualLexicon.NODE_BORDER_WIDTH, 12.0);//12
        }
        
        for(Object currentnode : upstreamheatVector.getnodeHeatSet()){
            tiedieView.getNodeView((CyNode)currentnode).setVisualProperty(BasicVisualLexicon.NODE_BORDER_PAINT, Color.RED);
            tiedieView.getNodeView((CyNode)currentnode).setVisualProperty(BasicVisualLexicon.NODE_BORDER_WIDTH, 6.25);//6.25
            currentnetworkview.getNodeView((CyNode)currentnode).setVisualProperty(BasicVisualLexicon.NODE_BORDER_PAINT, Color.RED);
            currentnetworkview.getNodeView((CyNode)currentnode).setVisualProperty(BasicVisualLexicon.NODE_BORDER_WIDTH, 12.0);//12
            currentnetworkview.getNodeView((CyNode)currentnode).setVisualProperty(BasicVisualLexicon.NODE_SHAPE, NodeShapeVisualProperty.OCTAGON);
        }
        for(Object currentnode : downstreamheatVector.getnodeHeatSet()){
            tiedieView.getNodeView((CyNode)currentnode).setVisualProperty(BasicVisualLexicon.NODE_FILL_COLOR, Color.RED);
            //tiedieView.getNodeView((CyNode)currentnode).setVisualProperty(BasicVisualLexicon.NODE_BORDER_PAINT, Color.RED);
            //tiedieView.getNodeView((CyNode)currentnode).setVisualProperty(BasicVisualLexicon.NODE_BORDER_WIDTH, 16.0);
            currentnetworkview.getNodeView((CyNode)currentnode).setVisualProperty(BasicVisualLexicon.NODE_FILL_COLOR, Color.RED);
            currentnetworkview.getNodeView((CyNode)currentnode).setVisualProperty(BasicVisualLexicon.NODE_SHAPE, NodeShapeVisualProperty.OCTAGON);
        }
        
        
        for(Object currentedge : newedges){
            tiedieView.getEdgeView((CyEdge)currentedge).setVisualProperty(BasicVisualLexicon.EDGE_STROKE_UNSELECTED_PAINT, Color.ORANGE);
            tiedieView.getEdgeView((CyEdge)currentedge).setVisualProperty(BasicVisualLexicon.EDGE_WIDTH, 6.5);
            currentnetworkview.getEdgeView((CyEdge)currentedge).setVisualProperty(BasicVisualLexicon.EDGE_STROKE_UNSELECTED_PAINT, Color.ORANGE);
            currentnetworkview.getEdgeView((CyEdge)currentedge).setVisualProperty(BasicVisualLexicon.EDGE_WIDTH, 7.5);
        }
        
        System.out.println("End---");
    }
    
   

    
    
    public Map getDiffusedMap(DiffusedHeatVector diffusedVector){
        Map diffScoreMap = new HashMap<CyNode, Double>();
        int count=0;
        double diffScr;
        for(CyNode root: nodeList){
            diffScr = diffusedVector.getVectorOfScores().get(0, count);
            diffScoreMap.put(root, diffScr);
            count++; 
        }
        return diffScoreMap;
    }
    
    
}
