/**
 * Stefan Pitigoi & Michael Tham
 * ICS4U, Mr.Benum
 * January 22, 2021
 * 
 * Referenced Websites:
 * https://docs.google.com/document/d/1lf1xsB8a3DSYhfEWGQnIHtZSweQsexpSHsr6DmW2_SE/edit#
 *     --> Used to embed audio into the JAR file
 */

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Font;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class TwoPlayerSnake extends Game{
	private final int DOT_SIZE = 20;
	private final int ACT_DELAY = 100;
	private final int INITIAL_LENGTH = 4;
	private final Color HEAD_ONE_COLOUR = Color.RED;
	private final Color HEAD_TWO_COLOUR = Color.BLUE;
	private final Color BODY_COLOUR = Color.GREEN;
	
	private int windowWidth, windowHeight;
	private int ALL_DOTS;
	private int lengthOne, lengthTwo;
	private SnakeTile[] snakeOne, snakeTwo;
	private Pellet orangeOne, orangeTwo, snakeOneScore, snakeTwoScore; 
	// snakeOneScore and snakeTwoScore are of type pellet because in 2 player games, the snake that eats the pellet must also be recorded, not just the amount of pellets eaten
	private JLabel scoreOne, scoreTwo;
	private JFrame endScreen;
	private boolean soundOn;
	
	private int[] p1cntrls, p2cntrls;
	
	private int p1VerticalDirection, p1HorizontalDirection = 1;
	private int p2VerticalDirection, p2HorizontalDirection = 1;
	
	

	/**
	 * 
	 * Constructor for the two player SnakeGame. Initializes variables that are used in the class
	 * 
	 * Pre: User chooses to play 2 player snake
	 * Post: A two player snake game is created
	 * 
	 * @param sizeX
	 * @param sizeY
	 * @param p1Controls
	 * @param p2Controls
	 * @param soundOn
	 */
	public TwoPlayerSnake(int sizeX, int sizeY, int[] p1Controls, int[] p2Controls, boolean soundOn) {
		super(sizeX, sizeY, p1Controls, p2Controls);
		windowWidth = sizeX;
		windowHeight = sizeY;
		p1cntrls = p1Controls;
		p2cntrls = p2Controls;
		this.soundOn = soundOn;
		this.setResizable(false);
	}
	
	/**
	 * 
	 * Plays the audio file for either the end sound or the audio played when eating a pellet
	 * 
	 * Pre: The snake either collides with a pellet, wall, or a snake tail
	 * Post: The correct audio is played
	 * 
	 * @param filePath
	 */
	public void playAudio(String filePath) {
		

		InputStream is = getClass().getResourceAsStream(filePath);
		BufferedInputStream bis = new BufferedInputStream(is); 
		AudioInputStream audioInput = null;
		try {
			audioInput = AudioSystem.getAudioInputStream(bis);
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Clip clip = null;
		try {
			clip = AudioSystem.getClip();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			clip.open(audioInput);
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		clip.start();
		
	}
	
	/**
	 * Draws the end screen when a player collides with a wall/snake tail. Virtual keypresses are input by a "robot" that resets the 
	 * keypresses before the next game is played. Players have the option to restart or play again.
	 * 
	 * Pre: A player loses
	 * Post: A screen appears with the winning player (the string "winner" holds the winning player) and also gives 
	 * 		 the option to quit or restart.
	 * 
	 * @param winner
	 */
	public void endScreen(String winner) {
		try {
			Robot robot = new Robot();
			for(int i=3; i>=0; i--) {
				robot.keyPress(p1cntrls[i]);
				robot.keyRelease(p1cntrls[i]);
				robot.keyPress(p2cntrls[i]);
				robot.keyRelease(p2cntrls[i]);
			}
			
			robot.keyPress(p1cntrls[1]);
			robot.keyRelease(p1cntrls[1]);
			robot.keyPress(p2cntrls[1]);
			robot.keyRelease(p2cntrls[1]);
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		stopGame();
		
		if (soundOn) {
		
			playAudio("resources/lose.wav");
			
		}
		
		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String command = event.getActionCommand();
				if (command.equals("restart")) {
					endScreen.dispose();
					restart();
				} else if (command.equals("quit")) {
					endScreen.dispose();
					System.exit(0);
				}
			}
		};
		
		endScreen = new JFrame();
		endScreen.setResizable(false);
		endScreen.setSize(300, 200);
		endScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		endScreen.getContentPane().setBackground(Color.BLACK);
		
		JLabel gameOver = new JLabel(winner, SwingConstants.CENTER);
		gameOver.setFont(gameOver.getFont ().deriveFont (30.0f));
		gameOver.setForeground(Color.WHITE);
		gameOver.setSize(700,30);
		gameOver.setLocation(endScreen.getWidth()/2 - gameOver.getWidth() / 2, 30);
		
		JButton restart = new JButton();
		restart.setText("RESTART");
		restart.setFont(restart.getFont ().deriveFont (15.0f));
		restart.setBackground(Color.WHITE);
		restart.setSize(125, 20);
		restart.setLocation(20, 100);
		restart.setBorderPainted(false);
		restart.setOpaque(true);
		restart.setActionCommand("restart");
		restart.addActionListener(actionListener);
		
		JButton quit = new JButton();
		quit.setText("QUIT");
		quit.setFont(quit.getFont ().deriveFont (15.0f));
		quit.setBackground(Color.WHITE);
		quit.setSize(125, 20);
		quit.setLocation(endScreen.getWidth()/2, 100);
		quit.setBorderPainted(false);
		quit.setOpaque(true);
		quit.setActionCommand("quit");
		quit.addActionListener(actionListener);
		
		endScreen.add(gameOver);
		endScreen.add(restart);
		endScreen.add(quit);
		
		endScreen.setLayout(null);
		endScreen.setLocationRelativeTo(null);
		endScreen.setVisible(true);
	}
	
	
	/**
	 * Removes the two snakes from the field and resets them to their initial locations. Both of the snakes scores and lengths are reset
	 * to the defaults.
	 * 
	 * Pre: The player clicks "restart" on the end screen
	 * Post: The game is reset as if they just opened the program for the first time
	 * 
	 */
	public void restart() {
		for (int x = 0; x < lengthOne; x++) {
			remove(snakeOne[x]);
		}
		for (int y = 0; y < lengthTwo; y++) {
			remove(snakeTwo[y]);
		}
		
		lengthOne = INITIAL_LENGTH;
		lengthTwo = INITIAL_LENGTH;
		snakeOneScore.resetEatenPellets();
		snakeTwoScore.resetEatenPellets();
		p1HorizontalDirection = 1;
		p1VerticalDirection = 0;
		p2HorizontalDirection = 1;
		p2VerticalDirection = 0;
			
		snakeOne[0] = new SnakeTile(DOT_SIZE, DOT_SIZE, 0, DOT_SIZE, HEAD_ONE_COLOUR);
		snakeTwo[0] = new SnakeTile(DOT_SIZE, DOT_SIZE, 0, windowHeight-(2*DOT_SIZE), HEAD_TWO_COLOUR);
		add(snakeOne[0]);
		add(snakeTwo[0]);
		
		for(int i=1; i<lengthOne; i++) {
			snakeOne[i] = new SnakeTile(DOT_SIZE, DOT_SIZE, 0 - i*DOT_SIZE, DOT_SIZE, BODY_COLOUR);
			snakeTwo[i] = new SnakeTile(DOT_SIZE, DOT_SIZE, 0 - i*DOT_SIZE, windowHeight-(2*DOT_SIZE), BODY_COLOUR);
			add(snakeOne[i]);
			add(snakeTwo[i]);
		}
		
		scoreOne.setText("Score: 0");
		scoreTwo.setText("Score: 0");
		repaint();
		startGame();
		
		try {
			Robot robot = new Robot();
			for(int i=3; i>=0; i--) {
				robot.keyPress(p1cntrls[i]);
				robot.keyRelease(p1cntrls[i]);
				robot.keyPress(p2cntrls[i]);
				robot.keyRelease(p2cntrls[i]);
			}
			
			robot.keyPress(p1cntrls[1]);
			robot.keyRelease(p1cntrls[1]);
			robot.keyPress(p2cntrls[1]);
			robot.keyRelease(p2cntrls[1]);
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Called the a snake collides with a pellet. The integer "snake" holds the value 1 or 2, and determines which snake ate the pellet.
	 * The int "orange" also holds a value of 1 or 2, and determines which orange was eaten (as there are two oranges that are on the field)
	 * Scores are updated accordingly and the pellets are redrawn.
	 * 
	 * Pre: A snake collides with a pellet.
	 * Post: Scores are updated accordingly and the pellet that was eaten is redrawn to a random coordinate on the screen.
	 * 
	 * @param snake
	 * @param orange
	 */
	public void updatePellet(int snake, int orange) {
		
		if(soundOn) {
			
			playAudio("resources/eatPellet.wav");
			
		}

		
		Random r = new Random();
		int xPos = (r.nextInt(windowWidth)/DOT_SIZE) * DOT_SIZE;
		int yPos = (r.nextInt(windowHeight)/DOT_SIZE) * DOT_SIZE;
		
		if (snake == 1) {
			
			snakeOneScore.pelletEaten();
			System.out.println(snakeOneScore.getEatenPellets());
			snakeOne[lengthOne] = new SnakeTile(DOT_SIZE, DOT_SIZE, snakeOne[lengthOne-1].getX()+DOT_SIZE, snakeOne[lengthOne-1].getY(), BODY_COLOUR);
			add(snakeOne[lengthOne]);
			lengthOne++;
			scoreOne.setText("Score: " + snakeOneScore.getEatenPellets());
			
			
		}
		
		if (snake == 2) {
			
			snakeTwoScore.pelletEaten();
			
			snakeTwo[lengthTwo] = new SnakeTile(DOT_SIZE, DOT_SIZE, snakeTwo[lengthTwo-1].getX()+DOT_SIZE, snakeTwo[lengthTwo-1].getY(), BODY_COLOUR);
			add(snakeTwo[lengthTwo]);
			lengthTwo++;
			scoreTwo.setText("Score: " + snakeTwoScore.getEatenPellets());
			
			
		}
		
		if (orange == 1) {
			remove(orangeOne);
			orangeOne = new Pellet(DOT_SIZE, DOT_SIZE, xPos, yPos, Color.ORANGE);
			add(orangeOne);
		}
		
		else if (orange == 2) {
			remove(orangeTwo);
			orangeTwo = new Pellet(DOT_SIZE, DOT_SIZE, xPos, yPos, Color.ORANGE);
			add(orangeTwo);
		}
		
		repaint();
		
	}
	
	/**
	 * Responsible for moving the snake in the correct direction based on the last keypress
	 * 
	 * Pre: The game is running (no one has lost)
	 * Post: The snake moves in the correct direction
	 * 
	 * @param snake
	 * @param directionX
	 * @param directionY
	 * @param length
	 */
	public void move(SnakeTile[] snake, int directionX, int directionY, int length) {
		
		for(int i=length-1; i>0; i--) {
			snake[i].setX(snake[i-1].getX());
			snake[i].setY(snake[i-1].getY());
		}
		
		if(directionX != 0) {
			snake[0].setX(snake[0].getX()+(directionX*DOT_SIZE));
		} else if (directionY != 0) {
			snake[0].setY(snake[0].getY()+(directionY*DOT_SIZE));
		}
		

	}
	
	/**
	 * Creates the playing field, along with the first instances of the two snakes and two pellets
	 * 
	 * Pre: A TwoPlayerSnake object is created
	 * Post: The first occurance of the game runs
	 * 
	 */
	public void setup() {
		ALL_DOTS = (windowWidth*windowHeight)/(DOT_SIZE*DOT_SIZE);
		setSize(windowWidth+(windowWidth-getFieldWidth()), windowHeight+(windowHeight-getFieldHeight()));
		
		lengthOne = INITIAL_LENGTH;
		lengthTwo = INITIAL_LENGTH;
		snakeOne = new SnakeTile[ALL_DOTS];
		snakeTwo = new SnakeTile[ALL_DOTS];
		
		snakeOne[0] = new SnakeTile(DOT_SIZE, DOT_SIZE, 0, DOT_SIZE, HEAD_ONE_COLOUR);
		snakeTwo[0] = new SnakeTile(DOT_SIZE, DOT_SIZE, 0, windowHeight-(2*DOT_SIZE), HEAD_TWO_COLOUR);
		add(snakeOne[0]);
		add(snakeTwo[0]);
		for(int i=1; i<lengthOne; i++) {
			snakeOne[i] = new SnakeTile(DOT_SIZE, DOT_SIZE, 0 - i*DOT_SIZE, DOT_SIZE, BODY_COLOUR);
			snakeTwo[i] = new SnakeTile(DOT_SIZE, DOT_SIZE, 0 - i*DOT_SIZE, windowHeight-(2*DOT_SIZE), BODY_COLOUR);
			add(snakeOne[i]);
			add(snakeTwo[i]);
		}
		
		snakeOneScore = new Pellet();
		snakeTwoScore = new Pellet();
		
		orangeOne = new Pellet(DOT_SIZE, DOT_SIZE, windowWidth/4, windowHeight/2, Color.ORANGE);
		orangeTwo = new Pellet(DOT_SIZE, DOT_SIZE, 3*(windowWidth/4), windowHeight/2, Color.ORANGE);
		add(orangeOne);
		add(orangeTwo);
		
		scoreOne = new JLabel();
		scoreOne.setForeground(Color.WHITE);
		scoreOne.setHorizontalAlignment(SwingConstants.CENTER);
		scoreOne.setSize(windowWidth/5, windowHeight/10);
		scoreOne.setLocation(windowWidth/30, windowHeight/30);
		scoreOne.setFont(new Font("Sans Serif", Font.BOLD, windowWidth/40));
		scoreOne.setText("Score: " + snakeOneScore.getEatenPellets());
		
		scoreTwo = new JLabel();
		scoreTwo.setForeground(Color.WHITE);
		scoreTwo.setHorizontalAlignment(SwingConstants.CENTER);
		scoreTwo.setSize(windowWidth/5, windowHeight/10);
		scoreTwo.setLocation(windowWidth/2, windowHeight/30);
		scoreTwo.setFont(new Font("Sans Serif", Font.BOLD, windowWidth/40));
		scoreTwo.setText("Score: " + snakeTwoScore.getEatenPellets());
		
		add(scoreOne);
		add(scoreTwo);
	}
	
	/**
	 * The act method checks in realtime for different scenarios and actions. This method is responsible for things such as keeping the snake
	 * moving at all times as well as checking for collision detection
	 * 
	 * Pre: The game has started
	 * Post: The game ends and the end screen appears (since game.stop() is called)
	 * 
	 */
	public void act() {
		setDelay(ACT_DELAY);
		
		move(snakeOne, p1HorizontalDirection, p1VerticalDirection, lengthOne);
		move(snakeTwo, p2HorizontalDirection, p2VerticalDirection, lengthTwo);
		
		// Responsible for setting the direction of the snake accordingly based off of keypressees
		if(this.p1LeftPressed() && p1HorizontalDirection == 0) {
			p1VerticalDirection = 0;
			p1HorizontalDirection = -1;
		} else if(this.p1RightPressed() && p1HorizontalDirection == 0) {
			p1VerticalDirection = 0;
			p1HorizontalDirection = 1;
		} else if(this.p1UpPressed() && p1VerticalDirection == 0) {
			p1VerticalDirection = -1;
			p1HorizontalDirection = 0;
		} else if(this.p1DownPressed() && p1VerticalDirection == 0) {
			p1VerticalDirection = 1;
			p1HorizontalDirection = 0;
		}
		
		if(this.p2LeftPressed() && p2HorizontalDirection == 0) {
			p2VerticalDirection = 0;
			p2HorizontalDirection = -1;
		} else if(this.p2RightPressed() && p2HorizontalDirection == 0) {
			p2VerticalDirection = 0;
			p2HorizontalDirection = 1;
		} else if(this.p2UpPressed() && p2VerticalDirection == 0) {
			p2VerticalDirection = -1;
			p2HorizontalDirection = 0;
		} else if(this.p2DownPressed() && p2VerticalDirection == 0) {
			p2VerticalDirection = 1;
			p2HorizontalDirection = 0;
		}
		
		if(snakeOne[0].collides(snakeTwo[0]) || (snakeOne[0].collides(snakeTwo[1]) && snakeTwo[0].collides(snakeOne[1]))) { // if the snakes collide head on its a tie game
			endScreen("Tie Game!");
		} else {
			for(int i=1; i<lengthOne; i++) {
				if (snakeOne[i].collides(snakeTwo[0])) { // if the head of player 2s snake collides with any part of snake 1s body
					endScreen("Player 1 Wins!");
				}
			}
			
			for(int i=1; i<lengthTwo; i++) {
				if (snakeTwo[i].collides(snakeOne[0])) { // if the head of player 1s snake collides with any part of snake 2s body
					endScreen("Player 2 Wins!");
				}
			}
		}
		
		if((snakeTwo[0].getY() < 0 ||  snakeTwo[0].getX() < 0 || snakeTwo[0].getX() > windowWidth || snakeTwo[0].getY() > windowHeight) &&
		(snakeOne[0].getY() < 0 || snakeOne[0].getX() < 0 || snakeOne[0].getX() > windowWidth || snakeOne[0].getY() > windowHeight)) { // if the snakes hit a border at the same time
			endScreen("Tie Game!");
		} else if(snakeTwo[0].getY() < 0 || snakeTwo[0].getX() < 0 || snakeTwo[0].getX() >= windowWidth || snakeTwo[0].getY() >= windowHeight) {
			endScreen("Player 1 Wins!");
		} else if(snakeOne[0].getY() < 0 || snakeOne[0].getX() < 0 || snakeOne[0].getX() >= windowWidth || snakeOne[0].getY() >= windowHeight) {
			endScreen("Player 2 Wins!");
		}
		
		for(int i=3; i<lengthOne; i++) { // if the snake collides with itself
			if(snakeOne[0].collides(snakeOne[i])) {
				endScreen("Player 2 Wins!");
			}
		}
		
		for(int i=3; i<lengthTwo; i++) {
			if(snakeTwo[0].collides(snakeTwo[i])) {
				endScreen("Player 1 Wins!");
			}
		}
		
		// every scenario of different snakes eating the different pellets
		if(snakeOne[0].collides(orangeOne)) { 
			updatePellet(1, 1);
		}
		
		if(snakeOne[0].collides(orangeTwo)) {
			updatePellet(1, 2);
		}
		
		if(snakeTwo[0].collides(orangeOne)) {
			updatePellet(2, 1);
		}
		
		if(snakeTwo[0].collides(orangeTwo)) {
			updatePellet(2, 2);
		}
	}
}