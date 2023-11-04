package code.controller;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Queue;
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

import code.binarytree.*;

public class AppFrame extends JFrame {
	
	/**
	 * the panel that displays the diagrams 
	 */
	private static AppPanel GUIPanel = new AppPanel();
	
	public AppFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//setResizable(false);
		
		getContentPane().add(GUIPanel);
		initialiseMenu();
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);

		askForExpression();
	}

	/*
	 * How the program currently runs:
	 * 1. create the menu item
	 * 
	 * 2. ask for an expression
	 * 3. take the expression and retrieve the info from it
	 * 4. then parse it to an instance of Panel.java
	 * 5. add the panel to the frame (this is what packs out the frame)
	 * 
	 * What should happen:
	 * 1. open the window then ask for an expression
	 * 
	 * 
	 *  
	 */

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
		new ExpressionInterface(this);
	}

	/**
	 * Parses the binary tree of the expression, 
	 * if an error is encountered then return false,
	 * otherwise return true. 
	 * 
	 * @param text, the expression to be parsed
	 * @return, whether the parsing of the expression was successful 
	 */
	public boolean evaluateExpression(String text){
		ExprEvaluate tree = null;
		Set<Coordinate> highlightCoords = new HashSet<>();
		List<SetNode> nodes = new ArrayList<>();

		try {
			//root = tree.createTree(text);
			tree = new ExprEvaluate(text);
			highlightCoords = tree.root().evaluate();
			nodes = tree.setNodes();
		} catch(Exception err) {
			err.printStackTrace();
			
			displayException(err);
			return false;
		}
		
		GUIPanel.updateDisplayData(highlightCoords, nodes);
		add(GUIPanel);

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
