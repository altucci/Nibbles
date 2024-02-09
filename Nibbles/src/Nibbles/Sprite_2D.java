/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Nibbles;

import java.awt.*;

/**
 *
 * @author John
 */
public abstract class Sprite_2D extends Sprite implements Runnable {

    Motion motion;
    
    protected int MaxWidth, MaxHeight, xshift, yshift, locx, locy;
    
    protected boolean changeColors;
    
    protected Dimension dimension;
    
    protected Color CurrentColor;
    protected boolean Fill;
    
    protected long ColorChangeTimeInterval;
    
    protected Graphics gra;
    
    Thread application = null;
    
    public void start() {
        
        if (application == null)
        {
            application = new Thread(this);
            application.start();
        }
    }

    public void run() {
        
        while (true)
        {
            paint(getGraphics());
            updateColors();
            try{
                Thread.sleep(ColorChangeTimeInterval);
            }catch(Exception e){
                
            }
        }
    }
    
    public boolean getChangeColors()
    {
        return changeColors;
    }
    
    public void setChangeColors(boolean b)
    {
        changeColors = b;
    }
    
    public byte getDirection()
    {
        if (motion != null)
        {
            return motion.getDirection();
        }
        else
        {
            return 0;
        }        
    }
    
    public void initMotion(Motion m)
    {
        motion = m;
        
        if (motion != null)
        {
            motion.setPosition(locx, locy);
            motion.setVelocity(xshift, yshift);
        }
    }

    @Override
    public void update() {
        
        if (motion != null)
        {
            motion.updatePosition();
            setPosition(motion.getX(), motion.getY());
        }
    }
    
    public void randomlySetPosition()
    {
        
    }
    
    public void randomlySetVelocity()
    {
        do{
            xshift = (int)(Math.random()*10);
            
        }while (xshift < 2);
        
        yshift = xshift;
    }

    public int getLocx()
    {
        return locx;
    }

    public int getLocy()
    {
        return locy;
    }

    public void setLocx(int locX)
    {
        locx = locX;
    }

    public void setLocy(int locY)
    {
        locy = locY;
    }
    
    public void setPosition(int locX, int locY)
    {
        locx = locX;
        locy = locY;
    }

    public int getVx()
    {
        return xshift;
    }

    public int getVy()
    {
        return yshift;
    }

    public void setVx(int vx)
    {
        xshift = vx;
    }

    public void setVy(int vy)
    {
        yshift = vy;
    }
    
    public void setVelocity(int vx, int vy)
    {
        xshift = vx;
        yshift = vy;
    }

    public Dimension getDimension()
    {        
        return dimension;
    }

    public void setDimension(Dimension dim) {
        
        this.dimension = dim;
        
        MaxWidth = dimension.width;
        MaxHeight = dimension.height;
    }

    public Color getCurrentColor() {
        return CurrentColor;
    }

    public void setCurrentColor(Color CurrentColor) {
        this.CurrentColor = CurrentColor;
    }

    public boolean isFill() {
        return Fill;
    }

    public void setFill(boolean Fill) {
        this.Fill = Fill;
    }

    public Graphics getGraphics() {
        
        return gra;
    }

    public void setGraphics(Graphics g) {
        
        this.gra = g;
    }
    
    protected void updateColors() {
        
        CurrentColor = new Color((int)(Math.random()*256), (int)(Math.random()*256), (int)(Math.random()*256));
    }
}