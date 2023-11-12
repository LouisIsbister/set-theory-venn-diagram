package code.controller;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
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
import javax.swing.SwingConstants;

import code.expressions.*;

public class AppFrame extends JFrame {

	/**
	 * the panel that displays the diagrams
	 */
	private static AppPanel guiPanel = new AppPanel();

	/**
	 * all the tested expressions so far
	 */
	private static List<String> exprHistory = new ArrayList<>();

	public AppFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		getContentPane().add(guiPanel);
		initialiseMenu();

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * Creates a new menu bar for the frame.
	 * Adds a new menu that contains one menu item
	 * to allow the user to enter a new expression
	 */
	private void initialiseMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Menu");

		JMenuItem newExpr = new JMenuItem("Enter new expression");
		newExpr.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// clear the pane and ask user for another expression
				getContentPane().removeAll();
				askForExpression();
			}
		});

		AppFrame comp = this;
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				comp.dispose();
			}
		});

		JMenuItem expressionHistory = new JMenuItem("View previous expressions");
		expressionHistory.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				displayExpressionHistory();
			}
		});

		menu.add(newExpr);
		menu.add(expressionHistory);
		menu.add(exit);

		menuBar.add(menu);
		setJMenuBar(menuBar);
	}

	/**
	 * Creates a new expression interface that allows the user
	 * to enter their expression
	 */
	private void askForExpression() {
		new ExpressionInterface(this);
	}

	/**
	 * Parses the binary tree of the expression,
	 * if an error is encountered then return false,
	 * otherwise return true.
	 * 
	 * @param text, the expression to be parsed
	 *              @return, whether the parsing of the expression was successful
	 */
	public boolean evaluateExpression(String text) {
		ExprEvaluate tree = null;
		Set<Coordinate> highlightCoords = new HashSet<>();
		List<SetNode> nodes = new ArrayList<>();

		try {
			// root = tree.createTree(text);
			tree = new ExprEvaluate(text);
			highlightCoords = tree.root().evaluate();
			nodes = tree.setNodes();
		} catch (Exception err) {
			err.printStackTrace();

			displayException(err);
			return false;
		}
		exprHistory.add(text);

		guiPanel.updateDisplayData(highlightCoords, nodes);
		add(guiPanel);

		repaint();
		return true;
	}

	/**
	 * displays all the expressions the current user has tested
	 */
	private void displayExpressionHistory() {
		JDialog dialogBox = new JDialog(this, "Your Previous Expressions", true);

		int height = 35;
		String content = "<html><center>History:";
		if (exprHistory.size() == 0) {
			height += 60;
			content += "<br><i>Not applicable</i>";
		} else {
			height += (exprHistory.size() + 1) * 30;
			for (int i = 0; i < exprHistory.size(); i++)
				content += "<br>" + exprHistory.get(i);
		}

		content += "</center></html>";

		JPanel panel = new JPanel();
		panel.setVisible(true);
		panel.setPreferredSize(new Dimension(350, height));
		panel.setLayout(null);

		JLabel label = new JLabel(content);
		label.setFont(new Font("Arial", 0, 20));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(0, 0, 350, height - 35);

		JButton contiueButton = new JButton("Continue");
		contiueButton.setBounds(100, height - 35, 150, 25);

		// button to dispose the dialog box when user is satisfied
		contiueButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialogBox.dispose();
			}
		});

		panel.add(label);
		panel.add(contiueButton);

		dialogBox.add(panel);

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
	private void displayException(Exception err) {
		JDialog dialogBox = new JDialog(this, "Enter your Expression", true);

		JPanel panel = new JPanel();
		panel.setVisible(true);
		panel.setPreferredSize(new Dimension(350, 175));
		panel.setLayout(null);

		String errorMsg = "<html><center>----- Error -----" +
				"<br>Expression evaluation falied." +
				"<br>Please check your format." +
				"<br>Error message: " + err.getMessage() + "</center></html>";

		JLabel errorLabel = new JLabel(errorMsg);
		errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		errorLabel.setBounds(0, 0, 350, 100);

		JButton contiueButton = new JButton("Continue");
		contiueButton.setBounds(100, 110, 150, 25);

		// button to dispose the dialog box when user is satisfied
		contiueButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialogBox.dispose();
			}
		});

		panel.add(errorLabel);
		panel.add(contiueButton);

		dialogBox.add(panel);

		dialogBox.pack();
		dialogBox.setLocationRelativeTo(this);
		dialogBox.setVisible(true);
	}
}
