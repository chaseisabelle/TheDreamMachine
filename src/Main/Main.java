/*
 * The Dream Machine
 */

package Main;

import java.awt.EventQueue;

/**
 * This class is the first class to be instantiated.  It contains the 
 * main method that is called by the OS.
 *
 * @author chase isabelle
 */
public class Main {
    
    /**
     * the main method, called by the op sys.
     * 
     * @param args is the arguments passed from op sys
     */
    public static void main(String[] args) {
        // testing...
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                new JFrame.TestJFrame();
            }
        });
    }

}
