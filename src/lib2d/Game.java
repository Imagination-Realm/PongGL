
package lib2d;

import java.util.ArrayList;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;


public abstract class Game {
    private final int screenWidth;
    private final int screenHeight;
    private final ArrayList<GameObject> gObjects;
    private Graphics graphics;
    
    public Game(int screenWidth, int screenHeight, String windowTitle) throws LWJGLException {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        
        gObjects = new ArrayList<>();
        
        graphics = new Graphics(screenWidth, screenHeight, windowTitle);
    }
    
    public void start() {
        while (!Display.isCloseRequested()) {
            think();
            objectsThink();
            
            graphics.startDraw();
            for(GameObject obj : gObjects) {
                obj.draw();
            }
            graphics.endDraw();
            
            Display.sync(60);
            Display.update();
        }
            
        close();
    }
    
    public void objectsThink() {
        for(GameObject obj : gObjects) {
            obj.think();
        }
    }
    
    public void close() {
        onClose();
        for(GameObject obj : gObjects) {
            obj.onRemove();
        }
        Display.destroy();
    }
    
    public void registerObject(GameObject obj) {
        obj.setEngine(this);
        gObjects.add(obj);
    }
    
    public void unregisterObject(GameObject obj) {
        obj.setEngine(null);
        gObjects.remove(obj);
    }
    
    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }
    
    public Graphics getGraphics() {
		return graphics;
	}

	public abstract void think();
    public abstract void onClose();
}
