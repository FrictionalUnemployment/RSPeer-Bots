package org.frictional.EmilsLooter.GUI;

import org.frictional.EmilsLooter.Main;
import org.rspeer.ui.Log;

import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

public class InterfaceGUI extends JFrame {
    public static JLabel l;
    public InterfaceGUI() {
// frame to contains GUI elements
        JFrame frame = new JFrame("Looter GUI");
        frame.setSize(400, 400);
        Container container = frame.getContentPane();
        container.setLayout(new FlowLayout());

        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(150, 25));

        JLabel label = new JLabel("Put Mule name");

        JButton okButton = new JButton("OK");
        JButton openButton = new JButton("Open");

        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

                // set the selection mode to directories only
                j.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

                // invoke the showsOpenDialog function to show the save dialog
                int r = j.showOpenDialog(null);

                if (r == JFileChooser.APPROVE_OPTION) {
                    // set the label to the path of the selected directory
                    try {
                        Main.readFile(j.getSelectedFile().getAbsolutePath());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        okButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String input = textField.getText();
                Main.MULE_NAME = input;

                label.setText(input);
            }
        });

        container.add(textField);
        container.add(okButton);
        container.add(label);
        container.add(openButton);


        setDefaultCloseOperation(HIDE_ON_CLOSE);
        frame.setVisible(true);
    }

}


