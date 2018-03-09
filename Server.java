import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class Server extends JFrame{
	private JTextField textbar;
	private JTextArea window;
	private Socket connection;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private ServerSocket server;
	
	public Server(){
		super("dude Messenger");
		textbar= new JTextField();
		textbar.setEditable(false);
		textbar.addActionListener(
			new ActionListener(){

				public void actionPerformed(ActionEvent e1) {
					sendmessage(e1.getActionCommand());
					textbar.equals("");
				}
			}	
			);
		add(textbar,BorderLayout.SOUTH);
		window = new JTextArea();
		window.setEditable(false);
		window.setBackground(Color.cyan);
		add(new JScrollPane(window),BorderLayout.CENTER);
		setSize(400,200);
		setVisible(true);
		}

	//run server
	public void startrun(){
		try{
			server = new ServerSocket(6789,100);
			while(true){
				try{
					waiting();
					setupstreams();
					chatting();
				}catch(EOFException eofexcept){
					showmessage("end of chat");
				}
				
				
			}
		}catch(IOException ioexcept){
			ioexcept.printStackTrace();
		}
		finally{
			closeall();
		}
	}
	
	//waiting method
	private void waiting() throws IOException{
		showmessage("waiting....");
		connection= server.accept();
		showmessage("\nnow connected to"+ connection.getInetAddress().getHostName());
		}
	
	private void setupstreams() throws IOException{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showmessage("\nstreams done!!");
	}
	
	private void chatting() throws IOException{
		String message = ("\nconnected");
		showmessage("\nyou can chat now");
		abletotype(true);
		do{
			try{
				message = (String) input.readObject();
				showmessage("\n"+message);
			}catch(ClassNotFoundException xx){
				showmessage("\ni am getting crap!!");
			}
			}while(!message.equals("Client - END"));
		
	}
	
	private void sendmessage(String data){
		try {
			output.writeObject("Server - "+ data);
			output.flush();
			showmessage("\nServer-"+ data);
			}catch(IOException abc){
				window.append("\not htat");
			}
	}
	
	private void showmessage(final String str){
		SwingUtilities.invokeLater(
			new Runnable(){
				public void run(){
					window.append(str);
				}
				
			}	
				
		);
	}
	
	private void closeall(){
		showmessage("\nclosing connection");
		abletotype(false);
		try{
			input.close();
			output.close();
			connection.close();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
		}
	
	private void abletotype(final boolean tof){
		SwingUtilities.invokeLater(
			new Runnable(){
				public void run(){
					textbar.setEditable(tof);
				}
				
			}	
				
		);
	}
	

}
