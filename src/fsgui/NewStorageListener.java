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
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

/**
 *
 * @author Andrej Borovski
 */
public class NewStorageListener implements ActionListener {
    JPasswordField pf1;
    JPasswordField pf2;
    char[] pwd1;
    NewStorageListener(JPasswordField p1, JPasswordField p2 ) 
    {
        pf1 = p1;
        pf2 = p2;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        pwd1 = pf1.getPassword();
        char[] pwd2 = pf2.getPassword();
        if((pwd1.length > 0) && (Arrays.equals(pwd1, pwd2))) {
            JFileChooser fc = new JFileChooser();
            int returnVal = fc.showSaveDialog((JComponent)e.getSource());
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                if (file.exists()) {
                   if (JOptionPane.showConfirmDialog((JComponent)e.getSource(), 
                            "Overwrite existing file?",
                        "FastSecret", JOptionPane.YES_NO_OPTION, 
                        JOptionPane.QUESTION_MESSAGE, CommonComponents.createImageIcon(CommonComponents.QUESTION)) == JOptionPane.YES_OPTION) {
                        trySave(file);                    
                   }        
                } else
                    trySave(file);
            }
        } else JOptionPane.showMessageDialog((JComponent)e.getSource(), "Passwords do not match", "FastSecret", JOptionPane.ERROR_MESSAGE, CommonComponents.createImageIcon(CommonComponents.EXCLAMATION));

    }

    void trySave(File file) {
        try {
            Storage.INSTANCE.createFile(file.getCanonicalPath(), new String(pwd1));
            Storage.INSTANCE.forgetStorage();
        } catch (IOException ex) {
            Logger.getLogger(ToolsDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
           
}
