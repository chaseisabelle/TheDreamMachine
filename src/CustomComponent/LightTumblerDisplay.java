/*
 * The Dream Machine
 */

package CustomComponent;

import Global.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

/**
 * This class represents a border of 'lights' that will
 * surround a component.
 *
 * @author Rob Whalen & chase isabelle
 */
public class LightTumblerDisplay 
extends JLayeredPane 
implements ComponentListener {
    
    /**
     * Default constructor.
     */
    public LightTumblerDisplay() {
        // add the component listener
        addComponentListener(this);
        
        // instantiate the tumbler display
        tumblerDisplay = new TumblerDisplay();
        
        // init flags
        keepFlashing = false;
        isFlashing = false;
        
        // instantiate the vectors
        topBorder = new Vector<Light>();
        bottomBorder = new Vector<Light>();
        leftBorder = new Vector<Light>();
        rightBorder = new Vector<Light>();
        
        // instantiate the lights in the borders
        for (int i = 0; i < maxHorizontalLights; i++) {
            topBorder.add(new Light());
        }
        for (int i = 0; i < maxHorizontalLights; i++) {
            bottomBorder.add(new Light());
        }
        for (int i = 0; i < maxVerticalLights; i++) {
            leftBorder.add(new Light());
        }
        for (int i = 0; i < maxVerticalLights; i++) {
            rightBorder.add(new Light());
        }
        
        // and the corners
        topLeft = new Light();
        topRight = new Light();
        bottomLeft = new Light();
        bottomRight = new Light();
        
        // add all the needed components
        for (int i = 0; i < topBorder.size(); i++) {
            add(topBorder.get(i));
        }
        for (int i = 0; i < bottomBorder.size(); i++) {
            add(bottomBorder.get(i));
        }
        for (int i = 0; i < leftBorder.size(); i++) {
            add(leftBorder.get(i));
        }
        for (int i = 0; i < rightBorder.size(); i++) {
            add(rightBorder.get(i));
        }
        add(topLeft);
        add(topRight);
        add(bottomLeft);
        add(bottomRight);
        add(tumblerDisplay);
        
        // set the opacity
        setOpaque(true);
        
        // show me
        setVisible(true);
    }
    
    /**
     * Gets the tumbler display component.
     * 
     * @return the tumbler display component.
     */
    public TumblerDisplay getTumblerDisplay() {
        return tumblerDisplay;
    }
    
    /**
     * Turns all the lights on.
     */
    public void turnLightsOn() {
        if (isFlashing()) {
            return;
        }
        
        for (int i = 0; i < topBorder.size(); i++) {
            topBorder.get(i).turnOn();
        }
        for (int i = 0; i < bottomBorder.size(); i++) {
            bottomBorder.get(i).turnOn();
        }
        for (int i = 0; i < leftBorder.size(); i++) {
            leftBorder.get(i).turnOn();
        }
        for (int i = 0; i < rightBorder.size(); i++) {
            rightBorder.get(i).turnOn();
        }
        topLeft.turnOn();
        topRight.turnOn();
        bottomLeft.turnOn();
        bottomRight.turnOn();
    }
    
    /**
     * Turns all the lights off.
     */
    public void turnLightsOff() {
        if (isFlashing()) {
            return;
        }
        
        for (int i = 0; i < topBorder.size(); i++) {
            topBorder.get(i).turnOff();
        }
        for (int i = 0; i < bottomBorder.size(); i++) {
            bottomBorder.get(i).turnOff();
        }
        for (int i = 0; i < leftBorder.size(); i++) {
            leftBorder.get(i).turnOff();
        }
        for (int i = 0; i < rightBorder.size(); i++) {
            rightBorder.get(i).turnOff();
        }
        topLeft.turnOff();
        topRight.turnOff();
        bottomLeft.turnOff();
        bottomRight.turnOff();
    }
    
    /**
     * Tells weather the lights are flashing or not.
     * 
     * @return true if the lights are flashing, flase if not.
     */
    public boolean isFlashing() {
        return isFlashing;
    }
    
    /**
     * Flashes the lights
     */
    public void flashLights() {
        // create a new thread to keep the GUI consistent
        new Thread(new Runnable() {
            // called when the thread starts
            public void run() {
                // check if lights are already flashing
                if (isFlashing()) {
                    return;
                }
                
                // set the keep flashing flag
                keepFlashing = true;
                
                // set the flashing flag
                isFlashing = true;
                
                // turn all the lights on
                turnLightsOn();
                
                // toggle all the lights as if they were flashing
                while (keepFlashing) {
                    for (int i = 0; i < topBorder.size(); i++) {
                        topBorder.get(i).toggle();
                    }
                    for (int i = 0; i < bottomBorder.size(); i++) {
                        bottomBorder.get(i).toggle();
                    }
                    for (int i = 0; i < leftBorder.size(); i++) {
                        leftBorder.get(i).toggle();
                    }
                    for (int i = 0; i < rightBorder.size(); i++) {
                        rightBorder.get(i).toggle();
                    }
                    topLeft.toggle();
                    topRight.toggle();
                    bottomLeft.toggle();
                    bottomRight.toggle();
                    
                    // take a time out
                    try {
                        Thread.sleep(500);
                    } catch (Exception exception) {
                        // empty body
                    }
                }
                
                // set the is flashing flag
                isFlashing = false;
                
                // reset all the lights to off
                turnLightsOff();
            }
        }).start();
    }
    
    /**
     * Stops the lights from flashing.
     */
    public void stopFlashingLights() {
        keepFlashing = false;
    }
    
    /**
     * Called when the component is resized.
     * 
     * @param event is the event that triggeres this method.
     */
    public void componentResized(ComponentEvent event) {
        // first, size the lights and corners
        double width = (double)getWidth() / ((double)maxHorizontalLights + 2); //< +2 for the corners!
        double height = (double)getHeight() / ((double)maxVerticalLights + 2); //< +2 for the corners!
        Dimension lightDimension = new Dimension((int)Math.round(width), (int)Math.round(height));
        
        // now set the locations and sizes of each light
        // top border location
        double x = width;
        double y = 0;
        for (int i = 0; i < topBorder.size(); i++) {
            topBorder.get(i).setPosition(x, y);
            topBorder.get(i).setPreferredSize(lightDimension);
            topBorder.get(i).setSize(lightDimension);
            x += width;
        }
        
        // bottom border locations
        x = width;
        y = getHeight() - height;
        for (int i = 0; i < bottomBorder.size(); i++) {
            bottomBorder.get(i).setPosition(x, y);
            bottomBorder.get(i).setPreferredSize(lightDimension);
            bottomBorder.get(i).setSize(lightDimension);
            x += width;
        }
        bottomRight.setPosition(x, y);
        
        // left border locations
        x = 0;
        y = height;
        for (int i = 0; i < leftBorder.size(); i++) {
            leftBorder.get(i).setPosition(x, y);
            leftBorder.get(i).setPreferredSize(lightDimension);
            leftBorder.get(i).setSize(lightDimension);
            y += height;
        }
        
        // right border
        x = getWidth() - width;
        y = height;
        for (int i = 0; i < rightBorder.size(); i++) {
            rightBorder.get(i).setPosition(x, y);
            rightBorder.get(i).setPreferredSize(lightDimension);
            rightBorder.get(i).setSize(lightDimension);
            y += height;
        }
        
        // set the size and locations of the corners
        x = 0;
        y = 0;
        topLeft.setPosition(x, y);
        topLeft.setPreferredSize(lightDimension);
        topLeft.setSize(lightDimension);
        x = getWidth() - width;
        y = 0;
        topRight.setPosition(x, y);
        topRight.setPreferredSize(lightDimension);
        topRight.setSize(lightDimension);
        x = 0;
        y = getHeight() - height;
        bottomLeft.setPosition(x, y);
        bottomLeft.setPreferredSize(lightDimension);
        bottomLeft.setSize(lightDimension);
        x = getWidth() - width;
        y = getHeight() - height;
        bottomRight.setPosition(x, y);
        bottomRight.setPreferredSize(lightDimension);
        bottomRight.setSize(lightDimension);
        
        // set the location and size of the tumbler display
        width = getWidth() - (2 * width);
        height = getHeight() - (2 * height);
        Dimension tumblerDisplayDimension = new Dimension((int)Math.round(width), (int)Math.round(height));
        tumblerDisplay.setPreferredSize(tumblerDisplayDimension);
        tumblerDisplay.setSize(tumblerDisplayDimension);
        tumblerDisplay.setLocation(topLeft.getWidth(), topLeft.getHeight());
    }

    /**
     * Called when the component is moved.
     * 
     * @param event is the event that triggers this action.
     */
    public void componentMoved(ComponentEvent event) {
        // empty body
    }

    /**
     * Called when the component is shown.  Weeee!
     * 
     * @param event is the event that triggers this method.
     */
    public void componentShown(ComponentEvent event) {
        // resize this
        componentResized(new ComponentEvent(this, ComponentEvent.COMPONENT_RESIZED));
    }

    /**
     * Called when the component is hidden.
     * 
     * @param event is the event that triggers this action.
     */
    public void componentHidden(ComponentEvent event) {
        // empty body
    }
    
    /**
     * The basic tumbler display
     */
    private TumblerDisplay tumblerDisplay;
    
    /**
     * The number of lights in each border.
     */
    private int maxHorizontalLights = 15;
    private int maxVerticalLights = 15;
    
    /**
     * The vectors of borders.  The left, right, top, and bottom borders
     */
    private Vector<Light> topBorder;
    private Vector<Light> bottomBorder;
    private Vector<Light> leftBorder;
    private Vector<Light> rightBorder;
    
    /**
     * The corners of the borders.
     */
    private Light topLeft;
    private Light topRight;
    private Light bottomLeft;
    private Light bottomRight;
    
    /**
     * Activated/deactivated when light flashin threads activate
     */
    private boolean keepFlashing;
    
    /**
     * Used to tell weather a flashing thread is active.
     */
    private boolean isFlashing;

}

/**
 * This class represents a Light component.
 */
class Light
extends JLabel 
implements ComponentListener {
    
    /**
     * Default constructor
     */
    public Light() {
        // add this as a component listener
        addComponentListener(this);
        
        // instantiate and init the on and off images
        onImageIcon = new ImageIcon(getClass().getResource("/Images/lightOn.png"));
        offImageIcon = new ImageIcon(getClass().getResource("/Images/lightOff.png"));
        turnOff();
        
        // set the opacity
        setOpaque(false);
        
        // instantiate tge position
        position = new Position(getLocation().x, getLocation().y);
        
        // show me
        setVisible(true);
    }
    
    /**
     * turns the light on
     */
    public void turnOn() {
        // set the flag
        onFlag = true;
        
        // set the image
        setIcon(Utilities.resizeImageIcon(getWidth(), getHeight(), onImageIcon));
    }
    
    /**
     * truns the light off
     */
    public void turnOff() {
        // set the flag
        onFlag = false;
        
        // set the image
        setIcon(Utilities.resizeImageIcon(getWidth(), getHeight(), offImageIcon));
    }
    
    /**
     * turns the light on if it's off, and vice-versa
     */
    public void toggle() {
        if (isOn()) {
            turnOff();
        } else {
            turnOn();
        }
    }
    
    /**
     * returns true if the ligh is on, and false if not
     * 
     * @return true if the light is on, and false in not
     */
    public boolean isOn() {
        return onFlag;
    }
    
    /**
     * Sets the exact position of this component.
     * 
     * @param x is the x position.
     * @param y is the y position.
     */
    public void setPosition(double x, double y) {
        // set the location
        setLocation((int)Math.round(x), (int)Math.round(y));
        
        // set the position variables
        position.x = x;
        position.y = y;
    }
    
    /**
     * gets the position of this component.
     * 
     * @return the position of this component.
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Called when the component is resized.
     * 
     * @param event is the event that triggeres this method.
     */
    public void componentResized(ComponentEvent event) {
        // resize the image
        if (isOn()) {
            setIcon(Utilities.resizeImageIcon(getWidth(), getHeight(), onImageIcon));
        } else {
            setIcon(Utilities.resizeImageIcon(getWidth(), getHeight(), offImageIcon));
        }
    }

    /**
     * Called when the component is moved.
     * 
     * @param event is the event that triggers this action.
     */
    public void componentMoved(ComponentEvent event) {
        // empty body
    }

    /**
     * Called when the component is shown.  Weeee!
     * 
     * @param event is the event that triggers this method.
     */
    public void componentShown(ComponentEvent event) {
        // resize this
        componentResized(new ComponentEvent(this, ComponentEvent.COMPONENT_RESIZED));
    }

    /**
     * Called when the component is hidden.
     * 
     * @param event is the event that triggers this action.
     */
    public void componentHidden(ComponentEvent event) {
        // empty body
    }
    
    /**
     * keeps track of weather or not the light is on or off
     */
    private boolean onFlag;
    
    /**
     * the image of a lit light
     */
    private ImageIcon onImageIcon;
    
    /**
     * the image of an unlit light
     */
    private ImageIcon offImageIcon;
    
    /**
     * the exact position of the component.
     */
    private Position position;
    
}