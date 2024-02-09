package Nibbles;

/**
 *
 * @author Albert Tucci
 */
public class MotionUp extends Motion {
    
    public MotionUp()
    {
        direction = 2;
    }

    @Override
    public void updatePosition() {
        
        y -= vy;
    }
}
