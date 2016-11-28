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
import java.awt.HeadlessException;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

/**
 *
 * @author artem
 */
public class ToolsDialogListener implements WindowListener {
    JPasswordField cp;
    JPasswordField np1;
    JPasswordField np2;
    RecordTableModel model;
    ToolsDialogListener(JPasswordField currentPassword, JPasswordField newPassword1, 
            JPasswordField newPassword2)
    {
        cp = currentPassword;
        np1 = newPassword1;
        np2 = newPassword2;
        
    }
    @Override
    public void windowOpened(WindowEvent e) {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowClosing(WindowEvent e) {
        String op = new String(cp.getPassword());
        String p1 = new String(np1.getPassword());
        String p2 = new String(np2.getPassword());        
        if ((!op.isEmpty())&&(Storage.INSTANCE.fileSet())) {
            boolean res = true;
            try {
                res =Storage.INSTANCE.verifyPassword(op);
            }
            catch(Exception ex)
            {
                res = false;
            }
            if (!res) {
                return;
            }
            if (p1.isEmpty()||(!(p1.equals(p2)))) {
                showErrorMessage(e);
                //System.out.println(new String(cp.getPassword()));
                //System.out.println(new String(np1.getPassword()));
                //System.out.println(np2);
                return;
            }
            if (Storage.INSTANCE.changePassword(op, p1) == false)
                showErrorMessage(e);
            else{
                Storage.INSTANCE.resetPassword();
                Storage.INSTANCE.closeFileReader();
                //Storage.INSTANCE.openFileWriter(p1);
                ((RecordTableModel)Commutator.INSTANCE.get("RecordTableModel"))
                        .fireTableDataChanged();
                JOptionPane.showMessageDialog(e.getWindow(), "The password was changed successfully.",
                "FastSecret", JOptionPane.INFORMATION_MESSAGE, CommonComponents.createImageIcon(CommonComponents.INFORMATION));
            }
        }    
    }

    void addModel(RecordTableModel rtm)
    {
        model = rtm;
    }
    void showErrorMessage(WindowEvent e) throws HeadlessException {
        JOptionPane.showMessageDialog(e.getWindow(), "The password was not changed.",
                "Error", JOptionPane.ERROR_MESSAGE, CommonComponents.createImageIcon(CommonComponents.EXCLAMATION));
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }
    
}
