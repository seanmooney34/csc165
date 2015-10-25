package myGameEngine.MyCamera;

import sage.camera.*;
import sage.input.action.*;
import graphicslib3D.Matrix3D;
import graphicslib3D.Vector3D;
import net.java.games.input.Event;
import myGameEngine.MyConstants;

public class RotateYAxisAction extends AbstractInputAction
{ 
	private ICamera camera;
	
	public RotateYAxisAction(ICamera c)
	{ 
		camera = c;
	}
 
	public void performAction(float time, Event e)
	{  
		if(e.getValue() < -MyConstants.DeadZone || e.getValue() > MyConstants.DeadZone){
			Vector3D upDir = camera.getUpAxis().normalize();
			Vector3D viewDir = camera.getViewDirection().normalize();
			Vector3D rightDir = camera.getRightAxis().normalize();
			
			double deg = 0;
			
			// get Direction
			if (e.getValue() < -MyConstants.DeadZone)
				{deg = MyConstants.DegreeOfRotation;} 
			else if (e.getValue() > MyConstants.DeadZone)
				{deg = -MyConstants.DegreeOfRotation;}
			
			Matrix3D matrixRotation = new Matrix3D((deg * time), rightDir);
			
			camera.setAxes(rightDir.mult(matrixRotation),
					upDir.mult(matrixRotation),
					viewDir.mult(matrixRotation)
					);
		}
	}
}
