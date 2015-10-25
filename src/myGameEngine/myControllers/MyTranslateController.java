package myGameEngine.myControllers;

import java.util.Random;

import graphicslib3D.Matrix3D;
import sage.scene.Controller;
import sage.scene.SceneNode;

public class MyTranslateController extends Controller {
	private double translationRate = .002; // movement per second
	private double cycleTime = 1000.0; // default cycle time
	private double totalTime;
	private double direction = 1.0;

	public void setCycleTime(double c) {
		cycleTime = c;
	}

	public void update(double time) // example controller
	{
		totalTime += time;
		
		if (totalTime > cycleTime) {
			direction = -direction;
			totalTime = 0.0;
		}

		double transAmount = translationRate * time * direction;
		
		Matrix3D newTrans = new Matrix3D();
		newTrans.translate(0, transAmount, 0);
		for (SceneNode node : controlledNodes) {
			Matrix3D curTrans = node.getLocalTranslation();
			curTrans.concatenate(newTrans);
			node.setLocalTranslation(curTrans);
		}
	}
}