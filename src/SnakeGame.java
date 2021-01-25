/**
 * Stefan Pitigoi & Michael Tham
 * ICS4U, Mr.Benum
 * January 22, 2021
 * 
 * Referenced Websites:
 * https://docs.oracle.com/javase/tutorial/uiswing/events/actionlistener.html
 *     --> used to implement actionlistener for buttons
 *     
 * https://stackoverflow.com/questions/2442599/how-to-set-jframe-to-appear-centered-regardless-of-monitor-resolution/15000866 
 *     --> solution for centering screen on startup
 * 
 */

import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;
import javax.swing.*;

public class SnakeGame extends JFrame {
	private int preferredHeight = 400;
	private int preferredWidth = 400;
	private int preferredResolution = 0;
	
	// gives sets the ascii value of the characters that will be used as controls for the snakes
	private int p1left = KeyEvent.VK_A, 
			    p1right = KeyEvent.VK_D,
			    p1up = KeyEvent.VK_W, 
			   	p1down = KeyEvent.VK_S;
	
	private int p2left = KeyEvent.VK_LEFT, 
				p2right = KeyEvent.VK_RIGHT, 
				p2up = KeyEvent.VK_UP, 
				p2down = KeyEvent.VK_DOWN;

	private int[] p1ControlArray, p2ControlArray;
	private boolean soundOn = true; // default option for sound is on
	
	
	/**
	 * Starts a one player game of snake.
	 * 
	 * Pre: The user clicks one player from the modes screen.
	 * Post: A game of one player snake starts.
	 *
	 */
	public void onePlayer() {
		
		OnePlayerSnake s = new OnePlayerSnake(preferredWidth, preferredWidth, p1ControlArray, soundOn);
		s.setVisible(true);
		s.initComponents();
		
	}
	
	/**
	 * Starts a two player game of snake.
	 * 
	 * Pre: The user clicks two player from the modes screen.
	 * Post: A game of two player snake starts.
	 *
	 */
	public void twoPlayer() {
		
		TwoPlayerSnake s = new TwoPlayerSnake(preferredWidth, preferredWidth, p1ControlArray, p2ControlArray, soundOn);
		s.setVisible(true);
		s.initComponents();
		
	}
	
	/**
	 * The screen that appears when a player chooses the "START" button on the menu
	 * 
	 * Pre: The "START" button is clicked
	 * Post: The player gets the option to either play one player or two player
	 * 
	 */
	public void chooseMode() {
		
		JFrame chooseMode = new JFrame();
		chooseMode.setResizable(false);
		chooseMode.setSize(500, 500);
		chooseMode.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		chooseMode.getContentPane().setBackground(Color.BLACK);
		
		JLabel choices = new JLabel("MODE", SwingConstants.CENTER);
		choices.setFont(choices.getFont ().deriveFont (80.0f));
		choices.setForeground(Color.WHITE);
		choices.setSize(500,80);
		choices.setLocation(chooseMode.getWidth()/2 - choices.getWidth() / 2, 50);
		
		// screen is closed once the player chooses a mode
		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String command = event.getActionCommand();
				if (command.equals("one")) {
					chooseMode.dispose();
					onePlayer();
				} else if (command.equals("two")) {
					chooseMode.dispose();
					twoPlayer();
				}
			}		
		};
		
		JButton onePlayer = new JButton();
		onePlayer.setText("1 PLAYER");
		onePlayer.setFont(onePlayer.getFont ().deriveFont (30.0f));
		onePlayer.setBackground(Color.WHITE);
		onePlayer.setSize(250, 50);
		onePlayer.setLocation(chooseMode.getWidth()/2 - onePlayer.getWidth() / 2, 200);
		onePlayer.setBorderPainted(false);
		onePlayer.setOpaque(true);
		onePlayer.setActionCommand("one");
		onePlayer.addActionListener(actionListener);

		JButton twoPlayer = new JButton();
		twoPlayer.setText("2 PLAYER");
		twoPlayer.setFont(twoPlayer.getFont ().deriveFont (30.0f));
		twoPlayer.setBackground(Color.WHITE);
		twoPlayer.setSize(250, 50);
		twoPlayer.setLocation(chooseMode.getWidth()/2 - twoPlayer.getWidth() / 2, 300);
		twoPlayer.setBorderPainted(false);
		twoPlayer.setOpaque(true);
		twoPlayer.setActionCommand("two");
		twoPlayer.addActionListener(actionListener);
		
		chooseMode.add(choices);
		chooseMode.add(onePlayer);
		chooseMode.add(twoPlayer);
		
		chooseMode.setLayout(null);
		chooseMode.setLocationRelativeTo(null); // the location of the window defaults to the middle of the screen
		chooseMode.setVisible(true);
		
	}
	
	/**
	 * The options screen. Allows users to change the game's resolution, controls, or toggle the sound setting
	 * 
	 * Pre: The user clicks "OPTIONS" from the main menu screen
	 * Post: The options window is drawn and the menu screen disappears
	 * 
	 */
	public void options() {
		JFrame options = new JFrame();
		options.setSize(500, 500);
		options.setResizable(false);
		options.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		options.getContentPane().setBackground(Color.BLACK);
		
		JLabel optionsTitle = new JLabel("OPTIONS", SwingConstants.CENTER);
		optionsTitle.setFont(optionsTitle.getFont().deriveFont(60.0f));
		optionsTitle.setForeground(Color.WHITE);
		optionsTitle.setSize(275, 80);
		optionsTitle.setLocation(options.getWidth()/2 - optionsTitle.getWidth() / 2, 25);
		
		
		// the options for the resolution of the game
		String[] resolutionOptions = {
				"400 x 400",
				"600 x 600",
				"800 x 800",
				"1000 x 1000"
		};
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // gets the current resolution of the screen

        JLabel currentResolution = new JLabel("Your current resolution is", SwingConstants.CENTER);
        currentResolution.setFont(currentResolution.getFont().deriveFont(20.0f));
        currentResolution.setForeground(Color.WHITE);
        currentResolution.setSize(350, 25);
        currentResolution.setLocation(options.getWidth()/2 - currentResolution.getWidth()/2, 100);
        
        JLabel resolution = new JLabel((int) screenSize.getWidth() + " x " + (int) screenSize.getHeight(), SwingConstants.CENTER);
        resolution.setFont(resolution.getFont().deriveFont(20.0f));
        resolution.setForeground(Color.WHITE);
        resolution.setSize(350, 25);
        resolution.setLocation(options.getWidth()/2 - resolution.getWidth()/2, 125);
        
        JComboBox<String> cb = new JComboBox<String>(resolutionOptions); // a dropdown that contains the resolution choices
        ((JLabel)cb.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        cb.setSize(225, 20);
        cb.setFont(cb.getFont ().deriveFont (20.0f));
        cb.setLocation(options.getWidth()/2 - cb.getWidth() / 2, 165);

		JButton controlsBtn = new JButton();
		controlsBtn.setText("CONTROLS");
	    controlsBtn.setFont(controlsBtn.getFont ().deriveFont (30.0f));
	    controlsBtn.setSize(225, 50);
	    controlsBtn.setLocation(options.getWidth()/2 - controlsBtn.getWidth() / 2, 200);
	    controlsBtn.setBorderPainted(false);
	    controlsBtn.setOpaque(true);
	    controlsBtn.setActionCommand("controls");
	    
	    String soundSetting = "ON";
	    
	    JButton toggleSound = new JButton();
	    toggleSound.setText("SOUND: " + soundSetting);
	    toggleSound.setFont(toggleSound.getFont().deriveFont(30.0f));
	    toggleSound.setSize(225, 50);
	    toggleSound.setLocation(options.getWidth()/2 - toggleSound.getWidth() / 2, 275);
	    toggleSound.setBorderPainted(false);
	    toggleSound.setOpaque(true);
	    toggleSound.setActionCommand("sound");
		
	    JButton okBtn = new JButton();
	    okBtn.setText("BACK");
	    okBtn.setFont(okBtn.getFont ().deriveFont (30.0f));
	    okBtn.setSize(225, 50);
	    okBtn.setLocation(options.getWidth()/2 - okBtn.getWidth() / 2, 350);
	    okBtn.setBorderPainted(false);
	    okBtn.setOpaque(true);
	    okBtn.setActionCommand("ok");

	    ActionListener actionListener = new ActionListener() {
	    	
			public void actionPerformed(ActionEvent event) {
				
				String command = event.getActionCommand();
				
				if (command.equals("controls")) {
					preferredWidth = Integer.parseInt(cb.getSelectedItem().toString().split("x")[0].trim());
					preferredHeight = Integer.parseInt(cb.getSelectedItem().toString().split("x")[1].trim());
					preferredResolution = cb.getSelectedIndex();
					options.dispose();
					controls();	

				} if (command.equals("ok")) {
					preferredWidth = Integer.parseInt(cb.getSelectedItem().toString().split("x")[0].trim());
					preferredHeight = Integer.parseInt(cb.getSelectedItem().toString().split("x")[1].trim());
					preferredResolution = cb.getSelectedIndex();
					options.dispose();
					startMenu();
				}
				
				if (command.equals("sound")) {
					
					if (soundOn) {
						
						toggleSound.setText("SOUND: OFF");
						
					}
					
					else if (!soundOn) {
						
						toggleSound.setText("SOUND: ON");
						
					}
					
					soundOn = !soundOn; // boolean is inverted between true and false each time
					
				}
			}		
		};
		
		controlsBtn.addActionListener(actionListener);
		okBtn.addActionListener(actionListener);
		toggleSound.addActionListener(actionListener);
	    
	    options.add(optionsTitle);
	    options.add(cb);
	    options.add(controlsBtn);
	    options.add(okBtn);
	    options.add(currentResolution);
	    options.add(resolution);
	    options.add(toggleSound);
	    
	    options.setLayout(null);
	    options.setLocationRelativeTo(null);
	    options.setVisible(true);
	}
	
	/**
	 * Sets the property of the button called to a standard that is used throughout the program. Used to prevent repetition
	 * 
	 * Pre: A button is initialized
	 * Post: Some of the general properties are applied to the button that is passed through the method
	 * 
	 * @param button
	 */
	public void setButtonProperties(JButton button) {
		button.setFont(button.getFont ().deriveFont (30.0f));
		button.setSize(225, 50);
		button.setBorderPainted(false);
		button.setOpaque(true);
	}
	
	/**
	 * The controls window that allows players to change the binding of a certain action to a different key. Also doesn't allow for more
	 * than one command to be set to the same key
	 * 
	 * Pre: User clicks "CONTROLS" on the options screen
	 * Post: The controls screen is created and users can edit their controls
	 * 
	 */
	public void controls() {
		
		JFrame controls = new JFrame();
		controls.setSize(500, 500);
		controls.setResizable(false);
		controls.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		controls.getContentPane().setBackground(Color.BLACK);
		
		JLabel arrowLegend = new JLabel("% =LeftArrow | \' =RightArrow | & =UpArrow | ( =DownArrow", SwingConstants.CENTER);
		arrowLegend.setFont(arrowLegend.getFont().deriveFont(15.0f));
		arrowLegend.setForeground(Color.WHITE);
		arrowLegend.setSize(500, 20);
		arrowLegend.setLocation(controls.getWidth()/2 - arrowLegend.getWidth()/2, 15);
		controls.add(arrowLegend);
		
		JLabel p1Label = new JLabel("p1 controls", SwingConstants.CENTER);
		p1Label.setFont(p1Label.getFont().deriveFont(25.0f));
		p1Label.setForeground(Color.WHITE);
		p1Label.setSize(200, 25);
		p1Label.setLocation(controls.getWidth()/2 - p1Label.getWidth()/2, 50);
		
	    JButton p1leftBtn = new JButton("Left: " + (char) p1left);
	    setButtonProperties(p1leftBtn);
	    p1leftBtn.setLocation(controls.getWidth()/2 - p1leftBtn.getWidth()/2 - 117, 90);
	    
	    JButton p1rightBtn = new JButton("Right: " + (char) p1right);
	    setButtonProperties(p1rightBtn);
	    p1rightBtn.setLocation(controls.getWidth()/2 - p1rightBtn.getWidth()/2 + 117, 90);
	    
	    JButton p1upBtn = new JButton("Up: " + (char) p1up);
	    setButtonProperties(p1upBtn);
	    p1upBtn.setLocation(controls.getWidth()/2 - p1upBtn.getWidth()/2 - 117, 150);
	    
	    JButton p1downBtn = new JButton("Down: " + (char) p1down);
	    setButtonProperties(p1downBtn);
	    p1downBtn.setLocation(controls.getWidth()/2 - p1downBtn.getWidth()/2 + 117, 150);
	    
	    JButton[] p1buttons = {p1leftBtn, p1rightBtn, p1upBtn, p1downBtn};
	    
		JLabel p2Label = new JLabel("p2 controls", SwingConstants.CENTER);
		p2Label.setFont(p1Label.getFont().deriveFont(25.0f));
		p2Label.setForeground(Color.WHITE);
		p2Label.setSize(200, 25);
		p2Label.setLocation(controls.getWidth()/2 - p1Label.getWidth()/2, 225);
	    
	    JButton p2leftBtn = new JButton("Left: " + (char) p2left);
	    setButtonProperties(p2leftBtn);
	    p2leftBtn.setLocation(controls.getWidth()/2 - p2leftBtn.getWidth()/2 - 117, 265);
	    
	    JButton p2rightBtn = new JButton("Right: " + (char) p2right);
	    setButtonProperties(p2rightBtn);
	    p2rightBtn.setLocation(controls.getWidth()/2 - p2rightBtn.getWidth()/2 + 117, 265);
	    
	    JButton p2upBtn = new JButton("Up: " + (char) p2up);
	    setButtonProperties(p2upBtn);
	    p2upBtn.setLocation(controls.getWidth()/2 - p2upBtn.getWidth()/2 - 117, 325);
	    
	    JButton p2downBtn = new JButton("Down: " + (char) p2down);
	    setButtonProperties(p2downBtn);
	    p2downBtn.setLocation(controls.getWidth()/2 - p2downBtn.getWidth()/2 + 117, 325);
	    
	    JButton[] p2buttons = {p2leftBtn, p2rightBtn, p2upBtn, p2downBtn}; 
	    
	    /*
	     * Iterates through the p1 button array and makes the popup for the new controls.
	     * 
	     */ 
	    for (JButton button : p1buttons) {
	    	button.addActionListener(new ActionListener() {
	    		public void actionPerformed(ActionEvent e) {
	    			String cntrl = JOptionPane.showInputDialog("Enter a char\n" + 
	    														"For Arrow Keys type: \n" + 
	    														"% for left\n" +
	    														"\' for right\n" +
	    														"& for up\n" +
	    														"( for down\n");
	    	        if(cntrl.length() == 1 &&
 	    	           cntrl.charAt(0) != p1left && cntrl.charAt(0) != p1right &&
 	    	           cntrl.charAt(0) != p1up && cntrl.charAt(0) != p1down &&
 	    	           cntrl.charAt(0) != p2left && cntrl.charAt(0) != p2right && 
 	    	           cntrl.charAt(0) != p2up && cntrl.charAt(0) != p2down) {
    	        		button.setText(button.getText().substring(0, button.getText().length()-1) + cntrl.toUpperCase());
	    	        	p1left = p1leftBtn.getText().charAt(p1leftBtn.getText().length()-1);
	    	        	p1right = p1rightBtn.getText().charAt(p1rightBtn.getText().length()-1);
	    	        	p1up = p1upBtn.getText().charAt(p1upBtn.getText().length()-1);
	    	        	p1down = p1downBtn.getText().charAt(p1downBtn.getText().length()-1);
	    	        	
	    	        }
	    		}
	    	});
	    	
	    	controls.add(button);
	    	
	    }
		
	    /*
	     * Iterates through the p2 button array and makes the popup for the new controls.
	     * 
	     */
	    for (JButton button : p2buttons) {
	    	button.addActionListener(new ActionListener() {
	    		public void actionPerformed(ActionEvent e) {
	    			String cntrl = JOptionPane.showInputDialog("Enter a char\n" + 
	    													   "For Arrow Keys type: \n" + 
	    													   "% for left\n" +
	    													   "\' for right\n" +
	    													   "& for up\n" +
	    													   "( for down\n");
	    	        if(cntrl.length() == 1 &&
	    	           cntrl.charAt(0) != p1left && cntrl.charAt(0) != p1right &&
	    	           cntrl.charAt(0) != p1up && cntrl.charAt(0) != p1down &&
	    	           cntrl.charAt(0) != p2left && cntrl.charAt(0) != p2right && 
	    	           cntrl.charAt(0) != p2up && cntrl.charAt(0) != p2down) {
    	        		button.setText(button.getText().substring(0, button.getText().length()-1) + cntrl.toUpperCase());
	    	        	p2left = p2leftBtn.getText().charAt(p2leftBtn.getText().length()-1);
	    	        	p2right = p2rightBtn.getText().charAt(p2rightBtn.getText().length()-1);
	    	        	p2up = p2upBtn.getText().charAt(p2upBtn.getText().length()-1);
	    	        	p2down = p2downBtn.getText().charAt(p2downBtn.getText().length()-1);
	    	        }
	    		}
	    	});
	    	controls.add(button);
	    }
	    
	    JButton okBtn = new JButton("SAVE");
	    okBtn.setFont(okBtn.getFont ().deriveFont (30.0f));
	    okBtn.setSize(250, 50);
	    okBtn.setLocation(controls.getWidth()/2 - okBtn.getWidth()/2, 395);
	    okBtn.setBorderPainted(false);
	    okBtn.setOpaque(true);
	    okBtn.setActionCommand("ok");
	    okBtn.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent event) {
	    		String command = event.getActionCommand();
				if (command.equals("ok")) {
					controls.dispose();
					options();
				}
	    	}
	    });
	    
	    controls.add(p1Label);
	    controls.add(p2Label);
	    controls.add(p1leftBtn);
	    controls.add(okBtn);
	    controls.setLayout(null);
	    controls.setVisible(true);
	    controls.setLocationRelativeTo(null);
	}
	
	/**
	 * The instructions window that has the rules
	 * 
	 * Pre: User clicks "INSTRUCTIONS" on the main menu screen
	 * Post: The instructions screen is created
	 * 
	 */
	public void instructions() {
		JFrame instructions = new JFrame();
		instructions.setResizable(false);
		instructions.setSize(500, 500);
		instructions.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		instructions.getContentPane().setBackground(Color.BLACK);
		
		String p1Steps = "<html> Classic snake is a challenge to get the highest score possible while "
						+ "planning a route to the next pellet. Your objective is to move your "
						+ "snake head (the X coloured tile) to the pellets (the Y coloured "
						+ "tiles). Each time your snake head reaches a pellet, it will \"eat\" it " 
						+ "and grow one tile longer. Your goal is to eat as many pellets as "
						+ "possible while avoiding collision with any barriers. Barriers consist "
						+ "of the window walls as well as any part of your snake body. As your "
						+ "snake increases in length, you will have to start watching out for "
						+ "your own body as you loop around the frame."
						+ "<p>"
						+ "</html>";
						
						
						
		String p2Steps = "<html> Two player snake is a challenge to get a higher score and force your "
						+ "opponent to collide with your snakes body. This can be made easier by "
						+ "collecting pellets. Eating a pellet will increase your score, but now "
						+ "that there's another snake, watch out! Your opponent may try and "
						+ "block your path with their snakes body in order to force you to "
						+ "collide. You not only need to be quick on your feet but also need to "
						+ "strategically move in order to avoid or sabotage your opponent. The "
						+ "last player standing wins. <p> <p> May the best Snake take the cake!"
						+ "<p>"
						+ "</html>";
		
		JLabel p1Controls = new JLabel("Player 1 (Red) Controls : W A S D");
		p1Controls.setFont(p1Controls.getFont ().deriveFont (11.0f));
		p1Controls.setForeground(Color.WHITE);
		p1Controls.setSize(250, 11);
		p1Controls.setLocation(25, 75);
		
		JLabel p2Controls = new JLabel("Player 2 (Blue) Controls : Arrow Keys");
		p2Controls.setFont(p2Controls.getFont ().deriveFont (11.0f));
		p2Controls.setForeground(Color.WHITE);
		p2Controls.setSize(250, 11);
		p2Controls.setLocation(instructions.getWidth()/2 + 25, 75);
		
		JLabel instructionsLabel = new JLabel("INSTRUCTIONS", SwingConstants.CENTER);
		instructionsLabel.setFont(instructionsLabel.getFont().deriveFont(50.0f));
		instructionsLabel.setForeground(Color.WHITE);
		instructionsLabel.setSize(500, 50);
		instructionsLabel.setLocation(instructions.getWidth()/2 - instructionsLabel.getWidth()/2, 10);
		
		JLabel p1Label = new JLabel(p1Steps, SwingConstants.CENTER);
		p1Label.setFont(p1Label.getFont ().deriveFont (10.0f));
		p1Label.setForeground(Color.WHITE);
		p1Label.setSize(200, 400);
		p1Label.setLocation(25, 0);
		
		JLabel p2Label = new JLabel(p2Steps, SwingConstants.CENTER);
		p2Label.setFont(p2Label.getFont ().deriveFont (10.0f));
		p2Label.setForeground(Color.WHITE);
		p2Label.setSize(200, 400);
		p2Label.setLocation(instructions.getWidth()/2 + 25, 0);
		
		JButton okBtn = new JButton("OK");
		okBtn.setText("OK");
		okBtn.setFont(okBtn.getFont ().deriveFont (50.0f));
		okBtn.setBackground(Color.WHITE);
		okBtn.setSize(250, 50);
		okBtn.setLocation(instructions.getWidth()/2 - okBtn.getWidth() / 2, 400);
		okBtn.setBorderPainted(false);
		okBtn.setOpaque(true);
		
		JLabel resolutionText = new JLabel("If you want a larger playing field, try changing the resolution in options!", SwingConstants.CENTER);
		resolutionText.setFont(resolutionText.getFont ().deriveFont (12.0f));
		resolutionText.setForeground(Color.WHITE);
		resolutionText.setSize(500, 20);
		resolutionText.setLocation(instructions.getWidth()/2 - resolutionText.getWidth()/2, 350);
		
		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {			
				String command = event.getActionCommand();
				if (command.equals("ok")) {
					instructions.dispose();
					startMenu();
				}
			}
		};
		
		
		okBtn.setActionCommand("ok");
		okBtn.addActionListener(actionListener);
		
		instructions.add(okBtn);
		instructions.add(p1Label);
		instructions.add(p2Label);
		instructions.add(p1Controls);
		instructions.add(p2Controls);
		instructions.add(instructionsLabel);
		instructions.add(resolutionText);
		instructions.setLayout(null);
		instructions.setLocationRelativeTo(null);
		instructions.setVisible(true);
	}
	
	
	/**
	 * The main menu that shows when game is started
	 * 
	 * Pre: User opens Snake.jar
	 * Post: The main menu is created
	 * 
	 */
	public void startMenu() {		
		JFrame menu = new JFrame();
		menu.setResizable(false);
		menu.setSize(500, 500);
		menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		menu.getContentPane().setBackground(Color.BLACK);
		
		JLabel title = new JLabel("SNAKE", SwingConstants.CENTER);
		title.setFont(title.getFont ().deriveFont (80.0f));
		title.setForeground(Color.WHITE);
		title.setSize(500,80);
		title.setLocation(menu.getWidth()/2 - title.getWidth() / 2, 50);
		
		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {			
				String command = event.getActionCommand();
				if (command.equals("start")) {
					p1ControlArray = Game.createControlArray(p1left, p1right, p1up, p1down);
					p2ControlArray = Game.createControlArray(p2left, p2right, p2up, p2down);
					menu.dispose();
					chooseMode();
				} else if (command.equals("options")) {
					menu.dispose();
					options();
				} else if (command.equals("instructions")) {
					menu.dispose();
					instructions();
				}
			}
		};
		
		JButton startButton = new JButton();
		startButton.setText("START");
		startButton.setFont(startButton.getFont ().deriveFont (50.0f));
		startButton.setBackground(Color.WHITE);
		startButton.setSize(250, 50);
		startButton.setLocation(menu.getWidth()/2 - startButton.getWidth() / 2, 200);
		startButton.setBorderPainted(false);
		startButton.setOpaque(true);
		startButton.setActionCommand("start");
		startButton.addActionListener(actionListener);
		
		JButton instructionsButton = new JButton();
		instructionsButton.setText("INSTRUCTIONS");
		instructionsButton.setFont(instructionsButton.getFont ().deriveFont (30.0f));
		instructionsButton.setBackground(Color.WHITE);
		instructionsButton.setSize(250, 50);
		instructionsButton.setLocation(menu.getWidth()/2 - instructionsButton.getWidth() / 2, 275);
		instructionsButton.setBorder(BorderFactory.createEmptyBorder());
		instructionsButton.setBorderPainted(false);
		instructionsButton.setOpaque(true);
		instructionsButton.setActionCommand("instructions");
		instructionsButton.addActionListener(actionListener);

		JButton optionsButton = new JButton();
		optionsButton.setText("OPTIONS");
		optionsButton.setFont(optionsButton.getFont ().deriveFont (40.0f));
		optionsButton.setBackground(Color.WHITE);
		optionsButton.setSize(250, 50);
		optionsButton.setLocation(menu.getWidth()/2 - optionsButton.getWidth() / 2, 350);
		optionsButton.setBorderPainted(false);
		optionsButton.setOpaque(true);
		optionsButton.setActionCommand("options");
		optionsButton.addActionListener(actionListener);
		
		menu.add(title);
		menu.add(startButton);
		menu.add(instructionsButton);
		menu.add(optionsButton);
		
		menu.setLayout(null);
		menu.setLocationRelativeTo(null);
		menu.setVisible(true);
	}
	
	/**
	 * Where the journey starts. :)
	 */
	
	public static void main(String[] args) {
		SnakeGame s = new SnakeGame();
		s.startMenu();
	}
}