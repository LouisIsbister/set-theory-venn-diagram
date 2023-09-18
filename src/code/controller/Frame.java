package code.controller;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import code.binarytree.*;

public class Frame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Frame constructor
	 */
	public Frame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		
		initialiseMenu();
		askForExpression();
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * Creates a new menu bar for the frame. 
	 * Adds a new menu that contains one menu item
	 * to allow the user to enter a new expression  
	 */
	private void initialiseMenu(){
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Menu");
		JMenuItem item = new JMenuItem("Enter new expression");
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// clear the pane and ask user for another expression 
				getContentPane().removeAll();
				askForExpression();
			}
		});

		menu.add(item);
		menuBar.add(menu);
		setJMenuBar(menuBar);
	}

	/**
	 * Creates a new JDialog box popup, this box contains a 
	 * text field for users to enter their expression and a 
	 * button to confirm it. If the expression is valid 
	 * then the dialog box will disappear and the user 
	 * expression will be rendered. 
	 */
	private void askForExpression(){
		JDialog dialogBox = new JDialog(this, "Enter your Expression", true);

		JPanel panel = new JPanel();
		panel.setVisible(true);
		panel.setPreferredSize(new Dimension(250, 100));
		panel.setLayout(null);

		JTextField expressionField = new JTextField();
		JButton confirmButton = new JButton("Confirm Expression");

		expressionField.setBounds(10, 10, 230, 25);
		confirmButton.setBounds(50, 45, 150, 25);

		// confirm button, if the expression is valid then program proceeds. 
		confirmButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = expressionField.getText();
				boolean valid = evaluateExpression(text);
				if(valid){
					dialogBox.dispose();
				}
			}
		});

		panel.add(expressionField);
		panel.add(confirmButton);

		dialogBox.add(panel);

        dialogBox.pack();
        dialogBox.setLocationRelativeTo(this);
        dialogBox.setVisible(true);
	}

	/**
	 * Parses the binary tree of the expression, 
	 * if an error is encountered then return false,
	 * otherwise return true. 
	 * 
	 * @param text, the expression to be parsed
	 * @return, whether the parsing of the expression was successful 
	 */
	private boolean evaluateExpression(String text){
		BTNode root = new BTNode();
		BuildTree tree = new BuildTree();
		Set<Coordinate> highlightCoords = new HashSet<>();

		try {
			root = tree.createTree(text);
			highlightCoords = root.evaluate();
		} catch(Exception err) {
			displayException(err);
			return false;
		}
		
		Collection<SetNode> nodes = tree.setNodes();

		// creates a new panel that will paint the expression
		Panel panel = new Panel(highlightCoords, nodes);
		add(panel);

		revalidate();
		repaint();
		return true;
	}
	
	/**
	 * Creates a new JDialog box popup that contains 
	 * a notification that the expression parsing failed
	 * and provides the error message.
	 * 
	 * @param err, the exception to be displayed
	 */
	private void displayException(Exception err){
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

	/**
	 * DEBUGGING METHOD: 
	 * Performs a breadth first traversal of the binary tree, printing the parent node 
	 * and its children.
	 * 
	 * @param root, the root node
	 */
	public static void bfs(BTNode root) {
		Queue<BTNode> queue = new ArrayDeque<BTNode>();
		queue.offer(root);

		while(!queue.isEmpty()) {
			BTNode node = queue.poll();
			System.out.println("Parent: " + node + " -> left: " + node.left()+ ", right: " + node.right());
			
			if(node.left() != null) {
				queue.add(node.left());
			}
			if(node.right() != null){
				queue.add(node.right());
			}
		}
	}
}
