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

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;


/**
 *
 * @author artem
 */
public class CommonComponents {
    public final RecordTableModel recordTableModel;
    public final FSPreferences preferences;
    public TableSelectionListener tableSelectionListener = null;
    private JPanel jPanel = null;
    private JFrame dndFrame = null;
    private JTextPane dropPane;
    public final static String EXCLAMATION = "images/excl.png";
    public final static String QUESTION = "images/dialog_question.png";
    public final static String INFORMATION = "images/information.png";
    boolean firstCall;
    private CommonComponents()
    {
        preferences = new FSPreferences();
        recordTableModel = new RecordTableModel(createImageIcon("images/file124.png"), createImageIcon("images/oo1.png"), createImageIcon("images/22.png"));
    }
    
    public void setTableSelectionListener(TableSelectionListener tsl)
    {
       tableSelectionListener = tsl; 
    }
    
    public void setPanel(JPanel p)
    {
        jPanel = p;
        if (preferences.getDefaultStorage().isEmpty()) {
            p.add(new NewStorageTutorPanel());
        }
    }
    
    public JPanel getPanel()
    {
        return jPanel;
    }
    
    public void showEncryptTutorPanel()
    {
        jPanel.removeAll();
        jPanel.add(new EncryptTutorPanel());
        jPanel.revalidate();
    }
    
    public JFrame getDnDFrame()
    {
        return dndFrame;
    }
    
    public void setDnDFrame(JFrame frame)
    {
        dndFrame = frame;
    }
    
    public static ImageIcon createImageIcon(String path)
    {
        java.net.URL imgURL = FastSecret.class.getClassLoader().getResource(path);
        if (imgURL != null) {
        return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    
    public void setDropPane(JTextPane p)
    {
        dropPane = p;
    }
    
    public void clearDropPane()
    {
        if (dropPane!= null)
            new Runnable() {
            @Override
            public void run() {
                if (dropPane.getDocument() != null)
                dropPane.setText(null);            }
        }.run();
            //dropPane.setText(null);
    }
    
    public final static CommonComponents SELECT = new CommonComponents();
}
