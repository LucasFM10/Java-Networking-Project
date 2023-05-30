package package2;

import java.io.*;
import java.net.*;

public class GameServer {
    
    private ServerSocket serverSocket;
    private int numPlayers;
    private ServerSideConnection player1, player2;
    private int turnsMade;
    private int maxTurns;
    private int[] values;
    private int playerOneButtonNum, playerTwoButtonNum;

    public GameServer() {

        System.out.println("----Game Server ----");
        numPlayers = 0;
        turnsMade =  0;
        maxTurns = 4;
        values = new int[4];

        for(int i = 0; i < values.length; i++) {
            values[i] = (int) Math.ceil(Math.random() * 100);
            System.out.println("Value #" + (i + 1) + " is " + values[i]);
        }

        try {
            serverSocket = new ServerSocket(51734);
        } catch(IOException ex) {
            System.out.println("IOException from GameServer Constructor");
        }
    }

    public void acceptConnections() {
        try {
            System.out.println("Waiting for connections...");
            while(numPlayers < 2) {
                Socket socket = serverSocket.accept();
                numPlayers++;
                System.out.println("Player #" + numPlayers + " has connected.");
                ServerSideConnection serverSideConnection = new ServerSideConnection(socket, numPlayers);
                if (numPlayers == 1) {
                    player1 = serverSideConnection;
                } else {
                    player2 = serverSideConnection;
                }
                Thread t = new Thread(serverSideConnection);
                t.start();
            }
            System.out.println("We now have 2 players. No longer accepting connections.");
        } catch (IOException ex) {
            System.out.println("IOException from acceptConnections()");
        }
    }

    private class ServerSideConnection implements Runnable {

        private Socket socket;
        private DataInputStream dataInputStream;
        private DataOutputStream dataOutputStream;
        private int playerID;

        public ServerSideConnection(Socket s, int id) {
            this.socket = s;
            this.playerID = id;
            try {
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
            } catch (IOException ex){
                System.out.println("IOException from ServerSideConnection constructor.");
            }
        }

        @Override
        public void run() {
            System.out.println("1\n");
            try {
                dataOutputStream.writeInt(playerID);
                dataOutputStream.writeInt(maxTurns);
                for(int i = 0; i < values.length; i++) {
                    dataOutputStream.writeInt(values[i]);
                }
                dataOutputStream.flush();

                while(turnsMade < maxTurns) {

                    if (playerID == 1) {
                        playerOneButtonNum = dataInputStream.readInt();
                        System.out.println("Player 1 clicked button #" + playerOneButtonNum);
                        player2.sendButtonNumber(playerOneButtonNum);
                    } else {
                        playerTwoButtonNum = dataInputStream.readInt();
                        System.out.println("Player 2 clicked button #" + playerTwoButtonNum);
                        player1.sendButtonNumber(playerTwoButtonNum);
                    }

                    turnsMade++;
                }
                System.out.println("Max turns have been reached.");
                player1.closeConnection();
                player2.closeConnection();
            } catch (IOException ex) {
                System.out.println("IOException from run() ServerSideConnection: " + ex);
            }
        }

        public void sendButtonNumber(int number) {
            try {
                dataOutputStream.writeInt(number);
                dataOutputStream.flush();
            } catch (Exception ex) {
                System.out.println("IOException from sendButtonNumber() in ServerSideConnection.");
            }
        }

        public void closeConnection() {
            try {
                serverSocket.close();
                socket.close();
                System.out.println("Connection closed.");
            } catch (IOException ex) {
                System.out.println("IOException on closeConnect() in ServerSideConnection.");
            }
        }
        
    }

    public static void main(String[] args) {
        GameServer gameServer = new GameServer();
        gameServer.acceptConnections();
    }
}
