import javax.swing.*;

public class testClient {
	public static void main(String[] args){
		dudeClient srk;
		srk= new dudeClient("127.0.0.1");
		srk.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		srk.startrun();
	}
}