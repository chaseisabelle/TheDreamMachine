/*
 * The Dream Machine
 */

package CustomComponent;

import Global.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import javax.swing.border.LineBorder;

/**
 * This class represents a spinning wheel of icons.
 *
 * @author chase isabelle
 */
public class Tumbler 
extends JLayeredPane
implements ComponentListener {
    
    /**
     * default constructor
     */
    public Tumbler() {
        // add the component listener
        addComponentListener(this);
        
        // instantiate the vecotr of icons
        slotIconVector = new Vector();
        
        // set the motion flag
        isSpinning = false;
        
        // set the start/stop spinning flag
        keepSpinning = false;
        
        // set the opacity
        setOpaque(true);
        
        // set the default for the spin flag
        unlock();
        
        // add all the slot icons to the vector
        for (int i=0; i<maxSlotIcons+1; i++) {
            // intantiate a new icon
            SlotIcon slotIcon = new SlotIcon();
            
            // instantiate all the needed slot icons
            slotIconVector.add(slotIcon);
            
            // and add them to this component
            add(slotIcon);
        }
        
        // show this
        setVisible(true);
    }
    
    /**
     * If this tumbler belongs to a tumbler display then this method
     * will returnt hat tumbler display.
     * 
     * @return the tumbler display which this tumbler belongs to.
     */
    public TumblerDisplay getTumblerDisplay() {
        return (TumblerDisplay)getParent();
    }
    
    /**
     * Gets the number of slot icons in a tumbler.
     * 
     * @return the number of icons in a tumbler.
     */
    public int getMaxSlotIcons() {
        return maxSlotIcons;
    }
    
    /**
     * spins this tumbler for given number of spins
     * 
     * @return the thread that is the spinning of the tumbler
     */
    public Thread startSpin() {
        // first, check to see if the spin flag is even activated
        if (isSpinning || lockFlag) {
            return null;
        }
        isSpinning = true;
        keepSpinning = true;
        
        // this variable is needed as a reference to the tumbler inside
        // the fallowing thread
        final Tumbler thisTumbler = this;
        
        // create a thread to keep the GUI consistnetly updated
        Thread thread = new Thread(new Runnable() {
            // the run method is called when the thread starts
            public void run() {
                // init algorithm variables
                double dy = 1;
                double dyInc = 1;
                double yDisplaced = 0;
                double yDisplacedMax = (getSlotIcon(slotIconVector.size()-1).getHeight()+getHeight());
                long sleepInterval = 20;   //< milliseconds
                
                // get needed loop limits
                for(yDisplaced = 0; yDisplaced<=yDisplacedMax; yDisplaced+=dy) {
                    // increment the changein y to give accelleration effect
                    dy += dyInc;
                }
                
                // one full spin (accelleration)
                dyInc = yDisplacedMax/yDisplaced;
                dy = dyInc;
                for(yDisplaced = 0; Math.round(yDisplaced)<=yDisplacedMax; yDisplaced+=dy) {
                    // spin each icon
                    for (int i=slotIconVector.size()-1; i>=0; i--) {
                        // get the current icon
                        SlotIcon slotIcon = getSlotIcon(i);
                        
                        // get the x position
                        double x = slotIcon.getPosition().x;
                    
                        // get the new y position for the slot icon
                        double y = slotIcon.getPosition().y + dy;
                        if (y >= getHeight()) {
                            slotIcon.setRandomCurrentIcon();
                            if (i == slotIconVector.size() - 1) {
                                y = getSlotIcon(0).getPosition().y - slotIcon.getHeight();
                            } else {
                                y = getSlotIcon(i + 1).getPosition().y - slotIcon.getHeight();
                            }
                        }
                        
                        // set the new Y position
                        slotIcon.setPosition(x, y);                     
                    }
                    
                    // slow down the loop for smooth look
                    try {
                        Thread.sleep(sleepInterval);
                    } catch (Exception exception) {
                        // empty body
                    }
                    
                    // increment the change in y to give accelleration effect
                    dy += dyInc;
                }
                
                // continue rolling until the user decides to keel the wheel...
                while (keepSpinning) { 
                    // one full spin
                    for (yDisplaced = 0; Math.round(yDisplaced) <= yDisplacedMax; yDisplaced += (int) Math.round(dy)) {
                        // spin each icon
                        for (int i = slotIconVector.size() - 1; i >= 0; i--) {
                            // get the current icon
                            SlotIcon slotIcon = getSlotIcon(i);

                            // get the x position
                            int x = slotIcon.getLocation().x;

                            // get the new y position for the slot icon
                            int y = slotIcon.getLocation().y + (int) Math.round(dy);
                            if (y >= getHeight()) {
                                slotIcon.setRandomCurrentIcon();
                                if (i == slotIconVector.size() - 1) {
                                    y = getSlotIcon(0).getLocation().y - slotIcon.getHeight();
                                } else {
                                    y = getSlotIcon(i + 1).getLocation().y - slotIcon.getHeight();
                                }
                            }

                            // set the new Y position
                            slotIcon.setLocation(x, y);
                        }

                        // slow down the loop for smooth look
                        try {
                            Thread.sleep(sleepInterval);
                        } catch (Exception exception) {
                            // empty body
                        }
                    }
                }

                // one more full spin (de-accelleration)
                for (yDisplaced = 0; Math.round(yDisplaced) <= yDisplacedMax; yDisplaced += dy) {
                    // spin each icon
                    for (int i = slotIconVector.size() - 1; i >= 0; i--) {
                        // get the current icon
                        SlotIcon slotIcon = getSlotIcon(i);

                        // get the x position
                        double x = slotIcon.getPosition().x;

                        // get the new y position for the slot icon
                        double y = slotIcon.getPosition().y + dy;

                        // check to see if the y position is out of bounds
                        if (y >= getHeight()) {
                            slotIcon.setRandomCurrentIcon();
                            if (i == slotIconVector.size() - 1) {
                                y = getSlotIcon(0).getPosition().y - slotIcon.getHeight();
                            } else {
                                y = getSlotIcon(i + 1).getPosition().y - slotIcon.getHeight();
                            }
                        }

                        // set the new Y position
                        slotIcon.setPosition(x, y);
                    }

                    // slow down the loop for smooth look
                    try {
                        Thread.sleep(sleepInterval);
                    } catch (Exception exception) {
                        // empt body
                    }

                    // increment the change in y to give accelleration effect
                    dy -= dyInc;
                }
                
                // a final chekc to be sure all slot icons resume their normal positions
                componentResized(new ComponentEvent(thisTumbler, ComponentEvent.COMPONENT_RESIZED));
                
                // reset the motion flag
                isSpinning = false;
            }
        });
        
        // run the thread
        thread.start();
        
        // and return the thread
        return thread;
    }
    
    /**
     * This method will stop this tumbler from spinning at any moment 
     * in time, but only if it's already spinning.
     */
    public void stopSpin() {
        keepSpinning = false;
    }
    
    /**
     * This method will lock this tumbler in place.  Thus, the
     * tumbler will not spin when the user spins the display.
     */
    public void lock() {
        lockFlag = true;
    }
    
    /**
     * This method will unlock this tumbler.  Thus, the tumbler
     * will spin with all the other tumblers.  Can ya dig it?
     */
    public void unlock() {
        lockFlag = false;
    }
    
    /**
     * This method will get the spin falg.
     * 
     * @return spin flag boolean
     */
    public boolean isLocked() {
        return lockFlag;
    }
    
    /**
     * tells weatehr the tumbler is in motion via motion flag
     * 
     * @return is spinning boolean
     */
    public boolean inMotion() {
        return isSpinning;
    }
    
    /**
     * This method will retrieve a slot icon at slot index s.
     * 
     * @param s is the slot index of the target slot icon.
     * @return the slot icon component at index s.
     */
    public SlotIcon getSlotIcon(int s) {
        return (SlotIcon)slotIconVector.get(s);
    }
    
    /**
     * This method is used to set the size of this component for the tumbler display's
     * component resizing method.
     * 
     * @param dimension is the new dimension of the tumbler.
     */
    public void setDimension(Dimension dimension) {
        setPreferredSize(dimension);
        setSize(dimension);
        componentResized(new ComponentEvent(this, ComponentEvent.COMPONENT_RESIZED));
    }

    /**
     * This method is called when the component is resized.
     * 
     * @param event is the event that triggered this action.
     */
    public void componentResized(ComponentEvent event) {
        // resize and relocate the slot icons
        double height = (double)getHeight() / (double)maxSlotIcons;
        double y = 0;
        for (int i=0; i<slotIconVector.size(); i++) {
            // get the current slot icon
            SlotIcon slotIcon = getSlotIcon(i);
            
            // change the location of the slot icon
            slotIcon.setPosition(0, y);
            y += height;
            
            // change the size fo the slot icon
            Dimension dimension = new Dimension(getWidth(), (int)Math.round(height));
            slotIcon.setDimension(dimension);
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
    private Vector slotIconVector;

    /**
     * represents the number of icons to a tumbler
     */
    private final static int maxSlotIcons = 5;
    
    /**
     * Decides weather the user wishes to spin this tumbler when the
     * spin method is activated.
     */
    private boolean lockFlag;
    
    /**
     * Tell wether or not this tumbler is in motion
     */
    private boolean isSpinning;
    
    /**
     * Tells wether or not the tumber should continue spinning.  This
     * boolean should be set to true when the user starts spinning the
     * tumbler and set to false when the user wishes the tumbler to 
     * keel it's spinningness.
     */
    private boolean keepSpinning;
    
}
