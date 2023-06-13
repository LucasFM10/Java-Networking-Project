package controllers;

import java.io.IOException;
import java.net.ServerSocket;

import application.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class MenuSceneController {

    App app;

    public void setApp(App app) {
        this.app = app;
    }

    @FXML
    private Text titleText;

    public void initialize() {
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
    }

    @FXML
    protected void exibirDialogoCriarServidor(ActionEvent e) {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20));

        TextField portaTextField = new TextField();
        portaTextField.setText("8888");
        portaTextField.setPrefWidth(100); // Definindo a largura preferencial do TextField

        gridPane.add(new Text("Porta:"), 0, 0);
        gridPane.add(portaTextField, 1, 0);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Criar Servidor");
        alert.setHeaderText(null);
        alert.getDialogPane().setContent(gridPane);
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        alert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                String porta = portaTextField.getText().trim();

                if (validarPorta(porta)) {
                    if (verificarPortaDisponivel(Integer.parseInt(porta))) {

                        this.app.setServer(true);

                        Thread createServer = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                MenuSceneController.this.app.createServer(Integer.parseInt(porta));
                            }
                        });
                        createServer.start();

                        Thread connectServer = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                MenuSceneController.this.app.connectToServer("localhost", Integer.parseInt(porta));
                            }
                        });
                        connectServer.start();

                    } else {
                        exibirMensagemErro("A porta não está disponível para uso.");
                    }
                } else {
                    exibirMensagemErro("Digite uma porta válida.");
                    exibirDialogoCriarServidor(e); // Chamada recursiva para exibir o diálogo novamente
                }
            }
        });
    }

    @FXML
    protected void exibirDialogoConectar(ActionEvent e) {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20));

        TextField ipTextField = new TextField();
        ipTextField.setText("192.168.0.23");
        TextField portaTextField = new TextField();
        portaTextField.setText("8888");

        gridPane.add(new Text("IP:"), 0, 0);
        gridPane.add(ipTextField, 1, 0);
        gridPane.add(new Text("Porta:"), 0, 1);
        gridPane.add(portaTextField, 1, 1);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conectar ao Servidor");
        alert.setHeaderText(null);
        alert.getDialogPane().setContent(gridPane);
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        alert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                String ip = ipTextField.getText().trim();
                String porta = portaTextField.getText().trim();

                if (validarIP(ip) && validarPorta(porta)) {

                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            MenuSceneController.this.app.connectToServer(ip, Integer.parseInt(porta));
                        }
                    });
                    t.start();

                    // System.out.println("Conectar ao servidor: IP=" + ip + ", Porta=" + porta);
                } else {
                    exibirMensagemErro("Digite um IP e uma porta válidos.");
                    exibirDialogoConectar(e); // Chamada recursiva para exibir o diálogo novamente
                }
            }
        });
    }

    @FXML
    protected boolean validarPorta(String porta) {
        // Verificar se a porta é um número válido
        try {
            int portaInt = Integer.parseInt(porta);
            return portaInt >= 0 && portaInt <= 65535;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @FXML
    protected boolean verificarPortaDisponivel(int porta) {
        try {
            // Tenta criar um ServerSocket na porta especificada
            ServerSocket serverSocket = new ServerSocket(porta);
            serverSocket.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @FXML
    protected boolean validarIP(String ip) {
        // Utilize uma expressão regular para verificar se o IP é válido
        String ipRegex = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
        return ip.matches(ipRegex);
    }

    @FXML
    protected void exibirMensagemErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
