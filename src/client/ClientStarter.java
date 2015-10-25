package client;
import javax.swing.JOptionPane;

//Client Starter
public class ClientStarter {

	public static void main(String[] args){
		System.out.println("ClientStarted!");
		// get HostIp and port from config file or command line
		String hostIP = JOptionPane.showInputDialog("Please input hostIP: ");
		int port = Integer.parseInt(JOptionPane.showInputDialog("Please input port: "));
		
		new MyNetworkingClient(hostIP, port).start();;
	}
}
