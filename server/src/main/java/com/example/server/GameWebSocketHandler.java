package com.example.server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class GameWebSocketHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(GameWebSocketHandler.class);
    private static final String LOG_FILE = "server_log.txt";
    private final Map<String, WebSocketSession> players = new HashMap<>();
    private final Queue<WebSocketSession> waitingPlayers = new LinkedList<>();
    private final Map<WebSocketSession, WebSocketSession> activeGames = new HashMap<>();
    private final Map<WebSocketSession, String> playerChoices = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        players.put(session.getId(), session);
        waitingPlayers.add(session);
        logEvent("Player connected: " + session.getId());
        matchPlayers();
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        logEvent("Received message from " + session.getId() + ": " + payload);
        WebSocketSession opponent = activeGames.get(session);

        if (opponent != null && opponent.isOpen()) {
            playerChoices.put(session, payload);

            if (playerChoices.containsKey(opponent)) {
                determineWinner(session, opponent);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        players.remove(session.getId());
        waitingPlayers.remove(session);
        logEvent("Player disconnected: " + session.getId());
        WebSocketSession opponent = activeGames.remove(session);
        playerChoices.remove(session);
        if (opponent != null) {
            activeGames.remove(opponent);
            playerChoices.remove(opponent);
            opponent.close();
            logEvent("Game ended due to player disconnect: " + session.getId() + " vs " + opponent.getId());
        }
    }

    private void matchPlayers() throws IOException {
        if (waitingPlayers.size() >= 2) {
            WebSocketSession player1 = waitingPlayers.poll();
            WebSocketSession player2 = waitingPlayers.poll();

            activeGames.put(player1, player2);
            activeGames.put(player2, player1);

            logEvent("Matched players: " + player1.getId() + " vs " + player2.getId());
            player1.sendMessage(new TextMessage("MATCHED"));
            player2.sendMessage(new TextMessage("MATCHED"));
        }
    }

    private void determineWinner(WebSocketSession player1, WebSocketSession player2) throws IOException {
        String choice1 = playerChoices.get(player1);
        String choice2 = playerChoices.get(player2);

        String result;
        if (choice1.equals(choice2)) {
            result = "DRAW";
        } else if ((choice1.equals("rock") && choice2.equals("scissors")) ||
                (choice1.equals("scissors") && choice2.equals("paper")) ||
                (choice1.equals("paper") && choice2.equals("rock"))) {
            result = "PLAYER 1 WINS";
        } else {
            result = "PLAYER 2 WINS";
        }

        logEvent("Game result: " + player1.getId() + " (" + choice1 + ") vs " + player2.getId() + " (" + choice2 + "): " + result);

        player1.sendMessage(new TextMessage("RESULT: " + result));
        player2.sendMessage(new TextMessage("RESULT: " + result));

        playerChoices.remove(player1);
        playerChoices.remove(player2);
        activeGames.remove(player1);
        activeGames.remove(player2);

        waitingPlayers.add(player1);
        waitingPlayers.add(player2);
        matchPlayers();
    }

    private void logEvent(String message) {
        logger.info(message);
        try {
            Files.write(Paths.get(LOG_FILE), (message + "\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            logger.error("Error writing to log file", e);
        }
    }
}
