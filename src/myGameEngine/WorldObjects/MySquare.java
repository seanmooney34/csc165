package myGameEngine.WorldObjects;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import sage.event.IEventListener;
import sage.event.IGameEvent;
import sage.scene.TriMesh;
import myGameEngine.myEvents.ColorSwapEvent;
import myGameEngine.myEvents.CrashEvent;

public class MySquare extends TriMesh implements IEventListener
{

	private boolean ColorSwapIsDirty = false;
	private static float[] vrts = new float[] {-1,1,1, -1,1,-1,   1,1,-1,   1,1,1, 
											   -1,-1,1, -1,-1,-1, 1,-1,-1,  1,-1,1 };
	private static float[] c1 = new float[]   {1,0,0,0, 0,1,0,0, 1,0,0,0, 0,1,0,0, 
											   0,1,0,0, 1,0,0,0, 0,1,0,0, 1,0,0,0};
	private static float[] c2 = new float[]   {1,1,1,1, 1,1,1,1, 1,1,1,1, 1,1,1,1, 
			   									1,1,1,1, 1,1,1,1, 1,1,1,1, 1,1,1,1};
	private static int[] squares = new int[]  {0,4,3, 4,7,3,
											   3,2,6, 3,7,6,
											   2,5,6, 2,1,5,
											   0,1,5, 0,4,5,
											   0,3,1, 1,2,3,
											   5,6,7, 5,4,7};
	
	public MySquare()
	{ 
		this.setName("MySquare");
		FloatBuffer vertBuf =
				com.jogamp.common.nio.Buffers.newDirectFloatBuffer(vrts);
		FloatBuffer colorBuf =
				com.jogamp.common.nio.Buffers.newDirectFloatBuffer(c1);
		IntBuffer squareBuf =
				com.jogamp.common.nio.Buffers.newDirectIntBuffer(squares);
		this.setVertexBuffer(vertBuf);
		this.setColorBuffer(colorBuf);
		this.setIndexBuffer(squareBuf); 
	} 

	@Override

	public boolean handleEvent(IGameEvent event) {
		if(event instanceof ColorSwapEvent){
			if(!ColorSwapIsDirty){
				if(((ColorSwapEvent) event).CheckSceneNode(this)){
					ColorSwapIsDirty = true;
					FloatBuffer colorBuf =
							com.jogamp.common.nio.Buffers.newDirectFloatBuffer(c2);
					this.setColorBuffer(colorBuf);
					return true;
				}
			}
		}
		return false;
	}
}