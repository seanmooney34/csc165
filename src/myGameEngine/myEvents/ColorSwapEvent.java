package myGameEngine.myEvents;

import java.awt.Color;

import sage.event.*;
import sage.scene.SceneNode;

public class ColorSwapEvent extends AbstractGameEvent
{
	private SceneNode sn;
	private Color newColor = null;
	private boolean fired = false;
	
	public ColorSwapEvent(SceneNode s){
		sn = s;
	}
	
	public ColorSwapEvent(SceneNode s, Color c){
		sn = s;
		newColor = c;
	}
	
	public Color getColor(){
		return newColor;
	}
	
	public boolean hasFired(){
		return fired;
	}
	
	public boolean CheckSceneNode(SceneNode s){
		if(sn.equals(s)){
			fired = true;
			return true;
		}
		return false;
	}
	// this class need to exist because java sucks at callback functions
}