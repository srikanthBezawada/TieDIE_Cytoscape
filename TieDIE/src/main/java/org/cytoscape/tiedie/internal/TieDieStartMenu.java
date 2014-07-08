package org.cytoscape.tiedie.internal;

import java.awt.Component;
import java.util.Collection;
import javax.swing.Icon;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.JCheckBox;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelName;
import org.cytoscape.model.CyColumn;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyTable;
import org.cytoscape.tiedie.internal.logic.TieDieLogicThread;
import org.cytoscape.view.model.CyNetworkView;



/**
 *
 * @author SrikanthB
 * Creates new form TieDieStartMenu
 * Start button activates TieDieLogicThread.java which keeps the ball rolling
 */

public class TieDieStartMenu extends javax.swing.JPanel implements CytoPanelComponent {
    
    CyApplicationManager cyApplicationManager;
    CySwingApplication cyDesktopService;
    CyNetwork currentnetwork;
    CyNetworkView currentnetworkview;
    private TieDieCore tiediecore;
    public CyActivator cyactivator;
    public TieDieLogicThread logicThread;
    
   
    public TieDieStartMenu(CyActivator cyactivator,TieDieCore tiediecore) {
        initComponents();
        this.cyactivator = cyactivator;
        this.tiediecore = tiediecore;
        cyApplicationManager = tiediecore.getCyApplicationManager();
        cyDesktopService = tiediecore.getCyDesktopService();
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

        mainPanel = new javax.swing.JPanel();
        startButton = new javax.swing.JButton();
        helpButton = new javax.swing.JButton();
        exitButton = new javax.swing.JButton();
        otherPanel = new javax.swing.JPanel();
        headingLabel = new javax.swing.JLabel();
        statusLabel = new javax.swing.JLabel();
        jScrollBar = new javax.swing.JScrollBar();

        startButton.setText("START executing TieDIE algorithm");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        helpButton.setText("Help");
        helpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpButtonActionPerformed(evt);
            }
        });

        exitButton.setText("Exit");
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout otherPanelLayout = new javax.swing.GroupLayout(otherPanel);
        otherPanel.setLayout(otherPanelLayout);
        otherPanelLayout.setHorizontalGroup(
            otherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        otherPanelLayout.setVerticalGroup(
            otherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 199, Short.MAX_VALUE)
        );

        headingLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        headingLabel.setForeground(new java.awt.Color(255, 0, 51));
        headingLabel.setText("TieDIE GUI");

        statusLabel.setFont(new java.awt.Font("Tahoma", 2, 10)); // NOI18N
        statusLabel.setText("TieDIE status");

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGap(100, 100, 100)
                        .addComponent(headingLabel))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(otherPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(startButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                                .addComponent(helpButton, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(exitButton))
                            .addComponent(statusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(48, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addComponent(headingLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addComponent(otherPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(startButton, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(statusLabel)
                .addGap(18, 18, 18)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(helpButton)
                    .addComponent(exitButton))
                .addGap(59, 59, 59))
        );

        // Customized code from here
        CyTable nodeTable = currentnetwork.getDefaultNodeTable();
        Collection<CyColumn> columnCollection = nodeTable.getColumns();

        String presentColumnName;
        Border border = BorderFactory.createTitledBorder("Select columns for diffusion");
        otherPanel.setBorder(border);

        for(CyColumn presentColumn : columnCollection){
            presentColumnName = presentColumn.getName();
            JCheckBox check = new JCheckBox("columnName");
            otherPanel.add(check);
        }

        // Customized code ends here

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jScrollBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    
    
    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        tiediecore.closecore();
        tiediecore.closeTieDieStartMenu();
    }//GEN-LAST:event_exitButtonActionPerformed

    
    
    
    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
        statusLabel.setText("Started executing TieDIE");
     
        currentnetworkview = cyApplicationManager.getCurrentNetworkView();
        currentnetwork = currentnetworkview.getModel();
        logicThread = new TieDieLogicThread(currentnetwork, currentnetworkview);
        logicThread.start();
        
        statusLabel.setText("SubNetwork extracted is available in the Control panel");
    }//GEN-LAST:event_startButtonActionPerformed


    
    
    
    
    
    private void helpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpButtonActionPerformed
        
        
      
    }//GEN-LAST:event_helpButtonActionPerformed

    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton exitButton;
    private javax.swing.JLabel headingLabel;
    private javax.swing.JButton helpButton;
    private javax.swing.JScrollBar jScrollBar;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel otherPanel;
    private javax.swing.JButton startButton;
    private javax.swing.JLabel statusLabel;
    // End of variables declaration//GEN-END:variables
}
