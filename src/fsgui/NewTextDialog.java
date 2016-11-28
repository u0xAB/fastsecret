/**
 * ************************************************************
 */
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
/**
 * ******************************************************************
 */
package fsgui;

import fscore.RecordInfo;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;


/**
 *
 * @author artem
 */
public class NewTextDialog extends javax.swing.JDialog {
    RecordInfo ri;
    TextPanel tp;
    /**
     * Creates new form NewTextDialog
     */
    public NewTextDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        ri = new RecordInfo();
        tp = new TextPanel(false);
        jPanel2.setVisible(true);
        this.jPanel2.add(tp);
        tp.setVisible(true);
        tp.setLabelText();
        this.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
            }

            @Override
            public void componentMoved(ComponentEvent e) {
            }

            @Override
            public void componentShown(ComponentEvent e) {
            }

            @Override
            public void componentHidden(ComponentEvent e) {
               CommonComponents.SELECT.clearDropPane();
            }
 });
    }
    
    public String getRecordTitle()
    {
        return tp.getTitle().isEmpty() ? "New Text" : tp.getTitle();
    }

    public String getText()
    {
        return tp.getText();
    }
    
    public void setText(String text)
    {
        tp.setText(text);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();

        setTitle("New Text");
        setMinimumSize(new java.awt.Dimension(404, 320));
        setModal(true);

        jPanel2.setLayout(new java.awt.BorderLayout());
        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}