package tictactoe;
import java.io.*;
import java.net.*;

public class TicTacToeServer {
    private int PORT = 45000;
    private PrintWriter[] playerOuts = new PrintWriter[2];
    private BufferedReader[] playerIns = new BufferedReader[2];
    private volatile boolean exit1 = false;
    private volatile boolean exit2 = false;
    private boolean restartRequested = false;

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started, waiting for players...");
            Socket player1Socket = serverSocket.accept();
            Socket player2Socket = serverSocket.accept();
            playerOuts[0] = new PrintWriter(player1Socket.getOutputStream(), true);
            playerOuts[1] = new PrintWriter(player2Socket.getOutputStream(), true);

            try{{playerIns[0] = new BufferedReader(new InputStreamReader(player1Socket.getInputStream()));
            playerIns[1] = new BufferedReader(new InputStreamReader(player2Socket.getInputStream()));

            Thread player1Thread = new Thread(() -> handlePlayerInput(0));
            Thread player2Thread = new Thread(() -> handlePlayerInput(1));

            player1Thread.start();
            player2Thread.start();

            Game game = new Game(playerIns[0], playerIns[1], playerOuts[0], playerOuts[1]);

            while (!exit1 && !exit2) {
                game.startGame(playerOuts);

                synchronized (this) {
                    if (restartRequested) {
                        restartRequested = false;
                        game = new Game(playerIns[0], playerIns[1], playerOuts[0], playerOuts[1]);
                    }
                }
            }

            player1Thread.join();
            player2Thread.join();
        }}catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handlePlayerInput(int playerIndex) {
        try {
            String line;
            while ((line = playerIns[playerIndex].readLine()) != null) {
                System.out.println("Player " + (playerIndex + 1) + ": " + line);
                if (line.startsWith("EXIT_GAME")) {
                    exitGame(playerIndex);
                    break;
                }
                if (line.startsWith("RESTART")) {
                    synchronized (this) {
                        restartRequested = true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void exitGame(int playerIndex) {
        if (playerIndex == 0) {
            exit1 = true;
            playerOuts[1].println("EXIT_GAME");
        } else {
            exit2 = true;
            playerOuts[0].println("EXIT_GAME");
        }
    }

    public static void main(String[] args) {
        TicTacToeServer server = new TicTacToeServer();
        server.start();
    }
}
