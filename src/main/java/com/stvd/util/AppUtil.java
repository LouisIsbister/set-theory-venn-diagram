package com.stvd.util;

import java.awt.Container;
import java.util.List;
import java.awt.Component;

public class AppUtil {

    /**
     * Given a container (i.e. JFrame, JLabel, JPanel etc) add each component 
     * from a list of components to said container
     * @param container
     * @param components
     */
    public static void addComponentsTo(Container container, List<Component> components) {
        for (Component c : components) {
            container.add(c);
        }
    }

    public static final String INTERSECT  = "\u2229";
    public static final String UNION      = "\u222A";
    public static final String DIFFERENCE = "\\";
    public static final String COMPLEMENT = "~";

    public static final String LANDING_PAGE_TEXT = """
            <html>To enter your own expressions navigate to 'Menu -> Enter new Expression'
            <br><br>Execute the expression by pressing 'Confirm Expression', to view the 
                    execution structure of the expression press 'Execution Representation'
            <br><br>Expressions that have been successfully executed can be re-executed 
                    by navigating to 'Menu -> View previous expressions', and click 'Redo' 
                    on the expression to re-open it in the expression interface
            <br><br>To view this page again navigate to 'Menu -> Home page', and to exit 
                    this application simply navigate to 'Menu -> Exit'
            </html>""";

}
