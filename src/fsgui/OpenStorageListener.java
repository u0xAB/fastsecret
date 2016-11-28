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

import fscore.Storage;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Andrej Borovski
 */
public class OpenStorageListener implements ActionListener {
    JComponent parentComponent;
    public OpenStorageListener(JComponent owner)
    {
        parentComponent = owner;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(parentComponent);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            doOpenFile(file);
        }
    }

    public void doOpenFile(File file) throws HeadlessException {
        if (file.exists()) {
            if(Storage.INSTANCE.openFileWriter(file.getPath())) {
                CommonComponents.SELECT.preferences.setDefaultStorage(file.getPath());
                CommonComponents.SELECT.tableSelectionListener.showEnterPasswordPanel();
            }
            else {
                JOptionPane.showMessageDialog(parentComponent, "File seems not to be a FastSecret file.", "FastSecret", JOptionPane.WARNING_MESSAGE, CommonComponents.createImageIcon("images/excl.png"));
            }
        } else {
            JOptionPane.showMessageDialog(parentComponent, "The selected file doesn't exist.", "FastSecret", JOptionPane.WARNING_MESSAGE, CommonComponents.createImageIcon("images/excl.png"));
            CommonComponents.SELECT.preferences.setDefaultStorage("");
            CommonComponents.SELECT.getPanel().removeAll();
            CommonComponents.SELECT.getPanel().add(new NewStorageTutorPanel());
            CommonComponents.SELECT.getPanel().revalidate();
        }
    }
}
