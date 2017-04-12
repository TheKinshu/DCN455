/**
 * Chat client and Connection client.
 * This client connects to the freenode.net
 * server using a pre-set channel.
 * Allowing user to communicate to others via
 * IRC.
 * 
 * @author Kelvin Cho
 * @version 1.0
 * @date April 8, 2017
 */

//imports
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.*;


public class chat extends JApplet implements Runnable{

	//Thread
	private Thread th;

	//JEntities
	private JTextField messageBox;
	private JTextArea textArea;
	
	//Buffer
	private BufferedWriter writer;
	private BufferedReader reader;
	
	//Socket
	private Socket socket;

	//String Variables
	private String user;
	private String nick;
	
	private String server;
	private String channel;


	/**
	 * Constructor
	 * @param u, n - The take in string variable through the Client.java 
	 *				 allowing them to set a username and nickname.
	 */
	public chat(String u, String n) {
		user = u;
		nick = n;
	}

	/**
	 * This starts the applet
	 */
	public void start(){
		th = new Thread(this);
		th.start();
	}

	/**
	 * This stops the applet
	 */
	public void stop(){
		th = new Thread(this);
		th.stop();
	}

	/**
	 * This stops the applet
	 */
	public void destroy(){
		th.stop();
	}

	/**
	 * This initializes the GUI
	 */
	public void init(){

		this.setSize(450, 300);

		getContentPane().setLayout(null);

		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setBackground(new Color(204, 204, 204));
		textArea.setEditable(false);
		textArea.setBounds(0, 0, 444, 200);
		JScrollPane sb = new JScrollPane(); 

		getContentPane().add(sb);
		sb.setBounds(0, 0, 444, 200);
		sb.setViewportView(textArea);

		messageBox = new JTextField();
		messageBox.setBackground(new Color(204, 204, 204));
		messageBox.setBounds(10, 226, 247, 20);
		getContentPane().add(messageBox);
		messageBox.setEditable(false);
		messageBox.setColumns(10);

		server = "irc.freenode.net";
		
		try{
			socket = new Socket(server, 6667);
			writer = new BufferedWriter(
					new OutputStreamWriter(socket.getOutputStream( )));
			reader = new BufferedReader(
					new InputStreamReader(socket.getInputStream( )));

		}catch(IOException e){
			e.printStackTrace();
		}

		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String msg = messageBox.getText();
				if(!messageBox.getText().isEmpty()){
					//textArea.append(msg + "\n");
					sendMessage(msg);
				}
			}
		});
		btnSend.setBounds(315, 225, 89, 23);
		getContentPane().add(btnSend);
		
		JLabel lblEnterMessage = new JLabel("Enter Message:");
		lblEnterMessage.setBounds(10, 211, 89, 14);
		getContentPane().add(lblEnterMessage);
	}

	/**
	 * This is where the actual code is run.
	 * It connects to the server and allowing user 
	 * to recieve information via IRC
	 */
	@Override
	public void run() {		

		//Channel Name
		channel = "#DCNAssignment";

		try {
			writer.write("NICK " + nick + "\r\n");
			writer.write("USER " + user + " 8 * : Java IRC Client\r\n");
			writer.flush( );

			textArea.append("Connecting to server...\n");

			String line = null;
			while ((line = reader.readLine( )) != null) {
				//This if statement will be true if the client has connected to the channel
				if (line.indexOf("004") >= 0) {
					break;
				}
				//This statement only works if the user nickname is already used
				else if (line.indexOf("433") >= 0) {
					textArea.append("Nickname is already in use.\n");
					return;
				}
			}
			
			//join the channel that was specified
			writer.write("JOIN " + channel + "\r\n");
			//clear the buffer
			writer.flush( );
			
			//Tells the user they have connected to the channel
			textArea.append("Connected to channel: " + channel + "\n");
			
			//calls chatting so the client can start recieving messages
	        chatting(writer, reader);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}//end of run
	//This is where it reads the message from the server
	public void chatting(BufferedWriter writer, BufferedReader reader) throws IOException{
		String line = null;
		ableToType(true);
		
		do{
			try{
				showMessage("\n" + line);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}while((line = reader.readLine()) != null);
	}//end of chatting
	
	//Show message to the user by append the income message and display in the textArea
	private void showMessage(String msg) {
		SwingUtilities.invokeLater(
				new Runnable(){
					public void run(){
						 textArea.append(msg);
					}
				}
		);
	}
	//allow user to type in the messagebox
	private void ableToType(boolean b) {
		SwingUtilities.invokeLater(
				new Runnable(){
					public void run(){
						messageBox.setEditable(b);
					}
				}
		);
	}//end of ableToType
	
	//send the message to the server and clear the message box
	private void sendMessage(String message) {
		try{
            writer.write("PRIVMSG " + channel + " :" + message + "\r\n");
            textArea.append("\n<"+ nick +">" + message);
			writer.flush();
			messageBox.setText(null);
		}
		catch(IOException io){
			io.printStackTrace();
		}
	}//end of sendMessage
}//end of class
