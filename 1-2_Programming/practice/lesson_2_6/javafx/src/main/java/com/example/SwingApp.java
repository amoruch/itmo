package com.example;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class SwingApp {

    public SwingApp() {
        JFrame frame = new JFrame("Swing");
        JLabel label = new JLabel("Нажми кнопку");
        JButton button = new JButton("Привет");

        frame.setLayout(new FlowLayout());
        frame.add(label);
        frame.add(button);
        button.addActionListener(event -> label.setText("Привет!"));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SwingApp::new);
    }
}