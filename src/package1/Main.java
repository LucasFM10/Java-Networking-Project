package package1;
import java.io.*;
import java.net.*;

public class Main {
    
    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(4000);
        Socket socket = serverSocket.accept();
        System.out.println("Cliente conectou");

        InputStreamReader inputReader = new InputStreamReader(socket.getInputStream());
        PrintStream saida = new PrintStream(socket.getOutputStream());
        BufferedReader reader = new BufferedReader(inputReader);
        String x;
        while((x = reader.readLine()) != null) {
            saida.println("Servidor: " + x);
        }
        serverSocket.close();
    }
}
