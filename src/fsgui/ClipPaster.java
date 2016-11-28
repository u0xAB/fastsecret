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

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.JFrame;

/**
 *
 * @author Andrej Borovski
 */
public class ClipPaster {
    final JFrame frame;
    final Clipboard clipboard;
    public ClipPaster(JFrame f)
    {
        frame = f;
        clipboard = frame.getToolkit().getSystemClipboard();
    }
    
    public boolean canPaste()
    {
        Transferable clipData = clipboard.getContents(clipboard); 
        if (clipData == null) return false;
        return clipData.isDataFlavorSupported (DataFlavor.stringFlavor);
    }
    
    public String paste()
    {
        Transferable clipData = clipboard.getContents(clipboard);
            if (clipData != null) {
              try {
                if 
                  (clipData.isDataFlavorSupported
				    (DataFlavor.stringFlavor)) {
                      return (String)(clipData.getTransferData(
                        DataFlavor.stringFlavor));
                }
              } catch (UnsupportedFlavorException | IOException ufe) {
         
              }
            }
            return null;
    }

}
