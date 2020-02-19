package classes;

import java.awt.Color;
import java.awt.Component;
import java.io.Serializable;
import java.util.Random;

/**
 * Represents a single dot - food - on a field.
 */

public class Dot extends Component implements Serializable{
    private int x;
    private int y;
    private Color c;
    private int size;

    public Dot(int x, int y){
        this.x = x;
        this.y = y;
        Random rand = new Random();
        int red = rand.nextInt(255);
        int green = rand.nextInt(255);
        int blue = rand.nextInt(255);
        this.c = new Color(red,green,blue);
        this.size = 10;
    }

	/**
	 * Getter for dot's X-coordinate
	 * @return dot's X-coordinate
	 */

	public int getX() {
		return x;
	}

	/**
	 * Getter for dot's Y-coordinate
	 * @return dot's Y-coordinate
	 */

	public int getY() {
		return y;
	}

	/**
	 * Overridden toString method.
	 * @return concatenated string containing dot's X- and Y-coordinates, in this order, seperated with spacebar.
	 */

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getX() + ","+getY();
	}
}
