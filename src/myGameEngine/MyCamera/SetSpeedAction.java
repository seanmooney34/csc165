package myGameEngine.MyCamera;

import sage.input.action.*;

import net.java.games.input.Event;


public class SetSpeedAction extends AbstractInputAction
{
	private boolean running = false;
	public boolean isRunning() { return running; }
	
	public void performAction(float time, Event event)
	{
		//System.out.println("Set Speed Action fired");
		running = !running;
	}
}