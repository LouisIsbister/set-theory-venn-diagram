package com.stvd.controller;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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

import com.stvd.parsing.*;
import com.stvd.util.AppUtil;

public class AppFrame extends JFrame {

    /* The apps main window */
    private static AppFrame window;

    /* the panel that displays the diagrams */
    private static AppPanel guiPanel;

    /* all the executed expressions so far */
    private static List<ExpressionTree> exprHistory;


    public static void start() {
        window = new AppFrame();
        guiPanel = new AppPanel();
        exprHistory = new ArrayList<>();

        initialiseMenu();

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.getContentPane().add(guiPanel);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    public static AppFrame window() {
        return window;
    }

    /**
     * Creates a new menu bar for the frame. Adds a new menu that contains menu items
     * to allow the user to enter a new expression, view previous expressions, 
     * see the home page, and exit the application
     */
    private static void initialiseMenu() {
        JMenuItem newExpr = createMenuItem("Enter new expression", e -> askForExpression());
        JMenuItem expressionHistory = createMenuItem("View previous expressions", e -> displayExpressionHistory());
        JMenuItem homePage = createMenuItem("Home page", e -> guiPanel.createDefaultView());
        JMenuItem exit = createMenuItem("Exit", e -> window.dispose());

        JMenu menu = new JMenu("Menu");
        AppUtil.addComponentsTo(menu, List.of(newExpr, expressionHistory, homePage, exit));

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menu);
        window.setJMenuBar(menuBar);
    }

    private static JMenuItem createMenuItem(String title, ActionListener e) {
        JMenuItem item = new JMenuItem(title);
        item.addActionListener(e);
        return item;
    }

    /**
     * Creates a new expression interface that allows the user
     * to enter their expression
     * 
     * @param defString, default expression in the expression field
     */
    private static void askForExpression(String defString) {
        new ExpressionInterface(defString);
    }
    private static void askForExpression() {
        new ExpressionInterface("");
    }

    /**
     * Creates a new JDialog box popup that contains
     * a notification that the expression parsing failed
     * and provides the error message.
     * 
     * @param err, the exception to be displayed
     */
    public static void displayException(Exception err, String expr) {
        JDialog dialogBox = new JDialog(window, "Error!", true);

        JPanel panel = new JPanel();
        final int WIDTH = expr.length() > 30 ? expr.length() * 12 : 350;
        panel.setPreferredSize(new Dimension(WIDTH, 175));
        panel.setLayout(null);
        panel.setVisible(true);

        final String ERROR_MSG = "<html><center>----- Error -----" +
                "<br>Expression evaluation failed<br>Threw: <b>" + err.getClass().getSimpleName() + 
                "</b><br>Cause: <b>" + err.getMessage() + "</center></html>";

        JLabel errorLabel = new JLabel(ERROR_MSG);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorLabel.setBounds(0, 0, WIDTH, 130);
        errorLabel.setFont(new Font("Monospaced", 4, 14));

        JButton contiueButton = new JButton("Continue");
        contiueButton.setBounds(WIDTH / 2 - 75, 130, 150, 25);
        // button to dispose the dialog box when user is satisfied
        contiueButton.addActionListener(e -> dialogBox.dispose());

        AppUtil.addComponentsTo(panel, List.of(errorLabel, contiueButton));

        dialogBox.add(panel);
        dialogBox.pack();
        dialogBox.setLocationRelativeTo(window);
        dialogBox.setVisible(true);
    }

    /**
     * displays all the expressions the current user has tested
     */
    private static void displayExpressionHistory() {
        JDialog dialogBox = new JDialog(window, "Your Previous Expressions", true);
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
        dialogBox.setLocationRelativeTo(window);
        dialogBox.setVisible(true);
    }

    private static class HistoryExpr extends JLabel {

        static final int H_EXPR_WIDTH = AppPanel.WIDTH - 20;
        static final int H_EXPR_HEIGHT = 40;

        public HistoryExpr(ExpressionTree tree, JDialog dialog) {
            super(tree.EXPR_STRING.length() <= 25 ? 
                    tree.EXPR_STRING : 
                    tree.EXPR_STRING.substring(0, 25) + "...");
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

            AppUtil.addComponentsTo(this, List.of(redoButton, delete));
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
    public static boolean executeExpression(String expr) {
        ExpressionTree tree;
        try {
            tree = new ExpressionTree(expr);
        } catch (Exception e) {
            displayException(e, expr);
            return false;
        }

        guiPanel.updateDisplayData(tree.execute(), tree.setNodes());

        // check whether the user has already entered an expression with the same outcome
        // if they haven't add it to expression history
        if (exprHistory.stream()
                .noneMatch(e -> ExpressionTree.areEqual(tree, e))) {    
            exprHistory.add(tree);
        }
        return true;
    }

    

}
