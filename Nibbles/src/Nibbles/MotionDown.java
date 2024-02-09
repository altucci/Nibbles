package Nibbles;

/**
 *
 * @author Albert Tucci
 */
public class MotionDown extends Motion {
        
    public MotionDown()
    {
        direction = 3;
    }

    @Override
    public void updatePosition() {
        
        y += vy;
    }
}
