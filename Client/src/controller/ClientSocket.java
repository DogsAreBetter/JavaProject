package controller;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

import model.Blob;
import model.Dot;

/**
 * Class representing and handling client's connection with a server.
 */

public class ClientSocket {
	static Socket socket;
	static InputStream is;
	static DataInputStream dis;
	static OutputStream os;
	static DataOutputStream dos;
	private String id;
	private String ip;
	private int port;
	int vSize;
	ArrayList<String> userID = new ArrayList<String>();
	ArrayList<String> chatHistory = new ArrayList<>();

	/**
	 * Default constructor, used only for testing.
	 */

	public ClientSocket() {
		this.id = "0";
		this.ip = "0";
		this.port = 0;
	}

	/**
	 * Constructor initiating connection with server.
	 * @param id user's id
	 * @param ip server's IP
	 * @param port port on which server functions
	 */

	public ClientSocket(String id, String ip, int port) {
		this.id = id;
		this.ip = ip;
		this.port = port;
		// blobs = new ArrayList<Blob>();
		network();
	}

	/**
	 * Connects client with server.
	 */

	public void network() {
		try {
			socket = new Socket(ip, port);
			// socket = new Socket("127.0.0.1", 30000);
			connection();
		} catch (UnknownHostException e) {
			System.out.print("Unknown error\n");
		} catch (IOException e) {
			System.out.print("Socket connection error");
		}
	}

	/**
	 * Handles connection and communication with server; also parses and interprets messages from server; starts a conttroller thread.
	 */

	public void connection() {
		Blob blob;
		MyFrame mf;
		try {
			is = socket.getInputStream();
			dis = new DataInputStream(is);
			os = socket.getOutputStream();
			dos = new DataOutputStream(os);
		} catch (Exception e) {
			System.out.println("Setup error");
		}

		Random random = new Random();
		int rX = random.nextInt(1920);
		int rY = random.nextInt(1080);

		chatHistory.add("test1");
		chatHistory.add("test2");

		Controller user = new Controller(id, rX, rY);//, chatHistory);
		blob = user.getBlob();
		mf = user.getMf();
		String colorS = Integer.toString(blob.getColor().getRGB());
		send("(" + id + "){" + rX + "," + rY + ":40}<" + colorS + ">");

		ArrayList<Dot> dots = new ArrayList<Dot>();
		// blobs.add(blob);
		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					try {
						byte[] recv = new byte[500];
						dis.read(recv);
						String recvMsg = new String(recv);
						// recvMsg.trim();
						// String recvMsg = dis.readUTF();

						if (recvMsg.startsWith("[VECTOR]")) {
							int start;
							int end;
							// int vSize;
							String tmp;
							start = recvMsg.indexOf("(");
							end = recvMsg.indexOf(")");
							tmp = recvMsg.substring(start + 1, end);
							vSize = Integer.parseInt(tmp);
						}

						if (recvMsg.startsWith("[DOTS]")) {
							int start;
							int end;
							int dX;
							int dY;
							Dot d;
							String tmp;
							// dot X
							start = recvMsg.indexOf("(");
							end = recvMsg.indexOf(",");
							tmp = recvMsg.substring(start + 1, end);
							dX = Integer.parseInt(tmp);
							// dot Y
							start = recvMsg.indexOf(",");
							end = recvMsg.indexOf(")");
							tmp = recvMsg.substring(start + 1, end);
							dY = Integer.parseInt(tmp);
							//System.out.println(dX+","+dY);
							d = new Dot(dX, dY);
							dots.add(d);
							user.addDot(d);
							//
							// String str = "[Dots CONNETED] ";
							// Iterator itr = dots.iterator();
							// while (itr.hasNext()) {
							// Dot d2 = (Dot) itr.next();
							// str += d2.toString() + " ";
							// }
							// // send(str);
							// System.out.println(str);
						}

						if (recvMsg.startsWith("[CONNECTED]")) {
							int start;
							int end;
							int sX;
							int sY = 0;
							String tmp;
							String sID;
							int size;
							int rgb;
							// ID
							start = recvMsg.indexOf('(');
							end = recvMsg.indexOf(')');
							sID = recvMsg.substring(start + 1, end);
							// X
							start = recvMsg.indexOf('{');
							end = recvMsg.indexOf(',');
							tmp = recvMsg.substring(start + 1, end);
							sX = Integer.parseInt(tmp);
							// Y
							start = recvMsg.indexOf(',');
							end = recvMsg.indexOf(':');
							tmp = recvMsg.substring(start + 1, end);
							sY = Integer.parseInt(tmp);
							// size
							start = recvMsg.indexOf(':');
							end = recvMsg.indexOf('}');
							tmp = recvMsg.substring(start + 1, end);
							size = Integer.parseInt(tmp);
							// color
							start = recvMsg.indexOf('<');
							end = recvMsg.indexOf('>');
							tmp = recvMsg.substring(start + 1, end);
							rgb = Integer.parseInt(tmp);

							if (sID.equals(id)) {
								System.out.println("Same id");
							} else {
								user.addBlob(new Blob(sID, sX, sY, size, new Color(rgb)));
							}
						}

						if (recvMsg.startsWith("[CONNECT]")) {
							int start;
							int end;
							int sX;
							int sY = 0;
							String tmp;
							String sID;
							int size;
							int rgb;
							// ID
							start = recvMsg.indexOf('(');
							end = recvMsg.indexOf(')');
							sID = recvMsg.substring(start + 1, end);
							// X
							start = recvMsg.indexOf('{');
							end = recvMsg.indexOf(',');
							tmp = recvMsg.substring(start + 1, end);
							sX = Integer.parseInt(tmp);
							// Y
							start = recvMsg.indexOf(',');
							end = recvMsg.indexOf(':');
							tmp = recvMsg.substring(start + 1, end);
							sY = Integer.parseInt(tmp);
							// size
							start = recvMsg.indexOf(':');
							end = recvMsg.indexOf('}');
							tmp = recvMsg.substring(start + 1, end);
							size = Integer.parseInt(tmp);
							// color
							start = recvMsg.indexOf('<');
							end = recvMsg.indexOf('>');
							tmp = recvMsg.substring(start + 1, end);
							rgb = Integer.parseInt(tmp);

							if (!id.equals(sID)) {
								userID.add(sID);
								Thread.sleep(210);
								send("[CONNECTED](" + id + "){" + blob.getX() + "," + blob.getY() + ":"
										+ blob.getOvalSize() + "}<" + colorS + ">");
								user.addBlob(new Blob(sID, sX, sY, size, new Color(rgb)));

							}
						} else if (recvMsg.startsWith("[MOVE]")) {
							int start;
							int end;
							int sX;
							int sY;

							String tmp;
							String sID;
							int size;
							// ID
							start = recvMsg.indexOf('(');
							end = recvMsg.indexOf(')');
							sID = recvMsg.substring(start + 1, end);
							// X
							start = recvMsg.indexOf('{');
							end = recvMsg.indexOf(',');
							tmp = recvMsg.substring(start + 1, end);
							sX = Integer.parseInt(tmp);
							// Y
							start = recvMsg.indexOf(',');
							end = recvMsg.indexOf(':');
							tmp = recvMsg.substring(start + 1, end);
							sY = Integer.parseInt(tmp);
							// size
							start = recvMsg.indexOf(':');
							end = recvMsg.indexOf('}');
							tmp = recvMsg.substring(start + 1, end);
							size = Integer.parseInt(tmp);
							// System.out.println("vector:" + vSize);

							if (vSize < user.getBlobs().size()) {
								Iterator itr = user.getBlobs().iterator();
								while (itr.hasNext()) {
									Blob b = (Blob) itr.next();
									if (!sID.equals(b.getID())) {
										if (!b.getID().equals(id)) {
											itr.remove();
										}
									}
								}
							}

							if (!id.equals(sID)) {
								try {
									for (Blob b : user.getBlobs()) {
										if (!id.equals(b.getID())) {
											if (b.getID().equals(sID)) {
												b.setX(sX);
												b.setY(sY);
												b.setOvalSize(size);
											}
										}
									}
								} catch (Exception e) {}
								// send("[CONNECT](" + id + "){" + sX + "," + sY
								// + "}");
							}
						}
						// System.out.println(recvMsg);
						send("[MOVE]" + "(" + id + ")" + "{" + blob.getX() + "," + blob.getY() + ":" + blob.getOvalSize() + "}");
					} catch (Exception e) {
						System.out.println("recv error");

						try {
							os.close();
							is.close();
							dos.close();
							dis.close();
							socket.close();
							break;
						} catch (Exception e2) {}
					}
				}
			}
		});
		th.start();
	}

	/**
	 * Sends a specified message to server.
	 * @param str message to be sent
	 */

	public void send(String str) {
		byte[] send;
		send = str.getBytes();
		try {
			dos.write(send, 0, str.length());
			// dos.writeUTF(str);
			// dos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("send error");
		}
	}

	/**
	 * Generic setter for all class parameters; used only for testing.
	 * @param ID user's ID to be set
	 * @param IP server's to be set
	 * @param port server's port to be set
	 */

	public void setArgs (String ID, String IP, int port) {
		this.id = ID;
		this.ip = IP;
		this.port = port;
	}
}
