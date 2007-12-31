package jmetest.monkeymahjongg.playground;

import com.jme.math.Vector2f;
import com.jme.scene.shape.Box;
import java.nio.FloatBuffer;

/**
 *
 * @author DGronau
 */
public class BoxRemapper {

    public static final int BACK = 1;
    public static final int RIGHT = 2;
    public static final int FRONT = 4;
    public static final int LEFT = 8;
    public static final int TOP = 16;
    public static final int BOTTOM = 32;
    public static final int ALL = 63;
    
    private float tex[] = new float[]{
        1,0, 0,0, 0,1, 1,1,
        1,0, 0,0, 0,1, 1,1,
        1,0, 0,0, 0,1, 1,1,
        1,0, 0,0, 0,1, 1,1,
        1,0, 0,0, 0,1, 1,1,
        1,0, 0,0, 0,1, 1,1
    }; 
    
    /**
     * Give the coordinates in the following order:
     * bottom right, bottom left, top left, top right.
     * For rotating or mirroring change the order.
     * The coordinates don't have to be rectangular, 
     * so you can create distortions.
     * @param side int the side that should be set, sides can be added together
     * @param coords the coordinates of the texture
     */
    public void setSide(int side, Vector2f... coords) {
        if (coords.length != 4) {
            throw new IllegalArgumentException("Requires 4 Vector2f objects.");
        }
        setSide(side, coords[0].x, coords[0].y, 
                      coords[1].x, coords[1].y, 
                      coords[2].x, coords[2].y, 
                      coords[3].x, coords[3].y);
    }
    
    /**
     * Give the coordinates in the following order:
     * bottom right, bottom left, top left, top right.
     * For rotating or mirroring change the order.
     * The coordinates don't have to be rectangular, 
     * so you can create distortions.
     * @param side the side that should be set, sides can be added together
     * @param coords the coordinates of the texture
     */
    public void setSide(int side, float... coords) {
        if (coords.length != 8) {
            throw new IllegalArgumentException("Requires 8 floats.");
        }        
        for(int i = 0; i < 6; i++) {
            int n = 1 << i;
            if ((side & n) > 0) {
                System.out.println("writing side " + i);
                for (int t = 0; t < 8; t++) {
                    tex[t + 8*i] = coords[t];
                }
            }
        }
    }
    
    /**
     * The Box to be remapped
     * @param box the box
     */
    public void remap(Box box) {
                FloatBuffer fb = box.getTextureBuffer(0, 0);
		fb.rewind();
		fb.put(tex);
    }

}
