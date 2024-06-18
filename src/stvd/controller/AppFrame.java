package stvd.controller;

import java.awt.Dimension;
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
import javax.swing.SwingConstants;

import stvd.expressionparser.ExpressionTree;
import stvd.tree.BTNode;
import stvd.tree.BTSetNode;
import stvd.util.Coordinate;
import stvd.util.InvalidExpressionException;

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
			String text = exprHistory.get(i);

			HistoryExpr expr = new HistoryExpr(this, text, dialogBox);
			expr.setBounds(10, 20 + i * 50, 450, 50);
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
	private void displayException(String errStr, String expr) {
		JDialog dialogBox = new JDialog(this, "Error!", true);

		JPanel panel = new JPanel();
		panel.setVisible(true);
		int width = expr.length() > 30 ? expr.length() * 12 : 350;
		panel.setPreferredSize(new Dimension(width, 175));
		panel.setLayout(null);

		String errorMsg = "<html><center>----- Error -----" +
				"<br>Expression evaluation failed." +
				"<br>Error message:<b><br>" + errStr + "</center></html>";

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

	private class HistoryExpr extends JLabel {

		public HistoryExpr(AppFrame frame, String text, JDialog dialog) {
			super(text);
			setFont(new Font("Monospaced", 1, 15));
			//setHorizontalAlignment(SwingConstants.LEFT);
			setLayout(null);

			JButton redoButton = new JButton("Redo");
			redoButton.setBounds(350, 10, 70, 30);
			redoButton.addActionListener(e -> {
				dialog.dispose();
				new ExpressionInterface(frame, text);
			});
			add(redoButton);
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

			if (!exprHistory.contains(expr)) {
				exprHistory.add(expr);
			}
		} 
		catch (Exception e) {
			displayException(e.getMessage(), expr);
			return false;
		}

		// this is stupid but so is swing
		getContentPane().remove(guiPanel);
		getContentPane().add(guiPanel);
		repaint();
		return true;
	}

	/**
	 * Returns the Cambridge Polish Notation form of the expression.
	 * This is how the expression is executed giving the user insight
	 * into what is actually happening.
	 * 
	 * @param expr, the user provided expression
	 * @return, cpn string
	 */
	public String cPNRepresentation(String expr) {
		try {
			// if the expression is invalid this will throw an exception
			ExpressionTree tree = new ExpressionTree(expr);
			BTNode root = tree.root();
			
			return recursiveBuilder(root);
		} catch (InvalidExpressionException e) {
			displayException(e.getMessage(), expr);
			return new String();
		}
	}

	/**
	 * recursively iterate through the binary tree to build up 
	 * the string representation in CPN
	 * 
	 * @param root, current/root node of subtree
	 * @return cpn string
	 */
	private String recursiveBuilder(BTNode root) {
		if (root == null) {
			return null;
		}
		String nodeStr = root.toString();
		if (!nodeStr.matches("[a-zA-Z]")) {
			String left = recursiveBuilder(root.left());
			String right = recursiveBuilder(root.right());
			right = right == null ? "" : " " + right; 
			
			return nodeStr + "(" + left + right + ")";
		}
		return nodeStr;
	}

}
