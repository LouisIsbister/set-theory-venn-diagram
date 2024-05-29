package code.controller;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JDialog;
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
        this.expressionField = new JTextField();
        
        panel.setVisible(true);
        panel.setPreferredSize(new Dimension(250, 160));
        panel.setLayout(null);

        display();
        addButtons();
        // set the default string in the text field, used for redoing an expr
        expressionField.setText(defaultExpr);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * display the expression interface dialog box
     */
    private void display() {
        JButton confirmButton = new JButton("Confirm Expression");
        confirmButton.setBounds(50, 110, 150, 25);
        expressionField.setBounds(10, 10, 230, 25);

        confirmButton.addActionListener(e -> {
            String text = expressionField.getText();
            boolean isValidExpression = frame.executeExpression(text);
            // if the expression was valid, then dispose of the interface
            if (isValidExpression)
                dispose();
        });

        panel.add(expressionField);
        panel.add(confirmButton);
        add(panel);
    }

    /**
     * Adds the various buttons that allow the user to input the math notation 
     * into their expressions 
     */
    private void addButtons() {
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

}
