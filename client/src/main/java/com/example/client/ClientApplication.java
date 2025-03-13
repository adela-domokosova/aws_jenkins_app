package com.example.client;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
public class ClientApplication extends Application {
    private WebSocketClient webSocketClient;
    private Label statusLabel;
    private TextField usernameField;
    private Button loginButton;
    private Button logoutButton;
    private VBox gameControls;
    private String username;

    @Override
    public void start(Stage primaryStage) {
        statusLabel = new Label("Enter username to connect");
        usernameField = new TextField();
        usernameField.setPromptText("Username");
        loginButton = new Button("Login");
        logoutButton = new Button("Logout");
        logoutButton.setVisible(false);

        gameControls = new VBox(10);
        gameControls.setAlignment(Pos.CENTER);
        gameControls.setVisible(false);

        Button rockButton = new Button("Rock");
        Button paperButton = new Button("Paper");
        Button scissorsButton = new Button("Scissors");

        rockButton.setOnAction(e -> sendChoice("rock"));
        paperButton.setOnAction(e -> sendChoice("paper"));
        scissorsButton.setOnAction(e -> sendChoice("scissors"));

        gameControls.getChildren().addAll(statusLabel, rockButton, paperButton, scissorsButton, logoutButton);

        loginButton.setOnAction(e -> {
            username = usernameField.getText().trim();
            if (username.isEmpty()) {
                statusLabel.setText("Username cannot be empty!");
                return;
            }
            connectToServer();
        });

        logoutButton.setOnAction(e -> disconnectFromServer());

        VBox loginBox = new VBox(10, statusLabel, usernameField, loginButton);
        loginBox.setAlignment(Pos.CENTER);

        VBox root = new VBox(20, loginBox, gameControls);
        root.setAlignment(Pos.CENTER);

        primaryStage.setTitle("Rock-Paper-Scissors Client");
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }

    private void connectToServer() {
        try {
            webSocketClient = new WebSocketClient(new URI("ws://localhost:8080/game")) {
                @Override
                public void onOpen(ServerHandshake handshake) {
                    webSocketClient.send("LOGIN:" + username);
                    statusLabel.setText("Connected as " + username + ". Waiting for opponent...");
                    gameControls.setVisible(true);
                    logoutButton.setVisible(true);
                }

                @Override
                public void onMessage(String message) {
                    statusLabel.setText("Server: " + message);
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    statusLabel.setText("Connection closed: " + reason);
                    gameControls.setVisible(false);
                    logoutButton.setVisible(false);
                }

                @Override
                public void onError(Exception ex) {
                    statusLabel.setText("Error: " + ex.getMessage());
                }
            };
            webSocketClient.connect();
        } catch (Exception e) {
            statusLabel.setText("Failed to connect to server.");
        }
    }

    private void sendChoice(String choice) {
        if (webSocketClient != null && webSocketClient.isOpen()) {
            webSocketClient.send("MOVE:" + choice);
            statusLabel.setText("You chose: " + choice);
        }
    }

    private void disconnectFromServer() {
        if (webSocketClient != null && webSocketClient.isOpen()) {
            webSocketClient.send("LOGOUT:" + username);
            webSocketClient.close();
        }
        statusLabel.setText("Disconnected");
        gameControls.setVisible(false);
        logoutButton.setVisible(false);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
