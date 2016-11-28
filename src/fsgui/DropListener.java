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


import java.awt.Color;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JComponent;



/**
 *
 * @author Andrej Borovski
 */
public class DropListener implements DropTargetListener {
    JComponent jLabel;
    AddDroppedFile adf;
    public DropListener(JComponent jl, AddDroppedFile f )
    {
        jLabel = jl; 
        adf = f;
    }
    
    @Override
    public void dragEnter(DropTargetDragEvent dtde) {
        jLabel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {

    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {
    }

    @Override
    public void dragExit(DropTargetEvent dte) {
        jLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }

    @Override
    public void drop(DropTargetDropEvent dtde) {
        dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
        Transferable transferable = dtde.getTransferable();
        jLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        DataFlavor[] flavors = transferable.getTransferDataFlavors();

        for (DataFlavor flavor : flavors) {
            
            System.out.println(flavor.getMimeType());
            try {
                if ((flavor.isFlavorJavaFileListType())||(flavor.isMimeTypeEqual("application/x-java-url"))) {
                    List<File> files = (List<File>) transferable.getTransferData(flavor);
                    
                    if (files == null) continue;
                    
                    for (File file : files) {
                        adf.setLocalFileName(file.getPath());
                        adf.setFileName(file.getCanonicalPath());
                        System.out.println("File path is '" + file.getPath() + "'.");
                    }
                    dtde.dropComplete(true);
                }
                
            } catch (UnsupportedFlavorException | IOException ex) {
                Logger.getLogger(DropListener.class.getName()).log(Level.SEVERE, null, ex);
            }
           
        }    
    }

    String getURLFromPlainText(Transferable transferable, DataFlavor flavor) {
        try {
            Object o = transferable.getTransferData(flavor);
            if (o instanceof InputStream) {
                InputStream isr = (InputStream) o;
                char cbuf[] = new char[1024];
                Arrays.fill(cbuf, ' ');
                InputStreamReader is = new InputStreamReader(isr,StandardCharsets.UTF_8);
                BufferedReader in = new BufferedReader(is);
                in.read(cbuf);
                                System.out.println("#################################");
                                String s = new String(cbuf);
                                byte bts[] = s.getBytes(StandardCharsets.UTF_8);
                                String s2 = new String(bts);
                System.out.println(s2);
                return washURL(new String(cbuf));
            }
        } catch (UnsupportedFlavorException | IOException ex) {
            Logger.getLogger(DropListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    String washURL(String InURL)
    {
        String url = InURL.trim();
        int begin;
        int end = url.length()-1;
        if ((begin = url.indexOf("http")) < 0) return null;
        for (int i = begin; i < url.length(); i++) {
            if((url.charAt(i)=='"')||(url.charAt(i)==' ')||(url.charAt(i)=='>')) {
                end = i - 1;
                break;
            }
        }
        return url.substring(begin, end);
    }

    
        
    
    
    String truncateToWhitespace(String s)
    {
        String res = new String(s);
        res.trim();
        int wsind = res.indexOf(' ');
        if (wsind < 0)
            return res;
        return res.substring(0, wsind);
    }
    
}
