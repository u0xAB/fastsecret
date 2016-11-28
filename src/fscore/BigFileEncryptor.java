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
package fscore;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Andrej Borovski
 */
public class BigFileEncryptor {
    final int ebs = 245;
    final int dbs = 256;
    byte[] pwd1 = new byte[16];
    byte[] pwd2 = new byte[16];
    byte[] pwd3 = new byte[16];
    byte iv1[] = new byte[16];
    byte salt1[] = new byte[8];
    byte iv2[] = new byte[16];
    byte salt2[] = new byte[8];
    byte iv3[] = new byte[16];
    byte salt3[] = new byte[8];
    byte block[] = null;
    SecretKey sk1;
    SecretKey sk2;
    SecretKey sk3;
    IvParameterSpec ivspec1;
    IvParameterSpec ivspec2;
    IvParameterSpec ivspec3;
    Cipher c1;
    Cipher c2;
    Cipher c3;

    
    public BigFileEncryptor()
    {
        block = new byte[ebs];
        SecureRandom sr = new SecureRandom();
        sr.nextBytes(block);
        initCiphers(true);
    }
    
    public BigFileEncryptor(byte[] file, Encryptor e)
    {
        block = new byte[dbs];
        System.arraycopy(file, 0, block, 0, dbs);
        block = e.RSADecrypt(block);
        initCiphers(false);
    }
    
    final void initCiphers(boolean encrypt) {
        System.arraycopy(block, 0, pwd1, 0, 16);
        System.arraycopy(block, 16, iv1, 0, 16);
        System.arraycopy(block, 32, salt1, 0, 8);
        System.arraycopy(block, 40, pwd2, 0, 16);
        System.arraycopy(block, 56, iv2, 0, 16);
        System.arraycopy(block, 72, salt2, 0, 8);
        System.arraycopy(block, 80, pwd3, 0, 16);
        System.arraycopy(block, 96, iv3, 0, 16);
        System.arraycopy(block, 112, salt3, 0, 8);
        sk1 = Encryptor.makeAESKey(new String(pwd1), salt1);
        sk2 = Encryptor.makeAESKey(new String(pwd2), salt2);
        sk3 = Encryptor.makeAESKey(new String(pwd3), salt3);
        ivspec1 = new IvParameterSpec(iv1);
        ivspec2 = new IvParameterSpec(iv2);
        ivspec3 = new IvParameterSpec(iv3);
        try {
            c1 = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c2 = Cipher.getInstance("AES/CTR/NoPadding");
            c3 = Cipher.getInstance("AES/CTR/NoPadding");
            try {
                if(encrypt) {
                    c1.init(Cipher.ENCRYPT_MODE, sk1, ivspec1);
                    c2.init(Cipher.DECRYPT_MODE, sk2, ivspec2);
                    c3.init(Cipher.DECRYPT_MODE, sk3, ivspec3);
                } else {
                    c1.init(Cipher.DECRYPT_MODE, sk1, ivspec1);
                    c2.init(Cipher.ENCRYPT_MODE, sk2, ivspec2);
                    c3.init(Cipher.ENCRYPT_MODE, sk3, ivspec3);
                }
            } catch (InvalidKeyException | InvalidAlgorithmParameterException ex) {
                Logger.getLogger(BigFileEncryptor.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
            Logger.getLogger(BigFileEncryptor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public byte[] encryptFile(byte[] file, Encryptor e) 
    {
        
            byte[] data1 = new byte[(int) file.length];
            System.arraycopy(file, 0, data1, 0, data1.length);
            byte[] data2 = performCrypt(c1, data1);
            if (data2 == null)
                return null;
            data1 = performCrypt(c2, data2);
            if (data1 == null)
                return null;
            data2 = performCrypt(c3, data1);
            return addBlock(data2, e);
    }

    public byte[] decryptFile(byte[] file) 
    {
            byte[] data1 = new byte[file.length - dbs];
            System.arraycopy(file, dbs, data1, 0, data1.length);
            byte[] data2 = performCrypt(c3, data1);
            if (data2 == null)
                return null;
            data1 = performCrypt(c2, data2);
            if (data1 == null)
                return null;
            return performCrypt(c1, data1);
    }
    
    byte[] performCrypt(Cipher c, byte[] data)
    {
      
            try {
                return c.doFinal(data);    
                        } catch (IllegalBlockSizeException | BadPaddingException ex) {
                Logger.getLogger(BigFileEncryptor.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
    }
    
    byte[] addBlock(byte[] file, Encryptor e)
    {
        byte[] eb = e.RSAEncrypt(block);
        byte[] out = new byte[file.length+eb.length];
        System.arraycopy(eb, 0, out, 0, eb.length);
        System.arraycopy(file, 0, out, eb.length, file.length);
        return out;
    }
}

