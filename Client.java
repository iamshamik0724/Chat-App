import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class dudeClient extends JFrame{
	
	private JTextField textbar;
	private JTextArea window;
	private Socket connection;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String message = "";
	private String serverIP;
	
	public dudeClient(String host){
		super("client");
		textbar = new JTextField();
		serverIP = host;
		textbar.setEditable(false);
		
		textbar.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e1){
						sendmsg(e1.getActionCommand());
						textbar.setText("");
					}
					
				}
				);
		
		add(textbar,BorderLayout.SOUTH);
		
		window = new JTextArea();
		window.setEditable(false);
		window.setBackground(Color.pink);
		add(new JScrollPane(window),BorderLayout.CENTER);
		setSize(400,200);
		setVisible(true);
	}
	

	public void startrun(){
		try{
			tryconnect();
			setup();
			chat();
		}catch(EOFException ex1){
			showmsg("\nend of chat");
     	}catch(IOException ex2){
			ex2.printStackTrace();
		}finally{
			closeall();
		}
	}
	
	//try to connect
	private void tryconnect() throws IOException{
		showmsg("\nconnecting");
		connection = new Socket(InetAddress.getByName(serverIP), 6789);
		showmsg("\nconnected to "+ serverIP);
	}
	
	//settings
	private void setup() throws IOException{
		input = new ObjectInputStream(connection.getInputStream());
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		showmsg("\nsettings done");
		
	}
	
	private void chat() throws IOException{
		textedit(true);
		do{try{
			message = (String) input.readObject();
			showmsg("\n"+message);
		}catch(ClassNotFoundException ex3){
			showmsg("\nhe is crazy");
		}}while(!message.equals("Server - END"));
	}
	
	private void closeall(){
		showmsg("shuting down");
		textedit(false);
		try{		
			showmsg("\nnow closing all");
			output.close();
			input.close();
			connection.close();
		}catch(IOException ert){
			ert.printStackTrace();
		}
	}
	
	private void showmsg(final String msg){
		SwingUtilities.invokeLater(
				new Runnable(){
					public void run(){
						window.append("\n"+msg);
					}
				}
				);
	}
	
	private void textedit(final boolean tof){
		
		SwingUtilities.invokeLater(
				new Runnable(){
					public void run(){
						textbar.setEditable(tof);
					}
				}
				);
	}
	private void sendmsg(String m){
		try{
			output.writeObject("Client - "+m);
			output.flush();
			showmsg("\nClient - "+m);
		}catch(IOException abc){
			showmsg("somethings wrong");
		}
		
	}

	
	
}
	
