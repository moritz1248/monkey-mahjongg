/*
 * MainMenuGameState.java
 *
 *  Copyright (c) 2007 Daniel Gronau
 *
 *  This file is part of Monkey Mahjongg.
 *
 *  Monkey Mahjongg is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Monkey Mahjongg is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 *
 */


package jmetest.monkeymahjongg.menu.swingui;

import com.jmex.awt.swingui.JMEDesktopState;

import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import jmetest.monkeymahjongg.Main;

/**
 *
 * @author Pirx
 */
public class MainMenuGameState extends JMEDesktopState {

    private Action settingsAction = new AbstractAction() {
		private static final long serialVersionUID = 1L;

		{
            putValue(NAME, "Settings");
            putValue(SHORT_DESCRIPTION, "Settings Button");
        }

        public void actionPerformed(ActionEvent e) {
            Main.selectSettingsMenu();
        }
    };

    private Action startAction = new AbstractAction() {
		private static final long serialVersionUID = 1L;
        {
            putValue(NAME, "Start");
            putValue(SHORT_DESCRIPTION, "Starts the game, using the selected layout");
        }

        public void actionPerformed(ActionEvent e) {
            Main.startLevel();
        }
    };

    private Action exitAction = new AbstractAction() {
		private static final long serialVersionUID = 1L;
        {
            putValue(NAME, "Exit");
            putValue(SHORT_DESCRIPTION, "Exits this game");
        }

        public void actionPerformed(ActionEvent e) {
            Main.exit();
        }
    };
    
    private Action[] actions = new Action[]{
        settingsAction, startAction, exitAction};


    public MainMenuGameState() {
        super(true);
        JDesktopPane jDesktop = getDesktop().getJDesktop();
        
        for (int i = 0; i < actions.length; i++) {
            JButton button = new JButton(actions[i]);
            button.setSize(100, 20);
            button.setLocation(50, 50 + 50*i);
            jDesktop.add(button);
        }

        JLabel label = new JLabel("Layout:");
        label.setSize(100, 15);
        label.setLocation(50, 280);
        label.setForeground(Color.white);
        jDesktop.add(label);
        final JList layoutList = new JList(getLayouts());
        layoutList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        layoutList.addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent e) {
                Main.setLayoutName(layoutList.getSelectedValue().toString().toLowerCase());
            }
        });
        layoutList.setSelectedIndex(0);
        JScrollPane scrollPane = new JScrollPane(layoutList);
        scrollPane.setSize(100,120);
        scrollPane.setLocation(50, 300);
        jDesktop.add(scrollPane);
        
        jDesktop.invalidate();
        jDesktop.validate();
        jDesktop.repaint();
    }
    
    private String[] getLayouts() {
        return new String[] 
           {"Standard", "Block", "Butterfly", "Castle", "Stairs", "Towers"};
    }
}