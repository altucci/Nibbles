/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Nibbles;

/**
 *
 * @author John
 */
public abstract class Motion {
    
    int x, y, vx, vy;
    
    byte direction;

    public int getX() {
        
        return x;
    }
    
    public int getY()
    {
        return y;
    }
    
    public void setPosition(int X, int Y) {
        
        x = X;
        y = Y;
    }

    public void setVelocity(int vX, int vY) {
        
        vx = vX;
        vy = vY;
    }
    
    public byte getDirection()
    {
        return direction;
    }
    
    public abstract void updatePosition();
}
