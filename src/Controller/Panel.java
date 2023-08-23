package Controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Collection;
import java.util.Set;

import javax.swing.JPanel;

import Tree.BuildTree;
import Tree.Coordinate;
import Tree.SetNode;

public class Panel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	
	private Set<Coordinate> coords;
	private Collection<SetNode> setNodes;
	
	public Panel(Set<Coordinate> coords, Collection<SetNode> setNodes) {
		this.coords = coords;
		this.setNodes = setNodes;
		this.setPreferredSize(new Dimension(500, 500));
	}
	
	public void paint(Graphics g) {
		Graphics2D g2D = (Graphics2D) g;
		
		// draw the shaded coordinates
		g2D.setColor(Color.green);
		for(Coordinate c : coords) {
			g2D.drawLine(c.x(), c.y(), c.x(), c.y());
		}
		
		// draw the outlines of the set "regions"
		g2D.setColor(Color.black);
		for(SetNode sn : setNodes) {
			g2D.drawOval(sn.center().x() - SetNode.DIAMETER/2, sn.center().y() - SetNode.DIAMETER/2, 
						SetNode.DIAMETER, SetNode.DIAMETER);
			g2D.drawRect(0, 0, 2*BuildTree.startX, 2*BuildTree.startX);
			g2D.drawString(sn.toString(), sn.stringPosition().x(), sn.stringPosition().y());
		}
	}
}
