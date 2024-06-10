package stvd.controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JPanel;

import stvd.tree.BTSetNode;
import stvd.util.Coordinate;

/**
 * panel that displays the Venn diagrams
 */
public class AppPanel extends JPanel {

	public static final int WIDTH = 500;
	public static final int HEIGHT = 500;

	/**
	 * all the coordinates that are part of the
	 * data set.
	 */
	private Set<Coordinate> coords = new HashSet<>();

	/**
	 * all the sets in the expression
	 */
	private List<BTSetNode> setNodes = new ArrayList<>();

	public AppPanel() {
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setOpaque(true);
	}

	public void updateDisplayData(Set<Coordinate> c, List<BTSetNode> s) {
		coords = c;
		setNodes = s;
	}

	/**
	 * draw the diagrams
	 * 
	 * @param g
	 */
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2D = (Graphics2D) g;

		// draw the shaded coordinates
		g2D.setColor(Color.green);
		for (Coordinate c : coords) {
			g2D.drawLine(c.x(), c.y(), c.x(), c.y());
		}

		// draw the outlines of the set "regions"
		g2D.setColor(Color.black);
		for (BTSetNode sn : setNodes) {
			g2D.drawOval(sn.center().x() - BTSetNode.DIAMETER / 2, sn.center().y() - BTSetNode.DIAMETER / 2,
					BTSetNode.DIAMETER, BTSetNode.DIAMETER);

			g2D.drawString(sn.toString(), sn.stringPosition().x(), sn.stringPosition().y());
		}
	}
}