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

import fscore.FileRecord;
import fscore.Record;
import fscore.RecordInfo;
import fscore.Storage;
import java.awt.Component;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.JTextComponent;

/**
 *
 * @author artem
 */
public class TableSelectionListener implements ListSelectionListener {
        JTable table;
        JPanel panel;
        ListSelectionEvent evt;
        Component inputPanel;
        RecordInfo oldri = null;
        public TableSelectionListener(JTable t, JPanel p)
        {
           table = t;
           panel = p;
           Storage.INSTANCE.addWrongPasswordListener(() -> {
            JOptionPane.showMessageDialog(panel, "Wrong password", "Input Error", JOptionPane.ERROR_MESSAGE, CommonComponents.createImageIcon(CommonComponents.EXCLAMATION));
            EnterPasswordPanel ep = new EnterPasswordPanel();
            panel.removeAll();
            panel.add(ep);
            panel.revalidate();
            panel.setFocusable(true);
            ep.AddSelectionListener(this, evt);
            ep.setFocus();
        });
        }
        
        RecordInfo getSelected()
        {
            String title = (String) CommonComponents.SELECT.recordTableModel.getValueAt(table.convertRowIndexToModel(table.getSelectedRow()), 0);
            String modified = ((Date)CommonComponents.SELECT.recordTableModel.getValueAt(table.convertRowIndexToModel(table.getSelectedRow()), 1)).toString();   
            return CommonComponents.SELECT.recordTableModel.findRecord(title, modified);
        }    

        @Override
        public void valueChanged(ListSelectionEvent e) {
            panel.removeAll();
            
            if (!(Storage.INSTANCE.canDecrypt())) {    
                showEnterPasswordPanel(e);
                evt = e;
                return;
            }
            AutolockTimer.INSTANCE.start(CommonComponents.SELECT.preferences.getAutoLockTimeout());
            if ((Storage.INSTANCE.isDirty())&&(oldri != null)) {
                Storage.INSTANCE.setDirty(false);
                if ("Text".equals(oldri.type)) 
                    saveTextData(oldri.title, oldri.date);
                if ("Record".equals(oldri.type)) {
                    saveRecord(oldri.title, oldri.date);    
                }
                if ("File".equals(oldri.type)) {
                    saveStoredFileRecord(oldri.title, oldri.date, oldri.compressed);
                }
                if (Storage.INSTANCE.deleteRecord(oldri) == false);
                //int r = table.getSelectedRow();
                table.addRowSelectionInterval(table.getRowCount()-1, table.getRowCount()-1);
                //table.addRowSelectionInterval(r, r);
                return;
            }
                
            RecordInfo ri = getSelected();
            oldri = ri;
            if (ri != null) {
                String title = ri.title;
                if ("Text".equals(ri.type)) {
                    String text = Storage.INSTANCE.readText(ri);
                    if (text != null) {
                        TextPanel tp = new TextPanel(true);
                        tp.setTitle(title);
                        tp.setText(text);
                        inputPanel = tp;
                        panel.add(tp);
                        //tp.getTextField().getDocument().addDocumentListener(AutolockTimer.INSTANCE.textChangedListener);
                        JTextComponent jt = (JTextField) tp.getTextField();
                        jt.getDocument().addDocumentListener(new ColoringDocumentListener(jt)); 
                        
                        //tp.getTextField().getDocument().addDocumentListener(AutolockTimer.INSTANCE.textChangedListener);
                        jt = tp.getTextPane();
                        jt.getDocument().addDocumentListener(new ColoringDocumentListener(jt));          

                        
                    } else
                        showEnterPasswordPanel(e);
                }
                if ("Record".equals(ri.type)) {
                    Record r = Storage.INSTANCE.readRecord(ri);
                    if(r != null) {
                        FormPanel fp = new FormPanel(true);
                        fp.setTitle(ri.title);
                        fp.setURL(r.url);
                        fp.setLogin(r.login);
                        fp.setPassword(r.password);
                        fp.setNote(r.note);
                        inputPanel = fp;
                        panel.add(fp);
                        fp.getTextField1().getDocument().addDocumentListener(new ColoringDocumentListener(fp.getTextField1()));
                        fp.getTextField2().getDocument().addDocumentListener(new ColoringDocumentListener(fp.getTextField2()));
                        fp.getTextField3().getDocument().addDocumentListener(new ColoringDocumentListener(fp.getTextField3()));
                        fp.getTextField4().getDocument().addDocumentListener(new ColoringDocumentListener(fp.getTextField4()));
                        fp.getTextPane1().getDocument().addDocumentListener(new ColoringDocumentListener(fp.getTextPane1()));
                    } else
                        showEnterPasswordPanel(e);
                }
                if ("File".equals(ri.type)) {
                    FileRecord fr = Storage.INSTANCE.readFileRecord(ri);
                    if(fr != null) {
                        FilePanel fp = new FilePanel(ri, true);
                        fp.setTitle(ri.title);
                        fp.setFileName(fr.name);
                        fp.setNote(fr.note);
                        inputPanel = fp;
                        fp.getTextPane().getDocument()
                                .addDocumentListener(new ColoringDocumentListener(fp.getTextPane()));
                        fp.getTitleField().getDocument()
                                .addDocumentListener(new ColoringDocumentListener(fp.getTitleField()));
                        panel.add(fp);
                    }
                }
                panel.revalidate();
            } else
                showEnterPasswordPanel(e);
        }

        void saveRecord(String title, String date) {
            if (date == null) date = new Date().toString();
            title = ((FormPanel)inputPanel).getTitle();
            if (title.isEmpty()) title = "New Record";
            String note = ((FormPanel)inputPanel).getNote();
            String url = ((FormPanel)inputPanel).getURL();
            String login = ((FormPanel)inputPanel).getLogin();
            String rpassword = ((FormPanel)inputPanel).getPassword();
            Storage.INSTANCE.writeRecord(title, url, login, rpassword, note, date);
        }
        void saveStoredFileRecord(String title, String date, boolean compressed)
        {
            if (date == null) date = new Date().toString();
            if (title.isEmpty()) title = "New File";
            title = ((FilePanel) inputPanel).getTitle();
            String fileName = ((FilePanel) inputPanel).getFileName();
            String fnote = ((FilePanel)inputPanel).getNote();
            byte[] bytes = Storage.INSTANCE.readStoredFile();
            Storage.INSTANCE.writeFile(title, fileName, fnote, compressed, bytes);
        }
        
        void saveTextData(String title, String date) {
            if (date == null) date = new Date().toString();
            title = ((TextPanel)inputPanel).getTitle();
            if (title.isEmpty()) title = "Some Text";
            Storage.INSTANCE.writeText(title, ((TextPanel)inputPanel).getText(), date);
        }

    public void SaveUpdates() 
    {
        if (Storage.INSTANCE.isDirty()) {
            RecordInfo ri = getSelected();
            Storage.INSTANCE.deleteRecord(oldri);
            if ("Text".equals(ri.type))
                saveTextData(ri.title, ri.date);
            if ("Record".equals(ri.type))
                saveRecord(ri.title, ri.date);
            if ("File".equals(ri.type))
                saveStoredFileRecord(ri.title, ri.date, ri.compressed);
        }
    }
        
    public void showEnterPasswordPanel(ListSelectionEvent e) {
        if (Storage.INSTANCE.canDecrypt())
            return;
        panel.removeAll();
        EnterPasswordPanel ep = new EnterPasswordPanel();
        panel.add(ep);
        panel.revalidate();
        ep.AddSelectionListener(this, e);
    }
    public void showEnterPasswordPanel() 
    {
        CommonComponents.SELECT.getPanel().removeAll();
        ListSelectionEvent e = new  ListSelectionEvent(this, 0, 0, false);
        if (CommonComponents.SELECT.recordTableModel.getRowCount() > 0) {
            table.addRowSelectionInterval(0, 0);
            showEnterPasswordPanel(e);
        } else CommonComponents.SELECT.showEncryptTutorPanel();
    }
} 
