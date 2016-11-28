/***************************************************************/
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
/*********************************************************************/
package fscore;

/**
 *
 * @author Andrej Borovski
 */

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.spec.RSAPublicKeySpec;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

class Writer {
    String _fileName;
    Encryptor encryptor;
    boolean append;
    Writer(String fileName)
    {
        append = true;
        encryptor = null;
        _fileName = fileName;
    }
    public boolean writeHeader(String date)
    {
        StringPacker sp = new StringPacker();
        sp.packString(Constants.MAGIC);
        if (date.isEmpty())
            sp.packString(new Date().toString());
        else
            sp.packString(date);
        append = false;
        boolean res = writeBytes(sp.getPack());
        append = true;
        return res;
    }
        
    boolean writeBytes(byte[] bytes)
    {
        if (null == bytes)
            return false;
        if (bytes.length == 0)
            return false;
        try (FileOutputStream fout = new FileOutputStream(_fileName, append)) {
            fout.write((byte)(bytes.length >> 24)&0xFF);;
            fout.write((byte)(bytes.length >> 16)&0xFF);
            fout.write((byte)(bytes.length >> 8)&0xFF);
            fout.write((byte)bytes.length&0xFF);
            fout.write(bytes);
            fout.flush();
            fout.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }
    boolean writePublicKey(StoredKey pk)
    {
        return writeBytes(pk.m.toByteArray());
    }
    public boolean createKeys(String password)
    {
        KeyPair kp = Encryptor.makeRSAKeyPair();
        RSAPublicKeySpec pks = Encryptor.getRSAPublicKeySpec(kp);
        StoredKey sk = new StoredKey();
        //k.e = pks.getPublicExponent();
        sk.m = pks.getModulus();
        writePublicKey(sk);
        byte[] key = Encryptor.encryptPrivateKey(Encryptor.getRSAPrivateKeySpec(kp).getPrivateExponent(), password);
        return writeBytes(key);
    }
    public boolean openPublicKey()
    {
        Reader r = new Reader(_fileName);
        FileHeader fh = r.readHeader();
        if (fh.magic == null) return false;
        if (!fh.magic.equals(Constants.MAGIC)) {
            r.close();
            return false;
        }
        StoredKey sk = r.readPublicKey();
        r.close();
        if (sk.e == null)
            return false;
        encryptor = new Encryptor(sk);
        return true;
    }
    public boolean writeText(String text, String title, String date)
    {
        if (encryptor == null)
           return false;
        byte[] stringBytes = StringPacker.washString(text);
        boolean compressed = false;
        byte[] bytesOut = stringBytes;
        if (stringBytes.length > 1024) {
            bytesOut = zip(stringBytes, stringBytes.length);
            if (bytesOut.length >= stringBytes.length)
                bytesOut = stringBytes;
            else
                compressed = true;
        }
        byte[] bytes = encryptor.RSAEncrypt(bytesOut);
        if (bytes.length == 0)
           return false;
        if (!writeBytes(makeRecordInfo("Text", title, bytes.length + 4, compressed, date)))
            return false;
        return writeBytes(bytes);
    }
    
    byte[] makeRecordInfo(String type, String title, int skip, boolean compressed, String date)
    {
       StringPacker sp = new StringPacker();
       sp.packString(type);
       sp.packString(title);
       sp.packString(!(date.isEmpty()) ? date : new Date().toString());
       sp.packString(new Date().toString());
       if (compressed)
           sp.packString("1");
       else
           sp.packString("0");
       sp.packString(Integer.toString(skip));
       return sp.getPack();
    }
    public boolean writeRecord(Record record, String Title, String date)
    {
       if (encryptor == null)
           return false;
       StringPacker sp = new StringPacker();
       sp.packString(record.url);
       sp.packString(record.login);
       sp.packString(record.password);
       sp.packString(record.note);
       byte[] bytes = encryptor.RSAEncrypt(sp.getPack());
       if (bytes.length == 0)
           return false;
       if (!writeBytes(makeRecordInfo("Record", Title, bytes.length + 4, false, date)))
           return false;
       return writeBytes(bytes);
    }
    
    public boolean writeFileRecord(FileRecord record, String title, String path)
    {
       if (encryptor == null)
           return false;
       StringPacker sp = new StringPacker();
       sp.packString(record.name);
       sp.packString(record.note);
       byte[] file = null;
       boolean compressed = false;
       try (FileInputStream fin = new FileInputStream(path)) {
            File f = new File(path);
            long l = f.length();
            byte[] filetmp = new byte[(int)l];
            fin.read(filetmp);
            fin.close();
            file = filetmp;
            if (l > 1024) {
                file = zip(filetmp, l);
                if ((file == null)||(l < file.length))
                    file = filetmp;
                else
                    compressed = true;
            }
        } catch (IOException e) {
          System.out.println(e.toString());  
        }
       if ((file == null)||(file.length == 0))
           return false;
       if (file.length < Constants.BIG_FILE) {
           byte[] fibytes = encryptor.RSAEncrypt(sp.getPack());
           byte[] bytes = encryptor.RSAEncrypt(file);
           if (bytes.length == 0)
               return false;
           if (!writeBytes(makeRecordInfo("File", title, fibytes.length + bytes.length + 8, compressed, "")))
               return false;
           if (!writeBytes(fibytes))
               return false;
           return writeBytes(bytes);
       } else {
           byte[] fibytes = encryptor.RSAEncrypt(sp.getPack());
           BigFileEncryptor bfe = new BigFileEncryptor();
           byte[] bytes = bfe.encryptFile(file, encryptor);
           if (!writeBytes(makeRecordInfo("File", title, fibytes.length + bytes.length + 8, compressed, "")))
               return false;
           if (!writeBytes(fibytes))
               return false;
           return writeBytes(bytes);
       }
    }   

    public boolean writeFileRecord(FileRecord record, String title, boolean compressed, byte[] bytes)
    {
        StringPacker sp = new StringPacker();
        sp.packString(record.name);
        sp.packString(record.note);
        if ((bytes == null)||(bytes.length == 0))
           return false;
        byte[] fibytes = encryptor.RSAEncrypt(sp.getPack());
        if (!writeBytes(makeRecordInfo("File", title, fibytes.length + bytes.length + 8, compressed, "")))
           return false;
       if (!writeBytes(fibytes))
           return false;
       return writeBytes(bytes);
    }
    
    byte[] zip(byte[] filetmp, long l) {
        byte[] file;
        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        try (ZipOutputStream zout = new ZipOutputStream(bas)) {
            ZipEntry zipEntry = new ZipEntry("file");
            zout.putNextEntry(zipEntry);
            zout.write(filetmp, 0, (int)l);
            zout.closeEntry();
            zout.finish();
        } catch (IOException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        }
        file = bas.toByteArray();
        return file;
    }
    byte[] zipAndEncrypt(byte[] input, RecordInfo ri, long l)
    {
        byte[] bytesToEncrypt = new byte[0];
        if (l > 1024) 
            bytesToEncrypt = zip(input, l);
        if (l < bytesToEncrypt.length) {
            bytesToEncrypt = input;
            ri.compressed = false;
        } else ri.compressed = true;
        return encryptor.RSAEncrypt(bytesToEncrypt);
    }
    
    public boolean writeRecordInfo(RecordInfo ri, Date modified)
    {
       StringPacker sp = new StringPacker();
       sp.packString(ri.type);
       sp.packString(ri.title);
       sp.packString(ri.date);
       if (modified == null)
           sp.packString(ri.modified);
       else
           sp.packString(modified.toString());
       if (ri.compressed)
           sp.packString("1");
       else
           sp.packString("0");
       sp.packString(Integer.toString(ri.skip));
       return writeBytes(sp.getPack());
    }
               
}
