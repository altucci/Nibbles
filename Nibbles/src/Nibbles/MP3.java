
/*************************************************************************
 *  Compilation:  javac -classpath .:jl1.0.jar MP3.java         (OS X)
 *                javac -classpath .;jl1.0.jar MP3.java         (Windows)
 *  Execution:    java -classpath .:jl1.0.jar MP3 filename.mp3  (OS X / Linux)
 *                java -classpath .;jl1.0.jar MP3 filename.mp3  (Windows)
 *  
 *  Plays an MP3 file using the JLayer MP3 library.
 *
 *  Reference:  http://www.javazoom.net/javalayer/sources.html
 *
 *
 *  To execute, get the file jl1.0.jar from the website above or from
 *
 *      http://www.cs.princeton.edu/introcs/24inout/jl1.0.jar
 *
 *  and put it in your working directory with this file MP3.java.
 *
 *************************************************************************/

package Nibbles;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import java.net.URL;
import javazoom.jl.player.Player;


public class MP3 {
    //private String filename;
    private Player player;
    //private URL url;
    
    // constructor that takes the name of an MP3 file
    
    public void close() { if (player != null) player.close(); }

    // play the MP3 file to the sound card
    public void play(String filename) {
        try {
            URL url = new MP3().getClass().getResource(filename);
            BufferedInputStream bis = new BufferedInputStream(url.openStream());
            player = new Player(bis);
        }
        catch (Exception e) {
            System.out.println("Problem playing file " + filename);
            System.out.println(e);
        }

        // run in new thread to play in background
        new Thread() {
            @Override
            public void run() {
                try { player.play(); }
                catch (Exception e) { System.out.println(e); }
            }
        }.start();
    }
    
}
