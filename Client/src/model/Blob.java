package model;
 
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Random;
 
public class Blob extends Component{
    private int x;
    private int y;
 
    private int size;
    private Color color;
    private String id;
    
    public Blob(String id, int x, int y){
        this.x = x;
        this.y = y;
        this.size = 40;
        this.id = id;
        Random rand = new Random();
        int r = rand.nextInt(128)+127;
        int green = rand.nextInt(128)+127;
        int b = rand.nextInt(128)+127;
        this.color = new Color(r,green,b);
       // this.color = new Color(-65536);
    }
    
    public Blob(String id, int x, int y, int size, Color color){
    	this.x=x;
    	this.y=y;
    	this.size=size;
    	this.id = id;
    	this.color = color;

    }

    public int getX() {
		return x;
	}

	public void setX(int x) {
		if(x <= 0) x = 0;
		if(x >= 1920 - this.getOvalSize() / 2) x = 1920 - this.getOvalSize() / 2;
		else this.x = x;
	}

	public int getY() {
		return y;
	}
 
	public void setY(int y) {
		if(y <= 0) y = 0;
		if(y >= 1080 - this.getOvalSize() / 2) y = 1080 - this.getOvalSize() / 2;
		else this.y = y;
	}
	
	public void setOvalSize(int size){
		this.size = size;
	}
	
	public int getOvalSize(){
		return size;
	}
	
	public String getID(){
		return id;
	}
	
	public Color getColor(){
		return color;
	}

	public synchronized void paint(Graphics g){
		//Font ID = new Font("id",Font.BOLD,15);
		//Font Score = new Font("size",Font.PLAIN,10);
        g.setColor(color);
        g.fillOval(x, y, size, size);
        g.setColor(Color.BLACK);
       // g.setFont(ID);
        if(id.length()==1)
        	g.drawString(id, x+size/2-id.length()*3, y+size/2);
        else
        	g.drawString(id, x+size/2-id.length()*3-3, y+size/2);
      //  g.setFont(Score);
        if(size<100)
        	g.drawString(String.valueOf(size), x+size/2-4, y+size/2+11); 
        else
        	g.drawString(String.valueOf(size), x+size/2-8, y+size/2+11);
    }
}