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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

/**
 *
 * @author Andrej Borovski
 */
public class ImportStorageListener implements ActionListener {
    JPasswordField p;
    JCheckBox c;
    boolean makeDefault;
    ImportStorageListener(JPasswordField pwd, JCheckBox cb)
    {
        this.makeDefault = true;
        p = pwd;
        c = cb;
        c.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED)
                        makeDefault = true;
                else
                    makeDefault = false;
            }
        });
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String pwd = new String(p.getPassword());
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setDialogTitle("Choose the storage file to import");
        if (fc.showOpenDialog(CommonComponents.SELECT.getPanel()) == JFileChooser.APPROVE_OPTION) {
            String input = fc.getSelectedFile().getPath();
            fc.setDialogTitle("Choose local storage file");
            if (fc.showSaveDialog(CommonComponents.SELECT.getPanel()) == JFileChooser.APPROVE_OPTION) {
                if (fc.getSelectedFile().exists()) {
                    if (JOptionPane.showConfirmDialog((JComponent)e.getSource(), "Do you want to overwrite existing file?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, CommonComponents.createImageIcon(CommonComponents.QUESTION)) == JOptionPane.YES_OPTION)
                        Storage.INSTANCE.cryptExternalFile(input, fc.getSelectedFile().getPath(), pwd, false); 
                }
                else    
                    Storage.INSTANCE.cryptExternalFile(input, fc.getSelectedFile().getPath(), pwd, false);
                if (makeDefault) {
                    Storage.INSTANCE.forgetStorage();
                    Storage.INSTANCE.openFileWriter(fc.getSelectedFile().getPath());
                }
            }
            
        }
    }
    
}
