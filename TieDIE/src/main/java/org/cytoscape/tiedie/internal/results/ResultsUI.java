package org.cytoscape.tiedie.internal.results;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelName;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;
import org.cytoscape.tiedie.internal.CyActivator;
import org.cytoscape.tiedie.internal.TieDieCore;
import org.cytoscape.view.model.CyNetworkView;

public class ResultsUI extends javax.swing.JPanel  implements CytoPanelComponent{

    /**
     * Creates new form ResultsUI
     */
    
    private TieDieCore core;
    private Map flMap;
    private CyNetwork mainNet;
    private CyNetwork subNet;
    private JTable tbl;
    private List<CyNode> tempList;
    
    public ResultsUI(TieDieCore core) {
        this.core=core;
        initComponents();
    }

    public void setEnabled(Map flMap, CyNetwork main, CyNetwork sub){
        super.setVisible(false);
        this.flMap = flMap;
        this.mainNet = main;
        this.subNet = sub;
        populateResults();
        super.setVisible(true);
    }
    
    private void populateResults(){
        tbl = new JTable();
        DefaultTableModel dtm = new DefaultTableModel(0, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }   
        };

        // add header of the table
        // get the network names
        String header[] = new String[] { "Linker Node Name", "Heat Score" };

        // add header in table model     
        dtm.setColumnIdentifiers(header);
            //set model into the table object
        tbl.setModel(dtm);
        
        tempList = new LinkedList<CyNode>();
        for (Iterator it = flMap.entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            CyNode node = (CyNode)entry.getKey();
            tempList.add(node);
            Double heat = (Double)entry.getValue();
            String name = subNet.getRow(node).get(CyNetwork.NAME, String.class);
            dtm.addRow(new Object[] { name, heat });
            
        }
                
        // attach listener
        tbl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tableMouseReleased(evt);
            }
        });
        this.jScrollPane2.setViewportView(tbl);
    }
    
    private void tableMouseReleased(java.awt.event.MouseEvent evt){
        int[] selections = tbl.getSelectedRows();
        CyTable nodeTable1 = mainNet.getDefaultNodeTable();
        for(CyNode n1 : mainNet.getNodeList()){	
            CyRow row = nodeTable1.getRow(n1.getSUID());
            row.set("selected", false);
        }
        CyTable nodeTable2 = subNet.getDefaultNodeTable();
        for(CyNode n2 : subNet.getNodeList()){	
            CyRow row = nodeTable2.getRow(n2.getSUID());
            row.set("selected", false);
        }
        // select the correspoding node in the net1
        // select the correspoding node in the net2
        for(int i=0;i<selections.length; i++){
            CyNode node = tempList.get(selections[i]);
            
            nodeTable2.getRow(node.getSUID()).set("selected", true);
            String name = subNet.getRow(node).get(CyNetwork.NAME, String.class);
            for(CyNode n: mainNet.getNodeList()){
                if(mainNet.getRow(n).get(CyNetwork.NAME, String.class).equals(name)){
                    nodeTable1.getRow(n.getSUID()).set("selected", true);
                    break;
                }
            }
        }
        //update views
        Collection<CyNetworkView> c1 = CyActivator.getNetworkViewManager().getNetworkViews(mainNet);
        Iterator<CyNetworkView> it1 = c1.iterator();
        while(it1.hasNext())
            it1.next().updateView();
        Collection<CyNetworkView> c2 = CyActivator.getNetworkViewManager().getNetworkViews(subNet);
        Iterator<CyNetworkView> it2 = c2.iterator();
        while(it2.hasNext())
            it2.next().updateView();
        
    }
    
    
    public Component getComponent() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return this;
    }

    @Override
    public CytoPanelName getCytoPanelName() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return CytoPanelName.EAST;
    }

    @Override
    public String getTitle() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return "TieDIE Results";
    }

    @Override
    public Icon getIcon() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return null;
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
                .addContainerGap())
        );

        jScrollPane1.setViewportView(jPanel2);

        jButton1.setText("Close Results Panel");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setText("Linker node heat scores, click on the row to highlight.");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1)
                    .addComponent(jScrollPane1)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(41, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addGap(24, 24, 24)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 358, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        core.closeCurrentResultPanel(this);
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
