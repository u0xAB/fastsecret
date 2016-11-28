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
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

/**
 *
 * @author Andrej Borovski
 */
public class ExportStorageListener implements ActionListener {
    JPasswordField p1;
    JPasswordField p2;
    ExportStorageListener(JPasswordField pwd1, JPasswordField pwd2)
    {
        p1 = pwd1;
        p2 = pwd2;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String pwd1 = new String(p1.getPassword());
        String pwd2 = new String(p2.getPassword());
        if ((pwd1.equals(pwd2))&&(!(pwd1.isEmpty()))) {
            JFileChooser fc = new JFileChooser();
            if (fc.showSaveDialog(CommonComponents.SELECT.getPanel()) == JFileChooser.APPROVE_OPTION) {
                if (fc.getSelectedFile().exists()) {
                    if (JOptionPane.showConfirmDialog((JComponent)e.getSource(), "Do you want to overwrite existing file?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, CommonComponents.createImageIcon(CommonComponents.QUESTION)) == JOptionPane.YES_OPTION)     
                        Storage.INSTANCE.cryptExternalFile(Storage.INSTANCE.getFileName(), fc.getSelectedFile().getPath(), pwd1, true);
                }
                else
                    Storage.INSTANCE.cryptExternalFile(Storage.INSTANCE.getFileName(), fc.getSelectedFile().getPath(), pwd1, true);
                Storage.INSTANCE.forgetStorage();
            }
            
        }
        else 
            JOptionPane.showMessageDialog((JComponent)e.getSource(), "Passwords do not match", "FastSecret", JOptionPane.ERROR_MESSAGE, CommonComponents.createImageIcon(CommonComponents.EXCLAMATION));
            
    }
    
}
