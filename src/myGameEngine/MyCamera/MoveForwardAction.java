package myGameEngine.MyCamera;

import sage.camera.*;
import sage.input.action.*;
import sage.scene.SceneNode;
import graphicslib3D.Matrix3D;
import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import net.java.games.input.Event;
import myGameEngine.MyConstants;

public class MoveForwardAction extends AbstractInputAction
{ 
	private SceneNode avatar;
	private ICamera camera;
	private SetSpeedAction runAction;
	private SetFollowAction followAction;
	
	public MoveForwardAction(SceneNode a, ICamera c, SetSpeedAction r, SetFollowAction f)
	{ 
		avatar = a;
		runAction = r;
		followAction = f;
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
		
		Vector3D dir = new Vector3D(0,0,1);
		
		dir = dir.mult(rot);
		dir.scale((double)(moveAmount * time));
		avatar.translate((float)dir.getX(),(float)dir.getY(),(float)dir.getZ());
	}
}
