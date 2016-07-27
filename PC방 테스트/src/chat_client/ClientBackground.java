package chat_client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import chat_server.ServerGUI;

public class ClientBackground {
	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;
	private ClientGUI gui;
	private ServerGUI sgui;
	private String msg;
	private String nickName;
	
	public void setGui(ClientGUI gui) {
		this.gui = gui;
	}

	public void connect(){
		try {
			socket = new Socket("127.0.0.1", 7777);
			System.out.println("서버 연결 됨.");
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());
			
			out.writeUTF(nickName);
			System.out.println("메시지 전송 완료");
			
			while(in!=null){
				msg = in.readUTF();
				gui.appendMsg("서버 : "+msg);
			}
			
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		ClientBackground clientBackground = new ClientBackground();
		clientBackground.connect();
	}

	public void sendMessage(String msg2) {
		try {
			out.writeUTF(msg2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
		
	}
}
