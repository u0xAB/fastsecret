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
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

/**
 *
 * @author Andrej Borovsky
 */
public class FocusLeaveListener implements FocusListener {
    final String VERIFY_PASSWORD ="vp";
    final String CHEK_PASSWORD ="cp";
    final String CHECK_PASSWORDS_MATCH ="cpm";
    final String CHANGE_PASSWORD ="chp";
    String cmd;
    JPasswordField field;
    JPanel panel;
    FocusLeaveListener(String command, JPasswordField subField, JPanel parent) 
    {
        cmd = command;
        field = subField;
        panel = parent;
    }
    
    @Override
    public void focusGained(FocusEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void focusLost(FocusEvent e) {
        JPasswordField pf = (JPasswordField) e.getComponent();
        String text = pf.getPassword().toString();
        if (text.isEmpty()) 
            return;
        if (cmd.equals(VERIFY_PASSWORD)) {
            if (!(Storage.INSTANCE.verifyPassword(text))) {
                JOptionPane.showMessageDialog(panel, "Wrong current password", 
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                
            }
        }
    }
    
}
