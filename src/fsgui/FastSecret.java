/***************************************************************/
/* Copyright (c) 2016 Andrej Borovski [andrej.borovski@mail.com]

This file is part of FastSecret.

FastSecret is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/> */
/*********************************************************************//***************************************************************/
/* Copyright (c) 2016 Andrej Borovski [andrej.borovski@mail.com]

This file is part of FastSecret.

FastSecret is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/> */
/*********************************************************************/
package fsgui;

import fscore.Storage;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Andrej Borovski
 */
public class FastSecret extends javax.swing.JFrame {
     
    
    
    /**
     * Creates new form НовыйJFrame
     */
    public FastSecret() {
        initComponents();
        CommonComponents.SELECT.setPanel(jPanel1);
        jButton9.addActionListener(new ToolsListener(this));
        jButton2.addActionListener(new OpenStorageListener(jPanel1));
        jButton4.addActionListener(new NewFormListener(this, jTable1));
        jButton5.addActionListener(new NewFileListener(this, jTable1));
        jTable1.setModel(CommonComponents.SELECT.recordTableModel);
        setTableHeader(jTable1);
        ButtonColumn bc = new ButtonColumn(jTable1, new RowRemover(jTable1), 3);
        jTable1.setRowSorter(new TableSorter(CommonComponents.SELECT.recordTableModel));
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(250);
        jTable1.getColumnModel().getColumn(0).setResizable(true);
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(200);
        jTable1.getColumnModel().getColumn(1).setResizable(true);
        jTable1.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable1.getSelectionModel().setValueIsAdjusting(false);
        CommonComponents.SELECT.setTableSelectionListener(new TableSelectionListener(jTable1, jPanel1));
        jTable1.getSelectionModel().addListSelectionListener(CommonComponents.SELECT.tableSelectionListener);
        this.addWindowListener(new MainFrameListener(CommonComponents.SELECT.tableSelectionListener));
        jSplitPane2.setDividerLocation(0.45);
        Storage.INSTANCE.addUpdateModelListener(new ModelListener(CommonComponents.SELECT.recordTableModel));
        Commutator.INSTANCE.put("RecordTableModel", CommonComponents.SELECT.recordTableModel);
        jButton3.addActionListener(new NewTextListener(this, jTable1));
        jButton8.addActionListener(new LockStorageListener(CommonComponents.SELECT.tableSelectionListener));
        DropFrame df = new DropFrame();
        df.setVisible(true);
        CommonComponents.SELECT.setDnDFrame(df);
        //rootPane.revalidate();
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        jButton2 = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jButton8 = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        jButton9 = new javax.swing.JButton();
        jSplitPane2 = new javax.swing.JSplitPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Fast Secret");

        jToolBar1.setRollover(true);
        jToolBar1.setMaximumSize(new java.awt.Dimension(73, 32));
        jToolBar1.setMinimumSize(new java.awt.Dimension(73, 32));
        jToolBar1.setPreferredSize(new java.awt.Dimension(73, 30));

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/fileopen242.png"))); // NOI18N
        jButton2.setToolTipText("Open existing storage");
        jButton2.setBorderPainted(false);
        jButton2.setContentAreaFilled(false);
        jButton2.setFocusPainted(false);
        jButton2.setFocusable(false);
        jButton2.setHideActionText(true);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setMaximumSize(new java.awt.Dimension(28, 28));
        jButton2.setMinimumSize(new java.awt.Dimension(28, 28));
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton2);
        jButton2.getAccessibleContext().setAccessibleName("openStorageButton"); // NOI18N

        jToolBar1.add(jSeparator2);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/22.png"))); // NOI18N
        jButton3.setToolTipText("Encrypt a text note");
        jButton3.setBorderPainted(false);
        jButton3.setContentAreaFilled(false);
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButton3.setFocusable(false);
        jButton3.setHideActionText(true);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setMaximumSize(new java.awt.Dimension(28, 28));
        jButton3.setMinimumSize(new java.awt.Dimension(28, 28));
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton3);
        jButton3.getAccessibleContext().setAccessibleName("encryptNoteButton"); // NOI18N

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/noname.png"))); // NOI18N
        jButton4.setToolTipText("Encrypt a text form");
        jButton4.setBorderPainted(false);
        jButton4.setContentAreaFilled(false);
        jButton4.setFocusable(false);
        jButton4.setHideActionText(true);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setMaximumSize(new java.awt.Dimension(28, 28));
        jButton4.setMinimumSize(new java.awt.Dimension(28, 28));
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton4);
        jButton4.getAccessibleContext().setAccessibleName("encryptRecordButton"); // NOI18N

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/file124.png"))); // NOI18N
        jButton5.setToolTipText("Encrypt a file");
        jButton5.setBorderPainted(false);
        jButton5.setContentAreaFilled(false);
        jButton5.setFocusable(false);
        jButton5.setHideActionText(true);
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton5.setMaximumSize(new java.awt.Dimension(28, 28));
        jButton5.setMinimumSize(new java.awt.Dimension(28, 28));
        jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton5);
        jButton5.getAccessibleContext().setAccessibleName("encryptFileButton"); // NOI18N

        jToolBar1.add(jSeparator3);

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/stock_lock_ok.png"))); // NOI18N
        jButton8.setToolTipText("Lock records");
        jButton8.setBorderPainted(false);
        jButton8.setContentAreaFilled(false);
        jButton8.setFocusable(false);
        jButton8.setHideActionText(true);
        jButton8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton8.setMaximumSize(new java.awt.Dimension(28, 28));
        jButton8.setMinimumSize(new java.awt.Dimension(28, 28));
        jButton8.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton8);
        jButton8.getAccessibleContext().setAccessibleName("lockButton"); // NOI18N

        jToolBar1.add(jSeparator4);

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/gkdebconf24.png"))); // NOI18N
        jButton9.setToolTipText("Tools");
        jButton9.setBorderPainted(false);
        jButton9.setContentAreaFilled(false);
        jButton9.setFocusable(false);
        jButton9.setHideActionText(true);
        jButton9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton9.setMaximumSize(new java.awt.Dimension(28, 28));
        jButton9.setMinimumSize(new java.awt.Dimension(28, 28));
        jButton9.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton9);
        jButton9.getAccessibleContext().setAccessibleName("settingsButton"); // NOI18N

        jSplitPane2.setDividerSize(6);
        jSplitPane2.setToolTipText("");

        jScrollPane2.setBackground(new java.awt.Color(255, 255, 222));
        jScrollPane2.setColumnHeaderView(null);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable1.setGridColor(new java.awt.Color(255, 255, 222));
        jTable1.setMaximumSize(new java.awt.Dimension(2147483647, 6400));
        jTable1.setMinimumSize(new java.awt.Dimension(120, 6400));
        jTable1.setPreferredSize(new java.awt.Dimension(300, 6400));
        jTable1.setRowHeight(22);
        jTable1.setShowHorizontalLines(false);
        jTable1.setShowVerticalLines(false);
        jScrollPane2.setViewportView(jTable1);

        jSplitPane2.setLeftComponent(jScrollPane2);

        jPanel1.setLayout(new java.awt.BorderLayout());
        jSplitPane2.setRightComponent(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 794, Short.MAX_VALUE)
            .addComponent(jSplitPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FastSecret.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FastSecret.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FastSecret.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FastSecret.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                FastSecret fastSecret = new FastSecret();
                fastSecret.setVisible(true);
            }
        });
    }
    
    
    static void setTableHeader(JTable table) {
        JTableHeader th = table.getTableHeader();
        TableColumnModel tcm = th.getColumnModel();
        TableColumn tc = tcm.getColumn(0);
        tc.setHeaderValue( "Title" );
        tc = tcm.getColumn(1);
        tc.setHeaderValue( "Date" );
        tc = tcm.getColumn(2);
        tc.setHeaderValue( "Type" );
        tc = tcm.getColumn(3);
        tc.setHeaderValue( "Delete" );
        th.repaint();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}
