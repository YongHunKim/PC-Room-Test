package chat_server;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ServerGUI extends JFrame implements ActionListener{
	private JTextArea jta = new JTextArea(40, 25);
	private JTextField jtf = new JTextField(25);
	//연동
	private ServerBackground server = new ServerBackground();
	public ServerGUI() {
		
		add(jta,BorderLayout.CENTER);
		add(jtf,BorderLayout.SOUTH);
		
		jtf.addActionListener(this);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		setBounds(200, 100, 400, 600);
		setTitle("서버부분");
		
		server.setGui(this);
		server.setting();
	}
	public static void main(String[] args) {
		new ServerGUI();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String msg = jtf.getText()+"\n";
		//jta.append("서버 : "+msg);
		System.out.println(msg);
		server.sendMessage(msg);
		jtf.setText("");
	}
	
	public void appendMsg(String msg) {
		jta.append(msg);
	}
}
