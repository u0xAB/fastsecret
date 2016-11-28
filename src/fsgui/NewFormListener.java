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
public class NewFormListener implements ActionListener {
    JFrame frame;
    JTable table;
    FSPreferences prefs;
    NewFormListener(JFrame parent, JTable t) 
    {
        frame = parent;
        table = t;
    }
 
    @Override
    public void actionPerformed(ActionEvent e) {
        if (Storage.INSTANCE.fileSet()) {
            NewFormDialog nfd = new NewFormDialog(frame, true);
            showDialog(nfd);
        }    
    }
    
    public void addForm(String url)
    {
        if (Storage.INSTANCE.fileSet()) {
            NewFormDialog nfd = new NewFormDialog(frame, true);
            nfd.setURL(url);
            showDialog(nfd);
        }    
    }
    
    void showDialog(NewFormDialog nfd) {
        nfd.setVisible(true);
        String title = nfd.getRecordTitle();
        String url = nfd.getURL();
        String login = nfd.getLogin();
        String password = nfd.getPasswod();
        String note = nfd.getNote();
        if((!url.isEmpty())||(!login.isEmpty())||(!note.isEmpty())) {
            title = title.isEmpty() ? "New Record" : title;
            Storage.INSTANCE.writeRecord(title, url, login, password, note, "");
            if (table != null)
                table.setRowSelectionInterval(table.getRowCount()-1,table.getRowCount()-1);
        }
    }
    
}
