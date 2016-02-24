package org.cytoscape.tiedie.internal;

import java.util.Map;
import java.util.Properties;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.events.SetCurrentNetworkListener;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.application.swing.CytoPanel;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelName;
import org.cytoscape.application.swing.CytoPanelState;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.tiedie.internal.results.ResultsUI;
import org.cytoscape.tiedie.internal.visuals.NodeAttributeListener;
import org.cytoscape.view.model.CyNetworkView;


/**
 *
 * @author SrikanthB
 * TieDieCore activates TieDieStartMenu
 */


public class TieDieCore {

    public CyNetwork network;
    public CyNetworkView view;
    public CyApplicationManager cyApplicationManager;
    public CySwingApplication cyDesktopService;
    public CyServiceRegistrar cyServiceRegistrar;
    public CyActivator cyactivator;
    private static TieDieGUI startmenu;

    public TieDieCore(CyActivator cyactivator) {
        this.cyactivator = cyactivator;
        this.cyApplicationManager = cyactivator.getcyApplicationManager();
        this.cyDesktopService = cyactivator.getcytoscapeDesktopService();
        this.cyServiceRegistrar = cyactivator.cyServiceRegistrar;
        System.out.println("Starting GUI of TieDIE in control panel");
        startmenu = createTieDieStartMenu();
        registerServices();
        updatecurrentnetwork();
    }

    public void updatecurrentnetwork() {
        if (view == null) {
            view = null;
            network = null;
        }
        else {
            view = cyApplicationManager.getCurrentNetworkView();  
            network = view.getModel();
        }
    }

    public void closecore() {
        network = null;
        view = null;
    }

    public TieDieGUI createTieDieStartMenu() {
        startmenu = new TieDieGUI(cyactivator, this);
        cyServiceRegistrar.registerService(startmenu, CytoPanelComponent.class, new Properties());
        CytoPanel cytopanelwest = cyDesktopService.getCytoPanel(CytoPanelName.WEST);
        int index = cytopanelwest.indexOfComponent(startmenu);
        cytopanelwest.setSelectedIndex(index);
        return startmenu;
    }

    public void closeTieDieStartMenu(TieDieGUI menu) {
        cyServiceRegistrar.unregisterService(menu, CytoPanelComponent.class);
    }
    
    public ResultsUI createResultsPanel(Map flMap, CyNetwork orig, CyNetwork sub){
        ResultsUI resultsPanel = new ResultsUI(this);
        cyServiceRegistrar.registerService(resultsPanel, CytoPanelComponent.class, new Properties());
        CytoPanel panelEast = cyDesktopService.getCytoPanel(CytoPanelName.EAST);
        panelEast.setState(CytoPanelState.DOCK);
        panelEast.setSelectedIndex(panelEast.indexOfComponent(resultsPanel));
        resultsPanel.setEnabled(flMap, orig, sub);
        return resultsPanel;
    
    }
    
    public void closeCurrentResultPanel(ResultsUI resultPanel) {
        cyServiceRegistrar.unregisterService(resultPanel, CytoPanelComponent.class);
    }
    
    public CyApplicationManager getCyApplicationManager() {
        return this.cyApplicationManager;
    }

    public CySwingApplication getCyDesktopService() {
        return this.cyDesktopService;
    }
    
    public static TieDieGUI getTiDieStartMenu(){
        return startmenu;
    }
    
    void registerServices(){
        NodeAttributeListener nodeAttributeListener = new NodeAttributeListener();
        cyactivator.cyServiceRegistrar.registerService(nodeAttributeListener, SetCurrentNetworkListener.class, new Properties());
        
    }
}
