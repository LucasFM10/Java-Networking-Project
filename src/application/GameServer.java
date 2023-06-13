package application;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class GameServer {
    
    private ServerSocket serverSocket;
    private int serverState = 0;
    private boolean waitingConnections = true;
    private List<ServerSideConnection> players;

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public int getServerState() {
        return serverState;
    }

    public void setServerState(int gameState) {
        this.serverState = gameState;
    }

    public boolean isWaitingConnections() {
        return waitingConnections;
    }

    public void setWaitingConnections(boolean waitingConnections) {
        this.waitingConnections = waitingConnections;
    }

    public List<ServerSideConnection> getPlayers() {
        return players;
    }

    public void setPlayers(List<ServerSideConnection> players) {
        this.players = players;
    }

    public GameServer(int porta) {

        System.out.println("----Game Server ----");
        players = new ArrayList<>();

        try {
            serverSocket = new ServerSocket(porta);
        } catch(IOException ex) {
            System.out.println("IOException from GameServer Constructor");
        }
    }

    public void acceptConnections() {
        try {
            System.out.println("Waiting for connections...");
            int i = 0;
            while(true) {
                if(waitingConnections){
                    serverSocket.setSoTimeout(5000);
                    try {
                        Socket socket = serverSocket.accept();
                        i++;
                        ServerSideConnection serverSideConnection = new ServerSideConnection(socket, i);
                        System.out.println("Player #" + (i) + " has connected.");
                        players.add(serverSideConnection);
                        Thread t = new Thread(serverSideConnection);
                        t.start();
                    } catch (IOException e) {
                        // Erro de aceitação do socket
                        System.out.println("Erro de aceitação do socket: " + e.getMessage());
                    }
                } else {
                    break;
                }
            }
            System.out.println("We now have " + i + " players. No longer accepting connections.");
            serverState = 1;
        } catch (IOException ex) {
            System.out.println("IOException from acceptConnections()");
        }
    }

    public void endConnections() {
        this.waitingConnections = false;
        for(int i = 0; i < players.size(); i++) 
            players.get(i).sendMessage("Conexões não são mais aceitas e  jogo está começando.");
    }

    private class ServerSideConnection implements Runnable {

        private Socket socket;
        
        // private DataInputStream dataInputStream;

        private BufferedReader bufferedReader;
        private PrintStream printStream;

        private int playerID;

        public ServerSideConnection(Socket s, int id) {
            this.socket = s;
            this.playerID = id;
            try {

                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                printStream = new PrintStream(socket.getOutputStream());
            } catch (IOException ex){
                System.out.println("IOException from ServerSideConnection constructor.");
            }
        }

        @Override
        public void run() {
            try {

                System.out.println("Thread ativa: " + Thread.currentThread().getName());

                String string;

                for(int i = 0; i < players.size(); i++) {
                    players.get(i).sendMessage("NumJogadores: " + players.size() + " Jogador: # " + playerID + " conectado");
                }
                while(true){
                    if((string = receiveMessage()) != null) {
                        System.out.println(string);
                        // sendMessage("Recebi sua mensagem, jogador #" + string.charAt(8) + "!");
                        double attack;
                        int playerToMove;
                        if(serverState == 1){
                            playerToMove = Integer.parseInt(string.split(" ")[3]);
                            attack = Double.parseDouble(string.split(" ")[4]);
                            for(int i = 0; i < players.size(); i++) {
                                players.get(i).sendMessage(playerToMove + " " + attack);
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                System.out.println("IOException from run() ServerSideConnection: " + ex);
            }
        }

        public void sendMessage(String string) {
            try {
                String sd = "Servidor diz: ";
                printStream.println(sd + string);
                printStream.flush();
            } catch (Exception ex) {
                System.out.println("IOException from sendButtonNumber() in ClientSideConnection.");
            }
        }
    
        public String receiveMessage() {
    
            String string = "-1";
            try {
                string = bufferedReader.readLine();
            } catch (IOException ex) {
                System.out.println("IOException from receiveButtonNumber() in ClientServerConection.");
            }
            return string;
        }

        // public void closeConnection() {
        //     try {
        //         serverSocket.close();
        //         socket.close();
        //         System.out.println("Connection closed.");
        //     } catch (IOException ex) {
        //         System.out.println("IOException on closeConnect() in ServerSideConnection.");
        //     }
        // }
        
    }
}