package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

public class ServerWindow extends JFrame {
    public static final int POX_X = 500;
    public static final int POX_Y = 550;
    public static final int WIDTH = 400;
    public static final int HEIGHT = 300;

    private final JButton btnStart;
    private final JButton btnStop;
    private final JTextArea log;
    private boolean isServerWorking;
    private static List<ClientGUI> connectedClient = new LinkedList<>();

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
                clearDestinationsForClients();

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
                addDestinationsForClients();

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
        JScrollPane logScroll = new JScrollPane(log);
        add(logScroll, BorderLayout.CENTER);
        //TextArea/////////////////////////////////////////////////////

        setVisible(true);
    }

    public void addClient(ClientGUI client){
        connectedClient.add(client);
    }

    public void sendMessage(String sender, String destinationLogin, String message){
        for (ClientGUI client : connectedClient) {
            if(client.getLogin().equals(destinationLogin)){
                client.getLog().append(sender + ": " + message + '\n');
            }
        }

        log.append(sender + ": " + message + '\n');
    }

    private void addDestinationsForClients(){
        for (ClientGUI client : connectedClient) {
            for (ClientGUI item : connectedClient) {
                client.getDestination().addItem(item.getLogin());
            }
        }
    }

    private void clearDestinationsForClients(){
        for (ClientGUI client : connectedClient) {
            client.getDestination().removeAllItems();
        }
    }
}
