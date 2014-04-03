
package lib2d;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Matrix4f;


public class Graphics {
    private int vsId = 0;
    private int fsId = 0;
    private int pId = 0;
    
    private int projectionMatrixLocation;
    private Matrix4f projectionMatrix = null;
    private final FloatBuffer matrix44Buffer;
    
    
    public Graphics(int screenWidth, int screenHeight, String title) throws LWJGLException {
        PixelFormat pixelFormat = new PixelFormat();
        ContextAttribs contextAtrributes = new ContextAttribs(3, 2)
                .withForwardCompatible(true)
                .withProfileCore(true);
        
        Display.setDisplayMode(new DisplayMode(screenWidth, screenHeight));
        Display.setTitle(title);
        Display.create(pixelFormat, contextAtrributes);
        
        setupShaders();
        
        projectionMatrix = MathUtil.toOrtho2D(null, 0, 0, screenWidth, screenHeight);
        matrix44Buffer = BufferUtils.createFloatBuffer(16);
        
        GL20.glUseProgram(pId);
        projectionMatrix.store(matrix44Buffer);
        matrix44Buffer.flip();
        GL20.glUniformMatrix4(projectionMatrixLocation, false, matrix44Buffer);
        GL20.glUseProgram(0);
    }
    
    public void startDraw() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        
        GL20.glUseProgram(pId);
    }
    
    public void endDraw() {
        GL20.glUseProgram(0);
    }
    
    public void setBackgroundColor(Color color) {
    	GL11.glClearColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }
    
    private void setupShaders() {
        vsId = this.loadShader("resources/shaders/vertex.glsl", GL20.GL_VERTEX_SHADER);
        fsId = this.loadShader("resources/shaders/fragment.glsl", GL20.GL_FRAGMENT_SHADER);

        pId = GL20.glCreateProgram();
        GL20.glAttachShader(pId, vsId);
        GL20.glAttachShader(pId, fsId);

        GL20.glBindAttribLocation(pId, 0, "in_Position");
        GL20.glBindAttribLocation(pId, 1, "in_Color");
        
        GL20.glLinkProgram(pId);
        GL20.glValidateProgram(pId);

        projectionMatrixLocation = GL20.glGetUniformLocation(pId, "projectionMatrix");
        
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
}
