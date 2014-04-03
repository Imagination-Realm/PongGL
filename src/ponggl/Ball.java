
package ponggl;

import java.awt.Color;
import java.nio.FloatBuffer;

import lib2d.GameObject;
import lib2d.Vector2D;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;


public class Ball extends GameObject {
    private final int NR_SEGMENTS = 20;
    private final int NR_VERTICES = (NR_SEGMENTS+2);
    private final int VALUES_PER_VERTEX = 2;
    private final int VALUES_PER_COLOR = 4;
    public Vector2D trajectory;
    private float radius;
    private final Color color;
    private final int vertBufferId, colBufferId, vertArrayId;
    private final FloatBuffer vertBuffer;
    
    
    public Ball(float x, float y, float radius) {
        this(x, y, radius, new Vector2D());
    }
    
    public Ball(float x, float y, float radius, Vector2D trajectory) {
        super(x, y, radius*2, radius*2);
        this.radius = radius;
        this.color = Color.WHITE;
        this.trajectory = trajectory;
        
        vertBuffer = BufferUtils.createFloatBuffer(VALUES_PER_VERTEX * NR_VERTICES);
        vertBuffer.put(new float[] {x+radius, y+radius});
        for( int i = 0; i < NR_SEGMENTS+1; i++ ) {  //+1 so it connects back to the first
            float angle = (float)(2*Math.PI*i)/NR_SEGMENTS;
            vertBuffer.put(x+radius + (float)Math.sin(angle) * radius);
            vertBuffer.put(y+radius + (float)Math.cos(angle) * radius);
        }
        vertBuffer.flip();
        
        FloatBuffer colBuffer = BufferUtils.createFloatBuffer(VALUES_PER_COLOR * NR_VERTICES);
        for(int i=0; i<NR_VERTICES; i++) {
            colBuffer.put(color.getRGBComponents(null));
        }
        colBuffer.flip();
        
        vertArrayId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vertArrayId);
        
        vertBufferId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertBufferId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertBuffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(0, VALUES_PER_VERTEX, GL11.GL_FLOAT, false, 0, 0);
        
        colBufferId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, colBufferId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colBuffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(1, VALUES_PER_COLOR, GL11.GL_FLOAT, false, 0, 0);
	
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);	
        GL30.glBindVertexArray(0);
    }
    
    private void update() {
        vertBuffer.rewind();
        vertBuffer.put(new float[] {area.x+radius, area.y+radius});
        for( int i = 0; i < NR_SEGMENTS+1; i++ ) {  //+1 so it connects back to the first
            float angle = (float)(2*Math.PI*i)/NR_SEGMENTS;
            vertBuffer.put(area.x+radius + (float)Math.sin(angle) * radius);
            vertBuffer.put(area.y+radius + (float)Math.cos(angle) * radius);
        }
        vertBuffer.flip();
        
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertBufferId);
        GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, vertBuffer);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }
    
    @Override
    public void draw() {
        GL30.glBindVertexArray(vertArrayId);
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);

        GL11.glDrawArrays(GL11.GL_TRIANGLE_FAN, 0, NR_VERTICES);

        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }
    
    @Override
    public void think() {
    	area.x += trajectory.x;
    	area.y += trajectory.y;
        
        update();
    }
    
    @Override
    public void onRemove() {
        GL15.glDeleteBuffers(vertBufferId);
        GL15.glDeleteBuffers(colBufferId);
        GL30.glDeleteVertexArrays(vertArrayId);
    }
}
