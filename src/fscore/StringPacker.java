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

import java.math.BigInteger;


/**
 *
 * @author Andrej Borovski
 */
class StringPacker {
    byte[] pack;
    int pos1, pos2;
    public StringPacker()
    {
        pack = new byte[0];
        pos1 = 0;
        pos2 = 0;
    }
    public StringPacker(byte[] bytes)
    {
        pack = bytes;
    }
    
    public static byte[] washString(String s)
    {
        if (s == null)
            return new byte [0];
        byte[] stringBytes = s.getBytes();
        for(int i = 0; i < stringBytes.length-1; i++)
        if ((stringBytes[i] == 0)&&(stringBytes[i+1] == 0)) {
            stringBytes[i+1] = 10;
        }
        if ((stringBytes.length > 0)&&(stringBytes[stringBytes.length-1] == 0))
        {
          byte[] tmpBytes = new byte[stringBytes.length+2];
          System.arraycopy(stringBytes, 0, tmpBytes, 0, stringBytes.length);
          tmpBytes[tmpBytes.length-1] = 10;
          tmpBytes[tmpBytes.length-2] = 0;
          stringBytes = tmpBytes;
        }
        return stringBytes;
    }
    
    public void packString(String s)
    {
        byte[] stringBytes = washString(s);
        if (pack.length == 0) {
            pack  = stringBytes;
            if (pack.length == 0) {
                pack = new byte[2];
                pack[0] = 0;
                pack[1] = 0;
            }
        }
        else {
            byte[] dest = new byte[pack.length + stringBytes.length + 2];
            System.arraycopy(pack, 0, dest, 0, pack.length);
            dest[pack.length] = (byte) 0;
            dest[pack.length+1] = (byte) 0;
            System.arraycopy(stringBytes, 0, dest, pack.length+2, s.getBytes().length);
            pack = dest;
        }
    }
    
    public byte[] getPack()
    {
        return pack;
    }
    public String unpack()
    {
        if (pack.length == 0)
            return "";
        if (pos1 == 0)
        if ((pos1 < pack.length - 1)&&(pack[pos1] == 0)&&(pack[pos1+1] == 0)) {
            pos1 +=4;
            return "";
        } 
        boolean z1 = false;
        for (int p = pos1; p < pack.length; p++) {
            if (pack[p] == 0) {
                if (z1) {
                    //byte[] out = new byte[pos1 == 0 ? p-1 : p-2-pos1];
                    byte[] out = new byte[p-1-pos1];
                    System.arraycopy(pack, pos1, out, 0, out.length);
                    pos1 = p+1;
                    return new String(out);
                } else {
                    z1 = true;
                }
            } else {
                z1 = false;
            }       
        }
        byte[] out = new byte[pack.length - pos1];
        System.arraycopy(pack, pos1, out, 0, out.length);
        return new String(out);    
    }
}
