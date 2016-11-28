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

import java.util.Random;


/**
 *
 * @author Andrej Borovski
 */
public class PasswordGenerator {
    private PasswordGenerator()
    {
    }
    static final String lower = "abcdefghijklmnopqrstuvwxyz";
    static final String upper = lower.toUpperCase();
    static final String digits = "1234567890";
    static final String punct = "!@#$%^&*()-=+[]{}?,%:;";
    public static String generate(int length, boolean excludePunct)
    {
        boolean has_digit = false;
        boolean has_lower = false;
        boolean has_upper = false;
        boolean has_punct = false;
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length - 4; i++) {
            byte from = (byte) (r.nextInt(25)%5);
            switch(from) {
                case 0: case 1: 
                    sb.append(lower.charAt(r.nextInt(lower.length())));
                    has_lower = true;
                    break;
                case 2:
                    sb.append(upper.charAt(r.nextInt(upper.length())));
                    has_upper = true;
                    break;
                case 3:
                    sb.append(digits.charAt(r.nextInt(24)%10));
                    has_digit = true;
                    break;
                case 4:
                    if (!excludePunct) {
                        sb.append(punct.charAt(r.nextInt(punct.length())));
                        has_punct = true;
                    }
                    break;
                default:
                    break;
            } 
        }
        int j = length - 4;
        if (!has_lower) {
            sb.append(lower.charAt(r.nextInt(lower.length())));
            j++;
        }
        if (!has_upper) {
            sb.append(upper.charAt(r.nextInt(upper.length())));
            j++;
        }
        if (!has_digit) {
            sb.append(digits.charAt(r.nextInt(digits.length())));
            j++;
        }
        if (!excludePunct) {
            if (!has_punct) {
                sb.append(punct.charAt(r.nextInt(punct.length())));
                j++;
            }
        }
        for (int i = j; i < length; i++)
            sb.append(((i%2) == 0 ? lower.charAt(r.nextInt(lower.length())) : upper.charAt(r.nextInt(upper.length()))));
        return sb.toString();
    }
}
