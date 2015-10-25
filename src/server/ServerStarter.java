package server;

import java.io.IOException;
import java.net.InetAddress;

import javax.swing.JOptionPane;

//Server Starter
public class ServerStarter {

	public static void main(String[] args) throws IOException {
		// get port
		System.out.println("ServerStarted!");
		InetAddress IP = InetAddress.getLocalHost();
		System.out.println("Server IP address: "+IP.getHostAddress());
		int port = Integer.parseInt(JOptionPane.showInputDialog("Please input port: "));
		new GameServerTCP(port);
		System.out.println("Server Port Number: " + port);
	}
}
