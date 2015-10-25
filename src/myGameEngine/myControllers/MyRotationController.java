package myGameEngine.myControllers;

import java.util.Iterator;

import graphicslib3D.Matrix3D;
import sage.scene.Controller;
import sage.scene.Group;
import sage.scene.SceneNode;

public class MyRotationController extends Controller {
	private double rotationAmount = .3; // movement per second

	private void doRotation(SceneNode node, Matrix3D newRot) {
		if (node instanceof Group) {
			Iterator<SceneNode> i = ((Group) node).iterator();
			while (i.hasNext()) {
				doRotation(i.next(), newRot);
			}
		} else {
			Matrix3D curTrans = node.getLocalTranslation();
			curTrans.concatenate(newRot);
			node.setLocalTranslation(curTrans);
		}
	}
	
	public void update(double time) // example controller
	{
		Matrix3D newRot = new Matrix3D();
		newRot.rotateY(rotationAmount);
		
		for (SceneNode node : controlledNodes) {
			doRotation(node, newRot);
		}
	}
}