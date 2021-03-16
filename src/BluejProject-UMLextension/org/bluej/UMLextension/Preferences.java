package org.bluej.UMLextension;

import bluej.extensions2.BlueJ;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToggleGroup;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import bluej.extensions2.PreferenceGenerator;
import javafx.scene.control.Separator;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;

/**
 * Long time ago Ian Utting developed UMLExtension for BlueJ
 * https://www.bluej.org/extensions/UMLextension/UMLextension.jar
 * https://www.bluej.org/extensions/extensions.html
 * 
 * I (Milton Jesus Vera Contreras) decompiled JAR file 
 * and upgraded UML Extension to BlueJ5 using JavaFX. I add drag funtionality
 * and change click event for double click event to close window.
 * 
 * This class has several changes. All swing code was changed to JavaFX
 */
class Preferences implements PreferenceGenerator
{
    public static boolean showParams;
    public static boolean showReturnTypes;
    public static boolean showAttributes;
    public static boolean showMethods;
    public static boolean showPrivates;
    public static boolean showVisibility;
    public static boolean useJavaSyntax;
    public static String showParamsString;
    public static String showReturnTypesString;
    public static String showAttributesString;
    public static String showMethodsString;
    public static String showPrivatesString;
    public static String showVisibilityString;
    public static String useJavaSyntaxString;
    private CheckBox paramBox;
    private CheckBox returnBox;
    private CheckBox attributesBox;
    private CheckBox methodBox;
    private CheckBox privatesBox;
    private CheckBox visibilityBox;
    private CheckBox useJavaSyntaxBox;
    private CheckBox useUMLSyntaxBox;
    private VBox myPanel;
    
    
    public Preferences() {
        this.paramBox = new CheckBox("Show Parameters");
        this.paramBox.setSelected(Preferences.showParams);
        this.paramBox.setAllowIndeterminate(false);
        
        this.returnBox = new CheckBox("Show Return Types");
        this.returnBox.setSelected(Preferences.showReturnTypes);
        this.returnBox.setAllowIndeterminate(false);
        
        this.attributesBox = new CheckBox("Show Attributes");
        this.attributesBox.setSelected(Preferences.showAttributes);
        this.attributesBox.setAllowIndeterminate(false);
        
        this.methodBox = new CheckBox("Show Methods");
        this.methodBox.setSelected(Preferences.showMethods);
        this.methodBox.setAllowIndeterminate(false);
        
        this.privatesBox = new CheckBox("Show Privates");
        this.privatesBox.setSelected(Preferences.showPrivates);
        this.privatesBox.setAllowIndeterminate(false);
        
        this.visibilityBox = new CheckBox("Show Visibility");
        this.visibilityBox.setSelected(Preferences.showVisibility);
        this.visibilityBox.setAllowIndeterminate(false);
        
        this.useJavaSyntaxBox = new CheckBox("Java");
        this.useJavaSyntaxBox.setSelected(Preferences.useJavaSyntax);
        this.useJavaSyntaxBox.setAllowIndeterminate(false);
        
        this.useUMLSyntaxBox = new CheckBox("UML");
        this.useUMLSyntaxBox.setSelected(!Preferences.useJavaSyntax);
        this.useUMLSyntaxBox.setAllowIndeterminate(false);
        
        this.myPanel = new VBox();
        final HBox b1 = new HBox();
        final VBox b2 = new VBox();
        b2.getChildren().add(this.methodBox);
        b2.getChildren().add(this.attributesBox);
        final VBox b3 = new VBox();
        b3.getChildren().add(this.paramBox);
        b3.getChildren().add(this.visibilityBox);
        final VBox b4 = new VBox();
        b4.getChildren().add(this.returnBox);
        b4.getChildren().add(this.privatesBox);
        
        final Separator sep1 = new Separator();
        sep1.setOrientation(Orientation.VERTICAL);
        final Separator sep2 = new Separator();
        sep2.setOrientation(Orientation.VERTICAL);
        final Separator sep3 = new Separator();
        sep3.setOrientation(Orientation.VERTICAL);
        final Separator sep4 = new Separator();
        sep4.setOrientation(Orientation.VERTICAL);
        
        b1.getChildren().add(b2);
        b1.getChildren().add(sep1);

        b1.getChildren().add(b3);
        b1.getChildren().add(sep2);
        
        b1.getChildren().add(b4);
        b1.getChildren().add(sep3);
        
        //pendiente exclusivos this.useJavaSyntaxBox y this.useUMLSyntaxBox
        this.useJavaSyntaxBox.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
               useUMLSyntaxBox.setSelected(!useJavaSyntaxBox.isSelected());
            }
        });
        
        this.useUMLSyntaxBox.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
               useJavaSyntaxBox.setSelected(!useUMLSyntaxBox.isSelected());
            }
        });
        
        final VBox sb1 = new VBox();
        final HBox sb2 = new HBox();
        sb1.getChildren().add(new Label("Syntax:"));
        sb2.getChildren().add(this.useJavaSyntaxBox);
        sb2.getChildren().add(this.useUMLSyntaxBox);
        sb1.getChildren().add(sb2);
        sb1.setAlignment(Pos.CENTER);
        sb2.setAlignment(Pos.CENTER);
        
        b1.getChildren().add(sb1);

        this.myPanel.getChildren().add(b1);
        this.myPanel.setAlignment(Pos.CENTER);
        
        this.loadValues();
        
        this.methodBox.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
               actionPerformed();
            }
        });
    }
    
    public Pane getWindow()
    {
        return myPanel;
    }
    
    public void saveValues() {
        final BlueJ bj = UMLExtension.bluej;
        Preferences.showParams = this.paramBox.isSelected();
        bj.setExtensionPropertyString(Preferences.showParamsString, this.btos(Preferences.showParams));
        Preferences.showReturnTypes = this.returnBox.isSelected();
        bj.setExtensionPropertyString(Preferences.showReturnTypesString, this.btos(Preferences.showReturnTypes));
        Preferences.showAttributes = this.attributesBox.isSelected();
        bj.setExtensionPropertyString(Preferences.showAttributesString, this.btos(Preferences.showAttributes));
        Preferences.showMethods = this.methodBox.isSelected();
        bj.setExtensionPropertyString(Preferences.showMethodsString, this.btos(Preferences.showMethods));
        Preferences.showPrivates = this.privatesBox.isSelected();
        bj.setExtensionPropertyString(Preferences.showPrivatesString, this.btos(Preferences.showPrivates));
        Preferences.showVisibility = this.visibilityBox.isSelected();
        bj.setExtensionPropertyString(Preferences.showVisibilityString, this.btos(Preferences.showVisibility));
        Preferences.useJavaSyntax = this.useJavaSyntaxBox.isSelected();
        bj.setExtensionPropertyString(Preferences.useJavaSyntaxString, this.btos(Preferences.useJavaSyntax));
    }
    
    public void loadValues() {
        final BlueJ bj = UMLExtension.bluej;
        Preferences.showParams = this.stob(bj.getExtensionPropertyString(Preferences.showParamsString, this.btos(Preferences.showParams)));
        this.paramBox.setSelected(Preferences.showParams);
        Preferences.showReturnTypes = this.stob(bj.getExtensionPropertyString(Preferences.showReturnTypesString, this.btos(Preferences.showReturnTypes)));
        this.returnBox.setSelected(Preferences.showReturnTypes);
        Preferences.showAttributes = this.stob(bj.getExtensionPropertyString(Preferences.showAttributesString, this.btos(Preferences.showAttributes)));
        this.attributesBox.setSelected(Preferences.showAttributes);
        Preferences.showMethods = this.stob(bj.getExtensionPropertyString(Preferences.showMethodsString, this.btos(Preferences.showMethods)));
        this.methodBox.setSelected(Preferences.showMethods);
        Preferences.showPrivates = this.stob(bj.getExtensionPropertyString(Preferences.showPrivatesString, this.btos(Preferences.showPrivates)));
        this.privatesBox.setSelected(Preferences.showPrivates);
        Preferences.showVisibility = this.stob(bj.getExtensionPropertyString(Preferences.showVisibilityString, this.btos(Preferences.showVisibility)));
        this.visibilityBox.setSelected(Preferences.showVisibility);
        Preferences.useJavaSyntax = this.stob(bj.getExtensionPropertyString(Preferences.useJavaSyntaxString, this.btos(Preferences.useJavaSyntax)));
        this.useJavaSyntaxBox.setSelected(Preferences.useJavaSyntax);
        this.useUMLSyntaxBox.setSelected(!this.useJavaSyntaxBox.isSelected());
        this.paramBox.setSelected(this.methodBox.isSelected());
        this.returnBox.setSelected(this.methodBox.isSelected());
    }
    
    public void actionPerformed() {
        this.paramBox.setSelected(this.methodBox.isSelected());
        this.returnBox.setSelected(this.methodBox.isSelected());
    }
    
    private boolean stob(final String s) {
        return new Boolean(s);
    }
    
    private String btos(final boolean b) {
        return Boolean.toString(b);
    }
    
    static {
        Preferences.showParams = true;
        Preferences.showReturnTypes = true;
        Preferences.showAttributes = true;
        Preferences.showMethods = true;
        Preferences.showPrivates = false;
        Preferences.showVisibility = true;
        Preferences.useJavaSyntax = false;
        Preferences.showParamsString = "showParams";
        Preferences.showReturnTypesString = "showReturnTypes";
        Preferences.showAttributesString = "showAttributes";
        Preferences.showMethodsString = "showMethods";
        Preferences.showPrivatesString = "showPrivates";
        Preferences.showVisibilityString = "showVisibility";
        Preferences.useJavaSyntaxString = "useJavaSyntax";
    }
}
