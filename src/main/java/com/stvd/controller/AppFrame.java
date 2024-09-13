package com.stvd.controller;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import com.stvd.expressionparser.ExpressionTree;
import com.stvd.nodes.*;
import com.stvd.util.*;

public class AppFrame extends JFrame {

    /**
     * the panel that displays the diagrams
     */
    private static AppPanel guiPanel;

    /**
     * all the executed expressions so far
     */
    private static List<ExpressionTree> exprHistory;

    public static void start() {
        guiPanel = new AppPanel();
        exprHistory = new ArrayList<>();
        final AppFrame WINDOW = new AppFrame();

        WINDOW.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        WINDOW.initialiseMenu();
        WINDOW.getContentPane().add(guiPanel);
        WINDOW.pack();
        WINDOW.setLocationRelativeTo(null);
        WINDOW.setVisible(true);
    }

    /**
     * Creates a new menu bar for the frame. Adds a new menu that contains menu items
     * to allow the user to enter a new expression, view previous expressions, 
     * see the home page, and exit the application
     */
    private void initialiseMenu() {
        JMenuItem newExpr = new JMenuItem("Enter new expression");
        newExpr.addActionListener(e -> this.askForExpression(new String()));

        JMenuItem expressionHistory = new JMenuItem("View previous expressions");
        expressionHistory.addActionListener(e -> this.displayExpressionHistory());

        JMenuItem homePage = new JMenuItem("Home page");
        homePage.addActionListener(e -> guiPanel.createDefaultView());

        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(e -> this.dispose());

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");

        menu.add(newExpr);
        menu.add(expressionHistory);
        menu.add(homePage);
        menu.add(exit);

        menuBar.add(menu);
        setJMenuBar(menuBar);
    }

    /**
     * Creates a new expression interface that allows the user
     * to enter their expression
     * 
     * @param defString, default expression in the expression field
     */
    private void askForExpression(String defString) {
        new ExpressionInterface(this, defString);
    }

    /**
     * displays all the expressions the current user has tested
     */
    private void displayExpressionHistory() {
        JDialog dialogBox = new JDialog(this, "Your Previous Expressions", true);
        dialogBox.setResizable(false);

        final int EXPR_HEIGHT = 40;
        final int PANEL_WIDTH = 450;
        final int PANEL_HEIGHT = 25 + exprHistory.size() * EXPR_HEIGHT;
        final int DIALOG_HEIGHT = PANEL_HEIGHT > 300 ? 300 : PANEL_HEIGHT + 40;

        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        String content = "<html><center>Previous expressions:</center></html>";
        JLabel label = new JLabel(content);
        label.setFont(new Font("Monospaced", 1, 15));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(PANEL_WIDTH, 25));
        
        panel.add(label);
        for (ExpressionTree expr : exprHistory) {
            panel.add(new HistoryExpr(expr, dialogBox, PANEL_WIDTH - 20, EXPR_HEIGHT));
        }

        JScrollPane scroller = new JScrollPane();
        scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroller.setViewportView(panel);

        dialogBox.add(scroller);
        dialogBox.setPreferredSize(new Dimension(PANEL_WIDTH + 10, DIALOG_HEIGHT));
        dialogBox.pack();
        dialogBox.setLocationRelativeTo(this);
        dialogBox.setVisible(true);
    }

    /**
     * Creates a new JDialog box popup that contains
     * a notification that the expression parsing failed
     * and provides the error message.
     * 
     * @param err, the exception to be displayed
     */
    private void displayException(String errStr, String expr) {
        JDialog dialogBox = new JDialog(this, "Error!", true);

        JPanel panel = new JPanel();
        int width = expr.length() > 30 ? expr.length() * 12 : 350;
        panel.setPreferredSize(new Dimension(width, 175));
        panel.setLayout(null);
        panel.setVisible(true);

        String errorMsg = "<html><center>----- Error -----" +
                "<br>Expression evaluation failed." +
                "<br>Error message:<b><br>" + errStr + "</center></html>";

        JLabel errorLabel = new JLabel(errorMsg);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorLabel.setBounds(0, 0, width, 130);
        errorLabel.setFont(new Font("Monospaced", 4, 14));

        JButton contiueButton = new JButton("Continue");
        contiueButton.setBounds(width / 2 - 75, 130, 150, 25);
        // button to dispose the dialog box when user is satisfied
        contiueButton.addActionListener(e -> dialogBox.dispose());

        panel.add(errorLabel);
        panel.add(contiueButton);

        dialogBox.add(panel);
        dialogBox.pack();
        dialogBox.setLocationRelativeTo(this);
        dialogBox.setVisible(true);
    }

    private class HistoryExpr extends JLabel {

        public HistoryExpr(ExpressionTree tree, JDialog dialog, int width, int height) {
            super(tree.EXPR_STRING.length() <= 26 ? 
                    tree.EXPR_STRING : 
                    tree.EXPR_STRING.substring(0, 26) + "...");
            setPreferredSize(new Dimension(width, height));
            setFont(new Font("Monospaced", 1, 15));
            setLayout(null);

            final int OFFSET = 5;

            final int DELETE_WIDTH = 45;
            JButton delete = new JButton("X");
            delete.setBounds(width - DELETE_WIDTH - OFFSET, 10, DELETE_WIDTH, 25);
            delete.addActionListener(e -> {
                exprHistory.remove(tree);
                dialog.dispose();
                displayExpressionHistory();
            });
            
            final int REDO_WIDTH = 65;
            JButton redoButton = new JButton("Redo");
            redoButton.setBounds(width - REDO_WIDTH - DELETE_WIDTH - (2 * OFFSET), 10, REDO_WIDTH, 25);
            redoButton.addActionListener(e -> {
                dialog.dispose();
                askForExpression(tree.EXPR_STRING);
            });
            
            add(redoButton);
            add(delete);
        }

    }


    // --- expression display and evaluation handling --- 


    /**
     * Parses the binary tree of the expression,
     * if an error is encountered then return false,
     * otherwise return true.
     * 
     * @param expr, the expression to be parsed
     * @return, whether the expression evaluation was successful
     */
    public boolean executeExpression(String expr) {
        try {
            ExpressionTree tree = new ExpressionTree(expr);
            Set<Coordinate> highlightCoords = tree.execute();
            Collection<BTSetNode> nodes = tree.setNodes();
            guiPanel.updateDisplayData(highlightCoords, nodes);

            // check whether the user has already entered an expression with the same outcome
            // if they haven't then add it to expression history
            if (exprHistory.stream().noneMatch(e -> exprCompare(tree, e))) {
                exprHistory.add(tree);
            }
        } catch (Exception e) {
            displayException(e.getMessage(), expr);
            return false;
        }
        return true;
    }

    /**
     * Returns the Polish Notation form of the expression.
     * This is how the expression is executed giving the user insight
     * into what is actually happening.
     * 
     * @param expr, the user provided expression
     * @return, cpn string
     */
    public String cPNRepresentation(String expr) {
        try {
            // if the expression is invalid this will throw an exception
            ExpressionTree tree = new ExpressionTree(expr);
            tree.execute();
            return recursiveBuilder(tree.root());
        } catch (InvalidExpressionException e) {
            displayException(e.getMessage(), expr);
            return new String();
        }
    }

    /**
     * recursively iterate through the binary tree to build up 
     * the string representation in CPN
     * 
     * @param root, current/root node of subtree
     * @return cpn string
     */
    private static String recursiveBuilder(BTNode root) {
        if (root == null) {
            return null;
        }
        String nodeStr = root.toString();
        if (!nodeStr.matches("[a-zA-Z]")) {
            String left = recursiveBuilder(root.left());
            String right = recursiveBuilder(root.right());
            right = right == null ? "" : " " + right;

            return nodeStr + "(" + left + right + ")";
        }
        return nodeStr;
    }

    /**
     * Given two expression trees, check if they are equivalent, i.e. they 
     * contain the same data points/coordinates when evaulated
     * 
     * @param tree1
     * @param tree2
     * @return whether the trees are equivalent
     */
    private static boolean exprCompare(ExpressionTree tree1, ExpressionTree tree2) {
        try {
            Set<Coordinate> t1Coords = tree1.execute();
            Set<Coordinate> t2Coords = tree2.execute();
            return t1Coords.containsAll(t2Coords) && t2Coords.containsAll(t1Coords);
        } catch(InvalidExpressionException e) {
            return false;
        }
    }
}
