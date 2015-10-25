package myGameEngine.WorldObjects;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import myGameEngine.myEvents.ColorSwapEvent;
import sage.event.IEventListener;
import sage.event.IGameEvent;
import sage.scene.TriMesh;

public class MyCircle extends TriMesh implements IEventListener{

	private boolean ColorSwapIsDirty = false;
	private float[] color1;
	private float[] color2;
	
	public MyCircle(){
		double zRadLimit = (double) 2;
		double zInc = (double)1/10;
		int zSections = (int)(zRadLimit/zInc);
		
		double yRadLimit = (double) 1;
		double yInc = (double)1/10;
		int ySections = (int) (yRadLimit/yInc)+1;
		
		float[] vert = new float[zSections*ySections*3];
		color1 = new float[zSections*ySections*4];
		color2 = new float[zSections*ySections*4];
		int[] triangles = new int[6*zSections*(ySections-1)];
		
		// loop to populate vert & color
		int vertIndex = 0;
		int colorIndex = 0;
		int colorFlag = 0;
		//System.out.println("zSections = " + zSections);
		//System.out.println("ySections = " + ySections);
		
		for(double yRad = -yRadLimit/2; yRad <= yRadLimit/2; yRad += yInc){
			// rotate around y axis
			//System.out.println("yRad = " + yRad);
			float yPoint = (float)Math.sin(yRad*Math.PI);
			for(double zRad = 0; zRad < zRadLimit; zRad += zInc){
				//gererate x and z
				float xPoint = (float)(Math.cos(zRad*Math.PI) * Math.cos(yRad*Math.PI));
				float zPoint = (float)(Math.sin(zRad*Math.PI) * Math.cos(yRad*Math.PI));
				//create vert
				vert[vertIndex]= xPoint;
				vertIndex++;
				vert[vertIndex]= yPoint;
				vertIndex++;
				vert[vertIndex]= zPoint;
				vertIndex++;
				//System.out.println("Vertex" + ((vertIndex-2) / 3) + " = " + xPoint + ", " + yPoint + ", " + zPoint);

				//make color for vert
				colorFlag = colorFlag % 3;
				// alternat vertext colors
				if(colorFlag == 0){
					color1[colorIndex]=1;
				} else {
					color1[colorIndex]=Math.abs(yPoint);
				}
				color2[colorIndex]=1;
				colorIndex++;
				if(colorFlag == 1){
					color1[colorIndex]=1;
					} else {
						color1[colorIndex]=Math.abs(zPoint);
					}
				color2[colorIndex]=1;
				colorIndex++;
				if(colorFlag == 2){
					color1[colorIndex]=1;
					} else {
						color1[colorIndex]=Math.abs(xPoint);
					}
				color2[colorIndex]=1;
				colorIndex++;
				color1[colorIndex]=0;
				color2[colorIndex]=0;
				colorIndex++;
				colorFlag++;
			}
			// off set the color of each ring
			colorFlag+=3;
		}
		
		// loop to populate triangles
		int triangleIndex = 0;
		int triangleVertexIndex = 0;
		//System.out.println("triangle[].length = " + zSections*ySections*3);
		
		for(int yAxisIndex = 0; yAxisIndex < ySections -1; yAxisIndex++){
			//System.out.println("yAxis loop = " + yAxisIndex);
			
			for(int zAxisIndex = 0; zAxisIndex < zSections; zAxisIndex++){
				boolean wrapFlag = ((triangleVertexIndex + 1) % zSections == 0);
				//System.out.println("zAxis Vertex = " + zAxisIndex);
				//System.out.println("triangleVertexIndex = " + triangleVertexIndex);
				//System.out.println("wrapFlag = " + wrapFlag);
				//draw first triangle
				triangles[triangleIndex] = triangleVertexIndex;
				triangleIndex++;
				if(!wrapFlag){
					triangles[triangleIndex] = triangleVertexIndex + 1;
				} else {
					// special case for wrapping back to beggining
					triangles[triangleIndex] = triangleVertexIndex - zAxisIndex;
				}
				triangleIndex++;
				triangles[triangleIndex] = triangleVertexIndex + zSections;
				triangleIndex++;
				//System.out.println("triangle = " + triangles[triangleIndex - 3] + ", " + triangles[triangleIndex - 2] + ", " + triangles[triangleIndex -1 ]);
				
				//draw second triangle
				if(!wrapFlag){
					triangles[triangleIndex] = triangleVertexIndex + 1;
				} else {
					triangles[triangleIndex] = triangleVertexIndex - zAxisIndex;
				}
				triangleIndex++;
				
				if(!wrapFlag){
					triangles[triangleIndex] = triangleVertexIndex + zSections + 1;
				} else {
					triangles[triangleIndex] = triangleVertexIndex + 1;
				}
				triangleIndex++;
				
				triangles[triangleIndex] = triangleVertexIndex + zSections;
				triangleIndex++;
				//System.out.println("triangle = " + triangles[triangleIndex - 3] + ", " + triangles[triangleIndex - 2] + ", " + triangles[triangleIndex -1 ]);
				triangleVertexIndex++;
			}
		}
		
		FloatBuffer vertBuf =
				com.jogamp.common.nio.Buffers.newDirectFloatBuffer(vert);
		FloatBuffer colorBuf =
				com.jogamp.common.nio.Buffers.newDirectFloatBuffer(color1);
		IntBuffer triangleBuf =
				com.jogamp.common.nio.Buffers.newDirectIntBuffer(triangles);
		this.setVertexBuffer(vertBuf);
		this.setColorBuffer(colorBuf);
		this.setIndexBuffer(triangleBuf); 
	}
	
	@Override
	public boolean handleEvent(IGameEvent event) {
		if(event instanceof ColorSwapEvent){
			if(!ColorSwapIsDirty){
				if(((ColorSwapEvent) event).CheckSceneNode(this)){
					ColorSwapIsDirty = true;
					FloatBuffer colorBuf =
							com.jogamp.common.nio.Buffers.newDirectFloatBuffer(color2);
					this.setColorBuffer(colorBuf);
					return true;
				}
			}
		}
		return false;
	}

}
