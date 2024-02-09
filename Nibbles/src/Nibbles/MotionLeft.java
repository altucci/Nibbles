package Nibbles;

/**
 *
 * @author Albert Tucci
 */
public class MotionLeft extends Motion {
    
    public MotionLeft()
    {
        direction = 4;
    }

    @Override
    public void updatePosition() {
        
        x -= vx;
    }
}
