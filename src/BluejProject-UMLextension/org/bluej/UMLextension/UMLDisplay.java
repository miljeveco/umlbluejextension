package org.bluej.UMLextension;

import java.lang.reflect.Modifier;
import bluej.extensions2.BMethod;
import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.lang.reflect.Field;
import java.util.ArrayList;
import bluej.extensions2.BClass;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToggleGroup;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import bluej.extensions2.PreferenceGenerator;
import javafx.scene.control.Separator;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Cursor;

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
 * and events was changed
 */
class UMLDisplay extends BorderPane
{
    private static final String[] UML_VIS;
    private static final String[] JAVA_VIS;
    private static final int PRIVATE_IX = 0;
    private static final int PROTECTED_IX = 1;
    private static final int PACKAGE_IX = 2;
    private static final int PUBLIC_IX = 3;
    private Stage stage;

    public UMLDisplay(final BClass bc, Stage stage) {
        this.stage = stage;
        Pane umlPane = getUMLPane(bc);
        BorderPane.setAlignment(umlPane, Pos.CENTER);
        BorderPane.setMargin(umlPane, new Insets(5,5,5,5));
        this.setCenter(umlPane);
        
        this.setOnMouseClicked(new EventHandler<MouseEvent>(){
                public void handle(MouseEvent e) { 
                    if(e.getEventType().equals(MouseEvent.MOUSE_CLICKED) && e.getClickCount()==2){
                        stage.close();
                    }
                }
            }); 
        this.setOnMouseDragged(new EventHandler<MouseEvent>(){
                public void handle(MouseEvent e){
                    stage.setX(e.getScreenX());
                    stage.setY(e.getScreenY());
                }
            });
    }

    private final String cssUMLBox = 
        "-fx-border-color: black;\n" +
        "-fx-border-insets: 5;\n" +
        "-fx-border-width: 3;\n";
    private final String cssUMLClassLabel = 
        "-fx-font-size: 12px;\n"+
        "-fx-font-weight: bold;\n"+
        "-fx-text-fill: #333333;\n"+
        "-fx-effect: innershadow( three-pass-box , rgba(0,0,0,0.7) , 6, 0.0 , 0 , 2 );";

    private Pane getUMLPane(final BClass bc) {
        final VBox box = new VBox();
        box.setStyle(cssUMLBox);
        try {
            final Pane jc = this.getStereotype(bc.getJavaClass());
            if (jc != null) {
                box.getChildren().add(jc);
            }
            final Label jl = new Label (bc.getJavaClass().getName());
            jl.setStyle(cssUMLClassLabel);
            final HBox inner = new HBox();
            inner.getChildren().add(jl);
            inner.setAlignment(Pos.CENTER);
            box.getChildren().add(inner);
            if (Preferences.showAttributes) {
                final ArrayList attribs = new ArrayList();
                final Field[] fa = bc.getJavaClass().getDeclaredFields();
                for (int i = 0; i < fa.length; ++i) {
                    if (this.shouldShow(fa[i].getModifiers())) {
                        attribs.add(this.formatAttribute(fa[i]));
                    }
                }
                if (attribs.size() > 0) {
                    box.getChildren().add(this.separator());
                    final Iterator it = attribs.iterator();
                    while (it.hasNext()) {
                        box.getChildren().add((Pane)it.next());
                    }
                }
            }
            if (Preferences.showMethods) {
                final ArrayList methods = new ArrayList();
                if (Preferences.showParams || Preferences.showVisibility) {
                    final Constructor[] bca = bc.getJavaClass().getDeclaredConstructors();
                    for (int i = 0; i < bca.length; ++i) {
                        if (this.shouldShow(bca[i].getModifiers())) {
                            methods.add(this.formatConstructor(bca[i]));
                        }
                    }
                }
                final BMethod[] bma = bc.getDeclaredMethods();
                for (int i = 0; i < bma.length; ++i) {
                    if (this.shouldShow(bma[i].getModifiers())) {
                        methods.add(this.formatMethod(bma[i]));
                    }
                }
                if (methods.size() > 0) {
                    box.getChildren().add(this.separator());
                    final Iterator it = methods.iterator();
                    while (it.hasNext()) {
                        box.getChildren().add((Pane)it.next());
                    }
                }
            }
        }
        catch (Exception ex) {}
        return box;
    }

    private boolean shouldShow(final int modifiers) {
        return Preferences.showPrivates || !Modifier.isPrivate(modifiers);
    }

    private Pane formatAttribute(final Field b) {
        if (Preferences.useJavaSyntax) {
            return this.displayString(this.visibility(b.getModifiers()) + this.trim(this.getSimpleName(b.getType())) + " " + b.getName());
        }
        return this.displayString(this.visibility(b.getModifiers()) + b.getName() + ": " + this.trim(this.getSimpleName(b.getType())));
    }

    private Pane formatConstructor(final Constructor c) {
        return this.displayString(this.visibility(c.getModifiers()) + c.getName() + this.formatArgs(c.getParameterTypes()));
    }

    private Pane formatMethod(final BMethod bm) {
        if (Preferences.useJavaSyntax) {
            return this.displayString(this.visibility(bm.getModifiers()) + this.trim(this.getSimpleName(bm.getReturnType())) + " " + bm.getName() + this.formatArgs(bm.getParameterTypes()));
        }
        return this.displayString(this.visibility(bm.getModifiers()) + bm.getName() + this.formatArgs(bm.getParameterTypes()) + ": " + this.trim(this.getSimpleName(bm.getReturnType())));
    }

    private String formatArgs(final Class[] args) {
        if (!Preferences.showParams) {
            return "";
        }
        String res = "(";
        for (int i = 0; i < args.length; ++i) {
            res += this.trim(this.getSimpleName(args[i]));
            if (i != args.length - 1) {
                res += ", ";
            }
        }
        res += ")";
        return res;
    }

    private Pane getStereotype(final Class c) {
        String s = null;
        if (c.isInterface()) {
            s = "<<interface>>";
        }
        else if (Modifier.isAbstract(c.getModifiers())) {
            s = "<<abstract>>";
        }
        try {
            if (Class.forName("java.lang.Enum").isAssignableFrom(c)) {
                s = "<<enum>>";
            }
        }
        catch (ClassNotFoundException ex) {}
        try {
            if (Class.forName("junit.framework.TestCase").isAssignableFrom(c)) {
                s = "<<unit test>>";
            }
        }
        catch (ClassNotFoundException ex2) {}
        try {
            if (Class.forName("java.applet.Applet").isAssignableFrom(c)) {
                s = "<<applet>>";
            }
        }
        catch (ClassNotFoundException ex3) {}
        if (s == null) {
            return null;
        }
        final HBox inner = new HBox();    
        final Label stereotypeLabel = new Label(s);
        stereotypeLabel.setStyle(cssUMLClassLabel);        
        inner.getChildren().add(stereotypeLabel);
        inner.setAlignment(Pos.CENTER);
        return inner;
    }

    private Pane separator() {
        final VBox b1 = new VBox();
        final Separator sep1 = new Separator();
        sep1.setOrientation(Orientation.HORIZONTAL);
        VBox.setMargin(sep1, new Insets(2,2,2,2));
        b1.getChildren().add(sep1);
        return b1;
    }

    private Pane displayString(final String content) {
        final HBox b = new HBox();
        final Label jl = new Label (content);
        b.getChildren().add(jl);
        return b;
    }

    private String trim(final String s) {
        return s.substring(s.lastIndexOf(46) + 1);
    }

    private String visibility(final int modifiers) {
        String[] vis = null;
        if (!Preferences.showVisibility) {
            return "";
        }
        if (Preferences.useJavaSyntax) {
            vis = UMLDisplay.JAVA_VIS;
        }
        else {
            vis = UMLDisplay.UML_VIS;
        }
        if (Modifier.isPrivate(modifiers)) {
            return vis[0];
        }
        if (Modifier.isProtected(modifiers)) {
            return vis[1];
        }
        if ((modifiers & 0x7) == 0x0) {
            return vis[2];
        }
        return vis[3];
    }

    private String getSimpleName(final Class c) {
        String name = c.getName();
        if (c.isArray()) {
            System.out.println("Array: \"" + name + "\"");
            int dimensions = 0;
            String type = null;
            int pos = 0;
            while (name.charAt(pos++) == '[') {
                ++dimensions;
            }
            switch (name.charAt(--pos)) {
                case 'Z': {
                    type = "boolean";
                    break;
                }
                case 'B': {
                    type = "byte";
                    break;
                }
                case 'C': {
                    type = "char";
                    break;
                }
                case 'L': {
                    type = name.substring(name.indexOf(76) + 1, name.indexOf(59));
                    break;
                }
                case 'D': {
                    type = "double";
                    break;
                }
                case 'F': {
                    type = "float";
                    break;
                }
                case 'I': {
                    type = "int";
                    break;
                }
                case 'J': {
                    type = "long";
                    break;
                }
                case 'S': {
                    type = "short";
                    break;
                }
                default: {
                    System.out.println("Type: '" + name.charAt(pos) + "'");
                    type = "???";
                    break;
                }
            }
            name = type;
            for (int i = 0; i < dimensions; ++i) {
                name += "[]";
            }
        }
        return name;
    }     

    static {
        UML_VIS = new String[] { "- ", "# ", "~", "+ " };
        JAVA_VIS = new String[] { "private ", "protected ", "", "public " };
    }
}
