package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ServerWindow extends JFrame {
    public static final int POX_X = 500;
    public static final int POX_Y = 550;
    public static final int WIDTH = 400;
    public static final int HEIGHT = 300;

    private final JButton btnStart;
    private final JButton btnStop;
    private final JTextArea log;
    private boolean isServerWorking;
    private static List<ClientGUI> connectedClient = new ArrayList<>();
    private static Stack<String> clientLog = new Stack<>();

    public static void main(String[] args) {
        new ServerWindow();
    }

    public ServerWindow(){
        isServerWorking = false;

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(POX_X, POX_Y, WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setResizable(true);
        setTitle("Chat server");

        //Buttons/////////////////////////////////////////////////////
        btnStart = new JButton("Start");
        btnStop = new JButton("Stop");
        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isServerWorking = false;
                sendResponseToDisconnectionForClients();

                log.append("Server stopped \n");
                for (ClientGUI client : connectedClient) {
                    log.append(client.getLogin() + " disconnect to server \n");
                }

                System.out.println("Server stopped " + isServerWorking + '\n');
            }
        });

        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isServerWorking = true;
                sendResponseToSuccessConnectionForClients();

                log.append("Server started \n");
                for (ClientGUI client : connectedClient) {
                    log.append(client.getLogin() + " connect to server \n");
                }

                System.out.println("Server started " + isServerWorking + '\n');
            }
        });

        JPanel panelButtons = new JPanel(new GridLayout(1,2));
        panelButtons.add(btnStop);
        panelButtons.add(btnStart);
        add(panelButtons, BorderLayout.SOUTH);
        //Buttons/////////////////////////////////////////////////////

        //TextArea/////////////////////////////////////////////////////
        log = new JTextArea();
        log.setEditable(false);

        try(FileInputStream logPuller = new FileInputStream("chat/serverLog.chat")) {
            int read;
            while ((read = logPuller.read()) != -1){
                log.append(String.valueOf((char) read));
            }
        } catch (Exception e) {}

        JScrollPane logScroll = new JScrollPane(log);
        add(logScroll, BorderLayout.CENTER);
        //TextArea/////////////////////////////////////////////////////

        //Window/////////////////////////////////////////////////////
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveLog();
                super.windowClosing(e);
            }
        });
        //Window/////////////////////////////////////////////////////

        setVisible(true);
    }

    public void addClient(ClientGUI client){
        connectedClient.add(client);
    }

    public void sendMessage(String sender, String message){
        if(!isServerWorking || stringIsEmpty(message)){
            return;
        }

        clientLog.push(sender + ": " + message + '\n');
        for (ClientGUI client : connectedClient) {
            client.getLog().append(clientLog.peek());
        }

        log.append(sender + ": " + message + '\n');
    }

    private void sendResponseToSuccessConnectionForClients(){
        for (ClientGUI client : connectedClient) {
            client.getLog().append("Connection success \n \n");

            try(FileInputStream logPuller = new FileInputStream("chat/clientsLog.chat")) {
                int read;
                while ((read = logPuller.read()) != -1){
                    client.getLog().append(String.valueOf((char) read));
                }
            } catch (Exception e) {}
        }
    }

    private void sendResponseToDisconnectionForClients() {
        for (ClientGUI client : connectedClient) {
            client.getLog().append("Connection failed \n \n");
        }
    }

    private void saveLog(){
        File logFile = new File("chat/");
        logFile.mkdirs();

        logFile = new File("chat/", "serverLog.chat");
        try(FileOutputStream serverWriter = new FileOutputStream(logFile, true)) {
            serverWriter.write(log.getText().getBytes());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Failed save server log", JOptionPane.ERROR_MESSAGE);
        }

        logFile = new File("chat/", "clientsLog.chat");
        try(FileOutputStream serverWriter = new FileOutputStream(logFile, true)) {
            for (String logLine :clientLog) {
                serverWriter.write(logLine.getBytes());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Failed save server log", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean stringIsEmpty(String str){
        if (str.isEmpty()) return true;

        char[] strArray = str.toCharArray();
        int countNotEmpty = 0;
        for (char elem : strArray) {
            if(elem != ' ') countNotEmpty++;
        }

        return countNotEmpty < 1;
    }


}
