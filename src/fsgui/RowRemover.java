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
import java.util.ArrayList;
import java.util.Date;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 *
 * @author Andrej Borovski
 */
public class RowRemover extends AbstractAction {
    JTable table;
    ArrayList<RecordInfo> al;
    int di;
    RowRemover(JTable t)
    {
        table = t;
    }
    
    RecordInfo findRecord(String title, String modified)
    {
        for (RecordInfo ri : al) {
            if (ri.title.equals(title)&&ri.modified.equals(modified))
                return ri;
        }
        return null;
    }
    RecordInfo getSelected()
    {
            di = table.getSelectedRow();
            String title = (String) table.getModel().getValueAt(table.convertRowIndexToModel(di), 0);
            String modified = ((Date) table.getModel().getValueAt(table.convertRowIndexToModel(di), 1)).toString();   
            return findRecord(title, modified);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (JOptionPane.showConfirmDialog(CommonComponents.SELECT.getPanel(), "Do you really want to remove this record?", "Confirm your action", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, CommonComponents.createImageIcon(CommonComponents.QUESTION)) == JOptionPane.NO_OPTION)
            return;
        al = Storage.INSTANCE.fileMap();
        Storage.INSTANCE.deleteRecord(getSelected());
        if (table.getRowCount() == 0) {
            table.repaint();  
            CommonComponents.SELECT.recordTableModel.fireTableDataChanged();
            return;
        }     
        table.addRowSelectionInterval(di-1, di-1);
        if (table.getRowCount() == 1) {
            
        }
    }
}
