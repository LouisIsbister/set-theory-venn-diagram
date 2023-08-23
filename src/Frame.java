import java.awt.FlowLayout;
import java.util.Collection;
import java.util.Set;

import javax.swing.JFrame;

public class Frame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	Panel panel;
	
	public Frame(Set<Coordinate> highlightCoords, Collection<SetNode> nodes) {
		
		this.panel = new Panel(highlightCoords, nodes);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new FlowLayout());
		
		this.add(panel);
				
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
