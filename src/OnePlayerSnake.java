/**
 * Stefan Pitigoi & Michael Tham
 * ICS4U, Mr.Benum
 * January 22, 2021
 * 
 * Referenced Websites:
 * https://zetcode.com/javagames/snake/
 * 	--> was used to build onto our original idea, helped us decide if it was feasible
 */

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Font;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.util.Random;
import javax.swing.*;

public class OnePlayerSnake extends Game {
	private final int DOT_SIZE = 20;
	private final int ACT_DELAY = 100;
	private final int INITIAL_LENGTH = 4;
	private final Color HEAD_COLOUR = Color.RED;
	private final Color BODY_COLOUR = Color.GREEN;
	
	private int windowHeight, windowWidth;
	private int allDots;
	private int length;
	private SnakeTile[] snake;
	private Pellet orange;
	private JLabel score;
	private JFrame endScreen;
	private boolean soundOn;
	
	private int[] controls;
	
	private int p1VerticalDirection, p1HorizontalDirection = 1;
	
	/**
	 * OnePlayerSnake Constructor
	 * 
	 * @param sizeX			horizontal size of the game
	 * @param sizeY			vertical size of the game
	 * @param p1Controls	preferred controls for movement
	 * @param soundOn		prefered sound setting
	 */
	public OnePlayerSnake(int sizeX, int sizeY, int[] p1Controls, boolean soundOn) {
		super(sizeX, sizeY, p1Controls);
		windowWidth = sizeX;
		windowHeight = sizeY;
		controls = p1Controls;
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
     * Draws the end screen when a player collides with a wall/snake body. Virtual key presses are input by a "robot" that resets the 
     * key presses before the next game is played. Players have the option to restart or play again.
     * 
     * 
     * Pre: Player loses
     * Post: A screen appears and gives 
     *          the option to quit or restart.
     */
	public void endScreen() {
		/**
		 * these virtual key presses are important because sometimes the stopGame() function makes the game class hang on a key press
		 * and it never detects that the key was released. This can cause problems when the player presses restart. To fix this, a robot
		 * presses the keys and releases them to.
		 */
		try {
			Robot robot = new Robot();
			for(int i=3; i>=0; i--) {
				robot.keyPress(controls[i]);
				robot.keyRelease(controls[i]);
			}
			
			robot.keyPress(controls[1]);
			robot.keyRelease(controls[1]);
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
				}
				else if (command.equals("quit")) {
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
		
		JLabel gameOver = new JLabel("GAME OVER", SwingConstants.CENTER);
		gameOver.setFont(gameOver.getFont ().deriveFont (30.0f));
		gameOver.setForeground(Color.WHITE);
		gameOver.setSize(300,30);
		gameOver.setLocation(endScreen.getWidth()/2 - gameOver.getWidth() / 2, 30);
		
		JLabel score = new JLabel("Score: " + orange, SwingConstants.CENTER);
		gameOver.setFont(gameOver.getFont ().deriveFont (25.0f));
		gameOver.setForeground(Color.WHITE);
		gameOver.setSize(300,30);
		gameOver.setLocation(endScreen.getWidth()/2 - gameOver.getWidth() / 2, 30);
		
		JButton restart = new JButton();
		restart.setText("RESTART");
		restart.setFont(restart.getFont ().deriveFont (15.0f));
		restart.setBackground(Color.WHITE);
		restart.setSize(125, 20);
		restart.setLocation(endScreen.getWidth() - (endScreen.getWidth()-20), 100);
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
		endScreen.add(score);
		endScreen.add(restart);
		endScreen.add(quit);
		
		endScreen.setLayout(null);
		endScreen.setLocationRelativeTo(null);
		endScreen.setVisible(true);
	}
	
	/**
     * Removes the snake from the field and resets it to its initial locations. Snake score and length is reset
     * to the default.
     * 
     * Pre: The player clicks "restart" on the end screen
     * Post: The game is reset as if they just opened the program for the first time
     * 
     */
	public void restart() {
		
		for (int x = 0; x < length; x++) {
			
			remove(snake[x]);
			
		}
		
		length = INITIAL_LENGTH;
		
		orange.resetEatenPellets();
		
		p1HorizontalDirection = 1;
		p1VerticalDirection = 0;
		
		snake = new SnakeTile[allDots];
			
		snake[0] = new SnakeTile(DOT_SIZE, DOT_SIZE, 0, 0, HEAD_COLOUR);
		add(snake[0]);
		
		for(int i=1; i<length; i++) {
			snake[i] = new SnakeTile(DOT_SIZE, DOT_SIZE, 0 - i*DOT_SIZE, 0, BODY_COLOUR);
			add(snake[i]);
		}
		
		score.setText("Score: " + (length-INITIAL_LENGTH));
		repaint();
		startGame();
		
		try {
			Robot robot = new Robot();
			for(int i=3; i>=0; i--) {
				robot.keyPress(controls[i]);
				robot.keyRelease(controls[i]);
			}
			
			robot.keyPress(controls[1]);
			robot.keyRelease(controls[1]);
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
     * Called the a snake collides with a pellet. Increases length of the snake by initializing a new snake tile. Increases score,
     * moves orange pellet to new location on the screen.
     * 
     * Pre: A snake collides with a pellet.
     * Post: Scores are updated accordingly and the pellet that was eaten is redrawn to a random coordinate on the screen.
     */
	public void updatePellet() {
		Random r = new Random();
		int xPos = (r.nextInt(windowWidth)/DOT_SIZE) * DOT_SIZE;
		int yPos = (r.nextInt(windowHeight)/DOT_SIZE) * DOT_SIZE;
		
		orange.pelletEaten();
		
		if (soundOn) {
		
			playAudio("resources/eatPellet.wav");
		
		}
		
		orange.setLocation(xPos, yPos);
		snake[length] = new SnakeTile(DOT_SIZE, DOT_SIZE, snake[length-1].getX()+DOT_SIZE, snake[length-1].getY(), BODY_COLOUR);
		add(snake[length]);
		length++;
		score.setText("Score: " + orange.getEatenPellets());
		repaint();
	}
	
	/**
	 * Sets up the game by initializing all the objects and setting their positions
	 * 
	 * Pre: The program is started, object are called
	 * Post: Objects are initialized
	 */
	public void setup() {
		/**
		 * The basis of the game is that the game field is made out of 20pixel * 20pixel squares called "dots". Objects such as pellets
		 * or the snake's body square can only exist in 1 dot and cannot overlap 2 or more. This way, the movement of the snake is more
		 * predictable and the creation of the snake (an array of SnakeTiles) is feasible. 
		 * 
		 * This is also necessary so that when a snake turns the body does not immediately follow it. Instead the body of the snake
		 * must reach the point at which the head turned and only then turn.
		 */
		allDots = (windowWidth*windowHeight)/(DOT_SIZE*DOT_SIZE);
		setSize(windowWidth+(windowWidth-getFieldWidth()), windowHeight+(windowHeight-getFieldHeight()));
		
		length = INITIAL_LENGTH;
		snake = new SnakeTile[allDots];
		
		/**
		 * The snake is an array of GameObjects called SnakeTiles. These are little squares. By moving a mass of little around
		 * it can appear as though the snake is one long game object, but that would be fairly impossible. 
		 */
		for(int i=0; i<length; i++) {
			if(i == 0) {
				snake[i] = new SnakeTile(DOT_SIZE, DOT_SIZE, 0, 0, HEAD_COLOUR);
			} else {
				snake[i] = new SnakeTile(DOT_SIZE, DOT_SIZE, DOT_SIZE - i*DOT_SIZE, 0, BODY_COLOUR);
			}
			add(snake[i]);
		}
		
		orange = new Pellet(DOT_SIZE, DOT_SIZE, windowWidth/2, windowHeight/2, Color.ORANGE);
		add(orange);
		
		score = new JLabel();
		score.setForeground(Color.WHITE);
		score.setHorizontalAlignment(SwingConstants.CENTER);
		score.setSize(windowWidth/5, windowHeight/10);
		score.setLocation(windowWidth/30, windowHeight/30);
		score.setFont(new Font("Sans Serif", Font.BOLD, windowWidth/40));
		score.setText("Score: 0");
		add(score);
	}
	
	/**
	 * checks for collisions, key presses and outlines situations were the game is lost
	 * 
	 * pre: objects are initialized
	 * post: objects are monitored
	 */
	public void act() {		
		setDelay(ACT_DELAY);
		
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
		
		if(snake[0].getX() < 0 || snake[0].getX() >= windowWidth) {
			endScreen();
		} else if (snake[0].getY() < 0 || snake[0].getY() >= windowHeight) {
			endScreen();
		}
		
		for(int i=3; i<length; i++) {
			if(snake[0].collides(snake[i])) {
				endScreen();
			}
		}
		
		if(snake[0].collides(orange)) {
			updatePellet();
		}
		
		if(length == allDots) {
			endScreen();
		}
		
		/**
		 * Moves each body tile of the snake to the position of the tile before it.
		 * Gives the illusion that the snake is one continuous piece moving in union.
		 */
		for(int i=length-1; i>0; i--) {
			snake[i].setX(snake[i-1].getX());
			snake[i].setY(snake[i-1].getY());
		}
		
		/**
		 * Moves the head of the snake in direction inputed by the user. By moving the head, the rest of the body
		 * will move in the next iteration of this act loop in due to the for loop above.
		 */
		if(p1HorizontalDirection != 0) {
			snake[0].setX(snake[0].getX()+(p1HorizontalDirection*DOT_SIZE));
		} else if (p1VerticalDirection != 0) {
			snake[0].setY(snake[0].getY()+(p1VerticalDirection*DOT_SIZE));
		}
		
	}
}