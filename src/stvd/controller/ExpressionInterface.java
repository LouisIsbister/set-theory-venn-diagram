package src.stvd.controller;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ExpressionInterface extends JDialog {

    /**
     * the parent frame component
     */
    private AppFrame frame;

    /**
     * panel to display dialog box contents
     */
    private JPanel panel;

     /**
     * text field to enter an experssion
     */
    private JTextField expressionField;

    public ExpressionInterface(AppFrame frame,  String defaultExpr) {
        super(frame, "Enter an expression", true);
        this.frame = frame;
        this.panel = new JPanel(); 
        // set the default string in the text field, used for redoing an expr
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
            boolean isValidExpression = frame.executeExpression(text);
            // if the expression was valid, then dispose of the interface
            if (isValidExpression) {
                dispose();
            }
        });

        JButton showEvaluation = new JButton("CPN Representation");
        showEvaluation.setBounds(20, 135, 210, 25);
        showEvaluation.addActionListener(e -> {
            String text = expressionField.getText();
            String cpnStr = frame.cPNRepresentation(text);
            if (!cpnStr.isBlank()) {
                displayCPNExpression(cpnStr);
            }
        });

        panel.add(expressionField);
        panel.add(showEvaluation);
        panel.add(confirmButton);

        // --- operator buttons

        JButton intersect = new JButton("\u2229");
        intersect.setBounds(10, 40, 50, 50);
        intersect.addActionListener(e -> updateExprField("\u2229"));

        JButton union = new JButton("\u222A");
        union.setBounds(70, 40, 50, 50);
        union.addActionListener(e -> updateExprField("\u222A"));

        JButton difference = new JButton("\\");
        difference.setBounds(130, 40, 50, 50);
        difference.addActionListener(e -> updateExprField("\\"));

        JButton complement = new JButton("~");
        complement.setBounds(190, 40, 50, 50);
        complement.addActionListener(e -> updateExprField("~"));

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
     * @param cpnStr, cambridge polish notation form of expression
     */
    private void displayCPNExpression(String cpnStr) {
        JDialog dialog = new JDialog(this, "CPN Representation", true);
        JPanel newPanel = new JPanel(); 
        JLabel label = new JLabel(cpnStr);
        label.setFont(new Font("Monospaced", 1, 15));

        newPanel.setLayout(new FlowLayout());
        newPanel.add(label);
        int width = cpnStr.length() * 12 + 20;
        newPanel.setPreferredSize(new Dimension(width, 40));

        dialog.add(newPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

}
