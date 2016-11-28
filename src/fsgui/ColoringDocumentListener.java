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
import java.awt.Color;
import javax.swing.event.DocumentEvent;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Andrej Borovski
 */
public class ColoringDocumentListener extends DocListener 
{
    JTextComponent jt;
    
    ColoringDocumentListener(JTextComponent c)
    {
        super();
        jt = c;
    }
            @Override
            public void insertUpdate(DocumentEvent e) {
                 paintBlue();
            }

    void paintBlue() {
        Storage.INSTANCE.setDirty(true);
        jt.setForeground(Color.BLUE);
        alert();
    }
    
    

            @Override
            public void removeUpdate(DocumentEvent e) {
                paintBlue();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                paintBlue();
            }
            
        
}
