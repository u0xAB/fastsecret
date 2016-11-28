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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 * @author Andrej Borovski
 */
class Reader {
    String _fileName;
    RandomAccessFile fin;
    int pos = 0;
    Encryptor encryptor;
    boolean hasPrivateKey;
    public byte[] readBytes()
    {
        byte[] i = new byte[4];
        try {
        fin.read(i);
        
        int l = ByteBuffer.wrap(i).getInt();
        if (l < 0)
            return new byte[0];
        byte[] b = new byte[l];
        fin.read(b);
        pos += b.length + 4;
        return b;
        } catch(IOException e) {
            return new byte[0];
        }
    }
    public Reader(String fileName)
    {
        this.hasPrivateKey = false;
        _fileName = fileName;
    }
    public FileHeader readHeader()
    {
        FileHeader fh = new FileHeader();
        try {
            fin = new RandomAccessFile(_fileName, "r"); 
                    byte b[] = null;
                    try {
                            {
                                byte[] i = new byte[4];
                                try {
                                    fin.read(i);    
        
                                    int l = ByteBuffer.wrap(i).getInt();
                                    if (l != 42)
                                        return fh;
                                    b = new byte[l];
                                    fin.read(b);
                                    pos += b.length + 4;
                                
                                } catch(IOException e) {
                                    return fh;
                                }
                            }
                        StringPacker sp = new StringPacker(b);
                        fh.magic = sp.unpack();
                        fh.creationDate = sp.unpack();
                        if (!fh.magic.equals(Constants.MAGIC))
                            throw new ClassNotFoundException();
                    } catch (ClassNotFoundException e) {
                        close();
                        return new FileHeader();
                    }
                            
        } catch (IOException i)
        {
            close();
            return new FileHeader();
        }
        
        return fh;
    }
    public StoredKey readPublicKey()
    {
        StoredKey pk = new StoredKey();
        try {
            try (FileInputStream fin = new FileInputStream(_fileName)) {

                        byte[] b;
                        b = readBytes();
                        pk.e = Encryptor.PUBLIC_EXPONENT;
                        pk.m = new BigInteger(b);
                        encryptor = new Encryptor(pk);
            }
        } catch (IOException i)
        {
            close();
            return pk;
        }
        return pk;
    }
    public BigInteger readPrivateKey(String password)
    {
        byte[] key = readBytes();
        BigInteger i = BigInteger.ZERO;
        try {
            i = Encryptor.decryptPrivateKey(key, password);
        } catch (WrongPasswordException ex) {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
            hasPrivateKey = false;
            return BigInteger.ZERO;
        }
        encryptor.setPrivateKey(i);
        hasPrivateKey = (i != null)&&(i != BigInteger.ZERO);
        return i;
    }
    public void close()
    {
        try {
        fin.close();
        } catch (IOException i) {
            
        }
    }
    public RecordInfo readRecordInfo()
    {
        RecordInfo res = new RecordInfo();
        byte[] bytes = readBytes();
        if (bytes.length == 0)
            return res;
        StringPacker sp = new StringPacker(bytes);
        res.type = sp.unpack();
        res.title = sp.unpack();
        res.date = sp.unpack();
        res.modified = sp.unpack();
        res.compressed = !"0".equals(sp.unpack());
        res.skip = Integer.parseInt(sp.unpack());
        return res;
    }
    public String readText(RecordInfo ri)
    {
        if (encryptor == null)
            return "";
        if (!encryptor.canDecrypt())
            return "";
        if (!ri.compressed)
            return new String(encryptor.RSADecrypt(readBytes()));
        return new String(unzip(encryptor.RSADecrypt(readBytes())));
    }
    public Record readRecord()
    {
        Record r = new Record();
        if (encryptor == null)
            return null;
        if (!encryptor.canDecrypt())
            return null;
        StringPacker sp = new StringPacker(encryptor.RSADecrypt(readBytes()));
        r.url = sp.unpack();
        r.login = sp.unpack();
        r.password = sp.unpack();
        r.note = sp.unpack();
        return r;
    }
    public FileRecord readFileRecord()
    {
        FileRecord r = new FileRecord();
        if (encryptor == null)
            return r;
        if (!encryptor.canDecrypt())
            return r;
        StringPacker sp = new StringPacker(encryptor.RSADecrypt(readBytes()));
        r.name = sp.unpack();
        r.note = sp.unpack();
        return r;
    }
    public boolean extractFile(String path, RecordInfo ri)
    {
        byte[] bytesOut = new byte[1024];
        byte[] b = readBytes();
        byte[] file;
        if ((b.length-256) < Constants.BIG_FILE)
            file = encryptor.RSADecrypt(b);
        else {
            BigFileEncryptor bfe = new BigFileEncryptor(b, encryptor);
            file = bfe.decryptFile(b);
        }
        if (ri.compressed)
        try { 
            ByteArrayInputStream bis = new ByteArrayInputStream(file);
            ZipInputStream zin = new ZipInputStream(bis);    
            ZipEntry zipEntry = zin.getNextEntry();
            int l;
            l = zin.read(bytesOut); 
            writeFile(path, bytesOut, l, false);
            l = zin.read(bytesOut);
            while (l > 0) {      
                writeFile(path, bytesOut, l, true);
                l = zin.read(bytesOut);
            } 
            zin.closeEntry();
            zin.close();
        } catch (IOException ex) {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
        }
        else writeFile(path, file, file.length, false);
        return true;
    }
    
    byte[] unzip(byte[] bytes)
    {
        byte[] bytesOut = new byte[1024];

            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try (ZipInputStream zin = new ZipInputStream(bis)) {
                zin.getNextEntry();
                int l;                
                l = zin.read(bytesOut);
                while (l > 0) {
                    bos.write(bytesOut, 0, l);
                    l = zin.read(bytesOut);
                }
                zin.closeEntry();
                return bos.toByteArray();
            }
            catch (IOException ex) {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
        }
            return new byte[0];
    }
    
    void writeFile(String path, byte[] bytesOut, int len, boolean append)
    {
        try {
            FileOutputStream out = new FileOutputStream(path, append);
            out.write(bytesOut, 0, len);
            out.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean EOF()
    {
        try {
            return fin.getFilePointer() >= fin.length();
        } catch (IOException ex) {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
    public void seek(int pos)
    {
        try {
            fin.seek((long)pos);
        } catch (IOException ex) {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ArrayList<RecordInfo> fileMap()
    {
        ArrayList<RecordInfo> res = new ArrayList<>();
        try {
            fin.seek(0);
        } catch (IOException ex) {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
            return res;
        }
        readHeader();
        this.readPublicKey();
        readBytes();
        while (!EOF()) {
            RecordInfo ri = this.readRecordInfo();
            if (ri.date == null)
                return res;
            int skpos = ri.skip;
            try {
                ri.skip = (int)fin.getFilePointer();
                res.add(ri);
                fin.skipBytes(skpos);
            } catch (IOException ex) {
                Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return res;
    }
    public void removeLast()
    {
        
        try {
            fin.seek(0);
        } catch (IOException ex) {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        readHeader();
        this.readPublicKey();
        readBytes();
        int LRP = 0x10000;
        while (!EOF()) {
            try {
                LRP =(int)fin.getFilePointer();
            } catch (IOException ex) {
                Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
            }
            RecordInfo ri = this.readRecordInfo();
            if (ri.date == null)
                return;
            int skpos = ri.skip;
            try {
                ri.skip = (int)fin.getFilePointer();
                fin.skipBytes(skpos);
            } catch (IOException ex) {
                Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            fin.seek(LRP);
            fin.getChannel().truncate(LRP);
        } catch (IOException ex) {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public boolean canDecrypt()
    {
        return encryptor.canDecrypt();
    }
    
    public void deletePrivateKey()
    {
        encryptor.deletePrivateKey();
    }
    
    //byte[] zipAndEncrypt()
}
