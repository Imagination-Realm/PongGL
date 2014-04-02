
package ponggl;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.*;


public class Paddle extends GameObject {
    private final int NR_VERTICES = 4;
    private final int VALUES_PER_VERTEX = 2;
    private final int VALUES_PER_COLOR = 4;
    private final byte[] indices = {
	0, 1, 2,
	2, 3, 0
    };
    private final Color color;
    private final int vertBufferId, colBufferId, vertArrayId, indicesBufferId;
    private final FloatBuffer vertBuffer;
    
    
    
    public Paddle(float x, float y, float width, float height) {
        super(x, y, width, height);
        
        color = Color.WHITE;
        
        
        vertBuffer = BufferUtils.createFloatBuffer(VALUES_PER_VERTEX * NR_VERTICES);
        vertBuffer.put(new float[] {
            area.x, area.y,
            area.x, area.y+area.height,
            area.x+area.width, area.y+area.height,
            area.x+area.width, area.y
        });
        vertBuffer.flip();
        
        FloatBuffer colBuffer = BufferUtils.createFloatBuffer(VALUES_PER_COLOR * NR_VERTICES);
        for(int i=0; i<NR_VERTICES; i++) {
            colBuffer.put(color.getComponents(null));
        }
        colBuffer.flip();
        
        ByteBuffer indicesBuffer = BufferUtils.createByteBuffer(indices.length);
		indicesBuffer.put(indices);
		indicesBuffer.flip();
        
        
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
        
        indicesBufferId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBufferId);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    }
    
    public void update() {
        vertBuffer.rewind();
        vertBuffer.put(new float[] {
        	area.x, area.y,
        	area.x, area.y+area.height,
        	area.x+area.width, area.y+area.height,
        	area.x+area.width, area.y
        });
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
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBufferId);

        GL11.glDrawElements(GL11.GL_TRIANGLES, indices.length, GL11.GL_UNSIGNED_BYTE, 0);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }
    
    @Override
    public void think() {
        if(Keyboard.isKeyDown(Keyboard.KEY_UP) && area.y-5 >= 0) {
            setLocation(area.x, area.y-5);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_DOWN) && area.y+5+area.height <= engine.getScreenHeight()) {
            setLocation(area.x, area.y+5);
        }
    }
    
    public void setLocation(float x, float y) {
    	super.setLocation(x, y);
        update();
    }
    
    @Override
    public void onRemove() {
        GL15.glDeleteBuffers(vertBufferId);
        GL15.glDeleteBuffers(colBufferId);
        GL15.glDeleteBuffers(indicesBufferId);
        GL30.glDeleteVertexArrays(vertArrayId);
    }
}
