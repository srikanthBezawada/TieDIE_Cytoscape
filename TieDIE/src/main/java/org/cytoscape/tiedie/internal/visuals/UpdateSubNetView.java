
package org.cytoscape.tiedie.internal.visuals;

import org.cytoscape.app.CyAppAdapter;
import org.cytoscape.tiedie.internal.CyActivator;
import org.cytoscape.view.layout.CyLayoutAlgorithm;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.work.SynchronousTaskManager;
import org.cytoscape.work.TaskIterator;


public class UpdateSubNetView {
    public static void updateView(CyNetworkView origNetView, CyNetworkView view, String layoutAlgorName){
        CyAppAdapter appAdapter = CyActivator.getCyAppAdapter();
        final CyLayoutAlgorithmManager alMan = appAdapter.getCyLayoutAlgorithmManager();
        CyLayoutAlgorithm algor = null;
        if (layoutAlgorName == null) {
            algor = alMan.getDefaultLayout(); // default grid layout
        } else{
            algor = alMan.getLayout(layoutAlgorName);
        }
        if(algor == null){
            algor = alMan.getDefaultLayout();
            throw new IllegalArgumentException ("No such algorithm found '" + layoutAlgorName + "'.");
        }
        TaskIterator itr = algor.createTaskIterator(view,algor.createLayoutContext(),CyLayoutAlgorithm.ALL_NODE_VIEWS,null);
        appAdapter.getTaskManager().execute(itr);// We use the synchronous task manager otherwise the visual style and updateView() may occur before the view is relayed out:
        SynchronousTaskManager<?> synTaskMan = appAdapter.getCyServiceRegistrar().getService(SynchronousTaskManager.class);           
        synTaskMan.execute(itr); 
        view.updateView(); // update view layout part
        appAdapter.getVisualMappingManager().getVisualStyle(origNetView).apply(view); // update view style part
    }
}
