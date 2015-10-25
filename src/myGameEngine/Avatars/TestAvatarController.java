package myGameEngine.Avatars;

import graphicslib3D.Matrix3D;
import graphicslib3D.Vector3D;
import myGameEngine.WorldObjects.MyCircle;
import sage.app.BaseGame;
import sage.scene.SceneNode;

public class TestAvatarController {

	private SceneNode avatar;
	
	public TestAvatarController(SceneNode n){
		avatar = n;
		initLocation();
	}
	
	private void initLocation(){
		Matrix3D avatarM = avatar.getLocalTranslation();
		avatarM.translate(0, 0, 0);
		avatar.scale((float) 1.0, (float) 1.0, (float) 1.0);
		avatar.setLocalTranslation(avatarM);
	}
	
	public SceneNode getAvatar(){
		return avatar;
	}
	
	public Vector3D getPosition(){
		return avatar.getWorldTranslation().getCol(3);
	}
	
	public void setWorldLocation(Vector3D newPosition){
		Matrix3D newLoc = avatar.getWorldTransform();
		newLoc.setCol(3, newPosition);
		avatar.setWorldTranslation(newLoc);
	}
}
