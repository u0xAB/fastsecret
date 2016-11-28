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

import java.awt.Image;
import java.awt.image.ImageObserver;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Andrej Borovski
 */
public class WidthObserver implements ImageObserver {
    TreeMap<Image, Integer> images;
    int count;
    boolean finished;
    WidthObserver(int count)
    {
        images = new TreeMap<>();
        this.count = count;
        finished = false;
    }
    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        if ((infoflags&ImageObserver.WIDTH) != 0)
            images.put(img, width);
        if ((infoflags&ImageObserver.ALLBITS) != 0)
            if (count > 0) count--;
        return count == 0;
    }
    
    boolean finished()
    {
       return count == 0; 
    }
    Image getFirst()
    {
        return images.firstEntry().getKey();
    }
}
