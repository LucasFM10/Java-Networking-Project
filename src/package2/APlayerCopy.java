package package2;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class APlayerCopy extends JFrame {
    
    private int width;
    private int height;
    private Container contentPane;
    private JTextArea message;
    private JButton b1;
    private JButton b2;
    private JButton b3;
    private JButton b4;
    private int playerID;
    private int[] playersIDs;

    private ClientSideConnection clientSideConnection;

    public APlayerCopy (int w, int h) {
        width = w;
        height = h;
        contentPane = this.getContentPane();
        message = new JTextArea();
        b1 = new JButton("1");
        b2 = new JButton("2");
        b3 = new JButton("3");
        b4 = new JButton("4");
    }

    public void connectToServer() {
        clientSideConnection = new ClientSideConnection();
    }

    public void setUpGUI() {
        this.setSize(width, height);
        this.setTitle("Player #" + playerID);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        contentPane.setLayout(new GridLayout(1, 5));
        contentPane.add(message);
        message.setText("Creating a simple turn-based game in Java");
        message.setWrapStyleWord(true);
        message.setLineWrap(true);
        message.setEditable(false);
        contentPane.add(b1);
        contentPane.add(b2);
        contentPane.add(b3);
        contentPane.add(b4);


        message.setText("You are player #" + playerID);
        

        this.setVisible(true);
    }

    private class ClientSideConnection {
        
        private Socket socket;
        private DataInputStream dataInputStream;
        private DataOutputStream dataOutputStream;

        public ClientSideConnection () {
            System.out.println("---Client---");
            try {
                socket = new Socket("localhost", 51734);
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                playerID = dataInputStream.readInt();
                System.out.println("Connected to server as Player #" + playerID + ".");
            } catch(IOException ex) {
                System.out.println("IOException from ClientSideConnection constructor.");
            }
        }
    }

    public static void main(String[] args) {
        APlayerCopy p = new APlayerCopy(500, 100);
        p.connectToServer();
        p.setUpGUI();
    }
}
