package myGameEngine.MyCamera;

import sage.camera.*;
import sage.input.action.*;
import sage.scene.SceneNode;
import graphicslib3D.Matrix3D;
import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import net.java.games.input.Event;
import myGameEngine.MyConstants;

import myGameEngine.MyCamera.SetSpeedAction;

public class MoveBackAction extends AbstractInputAction
{ 
	private SceneNode avatar;
	private SetSpeedAction runAction;
	
	public MoveBackAction(SceneNode a, SetSpeedAction r)
	{ 
		avatar = a;
		runAction = r;
	}
 
	public void performAction(float time, Event e)
	{ 
		// set movement amount
		float moveAmount;
		if (runAction.isRunning())
		{ 
			moveAmount = MyConstants.RunSpeed1; 
		} else { 
			moveAmount = MyConstants.MoveSpeed1; 
		}

		Matrix3D rot = avatar.getLocalRotation();
		Vector3D dir = new Vector3D(0,0,-1);
		dir = dir.mult(rot);
		dir.scale((double)(moveAmount * time));
		avatar.translate((float)dir.getX(),(float)dir.getY(),(float)dir.getZ());
	}
}
