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

import static java.lang.Math.abs;

/**
 *
 * @author Andrej Borovski
 */
enum PermutationTables {
    ;
    static final byte[][] LUT = {{0, 1, 2, 3, 4}, {0, 1, 2, 4, 3}, {0, 1, 3, 2, 4}, {0, 1, 3, 4, 
  2}, {0, 1, 4, 2, 3}, {0, 1, 4, 3, 2}, {0, 2, 1, 3, 4}, {0, 2, 1, 4, 
  3}, {0, 2, 3, 1, 4}, {0, 2, 3, 4, 1}, {0, 2, 4, 1, 3}, {0, 2, 4, 3, 
  1}, {0, 3, 1, 2, 4}, {0, 3, 1, 4, 2}, {0, 3, 2, 1, 4}, {0, 3, 2, 4, 
  1}, {0, 3, 4, 1, 2}, {0, 3, 4, 2, 1}, {0, 4, 1, 2, 3}, {0, 4, 1, 3, 
  2}, {0, 4, 2, 1, 3}, {0, 4, 2, 3, 1}, {0, 4, 3, 1, 2}, {0, 4, 3, 2, 
  1}, {1, 0, 2, 3, 4}, {1, 0, 2, 4, 3}, {1, 0, 3, 2, 4}, {1, 0, 3, 4, 
  2}, {1, 0, 4, 2, 3}, {1, 0, 4, 3, 2}, {1, 2, 0, 3, 4}, {1, 2, 0, 4, 
  3}, {1, 2, 3, 0, 4}, {1, 2, 3, 4, 0}, {1, 2, 4, 0, 3}, {1, 2, 4, 3, 
  0}, {1, 3, 0, 2, 4}, {1, 3, 0, 4, 2}, {1, 3, 2, 0, 4}, {1, 3, 2, 4, 
  0}, {1, 3, 4, 0, 2}, {1, 3, 4, 2, 0}, {1, 4, 0, 2, 3}, {1, 4, 0, 3, 
  2}, {1, 4, 2, 0, 3}, {1, 4, 2, 3, 0}, {1, 4, 3, 0, 2}, {1, 4, 3, 2, 
  0}, {2, 0, 1, 3, 4}, {2, 0, 1, 4, 3}, {2, 0, 3, 1, 4}, {2, 0, 3, 4, 
  1}, {2, 0, 4, 1, 3}, {2, 0, 4, 3, 1}, {2, 1, 0, 3, 4}, {2, 1, 0, 4, 
  3}, {2, 1, 3, 0, 4}, {2, 1, 3, 4, 0}, {2, 1, 4, 0, 3}, {2, 1, 4, 3, 
  0}, {2, 3, 0, 1, 4}, {2, 3, 0, 4, 1}, {2, 3, 1, 0, 4}, {2, 3, 1, 4, 
  0}, {2, 3, 4, 0, 1}, {2, 3, 4, 1, 0}, {2, 4, 0, 1, 3}, {2, 4, 0, 3, 
  1}, {2, 4, 1, 0, 3}, {2, 4, 1, 3, 0}, {2, 4, 3, 0, 1}, {2, 4, 3, 1, 
  0}, {3, 0, 1, 2, 4}, {3, 0, 1, 4, 2}, {3, 0, 2, 1, 4}, {3, 0, 2, 4, 
  1}, {3, 0, 4, 1, 2}, {3, 0, 4, 2, 1}, {3, 1, 0, 2, 4}, {3, 1, 0, 4, 
  2}, {3, 1, 2, 0, 4}, {3, 1, 2, 4, 0}, {3, 1, 4, 0, 2}, {3, 1, 4, 2, 
  0}, {3, 2, 0, 1, 4}, {3, 2, 0, 4, 1}, {3, 2, 1, 0, 4}, {3, 2, 1, 4, 
  0}, {3, 2, 4, 0, 1}, {3, 2, 4, 1, 0}, {3, 4, 0, 1, 2}, {3, 4, 0, 2, 
  1}, {3, 4, 1, 0, 2}, {3, 4, 1, 2, 0}, {3, 4, 2, 0, 1}, {3, 4, 2, 1, 
  0}, {4, 0, 1, 2, 3}, {4, 0, 1, 3, 2}, {4, 0, 2, 1, 3}, {4, 0, 2, 3, 
  1}, {4, 0, 3, 1, 2}, {4, 0, 3, 2, 1}, {4, 1, 0, 2, 3}, {4, 1, 0, 3, 
  2}, {4, 1, 2, 0, 3}, {4, 1, 2, 3, 0}, {4, 1, 3, 0, 2}, {4, 1, 3, 2, 
  0}, {4, 2, 0, 1, 3}, {4, 2, 0, 3, 1}, {4, 2, 1, 0, 3}, {4, 2, 1, 3, 
  0}, {4, 2, 3, 0, 1}, {4, 2, 3, 1, 0}, {4, 3, 0, 1, 2}, {4, 3, 0, 2, 
  1}, {4, 3, 1, 0, 2}, {4, 3, 1, 2, 0}, {4, 3, 2, 0, 1}, {4, 3, 2, 1, 
  0}};
  
  static final byte[] POW5MOD127 = {0, 127, 126, 116, 8, 77, 29, 43, 2, 121, 51, 15, 39, 72, 106, 42, 64, 124, 
62, 107, 108, 35, 99, 110, 105, 87, 18, 66, 90, 14, 74, 49, 16, 89, 
31, 9, 79, 52, 122, 97, 27, 70, 104, 101, 120, 46, 91, 25, 58, 71, 
117, 33, 68, 114, 80, 12, 86, 93, 67, 24, 82, 73, 44, 123, 4, 83, 54, 
45, 103, 60, 34, 41, 115, 47, 13, 59, 94, 10, 56, 69, 102, 36, 81, 7, 
26, 23, 57, 100, 30, 5, 75, 48, 118, 96, 38, 111, 78, 53, 113, 37, 
61, 109, 40, 22, 17, 28, 92, 19, 20, 65, 3, 63, 85, 21, 55, 88, 112, 
76, 6, 125, 84, 98, 50, 119, 11, 95, 32, 1, 60};  
  
    
  static byte[] permutation(byte[] bytes, int n)
  {
      byte[] idx = LUT[abs(n)%120];
      byte[] out = new byte[5];
      for (int i = 0; i < 5; i++)
          out[i] = bytes[idx[i]];
      return out;
  }
  static byte pow5mod127(int x)
    {
        return  (byte) (x > 0 ? POW5MOD127[x] : -POW5MOD127[-x]); 
    }
    
  static byte[] fakePermutation(byte[] bytes, int n)
  {
        byte[] idx = LUT[abs(n)%120];
        int zeroPos = 0;
        byte[] out = new byte[5];
        for (int i = 0; i < 5; i++) {
            if (idx[i] == 0)
                zeroPos = i;
            out[i] = bytes[i];
        }
        out[zeroPos] = bytes[0];
        out[0] = bytes[zeroPos];
        return out;
  }
  public static byte[] permute5(byte[] bytes, int from)
  {
        byte[] bs = new byte[5];
        System.arraycopy(bytes, from, bs, 0, 5);
        if (bs[0] == 0) bs[0] = 125;
        if (abs(bs[0]) == 1) bs[0] = 75;
        bs[0] = pow5mod127(bs[0]);
        bs = permutation(bs, abs(bs[0]));
        if (bs[0] == 0) bs[0] = 125;
        bs[0] = pow5mod127(bs[0]);
        bs = fakePermutation(bs, abs(bs[0]));
        System.arraycopy(bs, 0, bytes, from, 5);
        return bytes;
  }
  public static byte[] chainPermute(byte[] bytes) {
        byte[] out = new byte[bytes.length];
        System.arraycopy(bytes, 0, out, 0, bytes.length); 
        out = PermutationTables.permute5(out, 0);
        //out = PermutationTables.permute5(out, 2);
        out = PermutationTables.permute5(out, 4);
        out = PermutationTables.permute5(out, 8);
        return PermutationTables.permute5(out, 11);
    }
}
