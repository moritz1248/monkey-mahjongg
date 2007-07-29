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
import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import jmetest.monkeymahjongg.Main;

/**
 *
 * @author Pirx
 */
public class MainMenuGameState extends JMEDesktopState {

    private Action settingsAction = new AbstractAction() {
        {
            putValue(NAME, "Settings");
            putValue(SHORT_DESCRIPTION, "Settings Button");
        }

        public void actionPerformed(ActionEvent e) {
            Main.selectSettingsMenu();
        }
    };

    private Action levelAction = new AbstractAction() {
        {
            putValue(NAME, "Level");
            putValue(SHORT_DESCRIPTION, "Level Button");
        }

        public void actionPerformed(ActionEvent e) {
            Main.selectLevelMenu();
        }
    };

    private Action startAction = new AbstractAction() {
        {
            putValue(NAME, "Start");
            putValue(SHORT_DESCRIPTION, "Start Button");
        }

        public void actionPerformed(ActionEvent e) {
            Main.startLevel();
        }
    };

    private Action exitAction = new AbstractAction() {
        {
            putValue(NAME, "Exit");
            putValue(SHORT_DESCRIPTION, "Exut Button");
        }

        public void actionPerformed(ActionEvent e) {
            Main.exit();
        }
    };
    
    private Action[] actions = new Action[]{
        settingsAction, levelAction, startAction, exitAction};


    public MainMenuGameState() {
        super(true);
        JDesktopPane jDesktop = getDesktop().getJDesktop();

        for (int i = 0; i < actions.length; i++) {
            JButton button = new JButton(actions[i]);
            button.setSize(100, 20);
            button.setLocation(50, 50 + 50*i);
            jDesktop.add(button);
        }

        jDesktop.invalidate();
        jDesktop.validate();
        jDesktop.repaint();
    }
}