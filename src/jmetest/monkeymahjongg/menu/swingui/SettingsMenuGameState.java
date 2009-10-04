/*
 * SettingsMenuGameState.java
 * 
 * Created on Aug 5, 2007, 10:38:06 AM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jmetest.monkeymahjongg.menu.swingui;

import com.jme.system.DisplaySystem;
import com.jme.system.GameSettings;
import com.jme.util.GameTaskQueueManager;
import com.jmex.awt.swingui.JMEDesktop;
import com.jmex.awt.swingui.JMEDesktopState;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.Callable;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import jmetest.monkeymahjongg.Main;

/**
 *
 * @author DGronau
 */
public class SettingsMenuGameState  extends JMEDesktopState {
    
    static final String[] FREQUENCIES = 
            {"60", "70", "72", "75", "85", "100", "120", "140"};
    static final String[] RESOLUTIONS = {"640x480", "800x600", 
            "1024x768", "1280x1024", "1600x1200", "1440x900"};
    static final int[] WIDTH = {640, 800, 1024, 1280, 1600, 1440};
    static final int[] HEIGHTS = {480, 600, 768, 1024, 1200, 900};
    static final String[] COLORS = {"16", "24" , "32"};

    private final GameSettings gameSettings;
    private final JComboBox resolutionBox = new JComboBox();
    private final JComboBox colorBox = new JComboBox();
    private final JComboBox frequencyBox = new JComboBox();
    private final JCheckBox fullscreenBox = new JCheckBox();
    
    public SettingsMenuGameState(GameSettings gameSettings) {
        super(gameSettings.getWidth(), gameSettings.getHeight());
        this.gameSettings = gameSettings;
        GameTaskQueueManager.getManager().update(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                initGraphics();
                return null;
            }
        });
    }

    public void initGraphics() {
        JDesktopPane jDesktop = getDesktop().getJDesktop();

        setJLabel("Resolution", new Point(50, 50));
        setJComboBox(resolutionBox, RESOLUTIONS, new Point(200, 50));

        setJLabel("Color Bits", new Point(50, 100));
        setJComboBox(colorBox, COLORS, new Point(200, 100));

        setJLabel("Frequency", new Point(50, 150));
        setJComboBox(frequencyBox, FREQUENCIES, new Point(200, 150));

        setJLabel("Fullscreen", new Point(50, 200));
        jDesktop.add(fullscreenBox);
        fullscreenBox.setSize(20,20);
        fullscreenBox.setLocation(200, 200);
        
        final JButton okButton = new JButton("OK");
        jDesktop.add(okButton);
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                save();
            }
        });
        okButton.setSize(100,20);
        okButton.setLocation(50, 250);

        final JButton cancelButton = new JButton("Cancel");
        jDesktop.add(cancelButton);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Main.selectMainMenu();
            }
        });
        cancelButton.setSize(100,20);
        cancelButton.setLocation(50, 300);
        
        jDesktop.invalidate();
        jDesktop.validate();
        jDesktop.repaint();
    }
    
    @Override
    public void setActive(boolean active) {
        super.setActive(active);
        if (active) {
            reInit();
        }
    }

    public void reInit() {
        reInitJComboBox(resolutionBox, "" + gameSettings.getWidth() + "x" + gameSettings.getHeight());
        reInitJComboBox(colorBox, "" + gameSettings.getDepth());
        reInitJComboBox(frequencyBox, "" + gameSettings.getFrequency());
        fullscreenBox.setSelected(gameSettings.isFullscreen());
    }

    private void reInitJComboBox(JComboBox box, String value) {
        box.setSelectedItem(value);
        box.invalidate();
        box.validate();
        box.repaint();
    }

    private void save() {
        //final JDesktopPane jDesktop = getDesktop().getJDesktop();
        int width = WIDTH[resolutionBox.getSelectedIndex()];
        int height = HEIGHTS[resolutionBox.getSelectedIndex()];
        int depth = Integer.parseInt(COLORS[colorBox.getSelectedIndex()]);
        int freq = Integer.parseInt(FREQUENCIES[frequencyBox.getSelectedIndex()]);
        if (!DisplaySystem.getDisplaySystem().isValidDisplayMode(width, height, depth, freq)) {
            showMessageDialog("Vide Error", "The selected mode is not supported",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            gameSettings.setWidth(width);
            gameSettings.setHeight(height);
            gameSettings.setDepth(depth);
            gameSettings.setFrequency(freq);
            gameSettings.setFullscreen(fullscreenBox.isSelected());

            Main.savePreferences();
            Main.changeResolution();
            Main.selectMainMenu();
        }
    }
    
     private void setJLabel(String text, Point pos) {
        JDesktopPane jDesktop = getDesktop().getJDesktop();
        JLabel label = new JLabel(text);
        label.setForeground(Color.black);
        jDesktop.add(label);
        label.setSize(100,20);
        label.setLocation(pos);
    }
     
    private void setJComboBox(JComboBox box, String[] values, Point pos) {
        JDesktopPane jDesktop = getDesktop().getJDesktop();
        jDesktop.add(box);
        if (values != null) {
           for (String value : values) {
               box.addItem(value);
           }
        }
        box.setSize(100, 20);
        box.setLocation(pos);
        box.invalidate();
        box.validate();
        box.repaint();
    }
    
    private void showMessageDialog(String title, String message, int messageType) {
        final JMEDesktop desktop = getDesktop();
        final JInternalFrame modalDialog = new JInternalFrame(title);
        final JOptionPane optionPane = new JOptionPane(message, messageType);
        modalDialog.getContentPane().add(optionPane);
        desktop.setModalComponent(modalDialog);
        desktop.getJDesktop().add(modalDialog, 0);
        modalDialog.setVisible(true);
        modalDialog.setSize(modalDialog.getPreferredSize());
        modalDialog.setLocation(200,200);
        desktop.setFocusOwner(optionPane);

        optionPane.addPropertyChangeListener(JOptionPane.VALUE_PROPERTY, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                modalDialog.setVisible(false);
                desktop.setModalComponent(null);
                desktop.getJDesktop().remove(modalDialog);
            }
        });
    }
}
