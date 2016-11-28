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

import fscore.RecordInfo;
import fscore.Storage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JTable;

/**
 *
 * @author Andrej Borovski
 */
public class NewTextListener implements ActionListener {
    
    JFrame frame;
    RecordInfo rinfo;
    JTable table;
    NewTextListener(JFrame parent, JTable t) 
    {
        frame = parent;
        table = t;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (Storage.INSTANCE.fileSet()) {
            NewTextDialog ntd = new NewTextDialog(frame, true);
            showDialog(ntd);
        }
    }
    
    public void addText(String txt)
    {
       if (Storage.INSTANCE.fileSet()) {
            NewTextDialog ntd = new NewTextDialog(CommonComponents.SELECT.getDnDFrame(), true);
            ntd.setText(txt);
            showDialog(ntd);
           // CommonComponents.SELECT.clearDropPane();
        } 
    }
    
    void showDialog(NewTextDialog ntd) {
        ntd.setVisible(true);
        String s = ntd.getText();
        if(!s.isEmpty()) {
            String t = ntd.getRecordTitle();
            t = t.isEmpty() ? "New Text Record" : t;
            Storage.INSTANCE.writeText(t, s, "");
            if (table != null)
                table.setRowSelectionInterval(table.getRowCount()-1,table.getRowCount()-1);
        }
    }
    
}
