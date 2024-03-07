package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientGUI extends JFrame {
    public static final int WIDTH = 400;
    public static final int HEIGHT = 300;
    private final JTextArea log;

    private final JPanel panelTob;
    private final JTextField tfIPAddress;
    private final JTextField tfPort;
    private final JTextField tfLogin;
    private final JPasswordField tfPassword;

    private final JPanel panelButton;
    private final JTextField tfMessage;
    private final JButton btnSend;

    public static void main(String[] args) {
        new ClientGUI("127.0.0.1", "8080", "Alex12","12345", new ServerWindow());
    }

    public ClientGUI(String IP, String port, String login, String password, ServerWindow serverWindow) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setTitle("Chat Client: " + login);

        //Tob/////////////////////////////////////////////////////
        panelTob = new JPanel(new GridLayout(2, 3));
        tfIPAddress = new JTextField(IP);
        tfPort = new JTextField(port);
        tfLogin = new JTextField(login);
        tfPassword = new JPasswordField(password);

        panelTob.add(tfIPAddress);
        panelTob.add(tfPort);
        panelTob.add(tfLogin);
        panelTob.add(tfPassword);
        add(panelTob, BorderLayout.NORTH);
        //Tab/////////////////////////////////////////////////////

        //Buttons/////////////////////////////////////////////////////
        panelButton = new JPanel(new BorderLayout());
        tfMessage = new JTextField();
        btnSend = new JButton("Send");

        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                serverWindow.sendMessage(tfLogin.getText(), tfMessage.getText());

                tfMessage.setText(null);
            }
        });

        panelButton.add(tfMessage, BorderLayout.CENTER);
        panelButton.add(btnSend, BorderLayout.EAST);
        add(panelButton, BorderLayout.SOUTH);
        //Buttons/////////////////////////////////////////////////////

        //Log/////////////////////////////////////////////////////
        log = new JTextArea();
        //log.setForeground(Color.RED);
        log.setEditable(false);
        JScrollPane scrollLog = new JScrollPane(log);

        add(scrollLog);
        //Log/////////////////////////////////////////////////////

        if(serverWindow != null){
            serverWindow.addClient(this);
        }

        setVisible(true);
    }

    public String getLogin() {
        return tfLogin.getText();
    }

    public JTextArea getLog() {
        return log;
    }
}
