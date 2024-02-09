package Nibbles;

/**
 *
 * @author Albert Tucci
 */
public class MotionRight extends Motion {
    
    public MotionRight()
    {
        direction = 1;
    }

    @Override
    public void updatePosition() {
        
        x += vx;
    }
}
