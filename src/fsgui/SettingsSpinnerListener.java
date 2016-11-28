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

import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Andrej  Borovski
 */
public class SettingsSpinnerListener implements ChangeListener {
    
    JTabbedPane pane;
    JSpinner jSpinner1;
    JSpinner jSpinner2;
    SettingsSpinnerListener(JTabbedPane pn, JSpinner jSp1, JSpinner jSp2)
    {
        pane = pn;
        jSpinner1 = jSp1;
        jSpinner2 = jSp2;
    }
    
    @Override
    public void stateChanged(ChangeEvent e) {
        {
                jSpinner1.setValue(CommonComponents.SELECT.preferences.getDefaultPasswordLength());
                jSpinner1.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        int slen = (Integer) jSpinner1.getValue();
                        CommonComponents.SELECT.preferences.setDefaultPasswordLength(slen);
                        jSpinner1.setValue(CommonComponents.SELECT.preferences.getDefaultPasswordLength());
                    }
                });     
              
                jSpinner2.setValue(CommonComponents.SELECT.preferences.getAutoLockTimeout());
                jSpinner2.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        int timeout = (Integer) jSpinner2.getValue();
                        CommonComponents.SELECT.preferences.setAutoLockTimeout(timeout);
                        jSpinner2.setValue(CommonComponents.SELECT.preferences.getAutoLockTimeout());
                    }
                });
            }
    }  
}
