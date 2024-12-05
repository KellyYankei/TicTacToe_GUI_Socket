package tictactoe;

import java.io.*;
import java.net.*;

public class TicTacToeClient {
	private int PORT = 45000;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Frame gameFrame;
    
    public TicTacToeClient(String hostname) {
        try {
            socket = new Socket(hostname, PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            gameFrame = new Frame(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void start() {
        new Thread(new Receiver()).start();
    }
    private class Receiver implements Runnable {
        @Override
        public void run() {
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    gameFrame.update(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void submitPlayerName(String playerName) {
        out.println("NAME_SUBMITTED");
    }
    public void sendMove(String move) {
        out.println(move);
    }
    public void sendExitMessage() {
        out.println("EXIT_GAME");
    }
    
    public void sendRestart() {
    	out.println("RESTART");
    }
    
    public static void main(String[] args) {
        TicTacToeClient client = new TicTacToeClient("localhost"); // replace localhost with the server hostname
        client.start();
    }
        
    }

