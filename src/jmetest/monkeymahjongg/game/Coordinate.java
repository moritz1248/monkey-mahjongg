/*
 * Coordinate.java
 *
 *  Copyright (c) 2007 Daniel Gronau
 *
 *  This file is part of Monkey Mahjongg.
 *
 *  Monkey Mahjongg is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Monkey Mahjongg is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 *
 */

package jmetest.monkeymahjongg.game;

import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;

/**
 * Integer coordinate specifying the "logical" position of a tile.
 *
 * @author Gronau
 */
public class Coordinate implements Serializable {
    
   private final static long serialVersionUID = 1; 
    
   private static transient Map<String, Coordinate> map = new HashMap<String, Coordinate>();
   
   public final int x;
   public final int y;
   public final int z;
   
   private Coordinate(int x, int y, int z) {
       this.x = x;
       this.y = y;
       this.z = z;
   }

   public Coordinate add(int dx, int dy, int dz) {
       return at(x+dx, y+dy, z+dz);
   }
   
   public static Coordinate at(int x, int y, int z) {
       String key = String.format("%d:%d:%d",x,y,z);
       Coordinate c = map.get(key);
       if (c == null) {
           c = new Coordinate(x,y,z);
           map.put(key, c);
       }
       return c;
   }

   @Override
   public String toString() {
       return String.format("[%d,%d,%d]", x, y, z);
   }

}
