/*
 * The Dream Machine
 */

package CustomComponent;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import javax.swing.border.*;
import Global.*;

/**
 * This class represents a set of tumblers.
 *
 * @author chase isabelle
 */
public class TumblerDisplay 
extends JLayeredPane 
implements ComponentListener {
    
    /**
     * default constructor
     */
    public TumblerDisplay() {
        // add the component listener
        addComponentListener(this);
        
        // instantiate the vecotr of icons
        tumblerVector = new Vector<Tumbler>();
        
        // add all the tumblers to the vector
        for (int i = 0; i < maxTumblers; i++) {
            // instantiate a new tumbler
            Tumbler tumbler = new Tumbler();
            
            // add all the needed tumblers
            tumblerVector.add(tumbler);
            
            // and add them to this component
            add(tumbler);
        }
        
        // init the colors for the tumblers
        setTumblerColors(Color.BLACK, Color.RED);
        
        // instantiate and init the bet line vector
        betLineVector = new Vector<BetLine>();
        
        // set the motion flag
        isSpinning = false;
        
        // set the start/stop spinning flag
        keepSpinning = false;
        
        // init the bet line being edited to no line
        betLineBeingEdited = null;
        
        // set the opacity
        setOpaque(true);
        
        // show this
        setVisible(true);
    }
    
    /**
     * Gets the number of tumbler in the display.
     * 
     * @return the number of tumblers in the display.
     */
    public int getMaxTumblers() {
        return maxTumblers;
    }
    
    /**
     * spins the tumblers in the display.
     * 
     * @return the thread that is the spinning tumblers.
     */
    public Thread startSpin() {
        // check to see if tumblers can spin
        if (inMotion()) {
            return null;
        }
        isSpinning = true;
        keepSpinning = true;
        
        // init the bet line being edited to no line
        betLineBeingEdited = null;
        
        // hide the lines if they're shwoing
        hideBetLines();
        
        // use a thread to keep the GUI consistnetly updated
        Thread thread = new Thread(new Runnable() {
            // the run method is called when the thread starts
            public void run() {
                // the vector of threads that are running each tumbler's
                // spinning algorithm
                Vector<Thread> threadVector = new Vector<Thread>();
                
                // spin each tumbler one at a time
                for (int i = 0; i < tumblerVector.size(); i++) {
                    // spin each tumbler in the display
                    threadVector.add(getTumbler(i).startSpin());
                    
                    // give a a little time so all tumblers don't start at once
                    // this will give the slot machine a more real look and feel
                    try {
                        Thread.sleep(300);
                    } catch (Exception exception) {
                        // empty body
                    }
                    
                    // because of the delay between the start of each tumbler's
                    // spinning, check to see if the user has cancelled a spin
                    // during the delay between the first tumbler's spin start
                    // and the last tumblers spin start
                    if (!keepSpinning) {
                        stopSpin();
                    }
                }
                
                // reset the motion flag when spinning has completed for each 
                // tumbler
                // check each tumbler's spinning thread
                for (int i = 0; i < threadVector.size(); i++) {
                    // wait until the thread keels
                    try {
                        threadVector.get(i).join();
                    } catch (Exception exception) {
                        // empty body
                    }
                }
                isSpinning = false;
                keepSpinning = false;
            }
        });
        
        // start the thread
        thread.start();
        
        // return the thread
        return thread;
    }
    
    /**
     * stops the spinning
     */
    public void stopSpin() {
        // stop each tumbler from spinning
        for (int i = 0; i < tumblerVector.size(); i++) {
            getTumbler(i).stopSpin();
        }
        keepSpinning = false;
    }
    
    /**
     * This method is overloaded in order to draw the lines
     * that the user has decided to play.  It is not the ideal
     * way to draw lines to the display; however, it works for
     * now.  What is ideal is to have the showBetLines/hideBetLines
     * methods call each BetLine's draw method and just use that to
     * paint to the display.  But there was some trouble with
     * Graphics objects, hence, this overloaded paint method.
     * 
     * @param graphics is the Graphics object.
     */
    public void paint(Graphics graphics) {
        // call parents paint method
        super.paint(graphics);
        
        // paint the visible lines
        for (int i = 0; i < betLineVector.size(); i++) {
            // get the line in the line vextor
            BetLine betLine = betLineVector.get(i);
            
            // if the line should be painted
            if (betLine.isVisible()) {
                // then do it
                betLine.draw();
            }
        }
    }
    
    /**
     * Tells weather or not the display is in motion.
     * 
     * @return the is spinning flag
     */
    public boolean inMotion() {
        return isSpinning;
    }
    
    /**
     * This method will retrieve the tumbler at index t.
     * 
     * @param t is the index of the tumbler in the display.
     * @return the target tumbler.
     */
    public Tumbler getTumbler(int t) {
        return tumblerVector.get(t);
    }
    
    /**
     * This method will retrieve te slot icon component at
     * tumbler t and slot index s.
     * 
     * @param t is the tumbler index in the display.
     * @param s is the slot index in the tumbler.
     * @return the target slot icon.
     */
    public SlotIcon getSlotIcon(int t, int s) {
        return tumblerVector.get(t).getSlotIcon(s);
    }
    
    /**
     * Unlocks all the tumblers in the display.
     */
    public void unlockAll() {
        for (int i = 0; i < tumblerVector.size(); i++) {
            tumblerVector.get(i).unlock();
        }
    }
    
    /**
     * shows all the bet lines on the display.
     */
    public void showBetLines() {
        // set all the belines to visible
        for (int i = 0; i < betLineVector.size(); i++) {
            betLineVector.get(i).setVisible(true);
        }
        
        // init the bet line being edited to no line
        betLineBeingEdited = null;
        
        // repaint
        // not ideal, see overloaded paint method for more info
        repaint();
    }
    
    /**
     * hides all bet lines from user
     */
    public void hideBetLines() {
        // hide the bet lines
        for (int i = 0; i < betLineVector.size(); i++) {
            betLineVector.get(i).setVisible(false);
        }
        
        // repaint
        // not ideal, see overloaded paint method for more info
        repaint();
    }
    
    /**
     * Shows all bet lines that have winning combos.
     */
    public void showWinningBetLines() {
        // hide all lines
        hideBetLines();
        
        // now set visible the lines that are winners
        for (int i = 0; i < betLineVector.size(); i++) {
            // get the current line
            BetLine betLine = betLineVector.get(i);
            
            // if its a winner then make it visible
            if (betLine.isWinner()) {
                betLine.setVisible(true);
            }
        }
    }
    
    /**
     * Adds a line of randomly chosen slot icons to be played.
     */
    public void addRandomizedBetLine() {
        // init the bet line being edited to no line
        betLineBeingEdited = null;
        
        // new vector for the icons
        Vector vector = new Vector();
        
        // add a random icon from each tumbler
        for (int i = 0; i < maxTumblers; i++) {
            vector.add(getSlotIcon(i, Utilities.randomInt(0, tumblerVector.get(i).getMaxSlotIcons() - 1)));
        }
        
        // add the new bet line to the vector of bet lines
        BetLine betLine = new BetLine(this, vector);
        betLineVector.add(betLine);
        
        // show the new bet line
        showBetLine(betLine);
    }
    
    /**
     * Shows a given bet line.
     * 
     * @param betLine is the bet line that is to be shown.
     */
    public void showBetLine(BetLine betLine) {
        // hide all other bet lines
        hideBetLines();
        
        // make sure bet line is no null
        if (betLine == null) {
            return;
        }
        
        // set the visibility of the given bet line
        betLine.setVisible(true);
        
        // paint the display
        repaint();
    }
    
    /**
     * Adds a new custom bet line.
     * 
     * <p>
     * NOTE:  This will prompt the tumbler display toedit the newly added bet line.
     * </p>
     */
    public void addCustomLine() {
        // add a randomized line
        addRandomizedBetLine();
        
        // and set the new randomized line to be the edit line
        BetLine betLine = betLineVector.lastElement();
        
        // prompt this bet line for editing
        editBetLine(betLine);
    }
    
    /**
     * Prompts the user to edit a given bet line.
     * 
     * @param betLine is the given bet line to be edited.
     */
    public void editBetLine(BetLine betLine) {
        // show the bet line
        showBetLine(betLine);
        
        // set the bet line being edited to the bet line
        betLineBeingEdited = betLine;
    }
    
    /**
     * Retrieves the current bet line being editted.
     * 
     * @return the bet line being edited.
     */
    public BetLine getBetLineBeingEdited() {
        return betLineBeingEdited;
    }
    
    /**
     * This method will check the lines that the user played
     * for winning combos.
     * 
     * @param betAmount is the amount of credits the user is betting.
     * @return the amount of credits being won.
     */
    public int getWinnings(int betAmount) {
        // check each bet line for a win amount
        int winnings = 0;
        for (int i = 0; i < betLineVector.size(); i++) {
            winnings += betLineVector.get(i).getWinnings(betAmount);
        }
        
        // return the users bet times the winning multipliers
        return winnings;
    }
    
    /**
     * This method will set the colors for the tumblers.
     * 
     * @param oddTumblerColor is the color of the odd tumblers.
     * @param evenTumblerColor is the color of the even tumblers.
     */
    public void setTumblerColors(Color oddTumblerColor, Color eventTumblerColor) {
        boolean oddFlag = true;  // count 0 as odd
        
        // iterate through each tumbler
        for (int i = 0; i < tumblerVector.size(); i++) {
            // set the color for each tumbler
            if (oddFlag) {
                getTumbler(i).setBackground(oddTumblerColor);
            } else {
                getTumbler(i).setBackground(eventTumblerColor);
            }
            
            // go from even to odd, etc.
            oddFlag = !oddFlag;
        }
    }
    
    /**
     * Retrieves the vector of bet lines for this display.
     * 
     * @return the bet line vector.
     */
    public Vector<BetLine> getBetLineVector() {
        return betLineVector;
    }

    /**
     * This method is called when the component is resized.
     * 
     * @param event is the event that triggered this action.
     */
    public void componentResized(ComponentEvent event) {
        // resize and relocate the tumblers
        int width = (int)Math.round((double)getWidth() / (double)maxTumblers);
        int x = 0;
        for (int i = 0; i < tumblerVector.size(); i++) {
            // get the current tumbler
            Tumbler tumbler = getTumbler(i);
            
            // change the location of the tumbler
            tumbler.setLocation(x, 0);
            x += width;
            
            // change the size fo the tumbler
            Dimension dimension = new Dimension(width, getHeight());
            tumbler.setDimension(dimension);
        }
    }

    /**
     * This method is called when the component is moved.
     * 
     * @param event is the event that triggered this action.
     */
    public void componentMoved(ComponentEvent event) {
        // empty body
    }

    /**
     * This method is called when the component is shown.
     * 
     * @param event is the event that triggered this action.
     */
    public void componentShown(ComponentEvent event) {
        // resize compoinent stuff
        componentResized(new ComponentEvent(this, ComponentEvent.COMPONENT_RESIZED));
    }

    /**
     * This method is called when the component is hidden.
     * 
     * @param event is the event that triggered this action.
     */
    public void componentHidden(ComponentEvent event) {
        // empty body
    }
    
    /**
     * This is the array, or vector, of the icons on the tumbler.
     */
    private Vector<Tumbler> tumblerVector;

    /**
     * Represents the number of icons to a tumbler
     */
    private final static int maxTumblers = 5;
    
    /**
     * keeps track of the motion of each tumbler.  is true if
     * one or more tumblers are in motion, and false if not
     */
    private boolean isSpinning;
    
    /**
     * Tells wether or not the tumber should continue spinning.  This
     * boolean should be set to true when the user starts spinning the
     * tumbler and set to false when the user wishes the tumbler to 
     * keel it's spinningness.
     */
    private boolean keepSpinning;
    
    /**
     * This vector keeps track of all the lines that the user is 
     * currently playing.
     */
    private Vector<BetLine> betLineVector;
    
    /**
     * This bet line is used to keep track of the current
     * bet line that the user is editing.
     */
    private BetLine betLineBeingEdited;
    
}