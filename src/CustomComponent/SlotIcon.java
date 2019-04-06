/*
 * The Dream Machine
 */

package CustomComponent;

import Global.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.Graphics.*;
import java.util.*;

/**
 * This class will represent an icon on the face of the
 * spinning wheels of the slot machine.
 *
 * @author chase isabelle
 */
public class SlotIcon 
extends JLayeredPane 
implements MouseListener, ComponentListener {
    
    /**
     * default constructor
     */
    public SlotIcon() {
        // add mouse listener
        addMouseListener(this);
        
        // add the component listener for this component
        addComponentListener(this);
        
        // instantiate the graphic
        jLabel = new JLabel();
        add(jLabel);
        jLabel.addMouseListener(this);
        
        // instantiate the position
        position = new Position(getLocation().x, getLocation().y);
        
        // set the opacity
        setOpaque(false);
        jLabel.setOpaque(false);
        
        // set the icon code for this component
        setRandomCurrentIcon();
        
        // init the popup menu for this slot icon
        initPopupMenu();
        
        // show me
        setVisible(true);
    }
    
    /**
     * This method will initialzie the slot icon's popup menu.
     */
    public void initPopupMenu() {
        // instantiate the popupmenu
        jPopupMenu = new JPopupMenu();
        
        // for purposes of construction
        if (getTumbler() == null || getTumblerDisplay() == null) { 
            return;
        }

        // locking/unlocking the tumbler option
        String menuItemText = "Lock Tumbler";
        if (getTumbler().isLocked()) {
            menuItemText = "Unlock Tumbler";
        }
        JMenuItem lockTumblerJMenuItem = new JMenuItem(menuItemText);
        lockTumblerJMenuItem.addActionListener(new ActionListener() {
            // called when the user clicks the lock tumbler jmenu item
            public void actionPerformed(ActionEvent event) {
                // if the tumbler is locked then unlcok it, and vice0versa
                if (getTumbler().isLocked()) {
                    getTumbler().unlock();
                } else {
                    getTumbler().lock();
                }
            }
        });
        jPopupMenu.add(lockTumblerJMenuItem);

        // seperate next option
        jPopupMenu.add(new JSeparator());

        // showing the play table for this icon
        JMenuItem showPayOutJMenuItem = new JMenuItem("Show Pay Out");
        showPayOutJMenuItem.addActionListener(new ActionListener() {
            // called when the user clicks
            public void actionPerformed(ActionEvent e) {
                // show the play table for this slo icon
                showPayOut();
            }
        });
        jPopupMenu.add(showPayOutJMenuItem);
        
        // seperate next options
        jPopupMenu.add(new JSeparator());
        
        // allow the user to select a line to edit
        JMenu editLineJMenu = new JMenu("Edit Line");
        jPopupMenu.add(editLineJMenu);
        // get the bet lines
        Vector<BetLine> betLineVector = getTumblerDisplay().getBetLineVector();
        // let them add a new custom line
        JMenuItem newBetLineJMenuItem = new JMenuItem("New Line");
        newBetLineJMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getTumblerDisplay().addCustomLine();
            }
        });
        editLineJMenu.add(newBetLineJMenuItem);
        editLineJMenu.add(new JSeparator());
        // add menu item for each btline
        for (int i = 0; i < betLineVector.size(); i++) {
            final BetLine betLine = betLineVector.get(i);
            JMenuItem betLineJMenuItem = new JMenuItem("Line #" + i);
            betLineJMenuItem.setBackground(betLine.getColor());
            betLineJMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    getTumblerDisplay().editBetLine(betLine);
                }
            });
            editLineJMenu.add(betLineJMenuItem);
        }
        
        // allow the user to remove bet lines
        JMenu removeLineJMenu = new JMenu("Remove Line");
        jPopupMenu.add(removeLineJMenu);
        // add menu utem for each bet line
        for (int i = 0; i < betLineVector.size(); i++) {
            final BetLine betLine = betLineVector.get(i);
            JMenuItem betLineJMenuItem = new JMenuItem("Line #" + i);
            betLineJMenuItem.setBackground(betLine.getColor());
            betLineJMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    getTumblerDisplay().getBetLineVector().remove(betLine);
                }
            });
            removeLineJMenu.add(betLineJMenuItem);
        }
        
        // allow the user to show and hide lines
        JMenu showLinesJMenu = new JMenu("Show Line");
        jPopupMenu.add(showLinesJMenu);
        // allow user to veiw all
        JMenuItem showAllLinesJMenuItem = new JMenuItem("Show All");
        showAllLinesJMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getTumblerDisplay().showBetLines();
            }
        });
        showLinesJMenu.add(showAllLinesJMenuItem);
        showLinesJMenu.add(new JSeparator());
        // allow the user to veiw individual lines
        for (int i = 0; i < betLineVector.size(); i++) {
            final BetLine betLine = betLineVector.get(i);
            JMenuItem betLineJMenuItem = new JMenuItem("Line #" + i);
            betLineJMenuItem.setBackground(betLine.getColor());
            betLineJMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    getTumblerDisplay().showBetLine(betLine);
                }
            });
            showLinesJMenu.add(betLineJMenuItem);
        }
        
        // allow user to hide bet lines
        JMenuItem hideLinesJMenuItem = new JMenuItem("Hide Lines");
        hideLinesJMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getTumblerDisplay().hideBetLines();
            }
        });
        jPopupMenu.add(hideLinesJMenuItem);
        
        // seperate
        jPopupMenu.add(new JSeparator());
        
        // add a basic cancle option
        JMenuItem cancelJMenuItem = new JMenuItem("Cancel");
        cancelJMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jPopupMenu.setVisible(false);
            }
        });
        jPopupMenu.add(cancelJMenuItem);
    }
    
    /**
     * If this slot icon is part of a tumbler, then this method will
     * return that tumbler.
     * 
     * @return the tumbler this icon belongs to.
     */
    public Tumbler getTumbler() {
        return (Tumbler)getParent();
    }
    
    /**
     * If this slot icons belongs to a tumbler which belongs to a
     * tumbler display, this methdo will return that tumbler display.
     * 
     * @return the tumbler display which this slot icon belongs to.
     */
    public TumblerDisplay getTumblerDisplay() {
        return getTumbler().getTumblerDisplay();
    }
    
    /**
     * This method is used to get the current image code for this
     * SlotIcon.
     * 
     * @return the current code for this SlotIcon.
     */
    public int getCurrentIcon() {
        return currentIconCode;
    }
    
    /**
     * This method is used to set the curret icon code for this SlotIcon.
     * 
     * @param iconCode is the new icon code for this SlotIcon.
     */
    public void setCurrentIcon(int iconCode) {
        // set the curerent code, image path, and icon tool-tiptext
        ImageIcon imageIcon = null;
        String toolTip = "";
        switch (iconCode) {
            case HEINES:
                imageIcon = new ImageIcon(getClass().getResource(heinesImagePath));
                toolTip = "You're not gonna beat me!";
                currentIconCode = iconCode;
                break;
            case CANNING:
                imageIcon = new ImageIcon(getClass().getResource(canningImagePath));
                toolTip = "What in the frog's goin on here?";
                currentIconCode = iconCode;
                break;
            case ELBIRT:
                imageIcon = new ImageIcon(getClass().getResource(elbirtImagePath));
                toolTip = "This score is dog slow...";
                currentIconCode = iconCode;
                break;
            case HOLLY:
                imageIcon = new ImageIcon(getClass().getResource(hollyImagePath));
                toolTip = "Dream Machine?  More like Scheme Machine!";
                currentIconCode = iconCode;
                break;
            case TRAN:
                imageIcon = new ImageIcon(getClass().getResource(tranImagePath));
                toolTip = "It so easy!  Why you lose so many points?";
                currentIconCode = iconCode;
                break;
            case LIVINGSTON:
                imageIcon = new ImageIcon(getClass().getResource(livingstonImagePath));
                toolTip = "I AM LIVINGSTON!";
                currentIconCode = iconCode;
                break;
            case MOLONEY:
                imageIcon = new ImageIcon(getClass().getResource(moloneyImagePath));
                toolTip = "Less bologna, more Moloney!";
                currentIconCode = iconCode;
                break;
            case DANIELS:
                imageIcon = new ImageIcon(getClass().getResource(danielsImagePath));
                toolTip = "You'll be winning at order O(1)!!!";
                currentIconCode = iconCode;
                break;
            case WANG:
                imageIcon = new ImageIcon(getClass().getResource(wangImagePath));
                toolTip = "Can we concatenate a zero onto that score?";
                currentIconCode = iconCode;
                break;
            case MARTY:
                imageIcon = new ImageIcon(getClass().getResource(martyImagePath));
                toolTip = "We need funding!";
                currentIconCode = iconCode;
                break;
            default:
                imageIcon = new ImageIcon(getClass().getResource(defaultImagePath));
                toolTip = "";
                currentIconCode = DEFAULT;
        }
        
        // scale the image
        if (getWidth() != 0 && getHeight() != 0) {
            imageIcon = Utilities.resizeImageIcon(jLabel.getWidth(),
                    jLabel.getHeight(),
                    imageIcon);
        }
        
        // set the graphic of the jLabel
        jLabel.setIcon(imageIcon);
        jLabel.setAlignmentY(CENTER_ALIGNMENT);
        
        // set the tool tip text
        jLabel.setToolTipText(toolTip);
    }
    
    /**
     * Checks to see if this slot icon is identical to another.
     * 
     * @param otherSlotIcon is the other slot icon.
     * @return true is they are identical, flase if not.
     */
    public boolean isSame(SlotIcon otherSlotIcon) {
        return getCurrentIcon() == otherSlotIcon.getCurrentIcon();
    }
    
    /**
     * Gets the winning amoutn for this icon.
     * 
     * @return the winning amoutn of this icon.
     */
    public int getWinAmount() {
        switch (getCurrentIcon()) {
            case HEINES:
                return HEINES_WIN;
            case CANNING:
                return CANNING_WIN;
            case ELBIRT:
                return ELBIRT_WIN;
            case HOLLY:
                return HOLLY_WIN;
            case TRAN:
                return TRAN_WIN;
            case LIVINGSTON:
                return LIVINGSTON_WIN;
            case MOLONEY:
                return MOLONEY_WIN;
            case DANIELS:
                return DANIELS_WIN;
            case WANG:
                return WANG_WIN;
            case MARTY:
                return MARTY_WIN;
            default:
                return DEFAULT_WIN;
        }
    }
    
    /**
     * This method will be used to set a random icon to this
     * SlotIcon.
     */
    public void setRandomCurrentIcon() {
        setCurrentIcon(Utilities.randomInt(1, CODE_RANGE));
    }
    
    /**
     * Sets the exact position of the slot icon on a tumbler.
     * 
     * <p>
     * NOTE:  This method is only to be used by the tumbler's spin method!
     * No other object's should use this method!
     * </p>
     * 
     * @param x is the new x position
     * @param y is the y position
     */
    public void setPosition(double x, double y) {
        setLocation((int)Math.round(x), (int)Math.round(y));
        position.x = x;
        position.y = y;
    }
    
    /**
     * Gets the exact position of the slot icon on a tumbler.
     * 
     * <p>
     * NOTE:  This method is only to be used by the tumbler's spin method!
     * No other object's should use this method!
     * </p>
     * 
     * @return the x and y position of this icon as a point object.
     */
    public Position getPosition() {
        return position;
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
     * Shows the pay out for this icon.
     */
    public void showPayOut() {
        // init the pay out info
        String title = jLabel.getToolTipText();
        String message = "<html>"+
                "<center><b>" + jLabel.getToolTipText() + "</b></center><br>" +
                "(Amount That You Bet) x (Number of Icons) x (" + getWinAmount() + ")<br>" + 
                "</html>";
        
        // show the pay out in a dialog box
        JOptionPane.showMessageDialog(null, 
                message,
                title,
                JOptionPane.PLAIN_MESSAGE,
                jLabel.getIcon());
    }

    /**
     * This method is called when the user clicks the mouse "Click!"
     * 
     * @param event is the event that triggered this action.
     */
    public void mouseClicked(MouseEvent event) {
        // init a few varibales
        BetLine betLine = getTumblerDisplay().getBetLineBeingEdited();
        
        // get the button clicked
        switch(event.getButton()) {
            case 1:  // the left click
                // check to see if the user is currently editing a bet line
                // on the display
                if (betLine == null) {
                    // if not then show the pay out for this icon
                    showPayOut();
                } else {
                    // if so then set the bet line's icon
                    betLine.setSlotIcon(getTumbler(), this);
                    
                    // and repaint the line to the screen
                    getTumblerDisplay().showBetLine(betLine);
                }
                
                // exit switch
                break;
            case 2: // the middle click
                // do nothing
                break;
            case 3:  // the right click
                // check to see if the user is editing a bet line
                if (betLine == null) {
                    // init the popup menu
                    initPopupMenu();
                    
                    // if not then show the popup menu where the mouse is
                    jPopupMenu.show(this, event.getX(), event.getY());
                } else {
                    // otherwise the user has finished editing their line
                    getTumblerDisplay().editBetLine(null);
                }
                
                // exit switch
                break;
            default:
                // empty body
        }
    }

    /**
     * This method is called when a mouse button is pressed down on this
     * component.
     * 
     * @param event is the event that triggered this action.
     */
    public void mousePressed(MouseEvent event) {
        // empty body
    }

    /**
     * This method is called when the mouse button is released from a 
     * pressing.
     * 
     * @param event is the event that triggered this action
     */
    public void mouseReleased(MouseEvent event) {
        // empty body
    }

    /**
     * This method is called when the mouse's location on the screen is in
     * the vicinity of this component on the screen.
     * 
     * @param event is the event that triggered this action
     */
    public void mouseEntered(MouseEvent event) {
        // empty body
    }

    /**
     * This method is called when the mouse's location exits the vicinity of
     * this components on the screen.
     * 
     * @param event is the event that triggered this action
     */
    public void mouseExited(MouseEvent event) {
        // empty body
    }
    
    /**
     * This method is called when the mouse is dragged over this component.
     * 
     * @param event is the event that triggers this method
     */
    public void mouseDragged(MouseEvent e) {
        // empty body
    }

    /**
     * This method is called when the component is resized.
     * 
     * @param event is the event that triggered this action.
     */
    public void componentResized(ComponentEvent event) {
        // set the jlabel's location
        jLabel.setLocation(0, 0);
        
        // resize the jlabel
        Dimension dimension = new Dimension(getWidth(), getHeight());
        jLabel.setPreferredSize(dimension);
        jLabel.setSize(dimension);
       
        // resize the image graphic
        setCurrentIcon(getCurrentIcon());
    }

    /**
     * This method is called when the component is moved.
     * 
     * @param event is the event that triggered htis action.
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
        // resize the component
        componentResized(new ComponentEvent(this, ComponentEvent.COMPONENT_RESIZED));
    }

    /**
     * Called when the component is hidden.
     * 
     * @param event is the event that triggered this action.
     */
    public void componentHidden(ComponentEvent e) {
        // empty body
    }
    
    /**
     * Represents the image file's paths.
     */
    private final static String defaultImagePath = "/Images/default.gif";
    private final static String heinesImagePath = "/Images/heines.gif";
    private final static String canningImagePath = "/Images/canning.gif";
    private final static String elbirtImagePath = "/Images/elbirt.gif";
    private final static String hollyImagePath = "/Images/holly.gif";
    private final static String tranImagePath = "/Images/tran.gif";
    private final static String livingstonImagePath = "/Images/livingston.gif";
    private final static String moloneyImagePath = "/Images/moloney.gif";
    private final static String danielsImagePath = "/Images/daniels.gif";
    private final static String wangImagePath = "/Images/wang.gif";
    private final static String martyImagePath = "/Images/marty.gif";
    
    /**
     * These ints are used as codes for the image files.
     */
    private int currentIconCode;
    public final static int DEFAULT = 0;
    public final static int HEINES = 1;
    public final static int CANNING = 2;
    public final static int ELBIRT = 3;
    public final static int HOLLY = 4;
    public final static int TRAN = 5;
    public final static int LIVINGSTON = 6;
    public final static int MOLONEY = 7;
    public final static int DANIELS = 8;
    public final static int WANG = 9;
    public final static int MARTY = 10;
    public final static int CODE_RANGE = 10;
    
    /**
     * The fallowing ints are used to determine what each icon is worth
     */
    private final static int DEFAULT_WIN = 0;
    private final static int HEINES_WIN = 100;
    private final static int CANNING_WIN = 50;
    private final static int ELBIRT_WIN = 5;
    private final static int HOLLY_WIN = 10;
    private final static int TRAN_WIN = 5;
    private final static int LIVINGSTON_WIN = 10;
    private final static int MOLONEY_WIN = 25;
    private final static int DANIELS_WIN = 15;
    private final static int WANG_WIN = 10;
    private final static int MARTY_WIN = 75;
    
    /**
     * The graphic will be drawn via a JLabel's icon
     */
    private JLabel jLabel;
    
    /**
     * The position attribute of the slot icon.
     * 
     * <p>
     * NOTE:  These variables are only used by the set and get position
     * methods for this class.  See setPosition and getPosition documentation
     * for more info.
     * </p>
     */
    private Position position;
    
    /**
     * The pop-up menu for this slot icon.
     */
    private JPopupMenu jPopupMenu;
    
}
