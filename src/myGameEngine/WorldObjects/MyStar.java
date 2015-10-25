package myGameEngine.WorldObjects;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import sage.event.IEventListener;
import sage.event.IGameEvent;
import sage.scene.TriMesh;
import myGameEngine.myEvents.ColorSwapEvent;
import myGameEngine.myEvents.CrashEvent;

public class MyStar extends TriMesh implements IEventListener
{							
	private boolean ColorSwapIsDirty = false;
	private static float[] vrts = new float[] {0,3,0, -1,1,1,   1,1,1,  1,1,-1, -1,1,-1,
											   0,-3,0, -1,-1,1, 1,-1,1, 1,-1,-1, -1,-1,-1,
											   
											   3,0,0,  1,-1,1,  1,1,1,  1,1,-1,  1,-1,-1,
											   -3,0,0, -1,-1,1, -1,1,1, -1,1,-1, -1,-1,-1,
											   
											   0,0,3,  1,-1,1,  1,1,1,  -1,1,1,  -1,-1,1,
											   0,0,-3, 1,-1,-1, 1,1,-1, -1,1,-1, -1,-1,-1};
	
	private static float[] cl = new float[] {1,1,1,1, 1,0,0,1, 0,1,0,1, 0,0,1,1, 0,1,1,1,
											 1,1,1,0, 0,1,0,0, 0,0,1,0, 1,0,0,0, 1,0,1,0,
											 1,1,1,0, 0,0,1,0, 1,0,0,0, 0,1,0,0, 1,1,0,0,
											 1,1,1,0, 1,0,0,0, 0,1,0,0, 0,0,1,0, 0,1,1,0,
											 1,1,1,0, 0,1,0,0, 0,0,1,0, 1,0,0,0, 1,0,1,0,
											 1,1,1,0, 0,0,1,0, 1,0,0,0, 0,1,0,0, 1,1,0,0};
	
	private static float[] c2 = new float[] {1,1,1,1, 1,1,1,1, 1,1,1,1, 1,1,1,1, 1,1,1,1,
			 1,1,1,1, 1,1,1,1, 1,1,1,1, 1,1,1,1, 1,1,1,1,
			 1,1,1,1, 1,1,1,1, 1,1,1,1, 1,1,1,1, 1,1,1,1,
			 1,1,1,1, 1,1,1,1, 1,1,1,1, 1,1,1,1, 1,1,1,1,
			 1,1,1,1, 1,1,1,1, 1,1,1,1, 1,1,1,1, 1,1,1,1,
			 1,1,1,1, 1,1,1,1, 1,1,1,1, 1,1,1,1, 1,1,1,1};

	private static int[] triangles = new int[]{0,1,2, 0,2,3, 0,3,4, 0,4,1,
											   5,6,7, 5,7,8, 5,8,9, 5,9,6,
											   
											   10,11,12, 10,12,13, 10,13,14, 10,14,11,
											   15,16,17, 15,17,18, 15,18,19, 15,19,16,
											   
											   20,21,22, 20,22,23, 20,23,24, 20,24,21,
											   25,26,27, 25,27,28, 25,28,29, 25,29,26};
	
	public MyStar()
	{ 
		FloatBuffer vertBuf =
				com.jogamp.common.nio.Buffers.newDirectFloatBuffer(vrts);
		FloatBuffer colorBuf =
				com.jogamp.common.nio.Buffers.newDirectFloatBuffer(cl);
		IntBuffer triangleBuf =
				com.jogamp.common.nio.Buffers.newDirectIntBuffer(triangles);
		this.setVertexBuffer(vertBuf);
		this.setColorBuffer(colorBuf);
		this.setIndexBuffer(triangleBuf); 
	} 
	
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