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

import java.util.Comparator;
import java.util.Date;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Andrej Borovski
 */
public class TableSorter extends TableRowSorter{
    public TableSorter(TableModel M) {
        super(M);       
        setComparator(1, new Comparator() { 
            @Override
            public int compare(Object o1, Object o2) {
                if(o1 instanceof Date)
                    return ((Date)o1).compareTo((Date)o2);
                if (o1 instanceof Integer) 
                    return ((Integer)o1).compareTo(((Integer)o2));
                return 0;
            } 
        });
       /* setComparator(2, new Comparator() { 
            @Override
            public int compare(Object o1, Object o2) { 
                return o1.toString().compareTo(o2.toString());

            } 
        });*/
        setSortable(3, false);

    }
}
