/*
 * The Dream Machine
 */

package Global;

import javax.swing.*;
import java.awt.*;
import java.applet.*;
import java.io.*;
import java.net.*;


/**
 * This class conatins all utilities and global methods used
 * by the program.  This method has no private members, and
 * all methods are strictly static and public. 
 * 
 * <p>
 * NOTE:  There is also no constructors for this class.
 * </p>
 *
 * @author chase isabelle
 */
public class Utilities {
    
    /**
     * This method will return a random integer between a and b
     * 
     * @param a is the lower bound
     * @param b is the upper bound
     * @return a random int between a and b
     */
    public static int randomInt(int a, int b) {
        if (a > b) {
            return 0;
        }
        return (int)(Math.random() * (b - a + 1)) + a;
    }
    
    /**
     * This method will resize an image icon to the given dimensions.
     * 
     * @param w is the new width of the image icon
     * @param h is the new height of the image icon
     * @return the resized image icon
     */
    public static ImageIcon resizeImageIcon(int w, int h, ImageIcon imageIcon) {
        if (w == 0 || h == 0) {
            return null;
        }
        return new ImageIcon(imageIcon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
    }
    
    /**
     * This method will play a sound file.
     * 
     * @param file is the wav file.
     */
    public static void playWav(File file) {
        // play wav sound
        try {
            System.out.println(file);
            URL url = new URL(file.getAbsolutePath());
            Applet.newAudioClip(url).play();
        } catch (Exception exception) {
            // for programmer's use only
            System.out.println(exception.getMessage());
        }
    }

}
