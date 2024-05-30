package stvd.controller;

import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
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

import stvd.expressions.*;
import stvd.util.Coordinate;

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
		newExpr.addActionListener(e -> {
			// clear the pane and ask user for another expression
			getContentPane().removeAll();
			askForExpression();
		});

		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(e -> dispose());

		JMenuItem expressionHistory = new JMenuItem("View previous expressions");
		expressionHistory.addActionListener(e -> displayExpressionHistory());

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
		new ExpressionInterface(this, "");
	}

	/**
	 * Parses the binary tree of the expression,
	 * if an error is encountered then return false,
	 * otherwise return true.
	 * 
	 * @param text, the expression to be parsed
	 *              @return, whether the parsing of the expression was successful
	 */
	public boolean executeExpression(String text) {
		try {
			BTParser tree = new BTParser(text);
			Set<Coordinate> highlightCoords = tree.root().evaluate();
			List<SetNode> nodes = tree.setNodes();
			if (!exprHistory.contains(text))
				exprHistory.add(text);

			guiPanel.updateDisplayData(highlightCoords, nodes);
		} 
		catch (Exception err) {
			err.printStackTrace();
			displayException(err.getMessage());
			return false;
		}

		// this is stupid but so is swing
		getContentPane().remove(guiPanel);
		getContentPane().add(guiPanel);
		repaint();
		return true;
	}

	/**
	 * displays all the expressions the current user has tested
	 */
	private void displayExpressionHistory() {
		JDialog dialogBox = new JDialog(this, "Your Previous Expressions", true);

		int height = 30 + exprHistory.size() * 50;

		JPanel panel = new JPanel();
		panel.setVisible(true);
		panel.setPreferredSize(new Dimension(450, height));
		panel.setLayout(null);

		for (int i = 0; i < exprHistory.size(); i++) {
			String text = " " + exprHistory.get(i);

			HistoryExpr expr = new HistoryExpr(this, text, dialogBox);
			expr.setBounds(0, 20 + i * 50, 450, 50);
			panel.add(expr);
		}

		String content = "<html><center>Previous expressions:</center></html>";
		JLabel label = new JLabel(content);
		label.setFont(new Font("Monospaced", 1, 15));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(0, 0, 450, 25);

		panel.add(label);

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
	private void displayException(String errStr) {
		JDialog dialogBox = new JDialog(this, "Enter your Expression", true);

		JPanel panel = new JPanel();
		panel.setVisible(true);
		panel.setPreferredSize(new Dimension(350, 175));
		panel.setLayout(null);

		String errorMsg = "<html><center>----- Error -----" +
				"<br>Expression evaluation failed." +
				"<br>Error message:<b><br>" + errStr + "</center></html>";

		JLabel errorLabel = new JLabel(errorMsg);
		errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		errorLabel.setBounds(0, 0, 350, 130);
		errorLabel.setFont(new Font("Monospaced", 4, 14));

		JButton contiueButton = new JButton("Continue");
		contiueButton.setBounds(100, 130, 150, 25);
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

		public HistoryExpr(AppFrame frame, String text, JDialog dialog) {
			super(text);
			setFont(new Font("Monospaced", 1, 15));
			setHorizontalAlignment(SwingConstants.LEFT);
			setLayout(null);

			JButton redoButton = new JButton("Redo");

			redoButton.setBounds(370, 10, 70, 30);
			redoButton.addActionListener(e -> {
				dialog.dispose();
				new ExpressionInterface(frame, text);
			});

			add(redoButton);
		}

	}
}
