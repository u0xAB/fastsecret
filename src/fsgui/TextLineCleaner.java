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

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JPasswordField;

/**
 *
 * @author Andrej Borovski
 */
public class TextLineCleaner implements FocusListener {

    @Override
    public void focusGained(FocusEvent e) {
        JPasswordField pf = (JPasswordField) e.getComponent();
            pf.setText("");
            pf.removeFocusListener(this);
    }

    @Override
    public void focusLost(FocusEvent e) {
    }
    
}
