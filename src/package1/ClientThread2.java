package package1;
import java.net.*;
import java.util.Scanner;
import java.io.*;

public class ClientThread2 extends Thread {

    private Socket socket;

    public ClientThread2(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            Scanner scanner = new Scanner(System.in);
            PrintStream saida = new PrintStream(socket.getOutputStream());
            String teclado = scanner.nextLine();
            saida.println(teclado);
            scanner.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}