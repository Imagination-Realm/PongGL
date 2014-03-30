
package ponggl;

import java.util.ArrayList;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

public class Game {
    public static void main(String[] args) throws LWJGLException {
        Game game = new Game(640, 480);
    }
    
    private final int screenWidth;
    private final int screenHeight;
    private final ArrayList<GameObject> objects;
    private final Ball ball;
    private final Paddle paddle;
    
    public Game(int screenWidth, int screenHeight) throws LWJGLException {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        
        objects = new ArrayList<>();
        
        Graphics graphics = new Graphics(screenWidth, screenHeight, "PongGL");
        
        ball = new Ball(screenWidth/2, screenHeight/2, 10, new Vector2D(-3, 1));
        registerObject(ball);
        graphics.registerObject(ball);
        
        paddle = new Paddle(screenWidth-30, 100, 20, 200);
        //registerObject(paddle);
        //graphics.registerObject(paddle);
        
        while (!Display.isCloseRequested()) {
                update();
                updateObjects();
                graphics.draw();
                
                Display.sync(60);
                Display.update();
            }
            
            close();
    }
    
    public void registerObject(GameObject obj) {
        objects.add(obj);
    }
    
    public void unregisterObject(GameObject obj) {
        objects.remove(obj);
    }
    
    public void update() {
        if(ball.intersectsLine(0, 0, 0, screenHeight)) {  //left
            ball.trajectory = ball.trajectory.reflected(new Vector2D(1,0));
        }
        if(ball.intersectsLine(0, 0, screenWidth, 0)) {  //top
            ball.trajectory = ball.trajectory.reflected(new Vector2D(0,1));
        }
        if(ball.intersectsLine(screenWidth, 0, screenWidth, screenHeight)) {  //right
            ball.trajectory = ball.trajectory.reflected(new Vector2D(-1,0));
        }
        if(ball.intersectsLine(0, screenHeight, screenWidth, screenHeight)) {  //bottom
            ball.trajectory = ball.trajectory.reflected(new Vector2D(0,-1));
        }
    }
    
    public void updateObjects() {
        for(GameObject obj : objects) {
            obj.think();
        }
    }
    
    public void close() {
        for(GameObject obj : objects) {
            obj.onRemove();
        }
        Display.destroy();
    }
}
