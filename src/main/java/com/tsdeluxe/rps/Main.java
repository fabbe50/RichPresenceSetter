package com.tsdeluxe.rps;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.arikia.dev.drpc.callbacks.ReadyCallback;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by fabbe on 09/03/2018 - 3:30 PM.
 */
@SuppressWarnings("Since15")
public class Main {
    //"421679617129185281"

    public static class ReadyEvent implements ReadyCallback {
        public void apply(){
            System.out.println("Discord's ready!");
        }
    }

    public static void main(String[] args) {
        System.out.println("Launching from directory: " + System.getProperty("user.dir"));
        System.out.println(System.getenv("APPDATA"));
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("Closes Discord connection.");
                DiscordRPC.discordShutdown();
            }
        });
        final JFrame frame = new JFrame();
        frame.setTitle("Input Application Code");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(300, 100);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setBackground(new Color(44, 47, 51));
        JPanel panel = new JPanel();
        panel.setBounds(5, 5, 290, 190);
        panel.setLayout(null);
        panel.setBackground(new Color(44, 47, 51));
        final JTextField textField = new JTextField();
        textField.setBounds(0, 0, 285, 25);
        textField.setVisible(true);
        panel.add(textField);
        Button button = new Button("Submit");
        button.setBounds(50, 30, 185, 25);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.startup(textField.getText());
                frame.dispose();
                new Main();
            }
        });
        button.setVisible(true);
        panel.add(button);
        frame.add(panel);
        frame.setVisible(true);
    }

    private String state = "";
    private String details = "";
    private String largeImageKey = "";
    private String largeImageText = "";
    private String smallImageKey = "";
    private String smallImageText = "";
    private Main() {
        final JFrame frame = new JFrame();
        frame.setLayout(null);
        frame.setTitle("Rich Presence Setter");
        frame.setSize(400, 600);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(5, 5, 390, 590);
        final TextField detailsField = new TextField();
        detailsField.setText("Details");
        detailsField.setBounds(0, 0, 385, 25);
        detailsField.setVisible(true);
        panel.add(detailsField);
        final TextField stateField = new TextField();
        stateField.setText("State");
        stateField.setBounds(0, 30, 385, 25);
        stateField.setVisible(true);
        panel.add(stateField);
        JComboBox<String> largeImageComboBox = new JComboBox<String>();
        largeImageComboBox.setEditable(false);
        largeImageComboBox = addImagesToList(largeImageComboBox);
        largeImageComboBox.setBounds(0, 60, 385, 25);
        largeImageComboBox.setVisible(true);
        panel.add(largeImageComboBox);
        final TextField largeImageTextField = new TextField();
        largeImageTextField.setText("Large Image Text");
        largeImageTextField.setBounds(0, 90, 385, 25);
        largeImageTextField.setVisible(true);
        panel.add(largeImageTextField);
        JComboBox<String> smallImageComboBox = new JComboBox<String>();
        smallImageComboBox.setEditable(false);
        smallImageComboBox = addImagesToList(smallImageComboBox);
        smallImageComboBox.setBounds(0, 120, 385, 25);
        smallImageComboBox.setVisible(true);
        panel.add(smallImageComboBox);
        final TextField smallImageTextField = new TextField();
        smallImageTextField.setText("Small Image Text");
        smallImageTextField.setBounds(0, 150, 385, 25);
        smallImageTextField.setVisible(true);
        panel.add(smallImageTextField);
        Button button = new Button("Set Presence");
        button.setBounds(0, 535, 385, 25);
        final JComboBox<String> finalLargeImageComboBox = largeImageComboBox;
        final JComboBox<String> finalSmallImageComboBox = smallImageComboBox;
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                state = stateField.getText();
                details = detailsField.getText();
                largeImageKey = (String) finalLargeImageComboBox.getSelectedItem();
                largeImageText = largeImageTextField.getText();
                smallImageKey = (String) finalSmallImageComboBox.getSelectedItem();
                smallImageText = smallImageTextField.getText();
                createNewPresence();
            }
        });
        button.setVisible(true);
        panel.add(button);
        frame.add(panel);
        frame.setVisible(true);
    }

    private static void startup(String identifier) {
        DiscordEventHandlers handler = new DiscordEventHandlers();
        handler.ready = new ReadyEvent();
        DiscordRPC.discordInitialize(identifier, handler, false);
    }

    private JComboBox<String> addImagesToList(JComboBox<String> box) {
        try {
            if (!new File(System.getProperty("user.dir") + "\\data\\").exists())
                //noinspection ResultOfMethodCallIgnored
                new File(System.getProperty("user.dir") + "\\data\\").mkdir();
            if (!new File(System.getProperty("user.dir") + "\\data\\images.cfg").exists())
                //noinspection ResultOfMethodCallIgnored
                new File(System.getProperty("user.dir") + "\\data\\images.cfg").createNewFile();
            java.util.List<String> lines = Files.readAllLines(new File(System.getProperty("user.dir") + "\\data\\images.cfg").toPath());
            for (String line : lines)
                box.addItem(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return box;
    }

    private void createNewPresence() {
        DiscordRichPresence rich = new DiscordRichPresence();
        rich.state = state;
        rich.details = details;
        rich.largeImageKey = largeImageKey;
        rich.largeImageText = largeImageText;
        rich.smallImageKey = smallImageKey;
        rich.smallImageText = smallImageText;
        rich.startTimestamp = System.currentTimeMillis() / 1000;
        DiscordRPC.discordUpdatePresence(rich);
    }
}
