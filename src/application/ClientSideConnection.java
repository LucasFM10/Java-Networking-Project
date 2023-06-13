package application;

import java.io.*;
import java.net.*;

public class ClientSideConnection {
        
    private Socket socket;

    private BufferedReader bufferedReader;
    private PrintStream printStream;

    public int playerID;

    public ClientSideConnection (String ip, int porta) {
        System.out.println("---Client---");
        try {
            
            socket = new Socket(ip, porta);

            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printStream = new PrintStream(socket.getOutputStream());
            
        } catch(IOException ex) {
            System.out.println("IOException from ClientSideConnection constructor.");
        }
    }

    public void closeConnection() {
        try {
            socket.close();
            System.out.println("---CONNECTION CLOSED---");
        } catch (IOException ex) {
            System.out.println("IOException on closeConnection() in ClienteServerConnection.");
        }
    }

    public void sendMessage(String string) {
        try {
            String pd = "Player #" + playerID + " diz: ";
            printStream.println(pd + string);
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
            System.out.println("IOException from receiveMessage() in ClientServerConection.");
        }
        return string;
    }

}