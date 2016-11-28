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

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Abdrej Borovsky
 */
public class PasteAction extends AbstractAction {
    final JTextComponent component;
    final InputMap iMap;
    final ActionMap aMap;
    final String paste = "paste";
    PasteAction(JTextComponent tc)
    {
        component = tc;
        iMap = component.getInputMap(JComponent.WHEN_FOCUSED);
        aMap = component.getActionMap();
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_PASTE, 0), paste);
    }
    
    public void Install() {
        aMap.put(paste, this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
