package myGameEngine.WorldObjects;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import sage.event.IEventListener;
import sage.event.IGameEvent;
import sage.scene.TriMesh;
import myGameEngine.myEvents.ColorSwapEvent;

public class MyPyramid extends TriMesh implements IEventListener
{	
	private boolean ColorSwapIsDirty = false;	
	private static float[] vrts = new float[] {0,1,0, -1,-1,1, 1,-1,1, 1,-1,-1, -1,-1,-1};
	private static int[] triangles = new int[]{0,1,2, 0,2,3, 0,3,4, 0,4,1, 1,4,2, 4,3,2};
	private static float[] cl = new float[] {0,0,0, 0,0,0, 1,1,1, 1,1,1, 1,1,1, 1,1,1, 1,1};
	private static float[] c2 = new float[] {1,1,1, 1,1,1, 1,1,1, 1,1,1, 1,1,1, 1,1,1, 1,1};
	
	public MyPyramid()
	{ 
		this.setName("MySquare");
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
					System.out.println("MyPyramid Swap Color event called");
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