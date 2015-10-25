package myGameEngine.MyCamera;

import sage.camera.*;
import sage.input.action.AbstractInputAction;

import graphicslib3D.Vector3D;
import myGameEngine.MyConstants;
import graphicslib3D.Matrix3D;
import net.java.games.input.Event;

public class RotateLeftAction extends AbstractInputAction
{ 
	private ICamera camera;
	
	public RotateLeftAction(ICamera c)
	{ 
		camera = c;
	}
 
	public void performAction(float time, Event e)
	{ 
		Vector3D upDir = camera.getUpAxis().normalize();
		Vector3D viewDir = camera.getViewDirection().normalize();
		Vector3D rightDir = camera.getRightAxis().normalize();
		
		Matrix3D matrixRotation = new Matrix3D((MyConstants.DegreeOfRotation * time), upDir);
		
		camera.setAxes(rightDir.mult(matrixRotation),
				upDir.mult(matrixRotation),
				viewDir.mult(matrixRotation)
				);
	}
}
