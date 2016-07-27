package chat_server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.swing.text.html.HTMLDocument.Iterator;

public class ServerBackground {
	
	//이슈1. 클라이언트에서 서버로 메시지 주고 받기
	//이슈2. GUI
	//이슈3. 연동
	
	private ServerSocket serverSocket;
	private Socket socket;
	private ServerGUI gui;
	private String msg;
	//사용자들의 정보를 저장하는 맵
	private Map<String, DataOutputStream> clientMap = new HashMap<String, DataOutputStream>();
	
	
	public void setting(){
		try {
			//맵 교통정리
			Collections.synchronizedMap(clientMap);
			
			serverSocket = new ServerSocket(7777);
			
			while(true){
				//1.방문자를 계속 받아서 쓰레드 리시버를 생성
				System.out.println("대기중...");
				socket = serverSocket.accept();
				System.out.println(socket.getInetAddress()+"에서 접속했습니다.");
				
				Receiver receiver = new Receiver(socket);
				receiver.start();
			}
			
			//쓰레드 클래스 생성
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}


	public void setGui(ServerGUI gui) {
		this.gui = gui;
	}


	public static void main(String[] args) {
		ServerBackground serverBackground = new ServerBackground();
		serverBackground.setting();
	}


	public void sendMessage(String msg) {
		java.util.Iterator<String> it = clientMap.keySet().iterator();
		String key;
		while(it.hasNext()){
			try {
				key = it.next();
				clientMap.get(key).writeUTF(key+" : "+msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	class Receiver extends Thread{
		private DataInputStream in;
		private DataOutputStream out;
		private String nick;
		
		//2.네트워크 소켓을 받아서 계속 듣고 요청하는일
		public Receiver(Socket socket) {
			try {
				
				out = new DataOutputStream(socket.getOutputStream());
				in = new DataInputStream(socket.getInputStream());
				
				//클라이언트 아이디 받아오기
				nick = in.readUTF();
				addClient(nick, out);
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		@Override
		public void run() {

			try {
				//지속해서 메시지 듣기
				while(in != null){
					msg = in.readUTF();
					//gui.appendMsg("클라이언트 : "+msg);
					sendMessage(msg);
					gui.appendMsg(msg);
					
				}
			} catch (IOException e) {
				removeClient(nick);
			}
			
		}
	}

	public void addClient(String nick, DataOutputStream out) {
		clientMap.put(nick, out);
	}
	
	public void removeClient(String nick){
		clientMap.remove(nick);
	}
	
	//메시지 내용 전파
}
