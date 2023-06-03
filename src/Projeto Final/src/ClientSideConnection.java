import java.io.*;
import java.net.*;

public class ClientSideConnection {
        
    private Socket socket;
    
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    private BufferedReader bufferedReader;
    private PrintStream printStream;

    private int playerID;

    public ClientSideConnection () {
        System.out.println("---Client---");
        try {
            socket = new Socket("localhost", 51734);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());

            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printStream = new PrintStream(socket.getOutputStream());
            playerID = dataInputStream.readInt();
            System.out.println("Connected to server as Player #" + playerID + ".");
            // this.sendMessage("Olá, servidor!");
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
            System.out.println("Player #outro clicked button #" + number + ".");
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
            System.out.println("IOException from receiveButtonNumber() in ClientServerConection.");
        }
        return string;
    }

}