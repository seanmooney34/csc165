package myGameEngine.MyCamera;

import sage.camera.*;
import sage.input.action.*;

import graphicslib3D.Vector3D;
import graphicslib3D.Point3D;
import net.java.games.input.Event;

public class ResetCameraAction extends AbstractInputAction{
	private ICamera camera;
	private Point3D location;
	private Vector3D upAxis;
	private Vector3D rightAxis;
	private Vector3D viewAxis;
	
	public ResetCameraAction(ICamera c){
		camera = c;
		upAxis = camera.getUpAxis();
		rightAxis = camera.getRightAxis();
		viewAxis = camera.getViewDirection();
		location = camera.getLocation();
	}

	@Override
	public void performAction(float arg0, Event arg1) {
		camera.setLocation(location);
		
		camera.setAxes(rightAxis,upAxis,viewAxis);
	}

}
