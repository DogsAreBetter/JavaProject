package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import connector.Client;

/**
 * Class displays a simple menu when player's eaten.
 */

public class Restart  extends JFrame {
	
	private JPanel contentPane;
	private String id;

	/**
	 * Constructor; creates GUI and sets up listeners.
	 * @param id player's name
	 */

	public Restart(String id)
	{
		this.id = id;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 200, 200);
		contentPane = new JPanel();
		//contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel(id+" was eaten");
		lblNewLabel.setBounds(0, 0, 200, 100);
		contentPane.add(lblNewLabel);
		
		JButton btnreButton = new JButton("Restart");
		btnreButton.setBounds(0, 100, 90, 80);
		contentPane.add(btnreButton);
		

		JButton btnexitButton = new JButton("Exit");
		btnexitButton.setBounds(90, 100, 90, 80);
		contentPane.add(btnexitButton);
		
		restart restart = new restart();
		btnreButton.addActionListener(restart);
		
		exit exit1 = new exit();
		btnexitButton.addActionListener(exit1);
		
		setVisible(true);
	}

	/**
	 * Listener for "Restart" button; restarts the game by creating a new Client class.
	 */

	class restart implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {		
			Client client = new Client();
		}
	}

	/**
	 * Listener for "Exit" button; closes the program.
	 */

	class exit implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {		
			System.exit(0);
		}
	}
	
}
