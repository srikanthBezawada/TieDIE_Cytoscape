package org.cytoscape.tiedie.internal;

import java.util.Properties;
import org.cytoscape.app.CyAppAdapter;

import org.osgi.framework.BundleContext;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.event.CyEventHelper;


/**
 * @author SrikanthB
 * 
 * CyActivator is the entry point of execution of app
 */


public class CyActivator extends AbstractCyActivator {
    private static CyAppAdapter appAdapter;
    private static CyEventHelper eventHelper;
    private static CyApplicationManager cyApplicationManager;
    private static CySwingApplication cyDesktopService;
    public CyServiceRegistrar cyServiceRegistrar;
    public TieDieMenuAction menuaction;
    public static CyNetworkFactory networkFactory;
    public static CyNetworkManager networkManager;
    public static CyNetworkViewFactory networkViewFactory;
    public static CyNetworkViewManager networkViewManager;
    
    public CyActivator() {
        super();
    }
    /* Each bundle app. in "Cytoscape 3.0" is an OSGi. bundle Reference : http://wiki.cytoscape.org/Cytoscape_3/AppDeveloper
       TieDIE is a bundle app.
       Start our bundle here 
    */
    @Override
    public void start(BundleContext context) throws Exception {
        String version = new String(" 0.9.5");
        this.appAdapter = getService(context, CyAppAdapter.class);
        System.out.println("TieDIE app. is loading");
        this.networkViewManager = getService(context, CyNetworkViewManager.class);
        this.networkViewFactory = getService(context, CyNetworkViewFactory.class);
        this.networkFactory = getService(context, CyNetworkFactory.class);
        this.networkManager = getService(context, CyNetworkManager.class);
        this.cyApplicationManager = getService(context, CyApplicationManager.class);
        this.cyDesktopService = getService(context, CySwingApplication.class);
        this.cyServiceRegistrar = getService(context, CyServiceRegistrar.class);
        this.eventHelper = getService(context, CyEventHelper.class);
        menuaction = new TieDieMenuAction(cyApplicationManager, "TieDIE" + version, this);
        Properties properties = new Properties();
        registerAllServices(context, menuaction, properties);
    }

    public CyServiceRegistrar getcyServiceRegistrar() {
        return cyServiceRegistrar;
    }

    public CyApplicationManager getcyApplicationManager() {
        return cyApplicationManager;
    }

    public CySwingApplication getcytoscapeDesktopService() {
        return cyDesktopService;
    }
    
    public TieDieMenuAction getmenuaction() {
        return menuaction;
    }
    
    public static CyAppAdapter getCyAppAdapter(){
        return appAdapter;
    }
    
    public static CyEventHelper getCyEventHelper(){
        return eventHelper;
    }
    
    public static CyNetworkViewManager getNetworkViewManager(){
        return networkViewManager;
    }
}
