package package2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class Player extends JFrame {
    
    private int width;
    private int height;
    private Container contentPane;
    private JTextArea message;
    private JButton b1;
    private JButton b2;
    private JButton b3;
    private JButton b4;
    private int playerID;
    private int otherPlayer;
    private int[] values;
    private int maxTurns;
    private int turnsMade;
    private int myPoints;
    private int enemyPoints;
    private boolean buttonsEnabled;

    private ClientSideConnection clientSideConnection;

    public Player (int width, int height) {
        this.width = width;
        this.height = height;
        contentPane = this.getContentPane();
        message = new JTextArea();
        b1 = new JButton("1");
        b2 = new JButton("2");
        b3 = new JButton("3");
        b4 = new JButton("4");
        values = new int[4];
        this.turnsMade = 0;
        this.myPoints = 0;
        this.enemyPoints = 0;
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


        if(playerID == 1) {
            message.setText("You are player #1. You go first.");
            otherPlayer = 2;
            buttonsEnabled = true;
        } else {
            message.setText("You are player #2. Wait for yout turn.");
            otherPlayer = 1;
            buttonsEnabled = false;
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    updateTurn();
                }
            });
            t.start();
        }
        
        toggleButtons();

        this.setVisible(true);
    }

    public void connectToServer() {
        this.clientSideConnection = new ClientSideConnection();
    }

    public void setUpButtons() {
        ActionListener actionListener =  new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                JButton button = (JButton) actionEvent.getSource();
                int buttonNumber = Integer.parseInt(button.getText());

                message.setText("You clicked button #" + buttonNumber + ". Now wait for player #" + otherPlayer);
                turnsMade++;
                System.out.println("Turns made: " + turnsMade + ".");

                buttonsEnabled = false;
                toggleButtons();

                myPoints += values[buttonNumber - 1];
                System.out.println("My points: " + myPoints);
                clientSideConnection.sendButtonNumber(buttonNumber);
                
                if(playerID == 2 && turnsMade == maxTurns) {
                    checkWinner();
                } else {

                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            updateTurn();
                        }
                    });
                    t.start();
                }
            }
        };

        b1.addActionListener(actionListener);
        b2.addActionListener(actionListener);
        b3.addActionListener(actionListener);
        b4.addActionListener(actionListener);
    }

    public void toggleButtons() {
        b1.setEnabled(buttonsEnabled);
        b2.setEnabled(buttonsEnabled);
        b3.setEnabled(buttonsEnabled);
        b4.setEnabled(buttonsEnabled);
    }

    public void updateTurn() {
        int number = clientSideConnection.receiveButtonNumber();
        message.setText("Your enemy clicked button #" + number + ". Your turn.");
        enemyPoints+= values[number-1];
        System.out.println("Your enemy has " + enemyPoints + " points");
        if(playerID == 1 && turnsMade == maxTurns) {
            checkWinner();
        } else {
            buttonsEnabled = true;
        }
        toggleButtons();
    }

    private void checkWinner() {
        buttonsEnabled = false;
        if(myPoints > enemyPoints) {
            message.setText("You won!\nYOU: " + myPoints + "\nENEMY: " + enemyPoints);
        } else if (myPoints < enemyPoints){
            message.setText("You lost!\nYOU: " + myPoints + "\nENEMY: " + enemyPoints);
        } else {
            message.setText("Its a tie!\nYOU: " + myPoints + "\nENEMY: " + enemyPoints);
        }

        clientSideConnection.closeConnection();
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
                maxTurns = dataInputStream.readInt() / 2;
                System.out.println("maxTurns: " + maxTurns + ".");
                for(int i = 0; i < values.length; i++) {
                   values[i] = dataInputStream.readInt();
                   System.out.println("Value #" + (i + 1) + " is " + values[i] + ".");
                }
                System.out.println();
            } catch(IOException ex) {
                System.out.println("IOException from ClientSideConnection constructor.");
            }
        }

        public void sendButtonNumber(int number) {
            try {
                dataOutputStream.writeInt(number);
                dataOutputStream.flush();
            } catch (Exception ex) {
                System.out.println("IOException from sendButtonNumber() in ClientSideConnection.");
            }
        }

        public int receiveButtonNumber() {

            int number = -1;
            try {
                number = dataInputStream.readInt();
                System.out.println("Player #" + otherPlayer + " clicked button #" + number + ".");
            } catch (IOException ex) {
                System.out.println("IOException from receiveButtonNumber() in ClientServerConection.");
            }
            return number;
        }
    
        public void closeConnection() {
            try {
                socket.close();
                System.out.println("---CONNECTION CLOSED---");
            } catch (IOException ex) {
                System.out.println("IOException on closeConnection() in ClienteServerConnection.");
            }
        }
    
    }

    public static void main(String[] args) {
        Player p = new Player(500, 100);
        p.connectToServer();
        p.setUpGUI();
        p.setUpButtons();
    }
}
