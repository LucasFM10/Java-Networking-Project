import java.io.*;
import java.net.*;
import java.util.Scanner;

public class GameServer {
    
    private ServerSocket serverSocket;
    private int numPlayers;
    private int gameState = 0;
    private ServerSideConnection[] players;

    public GameServer(int numPlayers) {

        System.out.println("----Game Server ----");
        this.numPlayers = numPlayers;
        players = new ServerSideConnection[numPlayers];

        try {
            serverSocket = new ServerSocket(51734);
        } catch(IOException ex) {
            System.out.println("IOException from GameServer Constructor");
        }
    }

    public void acceptConnections() {
        try {
            System.out.println("Waiting for connections...");
            for(int i = 1; i <= numPlayers; i++) {
                Socket socket = serverSocket.accept();
                ServerSideConnection serverSideConnection = new ServerSideConnection(socket, i);
                System.out.println("Player #" + (i) + " has connected.");
                players[i - 1] = serverSideConnection;
                Thread t = new Thread(serverSideConnection);
                t.start();
            }
            System.out.println("We now have " + this.numPlayers + " players. No longer accepting connections.");
            gameState = 1;
        } catch (IOException ex) {
            System.out.println("IOException from acceptConnections()");
        }
    }

    private class ServerSideConnection implements Runnable {

        private Socket socket;
        
        private DataInputStream dataInputStream;
        private DataOutputStream dataOutputStream;

        private BufferedReader bufferedReader;
        private PrintStream printStream;

        private int playerID;
        private int carsLocs[] = new int[numPlayers];

        public ServerSideConnection(Socket s, int id) {
            this.socket = s;
            this.playerID = id;
            try {
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());

                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                printStream = new PrintStream(socket.getOutputStream());
            } catch (IOException ex){
                System.out.println("IOException from ServerSideConnection constructor.");
            }
        }

        @Override
        public void run() {
            try {
                dataOutputStream.writeInt(playerID);
                dataOutputStream.flush();

                String string;

                sendMessage("NumJogadores: " + numPlayers + " Jogador: #" + playerID);

                while(true){
                    if((string = receiveMessage()) != null) {
                        System.out.println(string);
                        // sendMessage("Recebi sua mensagem, jogador #" + string.charAt(8) + "!");
                        double attack;
                        int playerToMove;
                        if(gameState == 1){
                            playerToMove = Integer.parseInt(string.substring(15, 16));
                            attack = Double.parseDouble(string.substring(17));
                            for(int i = 0; i < numPlayers; i++) {
                                players[i].sendMessage(playerToMove + " " + attack);
                            }
                        }
                    }
                }
            } catch (IOException ex) {
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
        System.out.println("Escreva quantos players são!");
        Scanner scan = new Scanner(System.in);
        int nP = scan.nextInt();
        scan.close();
        GameServer gameServer = new GameServer(nP);
        gameServer.acceptConnections();
    }
}
