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

import com.stvd.expressiontree.ExpressionTree;
import com.stvd.util.AppUtil;

public class AppFrame extends JFrame {

    private AppPanel windowPanel;
    private List<ExpressionTree> exprHistory;

    public AppFrame() {
        windowPanel = new AppPanel();
        exprHistory = new ArrayList<>();

        initialiseMenu();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.getContentPane().add(windowPanel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }


    /**
     * Creates a new menu bar for the frame. Adds a new menu that contains menu items
     * to allow the user to enter a new expression, view previous expressions, 
     * see the home page, and exit the application
     */
    private void initialiseMenu() {
        JMenu menu = new JMenu("Menu");
        AppUtil.addComponentsTo(
                menu,
                List.of(
                    createMenuItem("Enter new expression",      e -> askForExpression()),
                    createMenuItem("View previous expressions", e -> displayExpressionHistory()),
                    createMenuItem("Home page",                 e -> windowPanel.createDefaultView()),
                    createMenuItem("Exit",                      e -> this.dispose())
                ));

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menu);

        this.setJMenuBar(menuBar);
    }


    private JMenuItem createMenuItem(String title, ActionListener e) {
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
    private void askForExpression(String defString) {
        new ExpressionInterface(this, defString);
    }
    private void askForExpression() {
        askForExpression("");
    }


    /**
     * Creates a new JDialog box popup that contains
     * a notification that the expression parsing failed
     * and provides the error message.
     * 
     * @param err, the exception to be displayed
     */
    public void displayException(Exception ex, String expr) {
        JDialog dialogBox = new JDialog(this, "Error!", true);

        final int DEFAULT_EXC_PANEL_HEIGHT = 175;
        final int DEFAULT_EXC_PANEL_WIDTH = 350;

        final int EXPR_WIDTH = expr.length() * 12;
        final int WIDTH = Math.max(DEFAULT_EXC_PANEL_WIDTH, EXPR_WIDTH);

        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(WIDTH, DEFAULT_EXC_PANEL_HEIGHT));
        panel.setLayout(null);
        panel.setVisible(true);


        JLabel errorLabel = new JLabel(formatExceptionMsg(ex));
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorLabel.setBounds(0, 0, WIDTH, 130);
        errorLabel.setFont(new Font("Monospaced", 4, 14));

        JButton contiueButton = new JButton("Continue");
        contiueButton.setBounds(WIDTH / 2 - 75, 130, 150, 25);
        contiueButton.addActionListener(e -> dialogBox.dispose());

        AppUtil.addComponentsTo(panel, List.of(errorLabel, contiueButton));

        dialogBox.add(panel);
        dialogBox.pack();
        dialogBox.setLocationRelativeTo(this);
        dialogBox.setVisible(true);
    }


    private static String formatExceptionMsg(Exception e) {
        return String.format("""
                <html><center>
                ----- Error -----
                <br>Expression evaluation failed 
                <br>Threw: <b>%s</b>
                <br>Cause: <b>%s</b>
                </center></html>
                """, 
                e.getClass().getSimpleName(), 
                e.getMessage());
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
    public boolean executeExpression(String expr) {
        ExpressionTree tree;
        try {
            tree = new ExpressionTree(expr);
        } catch (Exception e) {
            displayException(e, expr);
            return false;
        }

        windowPanel.updateDisplayData(tree.execute(), tree.setNodes());

        // check whether the user has already entered an expression with the same outcome
        // if they haven't add it to expression history
        if (exprHistory.stream()
                .noneMatch(e -> ExpressionTree.areEqual(tree, e))) {    
            exprHistory.add(tree);
        }
        return true;
    }

    

}
