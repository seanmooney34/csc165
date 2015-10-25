package server;

import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;
import sage.networking.server.GameConnectionServer;
import sage.networking.server.IClientInfo;

public class GameServerTCP extends GameConnectionServer<UUID> {

	public GameServerTCP(int localPort) throws IOException {
		super(localPort, ProtocolType.TCP);
	}

	public void acceptClient(IClientInfo ci, Object o) // override
	{
		String message = (String) o;
		String[] messageTokens = message.split(",");
		if (messageTokens.length > 0) {
			if (messageTokens[0].compareTo("join") == 0) // received �join�
			{ // format: join,localid
				UUID clientID = UUID.fromString(messageTokens[1]);
				addClient(ci, clientID);
				sendJoinedMessage(clientID, true);
			}
		}
	}

	public void processPacket(Object o, InetAddress senderIP, int sndPort) {
		String message = (String) o;
		String[] msgTokens = message.split(",");
		if (msgTokens.length > 0) {
			if (msgTokens[0].compareTo("bye") == 0) // receive �bye�
			{ // format: bye,localid
				UUID clientID = UUID.fromString(msgTokens[1]);
				sendByeMessages(clientID);
				removeClient(clientID);
			}
			if (msgTokens[0].compareTo("create") == 0) // receive �create�
			{ // format: create,localid,x,y,z
				UUID clientID = UUID.fromString(msgTokens[1]);
				String[] pos = { msgTokens[2], msgTokens[3], msgTokens[4] };
				sendCreateMessages(clientID, pos);
				sendWantsDetailsMessages(clientID);
			}
			if (msgTokens[0].compareTo("dsfr") == 0) // receive �details for�
			{ 
				UUID senderID = UUID.fromString(msgTokens[1]);
				UUID targetID = UUID.fromString(msgTokens[2]);
				String[] position = {msgTokens[3],msgTokens[4],msgTokens[5]};
				sndDetailsMsg(senderID,targetID, position);

			}
			if (msgTokens[0].compareTo("move") == 0)
			{
				UUID clientID = UUID.fromString(msgTokens[1]);
				String[] position = {msgTokens[3],msgTokens[4],msgTokens[5]};
				sendMoveMessages(clientID,position);
			}
		}
	}

	public void sendJoinedMessage(UUID clientID, boolean success) { 
		// format:join,	success			
		// or join, failure
		try {
			String message = new String("join,");
			if (success)
				message += "success";
			else
				message += "failure";
			sendPacket(message, clientID);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendCreateMessages(UUID clientID, String[] position) { 
		// format: create, remoteId, x, y, z
		try {
			String message = new String("create," + clientID.toString());
			message += "," + position[0];
			message += "," + position[1];
			message += "," + position[2];
			forwardPacketToAll(message, clientID);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sndDetailsMsg(UUID senderID, UUID targetID, String[] position) {
		try {
			String message = new String("dmsg," + senderID);
			message += "," + position[0];
			message += "," + position[1];
			message += "," + position[2];
			sendPacket(message, targetID);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendWantsDetailsMessages(UUID clientID) {
		try {
			String message = new String("dsfr," + clientID.toString());
			forwardPacketToAll(message, clientID);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendMoveMessages(UUID clientID, String[] position) { // etc�..
		try {
			String message = new String("move," + clientID.toString());
			message += "," + position[0];
			message += "," + position[1];
			message += "," + position[2];
			forwardPacketToAll(message, clientID);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendByeMessages(UUID clientID) { // etc�..
		try {
			String message = new String("bye," + clientID.toString());
			forwardPacketToAll(message, clientID);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
