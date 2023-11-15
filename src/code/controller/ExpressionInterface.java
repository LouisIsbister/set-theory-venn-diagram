package code.controller;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ExpressionInterface extends JDialog {

    /**
     * text field to enter an experssion
     */
    private JTextField expressionField;

    /**
     * the parent frame component
     */
    private AppFrame frame;

    /**
     * panel to display dialog box contents
     */
    private JPanel panel;

    public ExpressionInterface(AppFrame frame) {
        super(frame, "Enter an expression", true);
        this.frame = frame;

        display();
        addButtons();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * display the expression interface dialog box
     */
    private void display() {
        panel = new JPanel();
        panel.setVisible(true);
        panel.setPreferredSize(new Dimension(250, 160));
        panel.setLayout(null);

        expressionField = new JTextField();
        JButton confirmButton = new JButton("Confirm Expression");

        expressionField.setBounds(10, 10, 230, 25);
        confirmButton.setBounds(50, 110, 150, 25);

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = expressionField.getText();

                // if the expression was valid, then dispose of the expression interface
                if (frame.evaluateExpression(text)) 
                    dispose();
            }
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
        intersect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateExprField("\u2229");
            }
        });

        JButton union = new JButton("\u222A");
        union.setBounds(70, 40, 50, 50);
        union.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateExprField("\u222A");
            }
        });

        JButton difference = new JButton("\\");
        difference.setBounds(130, 40, 50, 50);
        difference.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateExprField("\\");
            }
        });

        JButton complement = new JButton("~");
        complement.setBounds(190, 40, 50, 50);
        complement.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateExprField("~");
            }
        });

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
