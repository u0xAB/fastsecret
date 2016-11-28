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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JTable;

/**
 *
 * @author Andrej Borovski
 */
public class NewFileListener implements ActionListener {
    JFrame frame;
    JTable table;   

    NewFileListener(JFrame parent, JTable t) 
    {
        frame = parent;
        table = t;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (Storage.INSTANCE.fileSet()) {
            NewFileDialog nfd = new NewFileDialog(frame, true);
            nfd.setVisible(true);
            String title = nfd.getRecordTitle();
            String file = nfd.getFilePath();
            String local = nfd.getFilePath();
            String note = nfd.getNote();
            if (file == null) return;
            if(!(file.isEmpty())) {
                title = title.isEmpty() ? "New Record" : title;
                Storage.INSTANCE.writeFile(title, file, note, local);
                table.setRowSelectionInterval(table.getRowCount()-1,table.getRowCount()-1);
                
            }
        }
    }
}
