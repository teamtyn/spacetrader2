/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License"). You
 * may not use this file except in compliance with the License. You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 * 
 */ 

package spacetrader;

import java.io.IOException;
import java.util.HashMap;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * @author TYN
 * @version 1.0
 */
public class ScreensController extends StackPane {

    // Holds the screens to be displayed
    private static final HashMap<String, Node> screens = new HashMap<>();
    private static final HashMap<String, ControlledScreen> controllers = new HashMap<>();
    private static final HashMap<String, Boolean> initialized = new HashMap<>();

    public ScreensController() {
        super();
    }

    // Adds the screen to the collection
    public void addScreen(String name, Node screen, ControlledScreen controller) {
        screens.put(name, screen);
        controllers.put(name, controller);
    }

    // Returns the Node with the appropriate name
    public static Node getScreen(String name) {
        return screens.get(name);
    }
    
    public static boolean isInitialized(String name) {
        return initialized.get(name) != null;
    }

    // Returns the Node with the appropriate name
    public static ControlledScreen getController(String name) {
        return controllers.get(name);
    }

    /**
     * Loads the FXML file, adds the screen to the screens collection and
     *   finally injects the screenPane to the controller
     * @param name The name of the screen being loaded
     * @param resource The FXML file for the screen to be loaded
     * @return Whether or not the screen was successfully loaded
    */
    public boolean loadScreen(String name, String resource) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
            Parent loadScreen = (Parent) loader.load();
            ControlledScreen controller = ((ControlledScreen) loader.getController());
            controller.setScreenParent(this);
            addScreen(name, loadScreen, controller);
            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println(e);
            e.printStackTrace();
            Thread.dumpStack();

            return false;
        }
    }

    /**
     * This method tries to display the screen with a predefined name
     *   First it makes sure the screen has been already loaded
     *   If there is more than one screen, the current screen
     *     is removed, and then the new screen is added
     *   If there isn't any screen being displayed, the new screen is just added
     * @param name The name of the screen to be displayed
     * @return Whether the screen was successfully added or not
    */
    public boolean setScreen(final String name) {
        Node screen = screens.get(name);
        ControlledScreen controller = controllers.get(name);
        if (screen != null) { // Screen is already loaded
            final DoubleProperty opacity = opacityProperty();
            if (!getChildren().isEmpty()) { // If there is more than one screen
                if (initialized.put(name, true) == null) {
                    controller.lazyInitialize();
                }
                Timeline fade = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1.0)),
                        new KeyFrame(new Duration(1000), (ActionEvent t) -> {
                            getChildren().remove(0); // Remove the displayed screen
                            getChildren().add(0, screen); // Add the screen
                            Timeline fadeIn = new Timeline(
                                    new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                                    new KeyFrame(new Duration(800), new KeyValue(opacity, 1.0)));
                            fadeIn.play();
                }, new KeyValue(opacity, 0.0)));
                fade.play();
            } else {
                setOpacity(0.0);
                controller.lazyInitialize();
                initialized.put(name, true);
                getChildren().add(screen); // No one else been displayed, then just show
                Timeline fadeIn = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                        new KeyFrame(new Duration(2500), new KeyValue(opacity, 1.0)));
                fadeIn.play();
            }
            return true;
        } else {
            //Thread.dumpStack();
            System.out.println("Screen has not been loaded.\n");
            
            return false;
        }
    }

    // This method will remove the screen with the given name from the collection of screens
    public boolean unloadScreen(String name) {
        if (screens.remove(name) == null) {
            System.out.println("Screen does not exist.\n");
            return false;
        } else {
            return true;
        }
    }
}