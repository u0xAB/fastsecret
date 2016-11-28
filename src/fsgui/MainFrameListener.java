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
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

/**
 *
 * @author artem
 */
public class MainFrameListener implements WindowListener {

    TableSelectionListener listener;
    int counter;
    MainFrameListener(TableSelectionListener l)
    {
        listener = l;
        counter = 1;
    }
    
    @Override
    public void windowOpened(WindowEvent e) {
        System.out.println("opened");
        if (CommonComponents.SELECT.preferences.getDefaultStorage().isEmpty()) {
            CommonComponents.SELECT.getPanel().removeAll();
            CommonComponents.SELECT.getPanel().add(new NewStorageTutorPanel());
            return;
        }
        
        File file = new File(CommonComponents.SELECT.preferences.getDefaultStorage());
        new OpenStorageListener(CommonComponents.SELECT.getPanel()).doOpenFile(file);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        listener.SaveUpdates();
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
        
                System.out.println("activated");
        
        
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
                System.out.println("deactivated");
                
    }
    
}
