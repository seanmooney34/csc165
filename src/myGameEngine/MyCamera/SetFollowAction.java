package myGameEngine.MyCamera;

import sage.input.action.*;

import net.java.games.input.Event;


public class SetFollowAction extends AbstractInputAction
{
	private boolean Following = false;
	public boolean isFollowing() { return Following; }
	
	public void performAction(float time, Event event)
	{
		Following = !Following;
	}
}