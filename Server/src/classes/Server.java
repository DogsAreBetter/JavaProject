package classes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * Server to which clients can connect. Functions on a port specified by user.
 */

public class Server extends JFrame {
	private JPanel contentPane;
	public JTextField textField; // public for purpose of testing
	private JButton Start;
	JTextArea textArea;

	private ServerSocket socket;
	private Socket soc;
	public int Port; // for purpose of testing
	private Vector vc = new Vector();

	private ArrayList<Dot> dots = new ArrayList<Dot>();

	public static void main(String[] args)
	{	
			Server frame = new Server();
			frame.setVisible(true);			
	}

	/**
	 * Constructor, creates server's GUI and sets up listeners.
	 */

	public Server() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 280, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane js = new JScrollPane();				

		textArea = new JTextArea();
		textArea.setColumns(20);
		textArea.setRows(5);
		js.setBounds(0, 0, 264, 254);
		contentPane.add(js);
		js.setViewportView(textArea);

		textField = new JTextField();
		textField.setBounds(98, 264, 154, 37);
		contentPane.add(textField);
		textField.setColumns(10);

		JLabel lblNewLabel = new JLabel("Port Number");
		lblNewLabel.setBounds(12, 264, 98, 37);
		contentPane.add(lblNewLabel);
		Start = new JButton("Run server");
		
		MyAction action = new MyAction();
		Start.addActionListener(action);
		textField.addActionListener(action);
		Start.setBounds(0, 325, 264, 37);
		contentPane.add(Start);
		textArea.setEditable(false);
	}

	/**
	 * Listener reacting to user pressing the "Run server" button.
	 */

	public class MyAction implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == Start || e.getSource() == textField) 
			{
				if (textField.getText().equals("") || textField.getText().length()==0)
				{
					textField.setText("Please enter a port number");
					textField.requestFocus();
				} 			
				else 
				{
					try
					{
						Port = Integer.parseInt(textField.getText());
						server_start();
					}
					catch(Exception er)
					{
						textField.setText("Please enter a number");
						textField.requestFocus();
					}	
				}
			}
		}
	}

	/**
	 * Starts the server on a given port.
	 */

	public void server_start() { // public for purpose of testing
		try {
			socket = new ServerSocket(Port);
			Start.setText("server running");
			Start.setEnabled(false);
			textField.setEnabled(false);
			
			if(socket!=null)
			{
				Connection();
			}
			
		} catch (IOException e) {
			textArea.append("The socket is already in use");
		}
	}

	/**
	 * Thread defining server's activity, connecting users etc.
	 */

	private void Connection()  {
		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						textArea.append("Waiting for clients...\n");
						soc = socket.accept();
						textArea.append("User connected\n");
					UserInfo user = new UserInfo(soc, vc);
						vc.add(user);
						user.start();
					} catch (IOException e) {
						textArea.append("Unexpected error occured\n");
					} 
				}
			}
		});
		th.start();
	}

	/**
	 * Inner class of Server defining a connected user.
	 */

	class UserInfo extends Thread {
		private InputStream is;
		private OutputStream os;
		private DataInputStream dis;
		private DataOutputStream dos;
		private Socket user_socket;
		private Vector user_vc;
		private String Nickname = "";


		public UserInfo(Socket soc, Vector vc) {
			this.user_socket = soc;
			this.user_vc = vc;
			User_network();
		}

		/**
		 * Broadcasts the connected user to the rest of the users.
		 */

		public void User_network() {
			try {
				is = user_socket.getInputStream();
				dis = new DataInputStream(is);
				os = user_socket.getOutputStream();
				dos = new DataOutputStream(os);
				
				byte[] b=new byte[128];
				dis.read(b);
				String Nickname = new String(b);

				textArea.append("ID " + Nickname + "\n");
				textArea.setCaretPosition(textArea.getText().length());		
				send_Message("[CONNECT]"+Nickname);
				broad_cast("[CONNECT]"+Nickname);
			} catch (Exception e) {
				textArea.append("Stream setting error\n");
				textArea.setCaretPosition(textArea.getText().length());
			}
		}

		/**
		 * Prepares and displays a specified message on a server; also broadcasts it.
		 * @param str message to be sent
		 */

		public void InMessage(String str) {
			textArea.append(str + "\n");
			textArea.setCaretPosition(textArea.getText().length());

			broad_cast(str);
		}

		/**
		 * Sends a message to all other users.
		 * @param str message to be sent
		 */

		public synchronized void broad_cast(String str) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserInfo imsi = (UserInfo) user_vc.elementAt(i);
				imsi.send_Message(str);
			}
		}

		/**
		 * Sends a specified message to a single user.
		 * @param str message to be sent
		 */

		public  void send_Message(String str) {
			try {
				byte[] bb;	
				bb = str.getBytes();
				dos.write(bb,0,str.length());
			} 
			catch (IOException e) {
				textArea.append("Error sending message\n");
				textArea.setCaretPosition(textArea.getText().length());
			}
		}

		/**
		 * Listens for clients messages and reacts accordingly.
		 */

		public void run()
		{
			while (true) {
				try {
					byte[] b = new byte[128];
					dis.read(b);
					String msg = new String(b);
					InMessage(msg);

					try {
						Thread.sleep(15);
						broad_cast("[VECTOR]("+vc.size()+")");
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					try {
						Thread.sleep(100);
						Random r = new Random();
						if (r.nextInt(10) == 5) {
							int randX = r.nextInt(900);
							int randY = r.nextInt(500);
                            Dot d = new Dot(randX, randY);
							synchronized (dots) {
								dots.add(d);
								InMessage("[DOTS]("+d.toString()+")");
								if(dots.size() > 100){
									dots.clear();
								}
							}
						}
                        if (r.nextInt(10) == 6) {
                            int randX = r.nextInt(900) * 2;
                            int randY = r.nextInt(500) * 2;
                            Dot d = new Dot(randX, randY);
                            synchronized (dots) {
                                dots.add(d);
                                InMessage("[DOTS]("+d.toString()+")");
                                if(dots.size() > 100){
                                    dots.clear();
                                }
                            }
                        }
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} 
				catch (IOException e) 
				{
					try {
						dos.close();
						dis.close();
						user_socket.close();
						vc.removeElement( this );
						textArea.append("User disconnected\n");
						textArea.append("The number of users currently connected: " + vc.size() + "\n");
						textArea.setCaretPosition(textArea.getText().length());

						break;
					} catch (Exception ee) {}
				}
			}
		}
	}
}
