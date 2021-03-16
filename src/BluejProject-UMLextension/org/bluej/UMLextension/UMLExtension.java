package org.bluej.UMLextension;

import java.net.URL;
import bluej.extensions2.PreferenceGenerator;
import bluej.extensions2.MenuGenerator;
import bluej.extensions2.BlueJ;
import bluej.extensions2.Extension;

/**
 * Long time ago Ian Utting developed UMLExtension for BlueJ
 * https://www.bluej.org/extensions/UMLextension/UMLextension.jar
 * https://www.bluej.org/extensions/extensions.html
 * 
 * I (Milton Jesus Vera Contreras) decompiled JAR file 
 * and upgraded UML Extension to BlueJ5 using JavaFX. I add drag funtionality
 * and change click event for double click event to close window.
 * 
 * This class has only changes at bluej-extensions libraries
 */
public class UMLExtension extends Extension
{
    public static BlueJ bluej;
    
    public void startup(final BlueJ bluej) {
        (UMLExtension.bluej = bluej).setMenuGenerator((MenuGenerator)new MenuBuilder());
        bluej.setPreferenceGenerator((PreferenceGenerator)new Preferences());
    }
    
    public boolean isCompatible() {
        return true;
    }
    
    public String getVersion() {
        return "1.0";
    }
    
    public String getName() {
        return "UML Class Icons";
    }
    
    public String getDescription() {
        return "Displays a UML Class Icon for a BlueJ class";
    }
    
    public URL getURL() {
        try {
            return new URL("http://www.bluej.org/extensions/UMLextension/index.html");
        }
        catch (Exception eee) {
            System.out.println("UMLExtension: getURL: Exception=" + eee.getMessage());
            return null;
        }
    }
    
    static {
        UMLExtension.bluej = null;
    }
    
    /**Only to Manifiest*/
    public static void main(String [] args)
    {
    }//end main
}
