package com.stvd.controller;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.stvd.parsing.Parser;
import com.stvd.util.ParserFailureException;
import com.stvd.util.Store;

public class ExpressionInterface extends JDialog {

    /* panel to display dialog box contents */
    private JPanel panel;

    /* text field to enter an experssion */
    private JTextField expressionField;

    public ExpressionInterface(String defaultExpr) {
        super(AppFrame.window(), "Enter an expression", true);

        this.panel = new JPanel(); 
        this.expressionField = new JTextField(defaultExpr);
        expressionField.setBounds(10, 10, 230, 25);
        
        panel.setVisible(true);
        panel.setPreferredSize(new Dimension(250, 170));
        panel.setLayout(null);

        addButtons();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

   /**
     * Adds the various buttons that allow the user to input the math notation 
     * into their expressions 
     */
    private void addButtons() {
        JButton confirmButton = new JButton("Confirm Expression");
        confirmButton.setBounds(50, 100, 150, 25);

        confirmButton.addActionListener(e -> {
            String text = expressionField.getText();
            
            if (!text.isBlank()) {
                boolean isValidExpression = AppFrame.executeExpression(text);
                // if the expression was valid, then dispose of the interface
                if (isValidExpression) {
                    dispose();
                }
            }
        });

        JButton showEvaluation = new JButton("Execution Representation");
        showEvaluation.setBounds(20, 135, 210, 25);
        showEvaluation.addActionListener(e -> {
            String text = expressionField.getText();
            String execStr = "";
            try {
                execStr = Parser.getExecRepresentation(text);
            } catch(ParserFailureException pfe) {
                AppFrame.displayException(pfe, execStr);
                return;
            }

            displayExpression(execStr);
        });

        panel.add(expressionField);
        panel.add(showEvaluation);
        panel.add(confirmButton);

        // --- operator buttons

        JButton intersect = new JButton(Store.INTERSECT);
        intersect.setBounds(10, 40, 50, 50);
        intersect.addActionListener(e -> updateExprField(Store.INTERSECT));

        JButton union = new JButton(Store.UNION);
        union.setBounds(70, 40, 50, 50);
        union.addActionListener(e -> updateExprField(Store.UNION));

        JButton difference = new JButton(Store.DIFFERENCE);
        difference.setBounds(130, 40, 50, 50);
        difference.addActionListener(e -> updateExprField(Store.DIFFERENCE));

        JButton complement = new JButton(Store.COMPLEMENT);
        complement.setBounds(190, 40, 50, 50);
        complement.addActionListener(e -> updateExprField(Store.COMPLEMENT));

        panel.add(intersect);
        panel.add(union);
        panel.add(difference);
        panel.add(complement);

        add(panel);
    }
   
    /**
     * Inserts the the operator into the textfield for the user to see. 
     * 
     * @param insertString
     */
    private void updateExprField(String insertString) {
        int pos = expressionField.getCaretPosition();
        StringBuilder ret = new StringBuilder(expressionField.getText());
        ret.insert(pos, insertString);
        
        // reset the expression field
        expressionField.setText(ret.toString());
        expressionField.requestFocus();
        expressionField.setCaretPosition(pos + insertString.length());
    }

    /**
     * @param execStr, exection form of expression
     */
    private void displayExpression(String execStr) {
        JDialog dialog = new JDialog(this, "Execution Representation", true);
        JPanel newPanel = new JPanel();
        JLabel label = new JLabel(execStr);
        label.setFont(new Font("Monospaced", 1, 15));

        newPanel.setLayout(new FlowLayout());
        newPanel.add(label);
        int width = execStr.length() * 12 + 20;
        newPanel.setPreferredSize(new Dimension(width, 40));

        dialog.add(newPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

}
