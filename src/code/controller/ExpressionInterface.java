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
        super(frame, "", true);
        this.frame = frame;

        display();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * 
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
                
				if (frame.evaluateExpression(text)) {
					dispose();
				}
			}
		});

        panel.add(expressionField);
        panel.add(confirmButton);
        addButtons();

        add(panel);
    }

    private void addButtons() {
        JButton intersect = new JButton("\u2229");
        intersect.setBounds(10, 40, 50, 50);
        intersect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String txt = expressionField.getText();
                expressionField.setText(txt + "\u2229");
            }
        });

        JButton union = new JButton("\u222A");
        union.setBounds(70, 40, 50, 50);
        union.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String txt = expressionField.getText();
                expressionField.setText(txt + "\u222A");
            }
        });

        JButton difference = new JButton("\\");
        difference.setBounds(130, 40, 50, 50);
        difference.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String txt = expressionField.getText();
                expressionField.setText(txt + "\\");
            }
        });

        JButton complement = new JButton("~");
        complement.setBounds(190, 40, 50, 50);
        complement.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String txt = expressionField.getText();
                expressionField.setText(txt + "~");
            }
        });

        panel.add(intersect);
        panel.add(union);
        panel.add(difference);
        panel.add(complement);
    }
}
