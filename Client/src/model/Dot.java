package model;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.util.Random;

public class Dot extends Component{
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
    
    
    public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public int getOvalSize(){
		return size;
	}
	
	public String toString() {
		return getX() + "," + getY();
	}

	public void paint(Graphics g){
        g.setColor(c);
        g.fillOval(this.x, this.y, this.size, this.size);
    }
}