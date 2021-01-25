/**
 * Stefan & Michael
 * ICS4U, Mr.Benum
 * January 22, 2021
 * 
 * Referenced Websites:
 */

import java.awt.Color;

public class SnakeTile extends GameObject {
	
	/**
	 * Constructor of the SnakeTile
	 * 
	 * @param width		sets width of the object
	 * @param height	sets height of the object
	 * @param posX		sets x location of the object
	 * @param posY		sets y location of the object
	 * @param color		sets colour of the object
	 */
	public SnakeTile(int width, int height, int posX, int posY, Color color) {
		super();
		this.setSize(width, height);
		this.setX(posX);
		this.setY(posY);
		this.setColor(color);
	}
	
	/**
	 * removes the snake tile by setting its size to 0
	 * necessary so that when the game is restart the game doesn't bug and make the player lose again
	 */
	public void delete() {
		setSize(0, 0);
	}
	
	public void act() {
		
	}
}