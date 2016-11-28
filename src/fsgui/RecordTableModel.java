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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.prefs.Preferences;
import javax.swing.Icon;
import javax.swing.table.AbstractTableModel;


/**
 *
 * @author Andrej Borovski
 */
public class RecordTableModel extends AbstractTableModel {
    public Icon fileIcon;
    public Icon recIcon;
    public Icon noteIcon;
    String title = "";
    String date = "";
    ArrayList<RecordInfo> al;
    Preferences prefs;
    
    
    
    static int monthToInt(String month)
    {
        switch(month) {
            case "Jan" :
                return 0;
            case "Feb" :
                return 1;
            case "Mar" :
                return 2;
            case "Apr" :
                return 3;
            case "May" :
                return 4;
            case "Jun" :
                return 5;
            case "Jul" :
                return 6;
            case "Aug" :
                return 7;
            case "Sep" :
                return 8;
            case "Oct" :
                return 9;
            case "Nov" :
                return 10;
            case "Dec" :
                return 11;
            default :
                return -1;
        }
    }
    public static Date dateFromString(String s)
    {
        String[] parts = s.split(" ");
        String wDay = parts[0];
        String month = parts[1];
        int mDay = Integer.parseInt(parts[2]);
        String[] time = parts[3].split(":");
        int hour = Integer.parseInt(time[0]);
        int minute = Integer.parseInt(time[1]);
        int second = Integer.parseInt(time[2]);
        String zone = parts[4];
        int year = Integer.parseInt(parts[5]);
        Calendar c = Calendar.getInstance();
        //c.setTimeZone(new SimpleTimeZone(0, "MSC"));
        c.set(year, monthToInt(month), mDay, hour, minute, second);
        return c.getTime();
    }
    public RecordTableModel(Icon fi, Icon ri, Icon ni)
    {
        super();
        fileIcon = fi;
        recIcon = ri;
        noteIcon = ni;
        al = new ArrayList<>();
    }
    public void clear()
    {
        al.clear();
    }
    public void addRecord(RecordInfo ri)
    {
        al.add(ri);
        fireTableRowsInserted(al.size()-1, al.size()-1);
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) { 
    //System.out.println("isCellEditable: " + rowIndex + " " + columnIndex);
    if(columnIndex == 3)
        return true;
    else
        return false;
}   
    
    @Override
    public int getRowCount() {
        return al.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0)
            return al.get(rowIndex).title;
        if (columnIndex == 1)
            return dateFromString(al.get(rowIndex).modified);
        if (columnIndex == 2) {
            switch(al.get(rowIndex).type) {
                case "File":
                    return fileIcon;
                case "Record":
                    return recIcon;
                case "Text":
                    return noteIcon;
                default:
                    break;
            }
            
        }
        if (columnIndex == 3)
            return "X";
        return "";
    }
    @Override
    public Class<?> getColumnClass(int column) {
        switch (column) {
            case 1: return Date.class;
            case 2: return Icon.class;
            default: 
                return String.class;
        }
    }
    
    public String getTip(int row) {
        if (getValueAt(row, 2).equals(fileIcon))
            return "File";
        if (getValueAt(row, 2).equals(recIcon))
            return "Record";
        if (getValueAt(row, 2).equals(noteIcon))
            return "Text note";
        return "Text";
    }
    
    RecordInfo findRecord(String title, String modified)
    {
        for (RecordInfo ri : al) {
            if (ri.title.equals(title)&&ri.modified.equals(modified))
                return ri;
        }
        return null;
    }
    public RecordInfo _findRecordByName(String title)
    {
        for (RecordInfo ri : al) {
            if (ri.title.equals(title))
                return ri;
        }
        return null;
    }
}
