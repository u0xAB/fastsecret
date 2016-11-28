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

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 *
 * @author Andrej Borovski
 */
public class DnDListener implements DocumentListener {
    JTextPane document;
    DnDListener(JTextPane doc)
    {
        super();
        document = doc;
    }
    
    
    @Override
    public void insertUpdate(DocumentEvent e) {
        try {
            String doc = e.getDocument().getText(0, e.getDocument().getLength()).trim();
            if (doc.contains(" ")) {
                NewTextListener ntl = new NewTextListener(CommonComponents.SELECT.getDnDFrame(), null);
                ntl.addText(doc);
//                document.setText("");
            } else {
                NewFormListener nfl = new NewFormListener(CommonComponents.SELECT.getDnDFrame(), null);
                nfl.addForm(doc);
//                document.setText("");
            }
            System.out.println(e.getDocument().getText(0, e.getDocument().getLength()));
           // e.getDocument().remove(0, e.getDocument().getLength());
            //document.setText("");
        } catch (BadLocationException ex) {
            Logger.getLogger(DnDListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        
    }
    
}
