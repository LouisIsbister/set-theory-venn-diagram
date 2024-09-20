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

import com.stvd.expressionparsing.*;
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
     * Creates a new JDialog box popup that contains
     * a notification that the expression parsing failed
     * and provides the error message.
     * 
     * @param err, the exception to be displayed
     */
    private void displayException(Exception err, String expr) {
        JDialog dialogBox = new JDialog(this, "Error!", true);

        JPanel panel = new JPanel();
        int width = expr.length() > 30 ? expr.length() * 12 : 350;
        panel.setPreferredSize(new Dimension(width, 175));
        panel.setLayout(null);
        panel.setVisible(true);

        String errorMsg = "<html><center>----- Error -----" +
                "<br>Expression evaluation failed.<br>Threw: <b>" + err.getClass().getSimpleName() + 
                "</b><br>Cause: <b>" + err.getMessage() + "</center></html>";

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

    /**
     * displays all the expressions the current user has tested
     */
    private void displayExpressionHistory() {
        JDialog dialogBox = new JDialog(this, "Your Previous Expressions", true);
        dialogBox.setResizable(false);

        final int PANEL_HEIGHT = exprHistory.size() * HistoryExpr.H_EXPR_HEIGHT + 25;
        
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(AppPanel.WIDTH, PANEL_HEIGHT));
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        String content = "<html><center>Previous expressions:</center></html>";
        JLabel label = new JLabel(content);
        label.setFont(new Font("Monospaced", 1, 15));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(AppPanel.WIDTH, 25));
        
        panel.add(label);
        for (ExpressionTree expr : exprHistory) {
            panel.add(new HistoryExpr(expr, dialogBox));
        }

        JScrollPane scroller = new JScrollPane();
        scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroller.setViewportView(panel);

        dialogBox.add(scroller);
        dialogBox.setPreferredSize(new Dimension(AppPanel.WIDTH + 10, PANEL_HEIGHT > 300 ? 300 : PANEL_HEIGHT + 40));
        dialogBox.pack();
        dialogBox.setLocationRelativeTo(this);
        dialogBox.setVisible(true);
    }

    private class HistoryExpr extends JLabel {

        static final int H_EXPR_WIDTH = AppPanel.WIDTH - 20;
        static final int H_EXPR_HEIGHT = 40;

        public HistoryExpr(ExpressionTree tree, JDialog dialog) {
            super(tree.EXPR_STRING.length() <= 26 ? 
                    tree.EXPR_STRING : 
                    tree.EXPR_STRING.substring(0, 26) + "...");
            setPreferredSize(new Dimension(H_EXPR_WIDTH, H_EXPR_HEIGHT));
            setFont(new Font("Monospaced", 1, 15));
            setLayout(null);

            final int OFFSET = 5;

            final int DELETE_WIDTH = 45;
            JButton delete = new JButton("X");
            delete.setBounds(H_EXPR_WIDTH - DELETE_WIDTH - OFFSET, 10, DELETE_WIDTH, 25);
            delete.addActionListener(e -> {
                exprHistory.remove(tree);
                dialog.dispose();
                displayExpressionHistory();
            });
            
            final int REDO_WIDTH = 65;
            JButton redoButton = new JButton("Redo");
            redoButton.setBounds(H_EXPR_WIDTH - REDO_WIDTH - DELETE_WIDTH - (2 * OFFSET), 10, REDO_WIDTH, 25);
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
            // if they haven't add it to expression history
            if (exprHistory.stream()
                        .noneMatch(e -> ExpressionTree.areEqual(tree, e))) {    
                exprHistory.add(tree);
            }
            return true;
        } catch (Exception e) {
            displayException(e, expr);
            return false;
        }
    }

    /**
     * Returns the Polish Notation form of the expression.
     * This is how the expression is executed giving the user insight
     * into what is actually happening.
     * 
     * @param expr, the user provided expression
     * @return, cpn string 
     */
    public String pnRepresentation(String expr) {
        try {
            java.util.Queue<String> expression = ExpressionParser.parse(expr);
            return recursiveBuilder(expression);
        } catch (ParserFailureException e) {
            displayException(e, expr);
            return new String();
        }
    }

    /**
     * recursively build the PN string representation of the expression 
     * 
     * @param expression, current/root node of subtree
     * @return cpn string
     */
    private static String recursiveBuilder(java.util.Queue<String> expression) {
        if (expression.isEmpty()) {
            return null;
        }

        String elem = expression.poll();
        if (ExpressionParser.isOperator(elem)) {
            String left = recursiveBuilder(expression);
            String right = new String();
            if (!elem.equals("~")) {    // complement should only have a left child node!
                right = " " + recursiveBuilder(expression);
            }
            return elem + "(" + left + right + ")";
        }
        return elem;
    }

}
