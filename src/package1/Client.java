package package1;
import java.io.*;
import java.net.*;

public class Client {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 4000);
    
        ClientThread1 clientThread1 = new ClientThread1(socket);
        clientThread1.start();
        ClientThread2 clientThread2 = new ClientThread2(socket);
        clientThread2.start();
    }
}
