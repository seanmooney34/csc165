package myGameEngine.myControllers;

import java.util.Iterator;

import graphicslib3D.Matrix3D;
import sage.scene.Controller;
import sage.scene.Group;
import sage.scene.SceneNode;

public class MySizeController extends Controller {

	private boolean fire = false;
	private boolean scaleState = false;
	
	public void setFireFlag(){
		fire = true;
	}
	
	public void update(double time) // example controller
	{
		if(fire){
			fire = false;
			for (SceneNode node : controlledNodes) {
				if(!scaleState){
					Matrix3D newSize = new Matrix3D();
					newSize.scale((float)0.5, (float)0.5, (float)0.5);
					Matrix3D oldSize = node.getLocalScale();
					newSize.concatenate(oldSize);
					node.setLocalScale(newSize);
				} else {
					Matrix3D newSize = new Matrix3D();
					newSize.scale((float)2.0, (float)2.0, (float)2.0);
					Matrix3D oldSize = node.getLocalScale();
					newSize.concatenate(oldSize);
					node.setLocalScale(newSize);
					//node.scale((float)1.0, (float)1.0, (float)1.0);
				}
			}
			scaleState = !scaleState;
		}
	}
}