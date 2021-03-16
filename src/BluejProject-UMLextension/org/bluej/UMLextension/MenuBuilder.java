package org.bluej.UMLextension;

import bluej.extensions2.ExtensionException;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Menu;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;

import bluej.extensions2.BClass;
import bluej.extensions2.MenuGenerator;

import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.WindowEvent;

/**
 * Long time ago Ian Utting developed UMLExtension for BlueJ
 * https://www.bluej.org/extensions/UMLextension/UMLextension.jar
 * https://www.bluej.org/extensions/extensions.html
 * 
 * I (Milton Jesus Vera Contreras) decompiled JAR file 
 * and upgraded UML Extension to BlueJ5 using JavaFX. I add drag funtionality
 * and change click event for double click event to close window.
 * 
 * This class has a few changes. All swing code was changed to JavaFX
 */
class MenuBuilder extends MenuGenerator
{
    private BClass bc;

    public MenuItem getClassMenuItem(final BClass aClass) {
        final MenuItem jmi = new MenuItem("Display UML");

        jmi.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    Stage stage = new Stage();        
                    UMLDisplay uml = new UMLDisplay(bc, stage);
                    Scene scene = new Scene(uml);
                    stage.setScene(scene); 
                    stage.sizeToScene(); 
                    stage.setResizable(false);
                    stage.centerOnScreen();
                    stage.setTitle("UML " + bc.getClass().getName());
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.initModality(Modality.WINDOW_MODAL);
                    stage.show();
                }
            });
        return jmi;
    }

    public void notifyPostClassMenu(final BClass bc, final MenuItem jmi) {
        this.bc = bc;
    }
}
