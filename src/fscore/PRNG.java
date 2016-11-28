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
package fscore;

import static java.lang.System.arraycopy;

/**
 *
 * @author Andrej Borovski
 */
class PRNG {
    byte[] vector;
    int skip;
    public PRNG(byte[] iv, int s) {
        vector = new byte[iv.length];
        System.arraycopy(iv, 0, vector, 0, iv.length);
        skip = s;
    }
    
    public byte[] nextBytes() 
    {
        vector = PermutationTables.chainPermute(vector);
        byte[] p1 = PermutationTables.chainPermute(vector);
        byte[] p2 = new byte[p1.length];
        arraycopy(p1, 0, p2, 0, p1.length);
        for (int i = 0; i < skip; i++)
            p1 = PermutationTables.chainPermute(p1); 
        vector = PermutationTables.chainPermute(p1);
        byte[] out = new byte[p2.length];
        for (int i = 0; i < p1.length; i++)
            out[i] = (byte) (p2[i]^vector[i]);
        return out;
    }
    
    public int nextXorBytes(byte[] in, int from)
    {
        if (in == null)
            return 0;
        byte[] b = nextBytes(); 
        int i = from;
        int j = 0;
        while((i < in.length)&&(j< b.length)){
           in[i] = (byte) (in[i]^b[j]);
           i++;
           j++;
        }
        return j;
    }
    public int outputLength()
    {
        return vector.length;
    }
}
