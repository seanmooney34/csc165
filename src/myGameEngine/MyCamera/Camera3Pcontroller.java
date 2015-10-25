package myGameEngine.MyCamera;

import graphicslib3D.Matrix3D;
import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import myGameEngine.MyConstants;
import sage.camera.ICamera;
import sage.input.IInputManager;
import sage.input.action.AbstractInputAction;
import sage.input.action.IAction;
import sage.scene.SceneNode;
import sage.util.MathUtils;

public class Camera3Pcontroller
{
	private ICamera cam; //the camera being controlled
	 private SceneNode target; //the target the camera looks at
	 private float cameraAzimuth; //rotation of camera around target Y axis
	 private float cameraElevation; //elevation of camera above target
	 private float cameraDistanceFromTarget;
	 private Point3D targetPos; // avatar’s position in the world
	 private Vector3D worldUpVec;
	 
	 public Camera3Pcontroller(ICamera cam, SceneNode target,
			 IInputManager inputMgr, String controllerName)
	 { 
		 this.cam = cam;
		 this.target = target;
		 worldUpVec = new Vector3D(0,1,0);
		 cameraDistanceFromTarget = 15.0f;
		 cameraAzimuth = 180; // start from BEHIND and ABOVE the target
		 cameraElevation = 20.0f; // elevation is in degrees
		 update(0.0f); // initialize camera state
		 setupInput(inputMgr, controllerName);
	 }
	 
	 public void update(float time)
	 {
		 updateTarget();
		 updateCameraPosition();
		 cam.lookAt(targetPos, worldUpVec); // SAGE built-in function
	 }
	 
	 private void updateTarget()
	 { 
		 targetPos = new Point3D(target.getWorldTranslation().getCol(3)); 
	 }
	 
	 private void updateCameraPosition()
	 {
		 double theta = cameraAzimuth;
		 double phi = cameraElevation ;
		 double r = cameraDistanceFromTarget;
		 // calculate new camera position in Cartesian coords
		 Point3D relativePosition = MathUtils.sphericalToCartesian(theta, phi, r);
		 Point3D desiredCameraLoc = relativePosition.add(targetPos);
		 cam.setLocation(desiredCameraLoc);
	 }
	 
	 private void setupInput(IInputManager im, String cn)
	 {
		SetFollowAction setFollow = new SetFollowAction();
		im.associateAction(cn,
				net.java.games.input.Component.Identifier.Button._0,
				setFollow,
				IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
			
		SetSpeedAction setSpeed = new SetSpeedAction();
		im.associateAction(cn,
				net.java.games.input.Component.Identifier.Button._1,
				setSpeed,
				IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
	 
		 IAction orbitAction = new OrbitAroundAction();
		 im.associateAction(cn, 
				 net.java.games.input.Component.Identifier.Axis.RX, 
				 orbitAction, 
				 IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

		 IAction rotateAction = new RotateAroundAction();
		 im.associateAction(cn, 
				 net.java.games.input.Component.Identifier.Axis.RY, 
				 rotateAction, 
				 IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

		 IAction zoomAction = new ZoomAction();
		 im.associateAction(cn, 
				 net.java.games.input.Component.Identifier.Axis.Z, 
				 zoomAction, 
				 IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		 
		 
		 IAction moveYAxisAction = new MoveYAxisAction(setSpeed, setFollow);
		 im.associateAction(cn, 
				 net.java.games.input.Component.Identifier.Axis.Y, 
				 moveYAxisAction, 
				 IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

		 IAction moveXAxisAction = new MoveXAxisAction(setSpeed, setFollow);
		 im.associateAction(cn, 
				 net.java.games.input.Component.Identifier.Axis.X, 
				 moveXAxisAction, 
				 IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
	 }
	 
	 private class OrbitAroundAction extends AbstractInputAction
	 {
		 public void performAction(float time, net.java.games.input.Event e)
		 {
			 if(e.getValue() < -MyConstants.DeadZone || e.getValue() > MyConstants.DeadZone){
			
				 float rotAmount = (float) 0.0;
				 
				 if (e.getValue() < -MyConstants.DeadZone)
					{rotAmount = (float) MyConstants.DegreeOfRotation;} 
				 else if (e.getValue() > MyConstants.DeadZone)
					{rotAmount = (float) -MyConstants.DegreeOfRotation;}

				 cameraAzimuth += rotAmount ;
				 cameraAzimuth = cameraAzimuth % 360; 
				 
			 }
		 }
	 }
	 
	 private class RotateAroundAction extends AbstractInputAction
	 {
		 public void performAction(float time, net.java.games.input.Event e)
		 {
			 if(e.getValue() < -MyConstants.DeadZone || e.getValue() > MyConstants.DeadZone){

				 float rotAmount = (float) 0.0;
				 
				 if (e.getValue() < -MyConstants.DeadZone)
					{rotAmount = (float) -MyConstants.DegreeOfRotation;} 
				 else if (e.getValue() > MyConstants.DeadZone)
					{rotAmount = (float) MyConstants.DegreeOfRotation;}
				 

				 float newElevation = cameraElevation + rotAmount;
				 
				 if(newElevation > 10 && newElevation < 90){
					 cameraElevation = newElevation;
				 }
			 }
		 }
	 }
	 
	 private class ZoomAction extends AbstractInputAction
	 {
		 public void performAction(float time, net.java.games.input.Event e)
		 {
			 if(e.getValue() < -MyConstants.DeadZone || e.getValue() > MyConstants.DeadZone){
				 float rotAmount = (float) 0.0;
				 
				 if (e.getValue() < -MyConstants.DeadZone)
					{rotAmount = (float) MyConstants.DegreeOfRotation;} 
				 else if (e.getValue() > MyConstants.DeadZone)
					{rotAmount = (float) -MyConstants.DegreeOfRotation;}
				 
				 float newDistance = cameraDistanceFromTarget + rotAmount;
				 
				 if(newDistance > 3 && newDistance < 30){
					 cameraDistanceFromTarget = newDistance;
				 }
				 
			 }
		 }
	 }
 
	 private class MoveYAxisAction extends AbstractInputAction
	 { 
	 	private SetSpeedAction runAction;
	 	private SetFollowAction followAction;
	 	
	 	public MoveYAxisAction(SetSpeedAction r, SetFollowAction f)
	 	{ 
	 		runAction = r;
	 		followAction = f;
	 	}
	  
	 	public void performAction(float time, net.java.games.input.Event e)
	 	{ 
	 		if(e.getValue() < -MyConstants.DeadZone || e.getValue() > MyConstants.DeadZone){
	 			Vector3D dir = new Vector3D(0,0,0);
	 			// set movement amount
	 			float moveAmount;
	 			if (runAction.isRunning())
	 			{ 
	 				moveAmount = MyConstants.RunSpeed1; 
	 			} else { 
	 				moveAmount = MyConstants.MoveSpeed1; 
	 			}
	 			
	 			double direction = 0;
	 			
	 			// get Direction
	 			if (e.getValue() < -MyConstants.DeadZone)
	 			 { 
	 				direction = 1; 
	 			 }
	 			 else 
	 			 { 
	 				 if (e.getValue() > MyConstants.DeadZone)
	 				 { 
	 					 direction = -1 ;
	 				 }
	 			 }

 				dir = new Vector3D(0,0,direction);
 				
	 			if(followAction.isFollowing()){
	 				Matrix3D cameraHeading = new Matrix3D(((cameraAzimuth + 180) % 360), new Vector3D(0,1,0));
	 				target.setLocalRotation(cameraHeading);
	 			}

	 			Matrix3D rot = target.getLocalRotation();
	 			
	 			dir = dir.mult(rot);
	 			dir.scale((double)(moveAmount * time));
	 			target.translate((float)dir.getX(),(float)dir.getY(),(float)dir.getZ());
	 		}
	 	}

	 }
	 
	 private class MoveXAxisAction extends AbstractInputAction
	 { 
	 	private SetSpeedAction runAction;
	 	private SetFollowAction followAction;
	 	
	 	public MoveXAxisAction(SetSpeedAction r, SetFollowAction f)
	 	{ 
	 		runAction = r;
	 		followAction = f;
	 	}
	  
	 	public void performAction(float time, net.java.games.input.Event e)
	 	{ 
	 		if(e.getValue() < -MyConstants.DeadZone || e.getValue() > MyConstants.DeadZone){
	 			Vector3D dir = new Vector3D(0,0,0);;
	 			
	 			// set movement amount
	 			float moveAmount;
	 			if (runAction.isRunning())
	 			{ 
	 				moveAmount = MyConstants.RunSpeed1; 
	 			} else { 
	 				moveAmount = MyConstants.MoveSpeed1; 
	 			}
	 			double direction = 0;
	 			// get Direction
	 			if (e.getValue() < -MyConstants.DeadZone)
	 			 { 
	 				direction = 1; 
	 			 }
	 			 else 
	 			 { 
	 				 if (e.getValue() > MyConstants.DeadZone)
	 				 { 
	 					direction = -1; 
	 				 }
	 			 }
	 			
	 			dir = new Vector3D(direction,0,0); 
	 			
	 			if(followAction.isFollowing()){
	 				Matrix3D cameraHeading = new Matrix3D(((cameraAzimuth + 180) % 360), new Vector3D(0,1,0));
	 				target.setLocalRotation(cameraHeading);
	 			}

	 			Matrix3D rot = target.getLocalRotation();
	 			//rot.rotateZ((float)direction);
	 			dir = dir.mult(rot);
	 			dir.scale((double)(moveAmount * time));
	 			target.translate((float)dir.getX(),(float)dir.getY(),(float)dir.getZ());
	 		}
	 	}

	 }
}