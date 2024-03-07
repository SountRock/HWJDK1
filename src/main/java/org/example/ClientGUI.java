package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ClientGUI extends JFrame {
    private boolean isConnected = false;
    private ServerWindow serverWindow;
    public static final int WIDTH = 400;
    public static final int HEIGHT = 300;
    private JTextArea log;

    private JTextField tfIPAddress;
    private JTextField tfPort;
    private JTextField tfLogin;
    private JPasswordField tfPassword;

    private JPanel panelButton;
    private JTextField tfMessage;
    private JButton btnSend;

    public static void main(String[] args) {
        new ClientGUI("127.0.0.1", "8080", "Alex12","12345", new ServerWindow());
    }

    private JPanel createTop(String IP, String port, String login, String password){
        JPanel panelTob = new JPanel(new GridLayout(2, 3));

        tfIPAddress = new JTextField(IP);
        tfPort = new JTextField(port);
        tfLogin = new JTextField(login);
        tfPassword = new JPasswordField(password);

        panelTob.add(tfIPAddress);
        panelTob.add(tfPort);
        panelTob.add(tfLogin);
        panelTob.add(tfPassword);

        return panelTob;
    }

    private JPanel createButtons(){
        JPanel panelButton = new JPanel(new BorderLayout());
        tfMessage = new JTextField();
        btnSend = new JButton("Send");

        panelButton.add(tfMessage, BorderLayout.CENTER);
        panelButton.add(btnSend, BorderLayout.EAST);

        return panelButton;
    }

    private JScrollPane createLog(){
        log = new JTextArea();
        log.setEditable(false);

        return new JScrollPane(log);
    }

    public ClientGUI(String IP, String port, String login, String password, ServerWindow serverWindow) {
        this.serverWindow = serverWindow;

        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setTitle("Chat Client: " + login);

        //Tob/////////////////////////////////////////////////////
        add(createTop(IP, port, login, password), BorderLayout.NORTH);
        //Tab/////////////////////////////////////////////////////

        //Buttons/////////////////////////////////////////////////////
        add(createButtons(), BorderLayout.SOUTH);

        tfMessage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    sendMessageToServer();
                }
                super.keyPressed(e);
            }
        });
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessageToServer();
            }
        });
        //Buttons/////////////////////////////////////////////////////

        //Log/////////////////////////////////////////////////////
        add(createLog());
        //Log/////////////////////////////////////////////////////

        if(serverWindow != null){
            serverWindow.addClient(this);
        }

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                serverWindow.sendMessageToServer(tfLogin.getText() + " disconnect to server \n");
                serverWindow.removeClient(tfLogin.getText());

                super.windowClosing(e);
            }
        });

        setVisible(true);
    }

    public String getLogin() {
        return tfLogin.getText();
    }

    public JTextArea getLog() {
        return log;
    }

    /**
     * Отправить запрос о подключении клиенту
     * @param isConnected
     */
    public void sendResponseConnectedStatus(boolean isConnected){
        this.isConnected = isConnected;

        String statusText = isConnected ? "Connection success \n \n" : "Connection failed \n \n";
        log.append(statusText);
    }

    /**
     * Метод для отсылания сообщения от имени ползьзователя в чат
     */
    public void sendMessageToServer(){
        if(isConnected){
            serverWindow.sendMessage(tfLogin.getText(), tfMessage.getText());
            tfMessage.setText(null);
        }
    }

}
