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
public abstract class Sprite {

    protected boolean Visible, Active;

    public boolean isVisible() {
        return Visible;
    }

    public void setVisible(boolean Visible) {
        this.Visible = Visible;
    }
    
    public boolean isActive() {
        return Active;
    }

    public void setActive(boolean Active) {
        this.Active = Active;
    }
    
    public void Restore()
    {
        Visible = true;
        Active = true;
    }
    
    public void Suspend()
    {
        Visible = false;
        Active = false;
    }
    
    public abstract void paint(Graphics g);
    
    public abstract void update();
}