package tictactoe;
import java.io.*;
import java.net.*;


public class TicTacToeServer {
    private int PORT = 45000;
    private PrintWriter[] playerOuts = new PrintWriter[2];
    private BufferedReader[] playerIns = new BufferedReader[2];
    boolean nameSubmitted1 = false;
    boolean nameSubmitted2 = false;
    boolean exit1 = false;
    boolean exit2 = false;
    boolean restartRequested = false;

    public void start() {
    	try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started, waiting for players...");
            Socket player1Socket = serverSocket.accept();
            Socket player2Socket = serverSocket.accept();
            playerOuts[0] = new PrintWriter(player1Socket.getOutputStream(), true);
            playerOuts[1] = new PrintWriter(player2Socket.getOutputStream(), true);
            try (InputStreamReader isr1 = new InputStreamReader(player1Socket.getInputStream());
                 InputStreamReader isr2 = new InputStreamReader(player2Socket.getInputStream());
                 BufferedReader playerIn1 = new BufferedReader(isr1);
                 BufferedReader playerIn2 = new BufferedReader(isr2)) {
                playerIns[0] = playerIn1;
                playerIns[1] = playerIn2;
                Game game = new Game(playerIns[0], playerIns[1], playerOuts[0], playerOuts[1]);
                while (!(nameSubmitted1 && nameSubmitted2)) {
                    String line1 = playerIns[0].readLine();
                    if (line1 != null && line1.startsWith("NAME_SUBMITTED")) {
                        nameSubmitted1 = true;
                    }
                    if(line1 != null && line1.startsWith("EXIT_GAME")) {
                    	playerOuts[1].println("EXIT_GAME");
                    }
                    String line2 = playerIns[1].readLine();
                    if (line2 != null && line2.startsWith("NAME_SUBMITTED")) {
                        nameSubmitted2 = true;
                    }
                    if(line2 != null && line2.startsWith("EXIT_GAME")) {
                    	playerOuts[0].println("EXIT_GAME");
                    }
                }
                while(!(exit1)) {
                	game.startGame(playerOuts);
                	String line1 = playerIns[0].readLine();
                	if (line1 != null && line1.startsWith("EXIT_GAME")) {
                        exit1 = true;
                        playerOuts[1].println("EXIT_GAME");
                    }
                    String line2 = playerIns[1].readLine();
                    if (line2 != null && line2.startsWith("EXIT_GAME")) {
                        exit1 = true;
                        playerOuts[0].println("EXIT_GAME");
                    }
                    if (line1 != null && line1.startsWith("RESTART") || line2 != null && line2.startsWith("RESTART")) {
                        restartRequested = true;
                    }
                    
                    if (restartRequested) {
                        exit1 = false;
                        exit2 = false;
                        restartRequested = false;
                        game = new Game(playerIns[0], playerIns[1], playerOuts[0], playerOuts[1]);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    
    public static void main(String[] args) {
        TicTacToeServer server = new TicTacToeServer();
        server.start();
    }
}
