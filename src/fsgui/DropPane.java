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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JTextPane;

/**
 *
 * @author Andrej Borovski
 */
public class DropPane extends JTextPane  {
    public DropPane()
    {
        super();
        setOpaque(false);
        setBackground(new Color(0,0,0,0));
        this.setEnabled(true);
//        this.setDropMode(DropMode.ON);
        this.getDocument().addDocumentListener(new DnDListener(this));
        this.setMinimumSize(new Dimension(60, 60));
        this.setMaximumSize(new Dimension(60, 60));
        CommonComponents.SELECT.setDropPane(this);
    }
    @Override
        protected void paintComponent(Graphics g) {
            // set background green - but can draw image here too
            g.setColor(Color.GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());
            Image img = CommonComponents.createImageIcon("images/safe.png").getImage();
            g.drawImage(img, 0, 0, this);
            super.paintComponent(g);
            getCaret().setVisible(false);
            this.setForeground(new Color(1,1,1,1));
        }
}
