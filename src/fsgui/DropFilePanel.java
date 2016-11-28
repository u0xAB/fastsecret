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

import fscore.Storage;
import javax.swing.JPanel;

/**
 *
 * @author Andrej Borovski
 */
public class DropFilePanel extends JPanel implements AddDroppedFile {

    String local;
    String name;
    
    public DropFilePanel()
    {
        super();
    }
    @Override
    public void setLocalFileName(String fn) {
       local = fn; 
    }

    @Override
    public void setFileName(String fn) {
        name = fn;
        Storage.INSTANCE.writeFile("New File", name, "", local);
    }
    
}
