package myGameEngine.WorldObjects;

import java.util.Iterator;

import graphicslib3D.Matrix3D;
import myGameEngine.myEvents.ColorSwapEvent;
import sage.event.IEventListener;
import sage.event.IGameEvent;
import sage.scene.Group;
import sage.scene.SceneNode;

public class MyPerfectCircle extends Group implements IEventListener {

	private String Name;
	
	public MyPerfectCircle(String n){
		Name = n;
		//drawSegment(1, 1.5, -.5, 0, .01);
	//	drawSegment(1.5, 2, -.5, 0, .01);
		
		double NextValue = .5;
		double Inc = .01;
		// loop around y
		for(double y = -.5; y < .5; y += NextValue){
			// loop around z
			for(double z = 0; z < 2; z += NextValue){
				drawSegment(z, z + NextValue, y, y + NextValue, Inc);
			}
		}
	}

	private void drawSegment(double zRadStart, double zRadLimit, double yRadStart, double yRadLimit, double Inc){
		MySegmentCircle aStr = new MySegmentCircle(zRadStart, zRadLimit, yRadStart, yRadLimit, Inc);
		Matrix3D strM = aStr.getLocalTranslation();
		strM.translate(0,0,0);
		aStr.setLocalTranslation(strM);
		addChild(aStr);
	}
	
	public String getName(){
		return Name;
	}
	
	@Override
	public boolean handleEvent(IGameEvent event) {
		if(event instanceof ColorSwapEvent){
			if(((ColorSwapEvent) event).CheckSceneNode(this)){
				//System.out.println("PerfectCirlce color is being flipped");
				Iterator<SceneNode> i = iterator();
				while(i.hasNext()){
					SceneNode s = i.next();
					if(s instanceof MySegmentCircle)
						((MySegmentCircle) s).flipColor();
				}
			}
		}
		return false;
	}

}
