package org.cytoscape.tiedie.internal;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelName;
import org.cytoscape.model.CyColumn;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyTable;
import org.cytoscape.tiedie.internal.logic.TieDieLogicThread;
import org.cytoscape.tiedie.internal.visuals.NodeAttributeListener;
import org.cytoscape.view.model.CyNetworkView;



/**
 *
 * @author SrikanthB
 * Creates new form TieDieStartMenu
 * Start button activates TieDieLogicThread.java which keeps the ball rolling
 */

public class TieDieGUI extends javax.swing.JPanel implements CytoPanelComponent {
    
    private TieDieCore tiediecore;
    public TieDieLogicThread logicThread;
    CyApplicationManager cyApplicationManager;
    CySwingApplication cyDesktopService;
    CyNetwork currentnetwork;
    CyNetworkView currentnetworkview;
    public CyActivator cyactivator;
    
    
   
    public TieDieGUI(CyActivator cyactivator,TieDieCore tiediecore) {
        this.cyactivator = cyactivator;
        this.tiediecore = tiediecore;
        cyApplicationManager = tiediecore.getCyApplicationManager();
        //this.currentnetwork = cyApplicationManager.getCurrentNetwork();
        //this.currentnetworkview = cyApplicationManager.getCurrentNetworkView();
        cyDesktopService = tiediecore.getCyDesktopService();
        initComponents();
        if(cyApplicationManager.getCurrentNetworkView() != null )
            upComboBox.setModel(new javax.swing.DefaultComboBoxModel(
                    NodeAttributeListener.getNodeAttributes(
                            cyApplicationManager.getCurrentNetworkView().getModel()).toArray()));
        upComboBox.setSelectedItem("None");
        if(cyApplicationManager.getCurrentNetworkView() != null )
            downComboBox.setModel(new javax.swing.DefaultComboBoxModel(
                    NodeAttributeListener.getNodeAttributes(
                            cyApplicationManager.getCurrentNetworkView().getModel()).toArray()));
        downComboBox.setSelectedItem("None");
    }
    
    public Icon getIcon() {
        return null;
    }
    
    public String getTitle() {
        return "TieDIE";
    }
    
    public CytoPanelName getCytoPanelName() {
        return CytoPanelName.WEST;
    }
    
    public Component getComponent() {
        return this;
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        mainPanel = new javax.swing.JPanel();
        startButton = new javax.swing.JButton();
        statusLabel = new javax.swing.JLabel();
        headingLabel = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        upComboBox = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        downComboBox = new javax.swing.JComboBox();
        jPanel4 = new javax.swing.JPanel();
        kernelRbutton = new javax.swing.JRadioButton();
        PagerankRbutton = new javax.swing.JRadioButton();
        jPanel5 = new javax.swing.JPanel();
        helpButton = new javax.swing.JButton();
        exitButton = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        subNetSizeField = new javax.swing.JTextField();

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        mainPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        startButton.setText("START executing TieDIE algorithm");
        startButton.setToolTipText("Make sure you imported the table files and selected the corresponding columns");
        startButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        statusLabel.setFont(new java.awt.Font("Tahoma", 2, 10)); // NOI18N
        statusLabel.setText("TieDIE status");

        headingLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        headingLabel.setForeground(new java.awt.Color(255, 0, 51));
        headingLabel.setText("   TieDIE ");

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Select columns to be used for diffusion"));

        jLabel1.setText("Upstream");

        upComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "None" }));

        jLabel2.setText("Downstream");

        downComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "None" }));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(upComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                        .addComponent(downComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(upComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(downComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Select way of diffusion"));

        buttonGroup1.add(kernelRbutton);
        kernelRbutton.setText("Heat Kernel");

        buttonGroup1.add(PagerankRbutton);
        PagerankRbutton.setText("Page Rank");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(kernelRbutton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(PagerankRbutton)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(kernelRbutton)
                    .addComponent(PagerankRbutton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        helpButton.setForeground(new java.awt.Color(0, 200, 0));
        helpButton.setText("Help");
        helpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpButtonActionPerformed(evt);
            }
        });

        exitButton.setForeground(new java.awt.Color(200, 0, 0));
        exitButton.setText("Exit");
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(helpButton, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 86, Short.MAX_VALUE)
                .addComponent(exitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(exitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(helpButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Select size of subnetwork, default = 1.0"));
        jPanel6.setToolTipText("As the size increases, more linker nodes are included in the subnetwork");

        subNetSizeField.setText("1.0");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(subNetSizeField, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(187, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(subNetSizeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(statusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(mainPanelLayout.createSequentialGroup()
                                    .addGap(52, 52, 52)
                                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGroup(mainPanelLayout.createSequentialGroup()
                                    .addGap(118, 118, 118)
                                    .addComponent(headingLabel)))
                            .addComponent(startButton, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(mainPanelLayout.createSequentialGroup()
                            .addGap(52, 52, 52)
                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(201, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(headingLabel)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(startButton, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(statusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(968, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(mainPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 771, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    
    
    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        tiediecore.closecore();
        tiediecore.closeTieDieStartMenu();
    }//GEN-LAST:event_exitButtonActionPerformed

    
    
    
    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
        currentnetwork = cyApplicationManager.getCurrentNetwork();
        currentnetworkview = cyApplicationManager.getCurrentNetworkView();
        //  start input validations
        String upComboSelected = inputNodeAttributeAndValidate(upComboBox);
        String downComboSelected = inputNodeAttributeAndValidate(downComboBox);
        
        if(upComboSelected == null && downComboSelected == null){
            JOptionPane.showMessageDialog(null, "Select the columns/heats to be considered for UPSTREAM & DOWNSTREAM after importing TABLE files", "Import TABLE files along with network file", JOptionPane.WARNING_MESSAGE);
            statusLabel.setText("TieDIE status");
            return;
        }
        
        if(upComboSelected == null){
            JOptionPane.showMessageDialog(null, "Select the columns/heats to be considered for UPSTREAM", "Import TABLE files along with network file", JOptionPane.WARNING_MESSAGE);
            statusLabel.setText("TieDIE status");
            return;
        }
        
        if(downComboSelected == null){
            JOptionPane.showMessageDialog(null, "Select the columns/heats to be considered for DOWNSTREAM", "Import TABLE files along with network file", JOptionPane.WARNING_MESSAGE);
            statusLabel.setText("TieDIE status");
            return;
        }
        /*
        CyTable nTable = currentnetwork.getDefaultNodeTable();
        if(nTable.getColumn(upComboSelected).getType() == Number.class && nTable.getColumn(downComboSelected).getType() == Number.class){
        } else{
             if((nTable.getColumn(upComboSelected).getType() != Number.class) || (nTable.getColumn(downComboSelected).getType() != Number.class)){
                JOptionPane.showMessageDialog(null, "Select the appropriate columns/heats to be considered for UPSTREAM & DOWNSTREAM", "Import TABLE files along with network file", JOptionPane.WARNING_MESSAGE);
                statusLabel.setText("TieDIE status");
                return;  
            }
            
            if((nTable.getColumn(upComboSelected).getType() != Number.class) && (nTable.getColumn(downComboSelected).getType() == Number.class)){
                JOptionPane.showMessageDialog(null, "Select the appropriate columns/heats to be considered for UPSTREAM", "Import TABLE files along with network file", JOptionPane.WARNING_MESSAGE);
                statusLabel.setText("TieDIE status");
                return;
            }
            
            if((nTable.getColumn(upComboSelected).getType() == Number.class) && (nTable.getColumn(upComboSelected).getType() != Number.class)){
                JOptionPane.showMessageDialog(null, "Select the appropriate columns/heats to be considered for UPSTREAM", "Import TABLE files along with network file", JOptionPane.WARNING_MESSAGE);
                statusLabel.setText("TieDIE status");
                return;
            }
        }
        */    
        
        if(kernelRbutton.isSelected() || PagerankRbutton.isSelected()){
        } else{
            JOptionPane.showMessageDialog(null, "Select either HeatKernel (or) PageRank based diffusion", "Select the WAY of diffusion", JOptionPane.WARNING_MESSAGE);
            statusLabel.setText("TieDIE status");
            return;
        }
        //  end input validations
        
        statusLabel.setText("TieDIE status: Started executing TieDIE");
        boolean isKernel = kernelRbutton.isSelected();
        logicThread = new TieDieLogicThread(currentnetwork, currentnetworkview, upComboSelected, downComboSelected, textFieldValidate(subNetSizeField),isKernel);
        logicThread.start();
        try {
            if(isKernel){
                Thread.sleep(2500);
            }else{
                Thread.sleep(3000);
            }   
        } catch (InterruptedException ex) {
            Logger.getLogger(TieDieGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        statusLabel.setText("<html> TieDIE status: SubNetwork -> Control panel<br>You might want to recompute with different inputs <html>");
        buttonGroup1.clearSelection();
    }//GEN-LAST:event_startButtonActionPerformed


    
    
    
    
    
    private void helpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpButtonActionPerformed
        TieDieHelp help = new TieDieHelp();		        
        help.setText(1);		
        help.setVisible(true);
        
      
    }//GEN-LAST:event_helpButtonActionPerformed

    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton PagerankRbutton;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox downComboBox;
    private javax.swing.JButton exitButton;
    private javax.swing.JLabel headingLabel;
    private javax.swing.JButton helpButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JRadioButton kernelRbutton;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton startButton;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JTextField subNetSizeField;
    private javax.swing.JComboBox upComboBox;
    // End of variables declaration//GEN-END:variables
    
    public double textFieldValidate(javax.swing.JTextField jtf){
        String sizeString = jtf.getText();
        double size=0.0;
        try{
            size = Double.parseDouble(sizeString);
        } catch(NumberFormatException e){
            System.out.println("Number format exception");
        } catch(NullPointerException e){
            System.out.println("String is null");
        }
        /*
        if(size == 0.0)
            size = 1.0;
        */
        return size;
    }
    
    
    public String inputNodeAttributeAndValidate(javax.swing.JComboBox jcb){
        String nodeAttribute = jcb.getSelectedItem().toString();
        if(nodeAttribute.equals("None")){
            nodeAttribute = null;
            return nodeAttribute;
        } else {
            System.out.println("using "+nodeAttribute+" as node attribute.");
            return nodeAttribute;
        }
    }
    
    public javax.swing.JComboBox getNodeAttributeComboBox1(){
        return upComboBox;
    }

    public javax.swing.JComboBox getNodeAttributeComboBox2(){
        return downComboBox;
    }
    
}
