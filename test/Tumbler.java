/*
 * The Dream Machine
 */



import Global.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

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
        
        // set the opacity
        setOpaque(true);
        
        // set the default for the spin flag
        setSpinFlag(true);
        
        // add all the slot icons to the vector
        for (int i=0; i<maxSlotIcons+1; i++) {
            // instantiate all the needed slot icons
            slotIconVector.add(new SlotIcon());
            
            // and add them to this component
            add((SlotIcon)slotIconVector.get(i));
        }
        
        // show this
        setVisible(true);
    }
    
    /**
     * spins this tumbler
     */
    public void spin() {
        // first, check to see if the spin flag is even activated
        if (!getSpinFlag()) {
            return;
        }
        
        // this variable is needed as a reference to the tumbler inside
        // the fallowing thread
        final Tumbler thisTumbler = this;
        
        // create a thread to keep the GUI consistnetly updated
        new Thread(new Runnable() {
            // the run method is called when the thread starts
            public void run() {
                // init algorithm variables
                double dy = 1;
                double dyInc = 1;
                double yDisplaced = 0;
                double yDisplacedMax = (((SlotIcon)slotIconVector.get(slotIconVector.size()-1)).getHeight()+getHeight());
                
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
                        SlotIcon slotIcon = (SlotIcon)slotIconVector.get(i);
                        
                        // get the x position
                        double x = slotIcon.getPosition().x;
                    
                        // get the new y position for the slot icon
                        double y = slotIcon.getPosition().y + dy;
                        if (y >= getHeight()) {
                            slotIcon.setRandomCurrentIcon();
                            if (i == slotIconVector.size() - 1) {
                                y = ((SlotIcon)slotIconVector.get(0)).getPosition().y - slotIcon.getHeight();
                            } else {
                                y = ((SlotIcon)slotIconVector.get(i + 1)).getPosition().y - slotIcon.getHeight();
                            }
                        }
                        
                        // set the new Y position
                        slotIcon.setPosition(x, y);                     
                    }
                    
                    // slow down the loop for smooth look
                    try {
                        Thread.sleep(15);
                    } catch (Exception exception) {
                        // for programmer's use only
                        System.out.println(exception.getMessage());
                    }
                    
                    // increment the change in y to give accelleration effect
                    dy += dyInc;
                }
                
                // one more full spin (de-accelleration)
                for(yDisplaced = 0; Math.round(yDisplaced)<=yDisplacedMax; yDisplaced+=dy) {
                    // spin each icon
                    for (int i=slotIconVector.size()-1; i>=0; i--) {
                        // get the current icon
                        SlotIcon slotIcon = (SlotIcon)slotIconVector.get(i);
                        
                        // get the x position
                        double x = slotIcon.getPosition().x;
                    
                        // get the new y position for the slot icon
                        double y = slotIcon.getPosition().y + dy;
                        
                        // check to see if the y position is out of bounds
                        if (y >= getHeight()) {
                            slotIcon.setRandomCurrentIcon();
                            if (i == slotIconVector.size() - 1) {
                                y = ((SlotIcon)slotIconVector.get(0)).getPosition().y - slotIcon.getHeight();
                            } else {
                                y = ((SlotIcon)slotIconVector.get(i + 1)).getPosition().y - slotIcon.getHeight();
                            }
                        }
                        
                        // set the new Y position
                        slotIcon.setPosition(x, y);
                    }
                    
                    // slow down the loop for smooth look
                    try {
                        Thread.sleep(15);
                    } catch (Exception exception) {
                        // for programmer's use only
                        System.out.println(exception.getMessage());
                    }
                    
                    // increment the change in y to give accelleration effect
                    dy -= dyInc;
                }
                
                // this is kinda ghetto, but its good for the beta version...
                componentResized(new ComponentEvent(thisTumbler, ComponentEvent.COMPONENT_RESIZED));
            }
        }).start();
    }
    
    /**
     * This method will decide weather this tumbler spins when
     * the spin emthod is activated.
     * 
     * @param spinFlag is the boolean
     */
    public void setSpinFlag(boolean spinFlag) {
        this.spinFlag = spinFlag;
    }
    
    /**
     * This method will get the spin falg.
     * 
     * @return spin flag boolean
     */
    public boolean getSpinFlag() {
        return spinFlag;
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
            SlotIcon slotIcon = (SlotIcon)slotIconVector.get(i);
            
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
    private boolean spinFlag;
    
}
