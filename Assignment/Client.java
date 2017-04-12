/**
 * This is the Login Client for the user to use.
 * It allows user to give themselves a nickname
 * and a username to use when chatting on the
 * IRC server.
 * 
 * @author Kelvin Cho
 * @version 1.0
 * @date April 8, 2017
 */

//import
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

//start of class
public class Client {
	
	//JEntity
	private JFrame frame;
	private JTextField login;
	private JTextField nickname;
	
	private JFrame chat;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client window = new Client();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}//end of main

	/**
	 * Create the application.
	 */
	public Client() {
		//calls the initialize
		initialize();
		/**These two below was used for testing purpose. It pre-populates the login and nickname text field
		 *with a pre-define name
		 */
		//login.setText("");
		//nickname.setText("");
	}
	//getFrame allow other class to get the frame
	public JFrame getFrame(){
		return frame;
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		login = new JTextField();
		login.setBounds(187, 150, 86, 20);
		frame.getContentPane().add(login);
		login.setColumns(10);
	
		nickname = new JTextField();
		nickname.setBounds(187, 191, 86, 20);
		frame.getContentPane().add(nickname);
		nickname.setColumns(10);
		
		JLabel lblLogin = new JLabel("Username:");
		lblLogin.setBounds(116, 153, 71, 14);
		frame.getContentPane().add(lblLogin);
		
		JLabel lblNickname = new JLabel("Nickname:");
		lblNickname.setBounds(116, 194, 61, 14);
		frame.getContentPane().add(lblNickname);
		
		JLabel lblIrcClient = new JLabel("IRC Client");
		lblIrcClient.setFont(new Font("Perpetua Titling MT", Font.PLAIN, 20));
		lblIrcClient.setBounds(166, 32, 132, 43);
		frame.getContentPane().add(lblIrcClient);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(nickname.getText().isEmpty() && login.getText().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please Enter a Username an Nickname!");
				}
				else if(nickname.getText().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please Enter a Nickname!");
				}
				else if(login.getText().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please Enter a Username!");
				}
				else{
					try {
						connect();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		btnConnect.setBounds(184, 237, 89, 23);
		frame.getContentPane().add(btnConnect);
	}// end of init
	
	/**
	 * This here class the chat client and define the connection nickname and username.
	 * @throws Exception
	 */
	public void connect() throws Exception{
		//Temp Variable
		String u = login.getText();
		String n = nickname.getText();
		
		//calls the chat client
		chat c = new chat(u,n);
		
		chat = new JFrame("IRC Chat");
		chat.getContentPane().add(c);
		
		chat.setResizable(true);
		chat.setVisible(true);
		chat .setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		chat.setBounds(100, 100, 460, 300);
		chat.getContentPane().add(c);
		//Set the visiablity to false so that they cant see the Login screen
		frame.setVisible(false);
		c.init();
		c.start();
	}//end of connect
}//end of class