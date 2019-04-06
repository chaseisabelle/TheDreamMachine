/*
 * The Dream Machine
 */

package CustomComponent;

import Global.*;
import java.awt.*;
import java.util.*;

/**
 * This class represents a line accross the tumbler
 * that determines which slot icons the user has decided
 * to play.
 *
 * @author chase isabelle
 */
public class BetLine {
    
    /**
     * Constructor.
     * 
     * @param TumblerDisplay is the tumbler display that this line is being played on.
     * @param slotIconVector is a vector of all the slot icons that this line will play.
     */
    public BetLine(TumblerDisplay tumblerDisplay, Vector<SlotIcon> slotIconVector) {
        // init the tumbler display
        this.tumblerDisplay = tumblerDisplay;
        
        // init the slot icon vector
        this.slotIconVector = slotIconVector;
        
        // init a random color for this line
        this.setRandomColor();
        
        // init the visibility
        setVisible(false);
    }
    
    /**
     * Retrieves the vector of slot icons.
     * 
     * @return the slot icon vector.
     */
    public Vector<SlotIcon> getSlotIconVector() {
        return slotIconVector;
    }
    
    /**
     * Checks to see if this line is a winning combo.
     */
    public boolean isWinner() {
        // check to see if winnings are nada
        if (getWinnings(1) <= 0) {
            return false;
        }
        return true;
    }
    
    /**
     * Draws this bet line to the given display or hides it from display
     * depending on param.
     * 
     * @param isVisible is the flag that determines this lines visibility.
     */
    public void setVisible(boolean isVisible) {
        // set the flag
        this.isVisible = isVisible;
    }
    
    /**
     * Tells weather this bet line is visible to the display.
     * 
     * @return true if this line is visible to the display, false if not.
     */
    public boolean isVisible() {
        return isVisible;
    }
    
    /**
     * Sets the color of this line.
     * 
     * @param color is the color for this line.
     */
    public void setColor(Color color) {
        this.color = color;
    }
    
    /**
     * Gets the color of this line.
     * 
     * @return the color of this line.
     */
    public Color getColor() {
        return color;
    }
    
    /**
     * Sets a random color for this line.
     */
    public void setRandomColor() {
        // instaintae a new color
        int r = Utilities.randomInt(0, 255);
        int g = Utilities.randomInt(0, 255);
        int b = Utilities.randomInt(0, 255);
        this.color = new Color(r, g, b);
    }
    
    /**
     * Draws this line to the display.
     */
    public void draw() {
        // instantiate the needed vars
        Vector<Polygon> polygonVector = new Vector<Polygon>();
        int thickness = 6;
        int[] xPoints = new int[slotIconVector.size()];
        int[] yPoints = new int[slotIconVector.size()];
        
        // init the need vars
        for (int i = 0 ; i < slotIconVector.size(); i++) {
            // get each slot icon
            SlotIcon slotIcon = slotIconVector.get(i);
            
            // get the middle of each slot icon with respect to the display
            int x = slotIcon.getTumbler().getLocation().x + slotIcon.getLocation().x + slotIcon.getWidth() / 2;
            int y = slotIcon.getTumbler().getLocation().y + slotIcon.getLocation().y + slotIcon.getHeight() / 2;
            
            // init the poiitns
            xPoints[i] = x;
            yPoints[i] = y;
        }
        
        // 
        
        // get the Graphics object for the display
        // this is kinda of like the display's artist, the display is
        // the artists (Graphics) canvas, so you can tell the artist,
        // (Graphics) to draw what ever you want, hence the object's
        // name, dig it?
        Graphics2D artist = (Graphics2D)tumblerDisplay.getGraphics();
        
        // tell the artist what color to paint with
        artist.setColor(color);
        
        // tell the artist to paint the line
        artist.setStroke(new BasicStroke(thickness));
        artist.drawPolyline(xPoints, yPoints, slotIconVector.size());
    }
    
    /**
     * Gets the amount of credits the user has won.
     * 
     * @param betAmount is the amount of credits being bet.
     * @return the amount of credits being won.
     */
    public int getWinnings(int betAmount) {
        // check line for winning combo
        int winnings = 0;
        SlotIcon tempSlotIcon = slotIconVector.get(0);
        for (int i = 1; i < slotIconVector.size(); i++) {
            if (slotIconVector.get(i).isSame(tempSlotIcon)) {
                tempSlotIcon = slotIconVector.get(i);
                winnings += tempSlotIcon.getWinAmount();
            } else {
                break;
            }
        }
        if (winnings != 0) {
            winnings += tempSlotIcon.getWinAmount();
        }
        winnings *= betAmount;
        
        return winnings;
    }
    
    /**
     * Sets the slot icon for a given tumbler
     * 
     * @param tumbler is the tumbler that the icon i being set for.
     * @param slotIcon is the slot icon in that tumbler.
     */
    public void setSlotIcon(Tumbler tumbler, SlotIcon slotIcon) {
        // check to see if the tumbler has an icon in this line already
        for (int i = 0; i < slotIconVector.size(); i++) {
            if (tumbler == slotIconVector.get(i).getTumbler()) {
                // then replace that icon
                slotIconVector.remove(i);
                slotIconVector.add(i, slotIcon);
            }
        }
    }
    
    /**
     * Represents a vector of all the slot icons that this line is playing.
     */
    private Vector<SlotIcon> slotIconVector;

    /**
     * The tumbler display that this line is being played on.
     */
    private TumblerDisplay tumblerDisplay;
    
    /**
     * The visibility of this line to the display.
     */
    private boolean isVisible;
    
    /**
     * The color of this line on the display.
     */
    private Color color;
    
}