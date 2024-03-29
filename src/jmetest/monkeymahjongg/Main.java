/*
 * Main.java
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
package jmetest.monkeymahjongg;

import com.jme.input.MouseInput;
import com.jme.renderer.ColorRGBA;
import com.jme.system.GameSettings;
import com.jme.system.PreferencesGameSettings;
import com.jme.util.GameTaskQueueManager;
import com.jmex.game.StandardGame;
import com.jmex.game.state.GameState;
import java.util.prefs.Preferences;
import jmetest.monkeymahjongg.menu.BackgroundGameState;
import com.jmex.game.state.GameStateManager;
import java.lang.reflect.Constructor;
import java.util.concurrent.Callable;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import jmetest.monkeymahjongg.game.CameraGameState;
import jmetest.monkeymahjongg.game.Level;
import jmetest.monkeymahjongg.game.LevelProvider;
import jmetest.monkeymahjongg.game.MahjonggGameState;
import static java.util.logging.Level.*; //avoids name clash

/**
 *
 * @author Pirx
 */
public class Main {

    private static Preferences preferences;
    private static GameSettings gameSettings;
    private static StandardGame standardGame;
    private static GameState backgroundGameState;
    private static GameState mainMenuGameState;
    private static GameState settingsMenuGameState;
    private static MahjonggGameState mahjonggGameState;
    private static CameraGameState cameraGameState;
    private static String menuPackage;
    private static String layoutName = "standard";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        menuPackage = (args.length == 0) ? "swingui" : args[0];

        preferences = Preferences.userNodeForPackage(Main.class);

        gameSettings = new PreferencesGameSettings(preferences);

        standardGame = new StandardGame("Monkey Mahjongg", StandardGame.GameType.GRAPHICAL, gameSettings);
        standardGame.setBackgroundColor(ColorRGBA.darkGray);
        standardGame.start();

        backgroundGameState = new BackgroundGameState("jmetest/monkeymahjongg/images/Monkey.jpg");
        GameStateManager.getInstance().attachChild(backgroundGameState);
        backgroundGameState.setActive(true);

        cameraGameState = new CameraGameState();
        GameStateManager.getInstance().attachChild(cameraGameState);
        cameraGameState.setActive(true);
        cameraGameState.setFixed();

        mahjonggGameState = new MahjonggGameState();
        GameStateManager.getInstance().attachChild(mahjonggGameState);

        try {
            mainMenuGameState = (GameState) Class.forName(
                    "jmetest.monkeymahjongg.menu." + menuPackage + ".MainMenuGameState").newInstance();
            GameStateManager.getInstance().attachChild(mainMenuGameState);
            mainMenuGameState.setActive(true);

            Constructor settingsConstructor = Class.forName(
                    "jmetest.monkeymahjongg.menu." + menuPackage + ".SettingsMenuGameState").getConstructor(GameSettings.class);
            settingsMenuGameState = (GameState) settingsConstructor.newInstance(gameSettings);
            GameStateManager.getInstance().attachChild(settingsMenuGameState);

        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }

        setCursorVisible(true);
    }

    private static void setCursorVisible(final boolean visible) {
        GameTaskQueueManager.getManager().update(new Callable<Object>() {
            @Override  
            public Object call() throws Exception {
                MouseInput.get().setCursorVisible(visible);
                return null;
            }
        });
    }

    public static void selectSettingsMenu() {
        mainMenuGameState.setActive(false);
        settingsMenuGameState.setActive(true);
    }

    public static void selectMainMenu() {
        settingsMenuGameState.setActive(false);
        backgroundGameState.setActive(true);
        mainMenuGameState.setActive(true);
    }

    public static void startLevel() {
        mainMenuGameState.setActive(false);
        backgroundGameState.setActive(false);
        mahjonggGameState.setActive(true);
        cameraGameState.setMoveable();
    }

    public static void stopLevel() {
        mainMenuGameState.setActive(true);
        backgroundGameState.setActive(true);
        mahjonggGameState.setActive(false);
        cameraGameState.setFixed();
    }

    public static void setLayoutName(String layoutName) {
        Main.layoutName = layoutName;
    }

    public static Level getLevel() {
        return LevelProvider.load("level/" + layoutName + ".xml");
    }

    public static void exit() {
        standardGame.shutdown();
    }

    public static void savePreferences() {
        try {
            preferences.flush();
        } catch (BackingStoreException ex) {
            Logger.getLogger(Main.class.getName()).log(SEVERE, null, ex);
        }
    }

    public static void changeResolution() {
        standardGame.recreateGraphicalContext();
    }

}
