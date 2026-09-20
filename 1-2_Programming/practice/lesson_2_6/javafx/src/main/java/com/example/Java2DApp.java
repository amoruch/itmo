package com.example;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class Java2DApp {

    public Java2DApp() {
        JFrame frame = new JFrame("Java 2D");
        frame.add(new DrawingPanel());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Java2DApp::new);
    }

    private static class DrawingPanel extends JPanel {

        private double angle;

        DrawingPanel() {
            setPreferredSize(new Dimension(320, 180));
            new Timer(30, event -> {
                angle += 0.05;
                repaint();
            }).start();
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g = (Graphics2D) graphics.create();

            try {
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setPaint(new GradientPaint(0, 0, Color.PINK, getWidth(), getHeight(), Color.CYAN));
                g.fillRect(0, 0, getWidth(), getHeight());
                g.translate(getWidth() / 2.0, getHeight() / 2.0);
                g.rotate(angle);
                g.setColor(Color.DARK_GRAY);
                g.fillRoundRect(-45, -45, 90, 90, 18, 18);
            } finally {
                g.dispose();
            }
        }
    }
}