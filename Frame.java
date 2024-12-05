package tictactoe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;


		
public class Frame extends JFrame {
    private TicTacToeClient client;
    private JFrame frame;
	private JButton submitButton;
	private JLabel statusLabel;
	private JLabel timeLabel;
	private JButton[][] gameBoardButtons = new JButton[3][3];
	private JLabel player1ScoreLabel;
	private JLabel player2ScoreLabel;
	private JLabel drawsLabel;
	private JPanel scorePanel;
	private JTextField textField;
	  
	private int player1Score = 0;
	private int player2Score = 0;
	private int draws = 0;
	private int Move = 0;
	private boolean nameSubmitted =  false;
	private boolean meExit = false;
	private boolean canMove = true;
	
    public Frame(TicTacToeClient client) {
    	this.client = client;
        setupGUI();
    }
    
    private void setupGUI() {
    	frame = new JFrame();
        frame.setTitle("Tic Tac Toe");
        frame.setSize(700, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Create menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu controlMenu = new JMenu("Control");
        JMenu helpMenu = new JMenu("Help");
        JMenuItem exitItem = new JMenuItem("Exit");
        JMenuItem instructionItem = new JMenuItem("Instruction");
        exitItem.addActionListener(e -> {
        	meExit = true ;
        	client.sendExitMessage();
        	System.exit(0);

        	}); // Exit the game
        instructionItem.addActionListener(e -> showInstructions());
        controlMenu.add(exitItem);
        helpMenu.add(instructionItem);
        menuBar.add(controlMenu);
        menuBar.add(helpMenu);
        frame.setJMenuBar(menuBar);
        frame.getContentPane().setLayout(new GridBagLayout());

        statusLabel = new JLabel("Enter your player name...", SwingConstants.CENTER);
        GridBagConstraints gbc_welcomeLabel = new GridBagConstraints();
        gbc_welcomeLabel.insets = new Insets(0, 0, 5, 5);
        gbc_welcomeLabel.gridx = 1;
        gbc_welcomeLabel.gridy = 0;
        frame.getContentPane().add(statusLabel, gbc_welcomeLabel);
        
        JPanel welcomePanel = new JPanel(new GridLayout(3,0));
        GridBagConstraints welcomePanelConstraints = new GridBagConstraints();
        welcomePanelConstraints.insets = new Insets(0, 0, 5, 5);
        welcomePanelConstraints.gridx = 1;
        welcomePanelConstraints.gridy = 6;
        frame.getContentPane().add(welcomePanel, welcomePanelConstraints);
        
        // Score panel
        JPanel scorePanel = new JPanel(new BorderLayout());
        scorePanel.setBorder(BorderFactory.createLineBorder(Color.blue, 2));
        JLabel scoreTitleLabel = new JLabel("Score");
        scoreTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scorePanel.add(scoreTitleLabel, BorderLayout.NORTH);
        player1ScoreLabel = new JLabel("Player 1 Wins: " + player1Score);
        player2ScoreLabel = new JLabel("Player 2 Wins: " + player2Score);
        drawsLabel = new JLabel("Draws: " + draws);
        JPanel scoreContentPanel = new JPanel(new GridLayout(3, 1));
        scoreContentPanel.add(player1ScoreLabel);
        scoreContentPanel.add(player2ScoreLabel);
        scoreContentPanel.add(drawsLabel);

        scorePanel.add(scoreContentPanel, BorderLayout.CENTER); 
        GridBagConstraints scorePanelConstraints = new GridBagConstraints();
        scorePanelConstraints.insets = new Insets(0, 0, 5, 0);
        scorePanelConstraints.gridx = 2;
        scorePanelConstraints.gridy = 6;
        scorePanelConstraints.gridheight = 10; 
        frame.getContentPane().add(scorePanel, scorePanelConstraints);
        
     // Game board panel
        JPanel gameBoardPanel = new JPanel(new GridLayout(3, 3));
        gameBoardButtons = new JButton[3][3];
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                gameBoardButtons[i][j] = new JButton("");
                gameBoardButtons[i][j].setBackground(Color.WHITE);
                gameBoardButtons[i][j].setRolloverEnabled(false);
                gameBoardButtons[i][j].setFocusPainted(false);
                gameBoardButtons[i][j].setPreferredSize(new Dimension(100, 100));
                Font currentFont = gameBoardButtons[i][j].getFont();
                Font newFont = new Font(currentFont.getName(), currentFont.getStyle(), 30); // Set font size to 30
                gameBoardButtons[i][j].setFont(newFont);
                gameBoardPanel.add(gameBoardButtons[i][j]);

                final int row = i;
                final int col = j;
                gameBoardButtons[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (nameSubmitted && canMove) {
                            client.sendMove(row+","+col);
                            Move++;
                        }
                    }
                });
            }
        }
        
        GridBagConstraints gamePanelConstraints = new GridBagConstraints();
        gamePanelConstraints.insets = new Insets(0, 0, 5, 5);
        gamePanelConstraints.gridx = 1;
        gamePanelConstraints.gridy = 6;
        frame.getContentPane().add(gameBoardPanel, gamePanelConstraints);
        // Panel for player name input and submit button
        
        JLabel enterNameLabel = new JLabel("Enter your name:");
        GridBagConstraints enterNameLabelConstraints = new GridBagConstraints();
        enterNameLabelConstraints.insets = new Insets(0, 0, 5, 5);
        enterNameLabelConstraints.gridx = 0;
        enterNameLabelConstraints.gridy = 16; // Set the same row as the text field
        frame.getContentPane().add(enterNameLabel, enterNameLabelConstraints);
        
        textField = new JTextField();
        textField.setPreferredSize(new Dimension(200, 30));
        GridBagConstraints textFieldConstraints = new GridBagConstraints();
        textFieldConstraints.gridx = 1; // Set the column next to the label
        textFieldConstraints.gridy = 16;
        textFieldConstraints.insets = new Insets(0, 0, 5, 5);
        frame.getContentPane().add(textField, textFieldConstraints);


        submitButton = new JButton("Submit");
        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.insets = new Insets(0, 0, 5, 0);
        buttonConstraints.gridx = 2;
        buttonConstraints.gridy = 16;
        frame.getContentPane().add(submitButton, buttonConstraints);
        
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!nameSubmitted && !textField.getText().isEmpty()) {
                    String playerName = textField.getText();
                    frame.setTitle("Tic Tac Toe - Player: " + playerName);
                    client.submitPlayerName(playerName);
                    submitButton.setEnabled(false); // Disable the submit button after name is submitted
                    nameSubmitted = true;
                    statusLabel.setText("WELCOME " + playerName); // Update the text of the label 
                    frame.revalidate();
            
                }
            }
        });
        
        timeLabel = new JLabel();
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints timeLabelConstraints = new GridBagConstraints();
        timeLabelConstraints.insets = new Insets(0, 0, 0, 5);
        timeLabelConstraints.gridx = 0;
        timeLabelConstraints.gridy = 30;
        timeLabelConstraints.gridwidth = 2;
//        timeLabelConstraints.anchor = GridBagConstraints.SOUTH;
        frame.getContentPane().add(timeLabel, timeLabelConstraints);

        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTime();
            }
        });
        timer.start();
        frame.setVisible(true);
    }
    private void showInstructions() {
        JOptionPane.showMessageDialog(frame,
                "Criteria for a valid move:\r\n"
                + "- The move is not occupied by any mark.\r\n"
                + "- The move is made in the player’s turn.\r\n"
                + "- The move is made within the 3 x 3 board.\r\n"
                + "The game continues and switches among the opposite players until it reaches either:\r\n"
                + "- Player 1 wins.\r\n"
                + "- Player 2 wins.\r\n"
                + "- A draw.", "Instructions", JOptionPane.INFORMATION_MESSAGE);
    }
    private void updateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        timeLabel.setText("Current Time: " + sdf.format(date));
    }
        
    
        
    
 
    public void update(String line) {
        // Update the game board buttons based on the received message
    	if(line.startsWith("Don't move.")) {
    		canMove = false;
    		if(Move!=0) {
    		statusLabel.setText("Valid move, wait for your opponent.");}
    	}
    	else if(line.startsWith("Can move.")) {
    		canMove = true;
    		if(Move!=0) {
    		statusLabel.setText("Your opponent has moved. Now is your turn.");}
    	}
        if (line.startsWith("Board:")) {
            String[] cells = line.substring(6).split(",");
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    gameBoardButtons[i][j].setText(cells[i * 3 + j]);
                }
            }
        }
        if (line.equals("EXIT_GAME")&&!meExit) {
        	JOptionPane.showMessageDialog(frame, "Game Ends. One of the players left.","Game Over", JOptionPane.INFORMATION_MESSAGE);
        	System.exit(0);
        }
        if (line.contains("winplayer")||line.contains("draw")) {
        	if(line.contains("1")){
        		player1Score ++;
        		player1ScoreLabel.setText("Player Wins: " + player1Score);
        	}
        	else if(line.contains("2")){
        		player2Score ++;
        		player2ScoreLabel.setText("Player Wins: " + player2Score);
        	}
        	else {
        		draws ++ ;
        		drawsLabel.setText("Draws: " + draws);
        	}
        }
        if(line.contains("Would you like to play again?")) {
        	Move = 0;
        	String[] options = {"Restart","Exit"};
        	int choice = JOptionPane.showOptionDialog(frame,line,"Game over",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        	if (choice == 0) {
                client.sendRestart();
            } else if (choice == 1) {
            	meExit = true ;
            	client.sendExitMessage();
                System.exit(0);
            }
        }
        }
    
}

