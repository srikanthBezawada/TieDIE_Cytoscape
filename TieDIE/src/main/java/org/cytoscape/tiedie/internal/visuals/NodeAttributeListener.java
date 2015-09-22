package org.cytoscape.tiedie.internal.visuals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.cytoscape.application.events.SetCurrentNetworkEvent;
import org.cytoscape.application.events.SetCurrentNetworkListener;
import org.cytoscape.model.CyColumn;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.tiedie.internal.TieDieCore;
import org.cytoscape.tiedie.internal.TieDieGUI;

public class NodeAttributeListener implements SetCurrentNetworkListener{

    @Override
    public void handleEvent(SetCurrentNetworkEvent scne) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        CyNetwork network = scne.getNetwork();
        //JOptionPane.showMessageDialog(null, network.getEdgeCount()+" : nodes", "Spanning Tree", JOptionPane.ERROR_MESSAGE);
        
        TieDieGUI menu = TieDieCore.getTiDieStartMenu();
        //menu.getEdgeAttributeComboBox().getModel().getSelectedItem().toString();
        menu.getNodeAttributeComboBox1().setModel(new javax.swing.DefaultComboBoxModel(getNodeAttributes(network).toArray()));
        menu.getNodeAttributeComboBox1().setSelectedItem("None");
        
        menu.getNodeAttributeComboBox2().setModel(new javax.swing.DefaultComboBoxModel(getNodeAttributes(network).toArray()));
        menu.getNodeAttributeComboBox2().setSelectedItem("None");
    }

    public static List<String> getNodeAttributes(CyNetwork network){
        Collection<CyColumn> nodeColumns = network.getDefaultNodeTable().getColumns();
        List<String> columnsToAdd = new ArrayList<String>(1);
        
        int i = 0;
        for(CyColumn c:nodeColumns){
            if(!c.isPrimaryKey()){
                columnsToAdd.add(c.getName());
                i++;
            }
        }
        columnsToAdd.add("None");
        
        return columnsToAdd;
    }
    
}
