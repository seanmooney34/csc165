package server;

import java.io.IOException;
import java.net.Socket;

public class ClientHandlerThread extends Thread {

	private Socket clientSock;
	public ClientHandlerThread(Socket client){
		clientSock = client;
	}
	
	@Override
	public void run() 
	{
		handleClient(clientSock); //talk to client until done
		try {
			clientSock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	private void handleClient(Socket clientSock2) {
		// TODO Auto-generated method stub
		
	}

}
