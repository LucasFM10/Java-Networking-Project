package package2;

import java.io.*;
import java.net.*;

public class AGameServerCopy {
    
    private ServerSocket serverSocket;
    private int numPlayers;
    private ServerSideConnection[] players;

    public AGameServerCopy() {

        System.out.println("----Game Server ----");
        numPlayers = 0;
        try {
            serverSocket = new ServerSocket(51734);
        } catch(IOException ex) {
            System.out.println("IOException from GameServer Constructor");
        }
    }

    public void acceptConnections() {
        try {
            System.out.println("Waiting for connections...");
            players = new ServerSideConnection[2];
            while(numPlayers < 2) {
                Socket socket = serverSocket.accept();
                numPlayers++;
                System.out.println("Player #" + numPlayers + " has connected.");
                ServerSideConnection serverSideConnection = new ServerSideConnection(socket, numPlayers);
                players[numPlayers - 1] = serverSideConnection;
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

        public ServerSideConnection(Socket socket, int id) {
            this.socket = socket;
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
            try {
                dataOutputStream.writeInt(playerID);
                dataOutputStream.flush();

                while(true) {

                }
            } catch (IOException ex) {
                System.out.println("IOException from run() ServerSideConnection ");
            }
        }
        
    }

    public static void main(String[] args) {
        AGameServerCopy gameServer = new AGameServerCopy();
        gameServer.acceptConnections();
    }
}
