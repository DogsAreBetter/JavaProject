package controller;

import java.awt.Label;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Deque;

import javax.swing.JFrame;

import model.Blob;
import model.Dot;

/**
 * Displays and handles the game.
 */

public class Controller extends JFrame {

	private ArrayList<Blob> blobs = new ArrayList<Blob>();
	private ArrayList<Dot> dots = new ArrayList<Dot>();
	private Blob blob;
	private MyFrame mf;
	private int mouseX = 0;
	private int mouseY = 0;
	private int xDis = 0;
	private int yDis = 0;
	private int speed = 40;
	private String id;

	/**
	 * Constructor for controller
	 * @param id client's ID
	 * @param x X-coordinate of client's blob
	 * @param y Y-coordinate of client's blob
	 */

	public Controller(String id, int x, int y) {//, ArrayList<String> chatHistory){
		this.id = id;
		blob = new Blob(id,x,y);
//		ArrayList<String> temp = new ArrayList<String> (chatHistory);
		mf = new MyFrame("Game", blob, blobs, dots);//, temp);
		startGame();
	}

	/**
	 * Starts all the threads necessary for the game to work properly; sets up listeners.
	 */

	public void startGame() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		mf.addMouseMotionListener(new MyMouseMoveListener());
		//mf.addKeyListener(new MyKeyListener());

		Refresh rf = new Refresh(dots, blob, speed, mf, blobs);
		Thread mThread = new Thread(rf);
		mThread.start();
		Thread mThread2 = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					try {
						Thread.sleep(rf.getSpeed());

						double easingAmount = 20;
						int x, y;

						x = (int) (blob.getX() + xDis / easingAmount);
						y = (int) (blob.getY() + yDis / easingAmount);

						blob.setX(x);
						blob.setY(y);
					} catch (Exception e) {}
				}
			}
		});
		mThread2.start();
	}

	/**
	 * Adds a specified dot (food) to client's array of dots
	 * @param d dot to be added
	 */
	
	public void addDot(Dot d){
		dots.add(d);
		mf.add(d);
	}

	/**
	 * Getter for client's blob
	 * @return client's blob
	 */

	public Blob getBlob() {
		return blob;
	}

	/**
	 * Getter for controller's frame
	 * @return controller's frame
	 */
	
	public MyFrame getMf(){
		return mf;
	}

	/**
	 * Adds a blob (representing a player) to controller's array of blobs.
	 * @param b blob to be added
	 */

	public void addBlob(Blob b){
		blobs.add(b);
	}

	/**
	 * Getter for controller's array of blobs.
	 * @return controller's array of blobs
	 */

	public ArrayList<Blob> getBlobs(){
		return blobs;
	}

	/**
	 * Listener reacting to mouse movements and updating player's blob's position.
	 */

	class MyMouseMoveListener extends MouseMotionAdapter {
		public void mouseMoved(MouseEvent m) {
			if(mf.isActive()) {
				mouseX = m.getX();
				mouseY = m.getY();
				xDis = mouseX - blob.getX();
				yDis = mouseY - blob.getY();
			}
		}
	}
/*
	class MyKeyListener implements KeyListener {
		@Override
		public void keyTyped(KeyEvent keyEvent) {}

		@Override
		public void keyPressed(KeyEvent event) {
			if(mf.isActive()) {
				if(event.getKeyCode() == KeyEvent.VK_ESCAPE) mf.chatVisible = !mf.chatVisible;
			}
		}

		@Override
		public void keyReleased(KeyEvent keyEvent) {}
	}
*/}