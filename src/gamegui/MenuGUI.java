package gamegui;

import application.*;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.net.*;

public class MenuGUI {

    App app;

    public MenuGUI(App app) {
        this.app = app;
    }

    public void showMenu() {

        VBox menuVBox = new VBox();

        // Título do jogo
        Text titleText = new Text("Corrida dos Unicórnios");
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        // Botões
        Button conectarButton = new Button("Conectar\nao Servidor");
        conectarButton.setPrefSize(100, 100);
        conectarButton.setWrapText(true);
        conectarButton.setTextAlignment(TextAlignment.CENTER);
        conectarButton.setOnAction(e -> exibirDialogoConectar(e));

        Button criarServidorButton = new Button("Criar\nServidor");
        criarServidorButton.setPrefSize(100, 100);
        criarServidorButton.setWrapText(true);
        criarServidorButton.setTextAlignment(TextAlignment.CENTER);
        criarServidorButton.setOnAction(e -> exibirDialogoCriarServidor(e));

        // Layout dos botões
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(conectarButton, criarServidorButton);

        // Layout principal
        menuVBox.setSpacing(20);
        menuVBox.setPadding(new Insets(20));
        menuVBox.setAlignment(Pos.CENTER);
        menuVBox.getChildren().addAll(titleText, buttonBox);

        this.app.getStage().setScene(new Scene(menuVBox, 400, 300));
    }

    public void exibirDialogoCriarServidor(ActionEvent e) {
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

                        exibirDialogoNickname(e);

                        try {
                            InetAddress ip = InetAddress.getLocalHost();
                            System.out.println("O endereço IP da máquina local é: " + ip.getHostAddress());

                            this.app.setServer(true);
                            this.app.setIp(ip.getHostAddress());
                        } catch (UnknownHostException ex) {
                            ex.printStackTrace();
                        }

                        this.app.setPorta(porta);

                        Thread createServer = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                MenuGUI.this.app.createServer(Integer.parseInt(porta));
                            }
                        });
                        createServer.start();

                        Thread connectServer = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                MenuGUI.this.app.connectToServer("localhost", Integer.parseInt(porta));
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

    private void exibirDialogoConectar(ActionEvent e) {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20));

        TextField ipTextField = new TextField();
        ipTextField.setText("127.0.1.1");
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

                    exibirDialogoNickname(e);

                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            MenuGUI.this.app.connectToServer(ip, Integer.parseInt(porta));
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

    private void exibirDialogoNickname(ActionEvent e) {

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20));

        TextField nickNameTextField = new TextField();

        gridPane.add(new Text("Nickname:"), 0, 0);
        gridPane.add(nickNameTextField, 1, 0);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Corrida Maluca");
        alert.setHeaderText(null);
        alert.getDialogPane().setContent(gridPane);
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        alert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                String nickName = nickNameTextField.getText().trim();

                if (validarNickname(nickName)) {
                    this.app.setNickName(nickName);
                } else {
                    exibirMensagemErro("Digite um nickname válido.");
                    exibirDialogoNickname(e); // Chamada recursiva para exibir o diálogo novamente
                }
            }
        });
    }

    private boolean validarIP(String ip) {
        // Utilize uma expressão regular para verificar se o IP é válido
        String ipRegex = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
        return ip.matches(ipRegex);
    }
    
    private boolean validarNickname(String nickNameString) {
        return nickNameString.length() < 20;
    }

    private boolean validarPorta(String porta) {
        // Verificar se a porta é um número válido
        try {
            int portaInt = Integer.parseInt(porta);
            return portaInt >= 0 && portaInt <= 65535;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean verificarPortaDisponivel(int porta) {
        try {
            // Tenta criar um ServerSocket na porta especificada
            ServerSocket serverSocket = new ServerSocket(porta);
            serverSocket.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private void exibirMensagemErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}