package com.stvd.controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.stvd.nodes.BTSetNode;
import com.stvd.util.AppUtil;
import com.stvd.util.Coordinate;

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

    /* all the sets in the expression */
    private Collection<BTSetNode> setNodes = new ArrayList<>();


    public AppPanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setOpaque(true);
        setLayout(new FlowLayout());
        
        createDefaultView();
    }

    public void updateDisplayData(Set<Coordinate> coords, Collection<BTSetNode> setNodes) {
        this.coords = coords;
        this.setNodes = setNodes;
        removeAll();
        revalidate();
        repaint();
    }

    public void createDefaultView() {
        // clear the current drawings on the panel
        updateDisplayData(Set.of(), List.of());
        
        JLabel label = new JLabel("Set Theory Venn Diagrams");
        label.setPreferredSize(new Dimension(300, 75));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Monospaced", 1, 20));
        
        JLabel content = new JLabel(AppUtil.LANDING_PAGE_TEXT);
        content.setPreferredSize(new Dimension(WIDTH - 20, HEIGHT - label.getHeight()));
        content.setVerticalAlignment(SwingConstants.NORTH);
        content.setFont(new Font("Monospaced", 0, 14));

        AppUtil.addComponentsTo(this, List.of(label, content));
    }

    /**
     * draw the diagrams
     * 
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;

        g2D.setFont(new Font("Monospaced", Font.BOLD, 20));

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
