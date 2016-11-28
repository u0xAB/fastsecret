/** ************************************************************ */
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
/** ****************************************************************** */
package fsgui;

import fscore.Storage;
import java.awt.Color;

/**
 *
 * @author Andrej Borovski
 */
public class AddFilePanel extends javax.swing.JPanel implements AddDroppedFile {
    String local;
    String name;
    /**
     * Creates new form AddFilePanel
     */
    public AddFilePanel() {
        initComponents();
        this.setBackground(Color.GRAY);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/folder_key48.png"))); // NOI18N
        add(jLabel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public void setLocalFileName(String fn) {
        local = fn; 
    }

    @Override
    public void setFileName(String fn) {
        name = fn;
        String names[] = name.split("\\/");
        if (names.length > 0)
            name = names[names.length-1];
        Storage.INSTANCE.writeFile(name, name, "", local);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}