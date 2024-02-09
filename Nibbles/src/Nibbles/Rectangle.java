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
public class Rectangle extends Sprite_2D {
    
    protected int width, height;
    
    public Rectangle() {
        
        changeColors = false;
        
        updateColors();
        
        do{
            ColorChangeTimeInterval = (int)(Math.random()*1000);
            
        }while (ColorChangeTimeInterval < 10);
        
        do{
            width = (int)(Math.random()*50);
            
        }while (width < 20);
        
        do{
            height = (int)(Math.random()*50);
            
        }while (height < 20);
        
        setDimension(Constants.DIMENSION);
        
        randomlySetPosition();
        
        randomlySetVelocity();
    }
    
    public Rectangle(int x, int y, int vx, int vy, int w, int h, Color c) {
        
        changeColors = false;
        
        locx = x;
        locy = y;
        xshift = vx;
        yshift = vy;
        width = w;
        height = h;
        CurrentColor = c;
        
        do{
            ColorChangeTimeInterval = (int)(Math.random()*1000);
            
        }while (ColorChangeTimeInterval < 10);
        
        ColorChangeTimeInterval = 100;
        
        setDimension(Constants.DIMENSION);
    }
    
    @Override
    public void paint(Graphics g) {
        
        if (Visible)
        {
            g.setColor(CurrentColor);
            
            if (Fill)
            {
                g.fillRect(locx-(int)(width/2), locy-(int)(height/2), width, height);
            }
            else
            {
                g.drawRect(locx-(int)(width/2), locy-(int)(height/2), width, height);
            }
        }
    }
    
    @Override
    public void randomlySetPosition()
    {
        do{
            locx = (int)(Math.random()*(MaxWidth-(width)));
            
        }while ((locx-(width)) <= 0);
        
        do{
            locy = (int)(Math.random()*(MaxHeight-(height)));
            
        }while ((locy-(height)) <= 0);
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
