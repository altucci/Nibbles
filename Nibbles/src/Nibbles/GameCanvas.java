package Nibbles;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author Albert Tucci
 */
public class GameCanvas extends JPanel implements Runnable { 
    
    private MP3 mp3;

    private PanelGroup panelGroup;
    
    private final byte NotDefined = 0, UP = 2, DOWN = 3, LEFT = 4, RIGHT = 1, OCCUPIED = 1, EMPTY = 0;
    
    public byte direction;
    
    private String code;
    
    private int snacks, score;
    
    private long beginTime, endTime, beginPauseTime, endPauseTime, totalPauseTime;
    
    private double totalTime;
    
    private boolean kingsOfLeon;
        
    private boolean hitWall, hitBody, youWin, wait, ready, gamePaused, startChangeColorThread, noWalls, noBody, immortality, checkExpert, noGrow, contra;
    
    public boolean allowed, easy, medium, hard;
    
    private int MaxWidth, MaxHeight;
    
    private long TimeInterval;
    
    private Dimension dimension;
    
    private ArrayList<Sprite> Worm;
    
    public Sprite Head;
    
    private Sprite Tail, Food;
    
    private byte[][] grid;
    
    private ArrayList<Integer> contraCode;
    
    private Thread application;
    
    public GameCanvas(PanelGroup panelgroup) {
        
        mp3 = new MP3();  
        
        panelGroup = panelgroup;
        
        application = null;
        
        dimension = new Dimension();
        
        setFocusable(true);
        
        addKeyListener(new KeyStrokes());
                
        setBackground(Color.WHITE);
        
        setDimension(Constants.DIMENSION);
        
        easy = false;
        medium = true;
        hard = false;
        
        grid = new byte[MaxHeight][MaxWidth];
        
        Worm = new ArrayList<Sprite>(); 
        
        contraCode = new ArrayList<Integer>();
        
        kingsOfLeon = false;
        
        initializeSettings();
        
        setFocusable(true);
    }
    
    public void initializeSettings()
    {        
        code = "";

        allowed = false;
        contraCode.clear();
        
        contra = false;
        
        checkExpert = true;
        
        startChangeColorThread = false;        
        noWalls = false;
        noBody = false;
        immortality = false;
        noGrow = false;
        
        snacks = 0;
        beginTime = 0L;
        endTime = 0L;
        beginPauseTime = 0L;
        endPauseTime = 0L;
        totalPauseTime = 0L;
        totalTime = 0;
        score = 0;
            
        panelGroup.jTextField1.setText(String.valueOf(""));
        panelGroup.jTextField2.setText(String.valueOf(""));            
        panelGroup.jTextField3.setText(String.valueOf(snacks));
                              
        clearGrid();
        
        Worm.clear();     
        
        Head = new Rectangle((int)(MaxWidth/2), (int)(MaxHeight/2)-5, 10, 10, 10, 10, Color.RED);
        Head.Restore();
        ((Sprite_2D)Head).setFill(true);
        ((Sprite_2D)Head).initMotion(null);
        
        direction = NotDefined;
        
        Worm.add(Head);
        
        Tail = Head;
        
        for (int i = 1;i <= 4;i++)
        {
            Tail = new Rectangle(((Sprite_2D)Tail).getLocx()-10, ((Sprite_2D)Tail).getLocy(), 10, 10, 10, 10, Color.BLACK);
            Tail.Restore();
            ((Sprite_2D)Tail).setFill(true);

            grid[((Sprite_2D)Tail).getLocy()][((Sprite_2D)Tail).getLocx()] = OCCUPIED;

            Worm.add(Tail);
        }
        
        if (easy)
        {
            Food =  new Rectangle(((int)(Math.random()*(int)((MaxWidth-10)/10))*10)+5,
                                  ((int)(Math.random()*(int)((MaxHeight-10)/10))*10)+5, 0, 0, 10, 10, Color.GREEN);

            while (grid[((Sprite_2D)Food).getLocy()][((Sprite_2D)Food).getLocx()] == 1 || ((Sprite_2D)Food).getLocx() < 45 || ((Sprite_2D)Food).getLocx() > 505 || ((Sprite_2D)Food).getLocy() < 45 || ((Sprite_2D)Food).getLocy() > 455)
            {
                ((Sprite_2D)Food).setPosition(((int)(Math.random()*(int)((MaxWidth-10)/10))*10)+5, ((int)(Math.random()*(int)((MaxHeight-10)/10))*10)+5);
            }
        }
        else if (medium)
        {
            Food =  new Rectangle(((int)(Math.random()*(int)((MaxWidth-10)/10))*10)+5,
                                  ((int)(Math.random()*(int)((MaxHeight-10)/10))*10)+5, 0, 0, 10, 10, Color.GREEN);

            while (grid[((Sprite_2D)Food).getLocy()][((Sprite_2D)Food).getLocx()] == 1)
            {
                ((Sprite_2D)Food).setPosition(((int)(Math.random()*(int)((MaxWidth-10)/10))*10)+5, ((int)(Math.random()*(int)((MaxHeight-10)/10))*10)+5);
            }
        }
        else if (hard)
        {
            Food =  new Rectangle(((int)(Math.random()*(int)((MaxWidth-10)/10))*10)+5,
                                  ((int)(Math.random()*(int)((MaxHeight-10)/10))*10)+5, 0, 0, 10, 10, Color.GREEN);

            while (grid[((Sprite_2D)Food).getLocy()][((Sprite_2D)Food).getLocx()] == 1 || (((Sprite_2D)Food).getLocx() > 25 && ((Sprite_2D)Food).getLocx() < 525) || (((Sprite_2D)Food).getLocy() > 25 && ((Sprite_2D)Food).getLocy() < 475))
            {
                ((Sprite_2D)Food).setPosition(((int)(Math.random()*(int)((MaxWidth-10)/10))*10)+5, ((int)(Math.random()*(int)((MaxHeight-10)/10))*10)+5);
            }
        }
        
        Food.Restore();
        ((Sprite_2D)Food).setFill(true);
        
        TimeInterval = 202;
        
        ready = true;        
        wait = false;        
        
        gamePaused = false;
        
        youWin = false;
        hitBody = false;
        hitWall = false;
    }
    
    private void clearGrid()
    {
        for (int i = 0;i < MaxHeight;i++)
        {
            for (int j = 0;j < MaxWidth;j++)
            {
                grid[i][j] = EMPTY;
            }
        }
    }
    
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
            if (!(hitWall || hitBody || youWin))
            {
                if (!(direction == NotDefined))
                {                    
//                    if (checkTime)
//                    {
//                        beginTime = System.nanoTime();
//                        
//                        checkTime = false;
//                    }
                    
                    if (!gamePaused)
                    {
//                        if (checkUnPause)
//                        {
//                            endPauseTime = System.nanoTime();
//                            
//                            totalPauseTime += (beginPauseTime - endPauseTime);
//
//                            checkPause = true;
//                            
//                            checkUnPause = false;
//                        }
                        
                        updateObj(); 

                        repaint();

                        try
                        {
                            if (wait)
                            {
                                Thread.sleep(1000);
                            }
                            else
                            {
                                Thread.sleep(TimeInterval);
                            }
                        }
                        catch(Exception e)
                        {

                        }

                        ready = true;
                    }
//                    else if (gamePaused)
//                    {   
//                        if (checkPause)
//                        {
//                            beginPauseTime = System.nanoTime();
//
//                            checkUnPause = true;
//                            
//                            checkPause = false;
//                        }
//                    }
                }
                else
                {
                    checkCodes();
                }
            }
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        
//        if (!hitWall)
//        {
            super.paintComponent(g);

            for (int i = Worm.size()-1;i >= 0;i--)
            {                
                Worm.get(i).paint(g);
                
                if (direction != NotDefined && startChangeColorThread && ((Sprite_2D)Worm.get(i)).getChangeColors() == false)
                {
                    ((Sprite_2D)Worm.get(i)).setChangeColors(true);
                    
                    ((Sprite_2D)Worm.get(i)).setGraphics(g);
                    
                    ((Sprite_2D)Worm.get(i)).start();
                }
            }
//        }

        if(!youWin)
            Food.paint(g);
        
        if (hitWall || hitBody)
        {        
            g.setFont(new java.awt.Font("OCR A Extended", 1, 64));
            g.setColor(Color.BLUE);
            g.drawString("GAME OVER", ((int)(MaxWidth/2))-175, ((int)(MaxHeight/2))-10);
        }
        else if (youWin)
        {
            g.setFont(new java.awt.Font("OCR A Extended", 1, 64));
            g.setColor(Color.BLUE);
            g.drawString("YOU WIN!", ((int)(MaxWidth/2))-175, ((int)(MaxHeight/2))-10);
        }
        else if (gamePaused)
        {
            g.setFont(new java.awt.Font("OCR A Extended", 1, 64));
            g.setColor(Color.BLUE);
            g.drawString("PAUSE", ((int)(MaxWidth/2))-100, ((int)(MaxHeight/2))-10);
        }
        else if (direction == NotDefined)
        {        
            g.setFont(new java.awt.Font("OCR A Extended", 1, 36));
            g.setColor(Color.BLUE);
            g.drawString("Welcome to Nibbles!", ((int)(MaxWidth/2))-225, ((int)(MaxHeight/2))-150);
            g.setFont(new java.awt.Font("OCR A Extended", 1, 18));
            g.drawString("Press Arrow Keys to move.", ((int)(MaxWidth/2))-175, ((int)(MaxHeight/2))+100);
            g.drawString("Press Enter Key to pause.", ((int)(MaxWidth/2))-175, ((int)(MaxHeight/2))+125);
        }
    }
    
    private void checkCodes()
    {
        if (!contra && contraCode.size() == 10)
        {
            if (contraCheck())
            {
                contra = true;
            }

            allowed = true;
        }
        
        if (!kingsOfLeon && code.toUpperCase().contains("BIGTOOCH"))
        {
            mp3.play("/music/kingsOfLeon_Fans.mp3");
            
            kingsOfLeon = true;
        }

        if (!startChangeColorThread && code.toUpperCase().contains("BIGHURT"))//(code.toUpperCase().contains("BIGHURT") || code.toUpperCase().contains("BIGTOOCH") || contra))
        {
            startChangeColorThread = true;

            allowed = true;
        }

        if ((TimeInterval != 10 && Worm.size() < 54) && (code.toUpperCase().contains("EXPERT") || code.toUpperCase().contains("BIGTOOCH") || contra))
        {                                
            checkExpert = false;

            TimeInterval = 10;

            for (int i = 5;i <= 54;i++)
            {
                Tail = new Rectangle(((Sprite_2D)Tail).getLocx()-10, ((Sprite_2D)Tail).getLocy(), 10, 10, 10, 10, Color.BLACK);
                Tail.Restore();
                ((Sprite_2D)Tail).setFill(true);

                try{
                    grid[((Sprite_2D)Tail).getLocy()][((Sprite_2D)Tail).getLocx()] = OCCUPIED;
                }
                catch (Exception e)
                {

                }

                Worm.add(Tail);
            }

            allowed = true;
        }
        else
        {
            if (TimeInterval != 10 && (code.toUpperCase().contains("FAST") || code.toUpperCase().contains("BIGSTEVE")))
            {                        
                TimeInterval = 10;

                allowed = true;
            }
            if (Worm.size() < 54 && (code.toUpperCase().contains("LONG")))
            {                            
                checkExpert = false;

                for (int i = 5;i <= 54;i++)
                {
                    Tail = new Rectangle(((Sprite_2D)Tail).getLocx()-10, ((Sprite_2D)Tail).getLocy(), 10, 10, 10, 10, Color.BLACK);
                    Tail.Restore();
                    ((Sprite_2D)Tail).setFill(true);

                    try{
                        grid[((Sprite_2D)Tail).getLocy()][((Sprite_2D)Tail).getLocx()] = OCCUPIED;
                    }
                    catch (Exception e)
                    {

                    }

                    Worm.add(Tail);
                }

                allowed = true;
            }
            if (Worm.size() > 1 && (code.toUpperCase().contains("SHORT") || code.toUpperCase().contains("BIGSTEVE")))
            {                        
                for (int i = Worm.size()-1;i > 0;i--)
                {
                    try{
                        grid[((Sprite_2D)Tail).getLocy()][((Sprite_2D)Tail).getLocx()] = EMPTY;
                    }
                    catch (Exception e)
                    {

                    }

                    Worm.remove(i);

                    Tail = Worm.get(i-1);
                }

                allowed = true;
            }
        }
        
        if (noGrow == false && (code.toUpperCase().contains("NOGROW") || code.toUpperCase().contains("BIGSTEVE")))
        {
            noGrow = true;
            
            allowed = true;
        }

        if ((noWalls == false && noBody == false) && (code.toUpperCase().contains("INVINCIBLE") || code.toUpperCase().contains("BIGTOOCH") || contra))
        {
            noWalls = true;

            noBody = true;

            allowed = true;
        }
        else
        {
            if (noWalls == false &&  (code.toUpperCase().contains("NOWALLS")))
            {                        
                noWalls = true;

                allowed = true;
            }
            if (noBody == false && (code.toUpperCase().contains("NOBODY") || code.toUpperCase().contains("BIGSTEVE")))
            {                        
                noBody = true;

                allowed = true;
            }
        }

        if (immortality == false && (code.toUpperCase().contains("IMMORTALITY") || code.toUpperCase().contains("BIGSTEVE") || contra))
        {
            immortality = true;

            allowed = true;
        }
    }
    
    private boolean contraCheck()
    {
        if (contraCode.get(0).intValue() == KeyEvent.VK_UP && 
            contraCode.get(1).intValue() == KeyEvent.VK_UP && 
            contraCode.get(2).intValue() == KeyEvent.VK_DOWN && 
            contraCode.get(3).intValue() == KeyEvent.VK_DOWN && 
            contraCode.get(4).intValue() == KeyEvent.VK_LEFT && 
            contraCode.get(5).intValue() == KeyEvent.VK_RIGHT && 
            contraCode.get(6).intValue() == KeyEvent.VK_LEFT && 
            contraCode.get(7).intValue() == KeyEvent.VK_RIGHT && 
            contraCode.get(8).intValue() == KeyEvent.VK_B && 
            contraCode.get(9).intValue() == KeyEvent.VK_A)
        {
            return true;
        }
        
        return false;
    }
    
    public boolean HitWall()
    {
        if (((Sprite_2D)Head).getLocx() < 5 || ((Sprite_2D)Head).getLocx() > (MaxWidth-5) || ((Sprite_2D)Head).getLocy() < 5 || ((Sprite_2D)Head).getLocy() > (MaxHeight-5))
            return true;
        
        return false;
    }
    
    public boolean HitBody()
    {        
        try{
            if (grid[((Sprite_2D)Head).getLocy()][((Sprite_2D)Head).getLocx()] == 1 && Worm.size() > 2)
                return true;
        }
        catch (Exception e)
        {
            for (int i = 4;i < Worm.size();i++)
            {
                if (((Sprite_2D)Head).getLocx() == ((Sprite_2D)Worm.get(i)).getLocx() && ((Sprite_2D)Head).getLocy() == ((Sprite_2D)Worm.get(i)).getLocy())
                    return true;
            }
        }
        
        return false;
    }
    
    public boolean AteFood()
    {
        if (((Sprite_2D)Head).getLocx() == ((Sprite_2D)Food).getLocx() && ((Sprite_2D)Head).getLocy() == ((Sprite_2D)Food).getLocy())
        {
            snacks++;
            
            panelGroup.jTextField3.setText(String.valueOf(snacks));
            
            return true;
        }
        
        return false;
    }
    
    public void updateObj() {
                                                
        try{
            grid[((Sprite_2D)Tail).getLocy()][((Sprite_2D)Tail).getLocx()] = EMPTY;
        }
        catch (Exception e)
        {
            
        }
        
        for (int i = Worm.size()-1;i > 0;i--)
        {
            ((Sprite_2D)Worm.get(i)).setPosition(((Sprite_2D)Worm.get(i-1)).getLocx(), 
                                                 ((Sprite_2D)Worm.get(i-1)).getLocy());
        }        
        
        try{
            if (Worm.size() > 1)
                grid[((Sprite_2D)Head).getLocy()][((Sprite_2D)Head).getLocx()] = OCCUPIED;
        }
        catch (Exception e)
        {
            
        }
        
        Head.update();
        
        if (AteFood())
        {            
            if (!immortality)
                TimeInterval -= 4;
            
            if (TimeInterval != 2 || snacks == 50)
            {
                if (!noGrow)
                {
                    Tail = new Rectangle(((Sprite_2D)Tail).getLocx(), ((Sprite_2D)Tail).getLocy(), 10, 10, 10, 10, Color.BLACK);
                    Tail.Restore();
                    ((Sprite_2D)Tail).setFill(true);

                    try{
                        grid[((Sprite_2D)Tail).getLocy()][((Sprite_2D)Tail).getLocx()] = OCCUPIED;
                    }
                    catch (Exception e)
                    {

                    }
                }
        
                if (easy)
                {
                    Food =  new Rectangle(((int)(Math.random()*(int)((MaxWidth-10)/10))*10)+5,
                                          ((int)(Math.random()*(int)((MaxHeight-10)/10))*10)+5, 0, 0, 10, 10, Color.GREEN);

                    while (grid[((Sprite_2D)Food).getLocy()][((Sprite_2D)Food).getLocx()] == 1 || ((Sprite_2D)Food).getLocx() < 45 || ((Sprite_2D)Food).getLocx() > 505 || ((Sprite_2D)Food).getLocy() < 45 || ((Sprite_2D)Food).getLocy() > 455)
                    {
                        ((Sprite_2D)Food).setPosition(((int)(Math.random()*(int)((MaxWidth-10)/10))*10)+5, ((int)(Math.random()*(int)((MaxHeight-10)/10))*10)+5);
                    }
                }
                else if (medium)
                {
                    Food =  new Rectangle(((int)(Math.random()*(int)((MaxWidth-10)/10))*10)+5,
                                          ((int)(Math.random()*(int)((MaxHeight-10)/10))*10)+5, 0, 0, 10, 10, Color.GREEN);

                    while (grid[((Sprite_2D)Food).getLocy()][((Sprite_2D)Food).getLocx()] == 1)
                    {
                        ((Sprite_2D)Food).setPosition(((int)(Math.random()*(int)((MaxWidth-10)/10))*10)+5, ((int)(Math.random()*(int)((MaxHeight-10)/10))*10)+5);
                    }
                }
                else if (hard)
                {
                    Food =  new Rectangle(((int)(Math.random()*(int)((MaxWidth-10)/10))*10)+5,
                                          ((int)(Math.random()*(int)((MaxHeight-10)/10))*10)+5, 0, 0, 10, 10, Color.GREEN);

                    while (grid[((Sprite_2D)Food).getLocy()][((Sprite_2D)Food).getLocx()] == 1 || (((Sprite_2D)Food).getLocx() > 25 && ((Sprite_2D)Food).getLocx() < 525) || (((Sprite_2D)Food).getLocy() > 25 && ((Sprite_2D)Food).getLocy() < 475))
                    {
                        ((Sprite_2D)Food).setPosition(((int)(Math.random()*(int)((MaxWidth-10)/10))*10)+5, ((int)(Math.random()*(int)((MaxHeight-10)/10))*10)+5);
                    }
                }
                
                Food.Restore();
                ((Sprite_2D)Food).setFill(true);

                Worm.add(Tail);

                wait = true;
            }
            else
            {
                if (!kingsOfLeon)
                {
                    mp3.play("/music/kingsOfLeon_Fans.mp3");

                    kingsOfLeon = true;
                }
                
                youWin = true;
                
                endTime = System.nanoTime();
                
                totalTime = (Math.round(((endTime - beginTime - totalPauseTime)/1000000000)*100))/100.0;
            
                score = (int)Math.round((double)(snacks/totalTime)*100)*10;

                panelGroup.jTextField1.setText(String.valueOf(totalTime));

                panelGroup.jTextField2.setText(String.valueOf(score));
            }
        }
        else
        {
            wait = false;
        }
        
        if (!noWalls && HitWall())
        {
            hitWall = true;
                
            endTime = System.nanoTime();
                
            totalTime = (Math.round(((endTime - beginTime - totalPauseTime)/1000000000)*100))/100.0;
            
            score = (int)Math.round((double)(snacks/totalTime)*100)*10;
                
            panelGroup.jTextField1.setText(String.valueOf(totalTime));
            
            panelGroup.jTextField2.setText(String.valueOf(score));
        }
        else if (!noBody && HitBody())
        {
            hitBody = true;
                
            endTime = System.nanoTime();
                
            totalTime = (Math.round(((endTime - beginTime - totalPauseTime)/1000000000)*100))/100.0;
            
            score = (int)Math.round((double)(snacks/totalTime)*100)*10;
                
            panelGroup.jTextField1.setText(String.valueOf(totalTime));
            
            panelGroup.jTextField2.setText(String.valueOf(score));
        }
    }

    public Dimension getDimension() {
        
        return dimension;
    }

    public void setDimension(Dimension dim) {
        
        dimension = dim;
        
        if (dimension != null)
        {
            MaxWidth = dimension.width;
        
            MaxHeight = dimension.height;
        }
    }
    
    private class KeyStrokes extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            
            if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
            {
                initializeSettings();

                repaint();
            }
            else if (e.getKeyCode() == KeyEvent.VK_ENTER && direction != NotDefined)
            {
                if (youWin || hitWall || hitBody)
                {
                    
                }
                else if (gamePaused)
                {
                    gamePaused = false;
                }
                else
                {
                    gamePaused = true;
                    
                    repaint();
                }
            }
            else if (ready || noBody)
            {
                if (e.getKeyCode() == KeyEvent.VK_SPACE)
                {
                    ((Sprite_2D)Head).initMotion(new MotionRight());

                    direction = ((Sprite_2D)Head).getDirection();

                    allowed = true;
                }
                else if (allowed)
                {
                    if (e.getKeyCode() == KeyEvent.VK_RIGHT && !(direction == RIGHT || direction == LEFT))
                    {
                        ((Sprite_2D)Head).initMotion(new MotionRight());
                    }
                    else if (e.getKeyCode() == KeyEvent.VK_LEFT && !(direction == RIGHT || direction == LEFT) && (direction != NotDefined || noBody))
                    {
                        ((Sprite_2D)Head).initMotion(new MotionLeft());
                    }
                    else if (e.getKeyCode() == KeyEvent.VK_UP && !(direction == UP || direction == DOWN))
                    {
                        ((Sprite_2D)Head).initMotion(new MotionUp());
                    }
                    else if (e.getKeyCode() == KeyEvent.VK_DOWN && !(direction == UP || direction == DOWN))
                    {
                        ((Sprite_2D)Head).initMotion(new MotionDown());
                    }
                    else if (direction == NotDefined)
                    {
                        code += String.valueOf(e.getKeyChar());
                    }

                    direction = ((Sprite_2D)Head).getDirection();

                    if (!(direction == NotDefined ))
                        ready = false;
                }
                else if (!allowed)
                {
                    code += String.valueOf(e.getKeyChar());
                    
                    contraCode.add(new Integer(e.getKeyCode()));
                }
            }
        }
    }
}
