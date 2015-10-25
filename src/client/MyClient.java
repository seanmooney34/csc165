package client;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.UUID;
import java.util.Vector;

import graphicslib3D.Vector3D;
import myGameEngine.Avatars.GhostAvatar;
import sage.networking.client.GameConnectionClient;
/*
 * This class communicates inbetween the gameworld and the server
 */
public class MyClient extends GameConnectionClient {
	private MyNetworkingClient game;
	private UUID id;
	private Vector<GhostAvatar> ghostAvatars;

	public MyClient(InetAddress remAddr, int remPort, ProtocolType pType, MyNetworkingClient game) throws IOException {
		super(remAddr, remPort, pType);
		this.game = game;
		this.id = UUID.randomUUID();
		this.ghostAvatars = new Vector<GhostAvatar>();
	}

	private void createGhostAvatar(UUID ghostID, Vector3D ghostPosition) {
		GhostAvatar avatar = new GhostAvatar(ghostID, ghostPosition);
		ghostAvatars.addElement(avatar);
		game.addToGameWorld(avatar.getAvatar());
	}
	
	private void removeGhostAvatar(UUID ghostID) {
		GhostAvatar avatar = getGhost(ghostID);
		ghostAvatars.remove(avatar);
		game.removeFromGameWorld(avatar.getAvatar());
	}

	private void updateGhostAvatar(UUID ghostID, Vector3D ghostPosition){
		GhostAvatar avatar = getGhost(ghostID);
		avatar.updatePosition(ghostPosition);
	}
	
	private GhostAvatar getGhost(UUID ghostID){
		Iterator<GhostAvatar> iterator = ghostAvatars.iterator();
		GhostAvatar avatar = null;
		boolean Discovered = false;
		// loop untill avatar is discovered or end of iterator
		while(!Discovered && iterator.hasNext()){
			avatar = iterator.next();
			if(avatar.getID() == ghostID){
				Discovered = true;
			}
		}
		return avatar;
	}

	
	protected void processPacket(Object msg) // override
	{ // extract incoming message into substrings. Then process:
		
		String[] msgTokens = ((String)msg).toLowerCase().split(",");
		
		if (msgTokens[0].compareTo("join") == 0) // receive “join”
		{ // format: join, success or join, failure
			//System.out.println("Resieved Join Message");
			if (msgTokens[1].compareTo("success") == 0) {
				game.setIsConnected(true);
				sendCreateMessage(game.getPlayerPosition());
			}
			if (msgTokens[1].compareTo("failure") == 0)
				game.setIsConnected(false);
		}
		// a client left the server
		if (msgTokens[0].compareTo("bye") == 0) // receive “bye”
		{ // format: bye, remoteId
			//System.out.println("Resieved Bye Message");
			UUID ghostID = UUID.fromString(msgTokens[1]);
			removeGhostAvatar(ghostID);
		}
		// a client joined the server
		if (msgTokens[0].compareTo("create") == 0) // receive “create”
		{ 
			//System.out.println("Resieved Create Message");
			UUID ghostID = UUID.fromString(msgTokens[1]);
			float x = Float.valueOf(msgTokens[2]);
			float y = Float.valueOf(msgTokens[3]);
			float z = Float.valueOf(msgTokens[4]);
			Vector3D ghostPosition = new Vector3D(x,y,z);
			createGhostAvatar(ghostID, ghostPosition);
		}
		// Give details to server
		if (msgTokens[0].compareTo("dsfr") == 0) // receive “wants…”
		{ 
			//System.out.println("Resieved Wants Data Message");
			UUID ghostID = UUID.fromString(msgTokens[1]);
			sendDetailsForMessage(ghostID, game.getPlayerPosition());
		}
		
		if (msgTokens[0].compareTo("dmsg") == 0) // receive “detail message”
		{ 
			UUID ghostID = UUID.fromString(msgTokens[1]);
			float x = Float.valueOf(msgTokens[2]);
			float y = Float.valueOf(msgTokens[3]);
			float z = Float.valueOf(msgTokens[4]);
			Vector3D ghostPosition = new Vector3D(x,y,z);
			createGhostAvatar(ghostID, ghostPosition);

		}
		
		if (msgTokens[0].compareTo("move") == 0) // receive “move”
		{ 
			UUID ghostID = UUID.fromString(msgTokens[1]);
			float x = Float.valueOf(msgTokens[2]);
			float y = Float.valueOf(msgTokens[3]);
			float z = Float.valueOf(msgTokens[4]);
			Vector3D ghostPosition = new Vector3D(x,y,z);
			updateGhostAvatar(ghostID,ghostPosition);
		}
	}
	
	public void sendCreateMessage(Vector3D pos) { // format: (create, localId,
													// x,y,z)
		//System.out.println("Send Create Message");
		try {
			String message = new String("create," + id.toString());
			message += "," + pos.getX() + "," + pos.getY() + "," + pos.getZ();
			sendPacket(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendJoinMessage() { // format: join, localId
		//System.out.println("Send Join Message");
		try {
			sendPacket(new String("join," + id.toString()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendByeMessage() { // etc…..
		//System.out.println("Send Bye Message");
		try {
			sendPacket(new String("bye," + id.toString()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendDetailsForMessage(UUID remId, Vector3D pos) { // etc…..
		//System.out.println("Send Details For Message");
		try {
			String message = new String("dsfr," + id.toString());
			message += "," + remId + "," + pos.getX() + "," + pos.getY() + "," + pos.getZ();
			sendPacket(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendMoveMessage(Vector3D pos) 
	{
		try {
			String message = new String("move," + id.toString());
			message += "," + pos.getX() + "," + pos.getY() + "," + pos.getZ();
			sendPacket(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
