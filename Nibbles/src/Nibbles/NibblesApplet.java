package Nibbles;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.*;

/**
 *
 * @author Albert Tucci
 */
public class NibblesApplet extends JApplet {
    
    public GameCanvas canvas;    
    public PanelGroup panelGroup;
    
    @Override
    public void init() {       
        
        panelGroup = new PanelGroup();
        panelGroup.jButton1.addActionListener(new panelGroupEventListener());
        panelGroup.jButton2.addActionListener(new panelGroupEventListener());
        panelGroup.jRadioButton1.addActionListener(new panelGroupEventListener());
        panelGroup.jRadioButton2.addActionListener(new panelGroupEventListener());
        panelGroup.jRadioButton3.addActionListener(new panelGroupEventListener());        
        add(panelGroup, BorderLayout.EAST);    
        
        canvas = new GameCanvas(panelGroup);
        canvas.setFocusable(true);
        canvas.requestFocus();
        canvas.setFocusable(true);
        canvas.start();
        canvas.setFocusable(true);
        canvas.requestFocus();
        canvas.setFocusable(true);        
        add(canvas);
        canvas.setFocusable(true);
        canvas.requestFocus();
    }
    
    public static void main(String[] args)
    {
        JFrame Frame = new JFrame();
        Frame.setSize(Constants.DIMENSION.width+172+16,Constants.DIMENSION.height+38);
        Frame.setVisible(true);
        Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        NibblesApplet aniApplet = new NibblesApplet();
        Frame.add(aniApplet);
        aniApplet.setLocation(0, 0);
        aniApplet.setSize(Frame.getWidth(),Frame.getHeight());
        aniApplet.init();
        aniApplet.canvas.setFocusable(true);
        aniApplet.canvas.requestFocus();
        Frame.validate();
    }    
    
    private class panelGroupEventListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            
            if (e.getSource() == panelGroup.jButton2)
            {
                canvas.initializeSettings();

                canvas.repaint();
            }
            else if (e.getSource() == panelGroup.jButton1)
            {
                ((Sprite_2D)canvas.Head).initMotion(new MotionRight());

                canvas.direction = ((Sprite_2D)canvas.Head).getDirection();

                canvas.allowed = true;
            }
            else if (e.getSource() == panelGroup.jRadioButton1)
            {
                panelGroup.jRadioButton1.setSelected(true);
                panelGroup.jRadioButton2.setSelected(false);
                panelGroup.jRadioButton3.setSelected(false);
                
                canvas.easy = true;
                canvas.medium = false;
                canvas.hard = false;
            }
            else if (e.getSource() == panelGroup.jRadioButton2)
            {
                panelGroup.jRadioButton2.setSelected(true);
                panelGroup.jRadioButton1.setSelected(false);
                panelGroup.jRadioButton3.setSelected(false);
                
                canvas.easy = false;
                canvas.medium = true;
                canvas.hard = false;
            }
            else if (e.getSource() == panelGroup.jRadioButton3)
            {
                panelGroup.jRadioButton3.setSelected(true);
                panelGroup.jRadioButton1.setSelected(false);
                panelGroup.jRadioButton2.setSelected(false);
                
                canvas.easy = false;
                canvas.medium = false;
                canvas.hard = true;
            }
            
            canvas.setFocusable(true);
            canvas.requestFocus();
        }
    }
}
