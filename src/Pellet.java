/**
 * Stefan & Michael
 * ICS4U, Mr.Benum
 * January 22, 2021
 * 
 * Referenced Websites:
 */

import java.awt.Color;

public class Pellet extends GameObject {
	private int pelletsEaten;
	
	/**
	 * Default pellet constructor, initializes as a rectangle
	 * 
	 * @param sizeX	horizontal size of the object
	 * @param sizeY	vertical size of the object
	 * @param posX	x position of the object
	 * @param posY	y position of the object
	 * @param colour	color of the object
	 */
	public Pellet(int sizeX, int sizeY, int posX, int posY, Color colour) {
        setX(posX);
        setY(posY);
        setSize(sizeX, sizeY);
        setColor(colour);
    }
	
	/**
	 * Pellet Score constructor, used for 2player game, does not initialize as a rectangle and is
	 * just used as a score keeper.
	 */
	public Pellet() {
		pelletsEaten = 0;
	}
	
	/**
	 * Increases the pellets eaten variable
	 */
	public void pelletEaten() {
		pelletsEaten++;
	}
	
	/**
	 * Gets the number of pellets eaten, also known as the score
	 * @return pelletsEaten
	 */
	public int getEatenPellets() {
		return pelletsEaten;
	}
	
	/**
	 * Resets the number of pellets eaten to 0
	 */
	public void resetEatenPellets() {
		pelletsEaten = 0;
	}
	
	public void act() {
		
	}
}