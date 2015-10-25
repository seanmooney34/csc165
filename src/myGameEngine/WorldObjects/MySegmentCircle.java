package myGameEngine.WorldObjects;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import sage.event.IEventListener;
import sage.event.IGameEvent;
import sage.scene.TriMesh;

public class MySegmentCircle extends TriMesh implements IEventListener{
	private float[] color1;
	private float[] color2;
	private boolean colorPick = true;
	
	
	public MySegmentCircle(double zRadStart, double zRadLimit, double yRadStart, double yRadLimit, double Inc){
		
		//for Debugging
//		zRadStart = 1;
//		zRadLimit = 2;
//		yRadStart = -.5;
//		yRadLimit = 0;
//		Inc = (double).03;
		
		int zSections = (int)(Math.abs(zRadLimit - zRadStart)/Inc) + 1;
		int ySections = (int) (Math.abs(yRadLimit - yRadStart)/Inc) + 1;

//		System.out.println("zSections.size = " + zSections);
//		System.out.println("zSections.Math.size = " + ((Math.abs(zRadLimit - zRadStart)/Inc) + 1));
//		System.out.println("ySections.size = " + ySections);
		
		float[] vert = new float[zSections*ySections*3];
		color1 = new float[zSections*ySections*4];
		color2 = new float[zSections*ySections*4];
		int[] triangles = new int[6*(zSections)*(ySections)];
//		
//		System.out.println("vert.size = " + (zSections*ySections*3));
//		System.out.println("color1.size = " + (zSections*ySections*4));
//		System.out.println("triangles.size = " + (6*(zSections - 1)*(ySections - 1)));
		
		// loop to populate vert & color
		int vertIndex = 0;
		int colorIndex = 0;
		int colorFlag = 0;
		
		for(double yRad = yRadStart; yRad <= yRadLimit; yRad = new BigDecimal(yRad + Inc).setScale(2, RoundingMode.HALF_EVEN).doubleValue()){
			float yPoint = (float)Math.sin(yRad*Math.PI);
			for(double zRad = zRadStart; zRad <= zRadLimit; zRad = new BigDecimal(zRad + Inc).setScale(2, RoundingMode.HALF_EVEN).doubleValue()){
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
				
				//System.out.println("zRad = " + zRad + " Vertex" + ((vertIndex-2)/3) + " = " + xPoint + ", " + yPoint + ", " + zPoint);
				
				//make color for vert
				colorFlag = colorFlag % 3;
				// alternat vertext colors
				if(colorFlag == 0){
					color1[colorIndex]=1;
					color2[colorIndex]=0;
				} else {
					color1[colorIndex]=Math.abs(yPoint);
					color2[colorIndex]=Math.abs(xPoint);
				}
				colorIndex++;
				if(colorFlag == 1){
					color1[colorIndex]=1;
					color2[colorIndex]=0;
					} else {
						color1[colorIndex]=Math.abs(zPoint);
						color2[colorIndex]=Math.abs(yPoint);
					}
				colorIndex++;
				if(colorFlag == 2){
					color1[colorIndex]=1;
					color2[colorIndex]=0;
					} else {
						color1[colorIndex]=Math.abs(xPoint);
						color2[colorIndex]=Math.abs(zPoint);
					}
				colorIndex++;
				color1[colorIndex]=0;
				colorIndex++;
				colorFlag++;
			}
			// off set the color of each ring
			colorFlag+=2;
		}
		//System.out.println("vertIndex = " + vertIndex);
		//System.out.println("colorIndex = " + colorIndex);
		
		// loop to populate triangles
		int triangleIndex = 0;
		int triangleVertexIndex = 0;

		//System.out.println("Number of loops = " + (ySections - 1));
		for(int yAxisIndex = 0; yAxisIndex < ySections - 1; yAxisIndex++){
			//System.out.println("At start of y loop_" + yAxisIndex + " triangleVertexIndex = " + triangleVertexIndex);
			
			for(int zAxisIndex = 0; zAxisIndex < zSections-1; zAxisIndex++){
				//draw first triangle
				triangles[triangleIndex] = triangleVertexIndex;
				triangleIndex++;
				
				triangles[triangleIndex] = triangleVertexIndex + 1;
				triangleIndex++;
				
				triangles[triangleIndex] = triangleVertexIndex + zSections;
				triangleIndex++;
				//System.out.println("triangle"+((triangleIndex - 2)/3)+" = " + triangles[triangleIndex - 3] + ", " + triangles[triangleIndex - 2] + ", " + triangles[triangleIndex -1 ]);
				
				//draw second triangle
				triangles[triangleIndex] = triangleVertexIndex + 1;
				triangleIndex++;
				
				triangles[triangleIndex] = triangleVertexIndex + zSections + 1;
				triangleIndex++;
				
				triangles[triangleIndex] = triangleVertexIndex + zSections;
				triangleIndex++;
				//System.out.println("triangle"+((triangleIndex - 2)/3)+" = " + triangles[triangleIndex - 3] + ", " + triangles[triangleIndex - 2] + ", " + triangles[triangleIndex -1 ]);
				triangleVertexIndex++;
			}
			// skip last vertex in order to prevent wrap around
			triangleVertexIndex++;
			//System.out.println("triangleVertexIndex = " + triangleVertexIndex);
		}
		//System.out.println("triangleIndex = " + triangleIndex);
		
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
	
	public void flipColor(){
		FloatBuffer colorBuf;
		if(colorPick){
				colorBuf =com.jogamp.common.nio.Buffers.newDirectFloatBuffer(color2);
		} else {
			colorBuf = com.jogamp.common.nio.Buffers.newDirectFloatBuffer(color1);
		}
		this.setColorBuffer(colorBuf);
		colorPick = !colorPick;
	}
	
	@Override
	public boolean handleEvent(IGameEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

}
