package org.cytoscape.tiedie.internal.logic;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;
import org.cytoscape.model.subnetwork.CyRootNetwork;
import org.cytoscape.model.subnetwork.CySubNetwork;
import org.cytoscape.view.model.CyNetworkView;

import org.cytoscape.tiedie.internal.CyActivator;
import org.cytoscape.tiedie.internal.TieDieGUI;
import org.cytoscape.tiedie.internal.logic.diffusers.Kernel;
import org.cytoscape.tiedie.internal.logic.diffusers.PageRank;
import org.cytoscape.tiedie.internal.results.ResultsUI;
import static org.cytoscape.tiedie.internal.visuals.UpdateSubNetView.updateView;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.view.presentation.property.NodeShapeVisualProperty;




/**
 * @author SrikanthB
  
  TieDieLogicThread.java  keeps the ball rolling according to our execution flow and 
  this will be called from start button of GUI. 
 */


public class TieDieLogicThread extends Thread {
    
    
    double sizeFactor, linker_cutoff;
    String upstreamColumn, downstreamColumn;
    
    public CyNetwork currentnetwork;
    public CyNetworkView currentnetworkview;
    public int totalnodecount;
    List<CyNode> nodeList;
    CyTable nodeTable;
    boolean isKernel;
    TieDieGUI menu;
            
    Kernel heatDiffusionKernel;
    HeatVector upstreamheatVector, downstreamheatVector;
    DiffusedHeatVector upstreamheatVectorDiffused, downstreamheatVectorDiffused;
    Map upnodeScoreMapDiffused, downnodeScoreMapDiffused;
    Map<CyNode, Double> linkers_nodeScoreMap, filtered_linkersNodeScoreMap;
    
    public TieDieLogicThread(TieDieGUI menu, CyNetwork currentnetwork, CyNetworkView currentnetworkview, String upstreamColumn, String downstreamColumn, double sizeFactor, boolean isKernel) {
        this.menu = menu;
        this.currentnetwork = currentnetwork;
        this.currentnetworkview = currentnetworkview;
        this.nodeList = this.currentnetwork.getNodeList(); 
        this.totalnodecount = nodeList.size();
        this.nodeTable = currentnetwork.getDefaultNodeTable();
        this.upstreamColumn = upstreamColumn;
        this.downstreamColumn = downstreamColumn;
        this.sizeFactor = sizeFactor;
        this.isKernel = isKernel;
        upstreamheatVector = new HeatVector(totalnodecount);
        downstreamheatVector = new HeatVector(totalnodecount);
        upstreamheatVectorDiffused = new DiffusedHeatVector(totalnodecount);
        downstreamheatVectorDiffused = new DiffusedHeatVector(totalnodecount);
    }

    @Override
    public void run(){
        System.out.println("Start---");
        menu.startComputation();
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
            upstreamheatVectorDiffused =  Kernel.diffuse(upstreamheatVector, currentnetwork);
            downstreamheatVectorDiffused = Kernel.diffuse(downstreamheatVector, currentnetwork);
        
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
            upstreamheatVectorDiffused =  PageRank.diffuse(upstreamheatVector, currentnetwork);
            downstreamheatVectorDiffused = PageRank.diffuse(downstreamheatVector, currentnetwork);
        
        }
        
        
        
        
        // Extracting the maps of node vs diffused heat
        upnodeScoreMapDiffused = getDiffusedMap(upstreamheatVectorDiffused, "upstreamDiffused");
        downnodeScoreMapDiffused = getDiffusedMap(downstreamheatVectorDiffused, "downstreamDiffused");
        
        extractSubnetwork();
        
        
      
        
    } 
    
    public void extractSubnetwork(){
        System.out.println("sizefactor"+sizeFactor);
        /*
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
        */
        
        System.out.println("upnodeScoreMapDiffused.size()"+upnodeScoreMapDiffused.size());
        System.out.println("downnodeScoreMapDiffused.size()"+downnodeScoreMapDiffused.size());
        
        linker_cutoff = TieDieUtil.findLinkerCutoff(upstreamheatVector.getnodeHeatSet(), downstreamheatVector.getnodeHeatSet(), upnodeScoreMapDiffused, downnodeScoreMapDiffused, sizeFactor);
        System.out.println("linker_cutoff"+linker_cutoff);
        // nodeList is the extra parameter to existing tiedie  https://github.com/epaull/TieDIE/blob/master/lib/tiedie_util.py#L336
        
        linkers_nodeScoreMap = TieDieUtil.findLinkersMap(upnodeScoreMapDiffused, downnodeScoreMapDiffused);
        nodeTable.createColumn("linkerScore", Double.class, true);
        CyRow row;
        for (Map.Entry<CyNode, Double> entry : linkers_nodeScoreMap.entrySet()){
            CyNode node = entry.getKey();
            Double value = entry.getValue();
            row = nodeTable.getRow(node.getSUID());
            row.set("linkerScore", value);
        }
        // calling "z" function here according to literature
        filtered_linkersNodeScoreMap = TieDieUtil.findFilteredLinkersMap(linkers_nodeScoreMap, linker_cutoff);
        createExtractedSubnetwork();
    }
    
    
    public void createExtractedSubnetwork(){
        Map<CyNode, Double> finalLinkerMap = new HashMap<CyNode, Double>(filtered_linkersNodeScoreMap);
        Iterator entries = filtered_linkersNodeScoreMap.keySet().iterator();
        while (entries.hasNext()) {
          CyNode currentnode = (CyNode) entries.next();
          if(upstreamheatVector.getnodeHeatSet().contains(currentnode) || downstreamheatVector.getnodeHeatSet().contains(currentnode)){
              finalLinkerMap.keySet().remove(currentnode); // connecting = all_linkers.difference(all_inputs 
          }
        }
        Set<CyNode> linkerNodes = finalLinkerMap.keySet();
        List<CyNode> newnodes = new ArrayList<CyNode>();
        List<CyEdge> edges = currentnetwork.getEdgeList();
        List<CyEdge> newedges = new ArrayList<CyEdge>();
        
        for(Object currentnode : upstreamheatVector.getnodeHeatSet()){
            newnodes.add((CyNode) currentnode);
        }
        for(Object currentnode : downstreamheatVector.getnodeHeatSet()){
            newnodes.add((CyNode) currentnode);
        }
        for(CyNode currentnode : linkerNodes){
            newnodes.add(currentnode);
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
        if(isKernel){
            TieDIEsubNetwork.getRow(TieDIEsubNetwork).set(CyNetwork.NAME, "TieDIE Heat diffusion subnetwork (s = "+sizeFactor+")");
        } else{
            TieDIEsubNetwork.getRow(TieDIEsubNetwork).set(CyNetwork.NAME, "TieDIE Pagerank simulation subnetwork (s = "+sizeFactor+")");
        }
        CyNetworkManager networkManager = CyActivator.networkManager;
        networkManager.addNetwork(TieDIEsubNetwork);
        CyNetworkView tiedieView = CyActivator.networkViewFactory.createNetworkView(TieDIEsubNetwork);
        CyActivator.networkViewManager.addNetworkView(tiedieView);
        updateView(currentnetworkview, tiedieView, "grid");
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
        
        
        for(CyEdge currentedge : newedges){
            tiedieView.getEdgeView(currentedge).setVisualProperty(BasicVisualLexicon.EDGE_STROKE_UNSELECTED_PAINT, Color.ORANGE);
            tiedieView.getEdgeView(currentedge).setVisualProperty(BasicVisualLexicon.EDGE_WIDTH, 6.5);
            currentnetworkview.getEdgeView(currentedge).setVisualProperty(BasicVisualLexicon.EDGE_STROKE_UNSELECTED_PAINT, Color.ORANGE);
            currentnetworkview.getEdgeView(currentedge).setVisualProperty(BasicVisualLexicon.EDGE_WIDTH, 7.5);
        }
        for(CyNode node : newnodes){
            tiedieView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_LABEL_COLOR, Color.black);
            tiedieView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_LABEL_FONT_SIZE, 15);
        }
        menu.endComputation();
        ResultsUI resultsPanel = menu.tiediecore.createResultsPanel(MapUtil.sortByValue(finalLinkerMap), currentnetwork, TieDIEsubNetwork);
        System.out.println("End---");
    }
    
   

    
    
    public Map getDiffusedMap(DiffusedHeatVector diffusedVector, String columnName){
        Map diffScoreMap = new HashMap<CyNode, Double>();
        int count=0;
        double diffScr;
        CyRow row;
        nodeTable.createColumn(columnName, Double.class, true);
        for(CyNode root: nodeList){
            diffScr = diffusedVector.getVectorOfScores().get(0, count);
            diffScoreMap.put(root, diffScr);
            row = nodeTable.getRow(root.getSUID());
            row.set(columnName, diffScr);
            count++; 
        }
        return diffScoreMap;
    }
    
    
}
