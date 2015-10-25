package myGameEngine.Avatars;

import java.util.UUID;

import graphicslib3D.Vector3D;
import myGameEngine.WorldObjects.MyPyramid;
import sage.scene.SceneNode;

public class GhostAvatar {
	
	private UUID ID;
	private TestAvatarController avatar;
	
	public GhostAvatar(UUID ghostID, Vector3D ghostPosition){
		ID = ghostID;
		avatar = new TestAvatarController(new MyPyramid());
		avatar.setWorldLocation(ghostPosition);
	}
	
	public UUID getID(){
		return ID;
	}
	
	public Vector3D getPosition(){
		return avatar.getPosition();
	}
	
	public void updatePosition(Vector3D newPosition){
		avatar.setWorldLocation(newPosition);
	}
	
	public SceneNode getAvatar(){
		return avatar.getAvatar();
	}
}
