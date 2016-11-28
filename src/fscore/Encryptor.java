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

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import static java.lang.System.arraycopy;
import java.math.BigInteger;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.BadPaddingException;
import javax.crypto.SecretKeyFactory;
import java.security.spec.KeySpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKey;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andrej Borovski
 */
class Encryptor {
    RSAPublicKeySpec pk;
    RSAPrivateKeySpec sk;
    boolean hasPrivateKey = false;
    public static final BigInteger PUBLIC_EXPONENT = new BigInteger("65537");
    public static byte[] encryptPrivateKey(BigInteger exponent, String password)
    {
        StringPacker sp = makeNewPasswords(password);
        String p1 = sp.unpack();
        String p2 = sp.unpack();
        byte[] iv = new byte[16];
        byte[] salt = new byte[8];
        makeIVAndSalt(p1, iv, salt);
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        SecretKey secret = makeAESKey(p1, salt);
        
        byte[] iv1 = new byte[16];
        byte[] salt1 = new byte[8];
        makeIVAndSalt(p2, iv1, salt1);
        IvParameterSpec ivspec1 = new IvParameterSpec(iv1);
        SecretKey secret1 = makeAESKey(p2, salt1);
        
        byte[] iv2 = new byte[16];
        byte[] salt2 = new byte[8];
        String p3 = sp.unpack();
        makeIVAndSalt(p3, iv2, salt2);
        IvParameterSpec ivspec2 = new IvParameterSpec(iv2);
        SecretKey secret2 = makeAESKey(p3, salt2);
        
        if (secret == null)
            return new byte[0];
        try {           
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.ENCRYPT_MODE, secret, ivspec);
            Cipher cnop = Cipher.getInstance("AES/CTR/NoPadding");
            cnop.init(Cipher.DECRYPT_MODE, secret1, ivspec1);
            //System.out.println(exponent.toString());
            byte[] tmp = cnop.doFinal(addCheck(exponent.toByteArray()));
            cnop.init(Cipher.DECRYPT_MODE, secret2, ivspec2);
            tmp = cnop.doFinal(tmp);
            return c.doFinal(tmp);
        } catch (NoSuchAlgorithmException|NoSuchPaddingException|InvalidKeyException|IllegalBlockSizeException|BadPaddingException|InvalidAlgorithmParameterException e) {
           System.out.println(e);
        }
        return new byte[0];
    }
    
    static byte[] addCheck(byte input[])
    {
        byte output[] = new byte[input.length+16];
        byte prefix[] = "FastSecretV0beta".getBytes();
        System.arraycopy(prefix, 0, output, 0, 16);
        System.arraycopy(input, 0, output, 16, input.length);
        return output;
    }
    
    static StringPacker makeNewPasswords(String password) {
        StringPacker res = new StringPacker();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] p3 = md.digest(password.getBytes());
            byte[] p4 = new byte[32];
            for (int i = 0; i < 32; i ++)
                p4[i] = p3[31-i];
            byte[] sha256 = md.digest(p4);
            byte[] s = new byte[16];
            System.arraycopy(sha256, 0, s, 0, 16);
            res.packString(new String(s));
            System.arraycopy(sha256, 16, s, 0, 16);
            res.packString(new String(s));
            res.packString(new String(p3));
            return res;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Encryptor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }
    
    static boolean makeIVAndSalt(String password, byte[] iv16, byte[] salt8)
    {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] md5 = md.digest(password.getBytes());
            md5 = PermutationTables.chainPermute(md5);
            md5 = PermutationTables.chainPermute(md5);
            md5 = PermutationTables.chainPermute(md5);
            md5 = PermutationTables.chainPermute(md5);
            md5 = PermutationTables.chainPermute(md5);
            md5 = PermutationTables.chainPermute(md5);
            md5 = PermutationTables.chainPermute(md5);
            md5 = PermutationTables.chainPermute(md5);
            md5 = md.digest(md5);
            for (int i = 0; i < 4; i++)
                iv16[i] = md5[i];
            md5 = md.digest(md5);
            for (int i = 4; i < 8; i++)
                iv16[i] = md5[i];
            md5 = md.digest(md5);
            for (int i = 8; i < 12; i++)
                iv16[i] = md5[i];           
            md5 = md.digest(md5);
            for (int i = 12; i < 16; i++)
                iv16[i] = md5[i];
            md5 = md.digest(md5);
            for (int i = 0; i < 4; i++)
                salt8[i] = md5[i];
            md5 = md.digest(md5);
            for (int i = 4; i < 8; i++)
                salt8[i] = md5[i];
            return true;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Encryptor.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
            
    static SecretKey makeAESKey(String password, byte[] salt)
    {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 0x20000, 128);
            SecretKey tmp = factory.generateSecret(spec);
            return new SecretKeySpec(tmp.getEncoded(), "AES");
        }
        catch(InvalidKeySpecException|NoSuchAlgorithmException e) {
          return null;          
        }
    }
    
    static KeyPair makeRSAKeyPair()
    {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = new SecureRandom();
            kpg.initialize(2048, random);
            //kpg.initialize(1024);
            return kpg.genKeyPair();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Encryptor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static RSAPublicKeySpec getRSAPublicKeySpec(KeyPair kp)
    {
        try {
            KeyFactory fact = KeyFactory.getInstance("RSA");
            return fact.getKeySpec(kp.getPublic(), RSAPublicKeySpec.class);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            Logger.getLogger(Encryptor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static RSAPrivateKeySpec getRSAPrivateKeySpec(KeyPair kp)
    {
        try {
            KeyFactory fact = KeyFactory.getInstance("RSA");
            return fact.getKeySpec(kp.getPrivate(), RSAPrivateKeySpec.class);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            Logger.getLogger(Encryptor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    static RSAPublicKeySpec makeRSAPublicKeySpec(StoredKey key)
    {
       return new RSAPublicKeySpec(key.m, key.e);       
    }
    
    public boolean privateKeyDecrypted;

    
    public static BigInteger decryptPrivateKey(byte[] key, String password) throws WrongPasswordException
    {
        StringPacker sp = makeNewPasswords(password);
        String p1 = sp.unpack();
        String p2 = sp.unpack();
        byte[] iv = new byte[16];
        byte[] salt = new byte[8];
        makeIVAndSalt(p1, iv, salt);
        SecretKey secret = makeAESKey(p1, salt);
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        
        byte[] iv1 = new byte[16];
        byte[] salt1 = new byte[8];
        makeIVAndSalt(p2, iv1, salt1);
        SecretKey secret1 = makeAESKey(p2, salt1);
        IvParameterSpec ivspec1 = new IvParameterSpec(iv1);
        
        byte[] iv2 = new byte[16];
        byte[] salt2 = new byte[8];
        String p3 = sp.unpack();
        makeIVAndSalt(p3, iv2, salt2);
        IvParameterSpec ivspec2 = new IvParameterSpec(iv2);
        SecretKey secret2 = makeAESKey(p3, salt2);
        
        if (secret == null)
            return BigInteger.ZERO;
        try {
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.DECRYPT_MODE, secret, ivspec);
            Cipher cnop = Cipher.getInstance("AES/CTR/NoPadding");
            byte[] key1 = c.doFinal(key);
            cnop.init(Cipher.ENCRYPT_MODE, secret2, ivspec2);
            key1 = cnop.doFinal(key1);
            cnop.init(Cipher.ENCRYPT_MODE, secret1, ivspec1);
            
                return new BigInteger(checkCheck(cnop.doFinal(key1)));
            
        } catch (NoSuchAlgorithmException|IllegalBlockSizeException|NoSuchPaddingException|BadPaddingException|InvalidKeyException|InvalidAlgorithmParameterException e) {
           System.out.println(e);
        }
        return BigInteger.ZERO;
    }
    
    static byte[] checkCheck(byte input[]) throws WrongPasswordException
    {
        byte prefix[] = new byte[16];
        System.arraycopy(input, 0, prefix, 0, 16);
        if (new String(prefix).startsWith("FastSecret"))
        {    
            byte output[] = new byte[input.length - 16];
            System.arraycopy(input, 16, output, 0, output.length);
            return output;
        }
        throw new WrongPasswordException();      
    }
    
    public Encryptor(StoredKey publicKey)
    {
        this.privateKeyDecrypted = false;
        pk = makeRSAPublicKeySpec(publicKey);
    }
    
    public void setPrivateKey(BigInteger exponent)
    {
        if (exponent.equals(BigInteger.ZERO))
            return;
        sk = new RSAPrivateKeySpec(pk.getModulus(), exponent);
        hasPrivateKey = true; 
    }
    
    static byte[] makePadding(int n)
    {
        int acc = 1;
        byte[] res = new byte[n+1];
        while (acc != 0) {
            SecureRandom sr = new SecureRandom();
            sr.nextBytes(res);
            for (byte b : res) {
                b = (byte) (b%127);
                if (b == 0)
                    b = 123;
            }

            acc = res[0];
            for (int i = 1; i < res.length-1; i++) {
                if (acc == 0) {
                    res[i] =0;
                    break;
                }         
                if ((acc + res[i] < -127)||(acc + res[i] > 127)||(i == n-1)) {
                    res[i] = (byte)-acc;
                    acc += res[i];
                    if (acc == 0) {
                        res[i+1] = 0;
                        break;
                    }
                }
                else
                    acc += res[i];            
            }
            res[res.length-1] = 0;
            acc = 0;
            int count = 0;
            while (res[count] != 0) {
                acc += res[count];
                count++;
            }
            if (count == 0)
                acc = 1;
        }
        return res;
    }
    
    public byte[] RSAEncrypt(byte[] inbytes)
    {
        if (inbytes == null)
            return null;
        if (inbytes.length == 0)
            return inbytes;
        byte[] bytes = new byte[inbytes.length];
        arraycopy(inbytes, 0, bytes, 0, bytes.length);
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey key = keyFactory.generatePublic(pk);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            ByteOutputStream bos = new ByteOutputStream();
            int l = bytes.length;
            int i1 = 0;
            int i2 = 245;
            while (l > i2) {
                bos.write(cipher.doFinal(bytes, i1, i2));
                l -= i2;
                i1 += i2;
            }
            //byte[] xbytes = new byte[bytes.length];
            //arraycopy(bytes, i1, xbytes, 0, l);
            //for (int i = i1; i< xbytes.length; i++)
            //    xbytes[i] = 0;
            //bos.write(cipher.doFinal(xbytes, 0, xbytes.length));
            bos.write(cipher.doFinal(bytes, i1, l));
            return bos.toByteArray();
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeySpecException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(Encryptor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new byte[0];
    }
    
    public byte[] RSADecrypt(byte[] bytes)
    {
        if (bytes == null)
            return null;
        if (bytes.length == 0)
            return bytes;
        if (!hasPrivateKey)
            return null;
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey key = keyFactory.generatePrivate(sk);
            ByteOutputStream bos = new ByteOutputStream();
            cipher.init(Cipher.DECRYPT_MODE, key);
            int l = bytes.length;
            int i1 = 0;
            int i2 = 256;
            while (l > i2) {
                bos.write(cipher.doFinal(bytes, i1, i2));
                l -= i2;
                i1 += i2;
            }
            bos.write(cipher.doFinal(bytes, i1, l));
            return bos.toByteArray();
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeySpecException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(Encryptor.class.getName()).log(Level.SEVERE, null, ex);
        }
        hasPrivateKey = false;
        Storage.INSTANCE.ReportWrongPassword();
        return null;
    }
    public boolean canDecrypt()
    {
        return hasPrivateKey;
    }
    
    public void deletePrivateKey()
    {
        sk = null;
        hasPrivateKey = false;
    }
    
    static byte[] xorBytes(byte[] bytes, byte[] xorArg, int fromb, int froma, int l)
    {
        int i = fromb;
        int j = froma;
        int k = 0;
        while ((i < bytes.length)&&(j < xorArg.length)&&(k < l)) {
            bytes[i] ^= xorArg[j];
            i++;
            j++;
            k++;
        }
        return bytes;
    }
    
    
    
    
        static Cipher InitXtCipher(String password, boolean encrypt, int round) {
        String rpassword = password + String.valueOf(round);
        byte[] iv = new byte[16];
        byte[] salt = new byte[8];
        makeIVAndSalt(rpassword, iv, salt);
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        SecretKey secret = makeAESKey(password, salt);
        if (secret == null)
            return null;
        try {           
            Cipher c = Cipher.getInstance("AES/CTR/NoPadding");
            c.init((encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE), secret, ivspec);
            return c;
        } catch (NoSuchAlgorithmException|NoSuchPaddingException|InvalidKeyException|InvalidAlgorithmParameterException e) {
           System.out.println(e);
        }
        return null;
    }
    
    public static byte[] cryptXt(Cipher c, byte[] bytes, int l) {
        return c.update(bytes, 0, l);  
    }
    public static byte[] cryptFinalXt(Cipher c, byte[] bytes, int l) {
        try {  
             return l > 0 ? c.doFinal(bytes, 0, l) : c.doFinal();
        } catch (IllegalBlockSizeException|BadPaddingException ex) {
            Logger.getLogger(Encryptor.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return null;
    }
}   

