package controller;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;

import model.Blob;
import model.Dot;

/**
 * Class solely used for displaying the game.
 */

public class MyFrame extends Frame {
	private Blob blob;
	private ArrayList<Blob> blobs;
	private ArrayList<Dot> dots;
	//public boolean chatVisible;
	//private ArrayList<String> chatHistory;

	/**
	 * Constructor; sets up listener handling closing the game properly.
	 * @param s window title
	 * @param blob player's blob
	 * @param blobs other players' blobs
	 * @param dots dots - food - to be displayed
	 */

	public MyFrame(String s, Blob blob, ArrayList<Blob> blobs, ArrayList<Dot> dots) {//, ArrayList<String> chatHistory) {
		super(s);
		this.blob = blob;
		this.blobs = blobs;
		this.dots = dots;
		//this.chatVisible = false;
		//this.chatHistory = new ArrayList<>(chatHistory);

		setBounds(0, 0, 1920, 1080);
		add(blob);
		blobs.add(blob);
		setVisible(true);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}

	/**
	 * Method overriding Frame.paint(Graphics g). Paints blobs and dots.
	 * @param g specified Graphics window
	 */

	public void paint(Graphics g) {
		//blob.paint(g);
		for (Blob b : blobs) {
			b.paint(g);
		}
		synchronized (dots) {
			for (Dot d : (ArrayList<Dot>) dots.clone()) {
				d.paint(g);
			}
		}
		/*if(chatVisible) {
			int i = 0;
			for (String s : chatHistory) {
				System.out.println(s);//chat.append(iterator.next() + "\n");
				g.drawString(s, 10, 20*i++);
			}
		}*/
	}

	/**
	 * Method overriding Component.update(Graphics g). Paints a field, blobs and dots. Called by this component's repaint.
	 * @param g specified Graphics window
	 */

	public void update(Graphics g) {
		Image buffer1 = createImage(getWidth(), getHeight());
		g = buffer1.getGraphics();
		paint(g);
		//getGraphics().drawImage(buffer1, blob.getX()/2, blob.getY()/2, this);
		getGraphics().drawImage(buffer1, 0, 0, this);
	}
}
