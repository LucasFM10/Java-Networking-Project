package package1;
import java.net.*;
import java.io.*;

public class ClientThread1 extends Thread {

    private Socket socket;

    public ClientThread1(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            
            String x;
            do {
                InputStreamReader inputReader = new InputStreamReader(socket.getInputStream());
                BufferedReader reader = new BufferedReader(inputReader);
                while((x = reader.readLine()) != null) {
                    System.out.println("Client: " + x);
                }
            } while (true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}