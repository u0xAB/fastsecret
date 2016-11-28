package fscore;

import fsgui.CommonComponents;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;


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
/**
 *
 * @author Andrej Borovski
 */
public class Storage {
    
    public static final Storage INSTANCE = new Storage();
    
    private Storage() {}
    Writer w;
    Reader r = null;
    String _fileName = "";
    String _password = "";
    boolean dirty = false;
    
    public void setDirty(boolean b)
    {
        dirty = b;
    }
    
    public boolean isDirty()
    {
        return dirty;
    }
    
    public boolean createFile(String fileName, String password)
    {
        return openFileW(fileName, password, true);
    }
    public boolean openFileWriter(String fileName)
    {
        boolean res = openFileW(fileName, "", false);
        if (res) openFileReader(fileName, "");
        if (res) updateModel();
        return res;
    }
    boolean openFileW(String fileName, String password, boolean create) {
        _fileName = fileName;
        w = new Writer(fileName);
        if (create) {
            if (w.writeHeader("") == false) return false;
            if (w.createKeys(password) == false) return false;
        }
        return w.openPublicKey();
    }
    public boolean openFileReader(String fileName, String password)
    {
        _fileName = fileName;
        if (!(password.isEmpty()))
            _password = password;
        return __openFileReader(password);
    }

    boolean __openFileReader(String password) {
        if (r != null) r.close();
        r = new Reader(_fileName);
        FileHeader fh = r.readHeader();
        if (fh.magic == null ? Constants.MAGIC != null : !fh.magic.equals(Constants.MAGIC))
            return false;
        r.readPublicKey();
        if (!(password.isEmpty())) {
            r.readPrivateKey(password);
            return r.canDecrypt();
        } else
            return r.readBytes().length != 0;
    }
    
    public void closeFileReader()
    {
        r.close();
    }
    
    public String getFileName() 
    {
        return _fileName;
    }
    
    public boolean fileSet()
    {
        if (_fileName == null)
            return false;
        return !(_fileName.isEmpty());
    }
        
    public String readText(RecordInfo ri)
    {
        if (r == null)
            return "";
        r.seek(ri.skip);
        return r.readText(ri);
    }
    
    public byte[] readStoredFile()
    {
        return r.readBytes();
    }
    
    static boolean copyRecords(Reader r, Writer w) {
        RecordInfo ri2;
        byte[] bytes;
        byte[] bytes2= new byte[0];
        while (!r.EOF()) {
            ri2 = r.readRecordInfo();
            bytes = r.readBytes();
            if ("File".equals(ri2.type))
                bytes2 = r.readBytes();
            if (!w.writeRecordInfo(ri2, null));
            if(!w.writeBytes(bytes))
                return false;
            if ("File".equals(ri2.type))
                if (!w.writeBytes(bytes2))
                    return false;
        }
        return true;
    }
    public boolean changePassword(String oldPassword, String newPassword)
    {
        String newFN = _fileName + ".tmp";
        Reader r = new Reader(_fileName);
        Writer w = new Writer(newFN);
        FileHeader fh = r.readHeader();
        if (!w.writeHeader(fh.creationDate))
            return false;
        if (!w.writePublicKey(r.readPublicKey()))
            return false;
        if (!w.writeBytes(Encryptor.encryptPrivateKey(r.readPrivateKey(oldPassword), newPassword)))
            return false;
        if (!copyRecords(r, w))
            return false;
        r.close();
        File f = new File(_fileName);
        if (!f.delete())
            return false;
        File f1 = new File(newFN);
        return f1.renameTo(f);
    }
    static boolean compareRIs(RecordInfo ri1, RecordInfo ri2)
    {
        if (!ri1.type.equals(ri2.type))
            return false;
        return ri1.date.equals(ri2.date);
    }
    
    public ArrayList<RecordInfo> fileMap()
    {
        return r.fileMap();
    }
    
    public void resetPassword()
    {
        r.deletePrivateKey();
        _password = "";
    }
    
    public boolean canDecrypt()
    {
        return r.canDecrypt();
    }
    
    boolean copyExceptRI(RecordInfo ri)
    {
        String newFN = _fileName + ".tmp";
        Reader r = new Reader(_fileName);
        Writer w = new Writer(newFN);
        FileHeader fh = r.readHeader();
        if(!w.writeHeader(fh.creationDate))
            return false;
        if (!w.writePublicKey(r.readPublicKey()))
            return false;
        if (!w.writeBytes(r.readBytes()))
            return false;
        RecordInfo ri2 = r.readRecordInfo();
        byte[] bytes = r.readBytes();
        byte[] bytes2 = new byte[0];
        if ("File".equals(ri2.type))
            bytes2 = r.readBytes();
        while (!compareRIs(ri, ri2)) {
            if (!w.writeRecordInfo(ri2, null))
                return false;
            if (!w.writeBytes(bytes))
                return false;
            if ("File".equals(ri2.type))
                if (!w.writeBytes(bytes2))
                    return false;
            ri2 = r.readRecordInfo();
            bytes = r.readBytes();
            if ("File".equals(ri2.type))
                bytes2 = r.readBytes();            
        }
        boolean res = copyRecords(r, w);
        r.close();
        return res;
    }
    public boolean deleteRecord(RecordInfo ri)
    {
        if (!copyExceptRI(ri))
            return false;
        File f = new File(_fileName);
        if (!f.delete())
            return false;
        File f1 = new File(_fileName + ".tmp");
        boolean res = f1.renameTo(f);
        return result(res);
    }

    boolean result(boolean res) {
        updateModel();
        openFileReader(_fileName, _password);
        return res;
    }
    public boolean writeRecord(String title, String url, String login, String rpassword, String note, String date)
    {
        Record rc = new Record();
        rc.login = login;
        rc.password = rpassword;
        rc.note = note;
        rc.url = url;
        boolean res = w.writeRecord(rc, title, date);
        return result(res);
    }
    public boolean writeText(String title, String text, String date)
    {
        boolean res = w.writeText(text, title, date);
        return result(res);
    }
    public boolean writeFile(String title, String newFileName, String fileNote, String localFileName)
    {
        FileRecord fr = new FileRecord() {{name = newFileName; note = fileNote;}};
        boolean res = w.writeFileRecord(fr, title, localFileName);
        return result(res);
    }
    
    public boolean writeFile(String title, String newFileName, String fileNote, boolean compressed, byte[] bytes)
    {
        FileRecord fr = new FileRecord() {{name = newFileName; note = fileNote;}};
        boolean res = w.writeFileRecord(fr, title, compressed, bytes);
        return result(res);
    }
    
    public void tryOpenReader()
    {
        openFileReader(_fileName, _password);
        updateModel();
    }
    
    public Boolean verifyPassword(String password)
    {
       __openFileReader(password);
       return readBaseRecord() != null;
    }
    
    public Record readRecord(RecordInfo ri)
    {
        if (r == null)
            return null;
        r.seek(ri.skip);
        return r.readRecord();
    }
    
    public  String getRandomFileName()
    {
        return PasswordGenerator.generate(8, true);
    }
    
    public FileRecord readFileRecord(RecordInfo ri)
    {
        if (r == null)
            return null;
        r.seek(ri.skip);
        return r.readFileRecord();
    }
    
    public boolean saveFile(String path, RecordInfo ri)
    {
        return r.extractFile(path, ri);
    }
    
    public BaseRecord readBaseRecord()
    {
       BaseRecord res = null;
       RecordInfo ri = r.readRecordInfo();
       if (ri == null) return null;
       if ("Text".equals(ri.type)) {
          res = new TextRecord() {{ type = ri.type; title = ri.title; date = ri.date;}};
          ((TextRecord) res).Text = r.readText(ri);
       }
       if ("Record".equals(ri.type)) {
          res = r.readRecord();
          ((Record) res).type = ri.type;
          ((Record) res).title = ri.title;
          ((Record) res).date = ri.date;
       }
       if ("File".equals(ri.type)) {
          res = r.readFileRecord();
          ((FileRecord) res).type = ri.type;
          ((FileRecord) res).title = ri.title;
          ((FileRecord) res).date = ri.date;
       }
       return res;
    }
    private final ArrayList<UpdateModelListener> modelListeners = new ArrayList<>();
    public void addUpdateModelListener(UpdateModelListener listener)
    {
        modelListeners.add(listener);
    }
    
    class RunMessage extends Thread 
    {
        @Override
        public void run()
        {
            modelListeners.stream().forEach((uml) -> {
                uml.updateModel();
            }); 
        }
    }
    
    void updateModel()
    {        
        //new RunMessage().start();
        modelListeners.stream().forEach((uml) -> {
                uml.updateModel();
            });
    }
    
    private final ArrayList<WrongPasswordListener> passwordListeners = new ArrayList<>();
    public void addWrongPasswordListener(WrongPasswordListener listener)
    {
        passwordListeners.add(listener);
    }
    void ReportWrongPassword()
    {
        passwordListeners.stream().forEach((wpl) -> {
            wpl.ReportWrongPassword();
        });
    }
    
    
    boolean cryptFile(String inputFileName, String OutputFileName, String xtPassword, boolean encrypt, boolean temp, int round)
    {
      try {
            String pwd = xtPassword + String.valueOf(round)+ xtPassword.toCharArray()[0];
            FileInputStream in = new FileInputStream(inputFileName);
            FileOutputStream out = new FileOutputStream(OutputFileName);
            Cipher c = Encryptor.InitXtCipher(pwd, encrypt, round);
            byte[] bytes = new byte[0x4000];
            int l = in.read(bytes);
            while (l > 0) {
                byte[] bout;
                bout = Encryptor.cryptXt(c, bytes, l);
                if (bout != null) out.write(bout);
                l = in.read(bytes);    
            }
            {
                byte[] bout = new byte[0];
                bout = Encryptor.cryptFinalXt(c, bout, l);
                if (bout != null) out.write(bout);
            }
            out.close();
            in.close();
            if(temp) 
              new File(inputFileName).delete();
                
        } catch (IOException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;  
    }
    
    public boolean cryptExternalFile(String input, String output, String password, boolean encrypt)
    {
        String tempOutput = output+".tmp.1";
        if (!cryptFile(input, tempOutput, password, encrypt, false, encrypt ? 1 : 3)) {
            new File(tempOutput).delete();
            return false;
        }
        String tempOutput2 = output+".tmp.2";
        if (!cryptFile(tempOutput,tempOutput2, password , !encrypt, true, 2)) {
            new File(tempOutput).delete();
            new File(tempOutput2).delete();
            return false;
        }
        new File(tempOutput).delete();
        if (!cryptFile(tempOutput2, output, password, encrypt, true, encrypt ? 3 : 1)) {
            new File(tempOutput).delete();
            new File(tempOutput2).delete();
            new File(output).delete();
            return false;
        }
        new File(tempOutput2).delete();
        return true;
    }
    
    
    boolean writePasswordChecker(StoredKey sk, BigInteger pe, String password)
    {
        byte checker[] = new byte[128];
        for (int i = 0; i < 128; i++)
            checker[i] = (byte)i;
        Encryptor e = new Encryptor(sk);
        e.setPrivateKey(pe);
        byte bytes[] = e.RSAEncrypt(checker);
        Writer w = new Writer(_fileName);
        return w.writeBytes(bytes);
    }
    
    boolean readPasswordChecker(Reader r)
    {
        byte ck[] = r.readBytes();
        for (int i = 0; i < 128; i++)
            if (ck[i] != (byte)i)
                return false;
        return true;
    }
    
    public void forgetStorage()
    {
        resetPassword();
        _fileName = "";
        CommonComponents.SELECT.preferences.setDefaultStorage("");
    }
}
