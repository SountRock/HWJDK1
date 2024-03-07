package org.example;

public class Main {
    public static void main(String[] args) {
        ServerWindow serverWindow = new ServerWindow();
        new ClientGUI("127.0.0.1", "8080", "Alex12","12345", serverWindow);
        new ClientGUI("127.0.0.2", "8080", "SoniaZX","12345", serverWindow);
    }
}