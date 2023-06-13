package application;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Teste {
    public static void main(String[] args) {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            System.out.println("O endereço IP da máquina local é: " + ip.getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
