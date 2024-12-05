package tictactoe;
import java.io.*;
import java.io.PrintWriter;
import java.io.IOException;

public class Game {
		
		private int[][] board = new int[3][3];
	    private int currentPlayer = 0;
	    private BufferedReader player1In;
	    private BufferedReader player2In;
	    private PrintWriter player1Out;
	    private PrintWriter player2Out;
	    
	    public Game(BufferedReader player1In, BufferedReader player2In, PrintWriter player1Out, PrintWriter player2Out) {
	        this.player1In = player1In;
	        this.player2In = player2In;
	        this.player1Out = player1Out;
	        this.player2Out = player2Out;
	    }

	    public void startGame(PrintWriter[] playerOuts) {
    	
	        for (PrintWriter out : playerOuts) {
	            out.println("Board:, , , , , , , , ,");
	        }
	        while (true) {
	        	if (currentPlayer==0) { player2Out.println("Don't move.");
	        	player1Out.println("Can move.");
	        	}
	        	else {player1Out.println("Don't move.");
	        	player2Out.println("Can move.");
	        	}
	        	
	            String move = readMoveFromCurrentPlayer();

	            if (makeMove(move, currentPlayer == 0 ? 'X' : 'O')) {
	                sendBoardToPlayers();
	                if (checkWin(currentPlayer == 0 ? 'X' : 'O')) {
	                    sendMessageToPlayers(playerOuts, "Player" + (currentPlayer+1)+" win!");
	                    break;
	                }
	                if (isDraw()) {
	                    sendMessageToPlayers(playerOuts, "It's a draw! Would you like to play again?");
	                    break;
	                }
	                currentPlayer = (currentPlayer + 1) % 2;
	            }
	        }
	    }
	    
	    private String readMoveFromCurrentPlayer() {
	    	BufferedReader in = currentPlayer == 0 ? player1In : player2In;
	        PrintWriter out = currentPlayer == 0 ? player1Out : player2Out;
	        
	        String move;
	        try {
	            move = in.readLine();
	        } catch (IOException e) {
	            out.println("Error reading input. Please try again.");
	            return readMoveFromCurrentPlayer(); 
	        }
	        return move;
	     }
	   
	    
	    private boolean makeMove(String move, char symbol) {
	    	int row = Integer.parseInt(move.split(",")[0]);
	        int col = Integer.parseInt(move.split(",")[1]);
	        if (row < 0 || row > 2 || col < 0 || col > 2) {
	            return false; 
	        }
	        if (board[row][col] != 0) {
	            return false; 
	        }
	        board[row][col] = symbol == 'X' ? 1 : 10;
	        sendBoardToPlayers();
	        return true;
	    }
	  
	    private boolean checkWin(char symbol) {
	    	int check = currentPlayer == 0 ? 1 : 10;
	    	for(int i = 0;i<3;i++) {
	    		int row = 0;
	    		int column = 0;
	    		for (int j = 0; j<3;j++) {
	    			row += board[i][j];
	    			column += board[j][i];
	    		}
	    		if (row == 3*check || column == 3*check) {
	    			return true;
	    		}
	    	}
	    	int slash1 = 0;
	    	int slash2 = 0;
	    	for(int i =0;i<3;i++) {
	    		slash1 += board[i][i];
	    		slash2 += board[i][2-i];
	    	}
	    	if(slash1 == 3*check || slash2 == 3*check ) {
	    		return true;
	    	}
	    	return false;
	    }
	    
	    private boolean isDraw() {
	    	for(int i= 0;i<3;i++) {
	    		for(int j =0;j<3;j++) {
	    			if (board[i][j] == 0) {
	    				return false;
	    			}
	    		}
	    	}
	    	return true;
	    }
	    private void sendBoardToPlayers() {
	    	String boardString = "";
	        for (int i = 0; i < 3; i++) {
	            for (int j = 0; j < 3; j++) {
	                boardString += (board[i][j] == 0 ? "" : board[i][j] == 1 ? "X" : "O") + ",";
	            }
	            boardString += " ";
	        }

	        player2Out.println("Board:" + boardString);
	        player1Out.println("Board:" + boardString);

	    }
	    
	    private void sendMessageToPlayers(PrintWriter[] playerOuts, String message) {
	    	if(message.contains("1")) {
	    		player1Out.println("winplayer1");
	    		player2Out.println("winplayer1");
		    	player1Out.println("Congratulations! You win. Would you like to play again?");
		        player2Out.println("You lose. Would you like to play again?");
	    	}
	    	else if(message.contains("2")) {
	    		player1Out.println("winplayer2");
	    		player2Out.println("winplayer2");
	    		player2Out.println("Congratulations! You win. Would you like to play again?");
		        player1Out.println("You lose. Would you like to play again?");
	    	}
	    	else {
	    		player1Out.println(message);
		        player2Out.println(message);
	    	}
	    }
	

	}


