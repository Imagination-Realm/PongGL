
package ponggl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;


class RectData {
    public Rectangle rect;
    public FloatBuffer vertBuffer;
    public int vertBufferId, colBufferId, vertArrayId = 0;
}


public class Graphics {
    private final int VALUES_PER_VERTEX = 4;  //used for the position (x,y,z,w), or for the color (r,g,b,a)
    private final int VERTICES_PER_SHAPE = 4;  //we're drawing rectangles
    
    private int indicesBufferId = 0;
    private byte[] rectIndices = {  //the vertices needed for a rectangle (same for all so we use a common buffer)
	0, 1, 2,
	2, 3, 0
    };
    
    private int vsId = 0;
    private int fsId = 0;
    private int pId = 0;
    
    private int screenWidth;
    private int screenHeight;
    
    private ArrayList<RectData> rects;
    
    public Graphics(int screenWidth, int screenHeight, String title) throws LWJGLException {
        PixelFormat pixelFormat = new PixelFormat();
        ContextAttribs contextAtrributes = new ContextAttribs(3, 2)
                .withForwardCompatible(true)
                .withProfileCore(true);
        
        Display.setDisplayMode(new DisplayMode(screenWidth, screenHeight));
        Display.setTitle(title);
        Display.create(pixelFormat, contextAtrributes);
        
        
        rects = new ArrayList<>();
        
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        
	ByteBuffer indicesBuffer = BufferUtils.createByteBuffer(rectIndices.length);
	indicesBuffer.put(rectIndices);
	indicesBuffer.flip();
        
        indicesBufferId = GL15.glGenBuffers();
	GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBufferId);
	GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
	GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        
        setupShaders();
         
        GL11.glClearColor(0.4f, 0.6f, 0.9f, 0f);
    }
    
    public void addRect(Rectangle rect) {
        RectData rdata = new RectData();
        rdata.rect = rect;
        
        rdata.vertBuffer = BufferUtils.createFloatBuffer(VALUES_PER_VERTEX * VERTICES_PER_SHAPE);
        rdata.vertBuffer.put(new float[] {
            rect.x, rect.y, 0f, 1f,
            rect.x, rect.y+rect.height, 0f, 1f,
            rect.x+rect.width, rect.y+rect.height, 0f, 1f,
            rect.x+rect.width, rect.y, 0f, 1f
        });
        rdata.vertBuffer.flip();
        
        FloatBuffer colBuffer = BufferUtils.createFloatBuffer(VALUES_PER_VERTEX * VERTICES_PER_SHAPE);
        //use a single color for the entire shape
        for(int i=0; i<VERTICES_PER_SHAPE; i++) {
            colBuffer.put(rect.color.getRGBComponents(null));
        }
        colBuffer.flip();
        
        rdata.vertArrayId = GL30.glGenVertexArrays();
	GL30.glBindVertexArray(rdata.vertArrayId);
        
        rdata.vertBufferId = GL15.glGenBuffers();
	GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, rdata.vertBufferId);
	GL15.glBufferData(GL15.GL_ARRAY_BUFFER, rdata.vertBuffer, GL15.GL_STATIC_DRAW);
	GL20.glVertexAttribPointer(0, VALUES_PER_VERTEX, GL11.GL_FLOAT, false, 0, 0);
        
        rdata.colBufferId = GL15.glGenBuffers();
	GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, rdata.colBufferId);
	GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colBuffer, GL15.GL_STATIC_DRAW);
	GL20.glVertexAttribPointer(1, VALUES_PER_VERTEX, GL11.GL_FLOAT, false, 0, 0);
	
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);	
	GL30.glBindVertexArray(0);
        
        rects.add(rdata);
    }
    
    private RectData getRData(Rectangle rect) {
        for(RectData rdata : rects) {
            if(rdata.rect == rect) {
                return rdata;
            }
        }
        return null;
    }
    
    public void removeRect(Rectangle rect) {
        RectData rdata = getRData(rect);
        if(rdata != null) {
            deleteRData(rdata);
            rects.remove(rdata);
        }
    }
    
    private void deleteRData(RectData rdata) {
	GL15.glDeleteBuffers(rdata.vertBufferId);
        GL15.glDeleteBuffers(rdata.colBufferId);
        GL30.glDeleteVertexArrays(rdata.vertArrayId);
    }
    
    public void updateRect(Rectangle rect) {
        RectData rdata = getRData(rect);
        if(rdata == null) return;
        
        rdata.vertBuffer.rewind();
        rdata.vertBuffer.put(new float[] {
            rect.x, rect.y-rect.height, 0f, 1f,
            rect.x, rect.y, 0f, 1f,
            rect.x+rect.width, rect.y, 0f, 1f,
            rect.x+rect.width, rect.y-rect.height, 0f, 1f
        });
        rdata.vertBuffer.flip();
        
        GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, rdata.vertBuffer);
    }
    
    public void draw() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        
        GL20.glUseProgram(pId);
	
        for(RectData rdata : rects) {
            GL30.glBindVertexArray(rdata.vertArrayId);
            GL20.glEnableVertexAttribArray(0);
            GL20.glEnableVertexAttribArray(1);

            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBufferId);

            GL11.glDrawElements(GL11.GL_TRIANGLES, rectIndices.length, GL11.GL_UNSIGNED_BYTE, 0);

            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
            GL20.glDisableVertexAttribArray(0);
            GL20.glDisableVertexAttribArray(1);
            GL30.glBindVertexArray(0);
        }
        
        GL20.glUseProgram(0);
    }
    
    private void setupShaders() {
        vsId = this.loadShader("shaders/vertex.glsl", GL20.GL_VERTEX_SHADER);
        fsId = this.loadShader("shaders/fragment.glsl", GL20.GL_FRAGMENT_SHADER);

        pId = GL20.glCreateProgram();
        GL20.glAttachShader(pId, vsId);
        GL20.glAttachShader(pId, fsId);

        GL20.glBindAttribLocation(pId, 0, "in_Position");
        GL20.glBindAttribLocation(pId, 1, "in_Color");

        GL20.glLinkProgram(pId);
        GL20.glValidateProgram(pId);

        int  errorCheckValue = GL11.glGetError();
        if (errorCheckValue != GL11.GL_NO_ERROR) {
                System.out.println("ERROR - Could not create the shaders:" + GLU.gluErrorString(errorCheckValue));
                System.exit(-1);
        }
    }
    
    public int loadShader(String filename, int type) {
        StringBuilder shaderSource = new StringBuilder();
        int shaderID = 0;

        try {
                BufferedReader reader = new BufferedReader(new FileReader(filename));
                String line;
                while ((line = reader.readLine()) != null) {
                        shaderSource.append(line).append("\n");
                }
                reader.close();
        } catch (IOException e) {
                System.err.println("Could not read file.");
                e.printStackTrace();
                System.exit(-1);
        }

        shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);

        return shaderID;
    }
    
    public void close() {
        for(RectData rdata : rects) {
            deleteRData(rdata);
        }
        Display.destroy();
    }
}
