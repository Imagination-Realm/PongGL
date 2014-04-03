
package ponggl;

import java.awt.Color;

import lib2d.Vector2D;
import lib2d.Game;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;


public class Pong extends Game {
    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;
    
    public static void main(String[] args) throws LWJGLException {
        Pong pong = new Pong();
        pong.start();
    }
    
    private final Ball ball;
    private final Paddle rPaddle;
    private final Paddle lPaddle;
    
    public Pong() throws LWJGLException {
        super(WIDTH, HEIGHT, "PongGL");
        
        ball = new Ball(0, 0, 20, new Vector2D(-5, 2));
        registerObject(ball);
        ball.center();
        
        rPaddle = new Paddle(WIDTH-30, 100, 20, 200);
        registerObject(rPaddle);
        
        lPaddle = new Paddle(10, 100, 20, 200);
        registerObject(lPaddle);
        
        getGraphics().setBackgroundColor(Color.BLACK);
    }
    
    public void reset() {
        ball.center();
        ball.trajectory =  new Vector2D(-5, 2);
        
        rPaddle.centerVertically();
        lPaddle.centerVertically();
    }
    
    @Override
    public void think() {
    	if(Keyboard.isKeyDown(Keyboard.KEY_UP) && rPaddle.area.y-5 >= 0) {
    		rPaddle.setLocation(rPaddle.area.x, rPaddle.area.y-5);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_DOWN) && rPaddle.area.y+5+rPaddle.area.height <= getScreenHeight()) {
        	rPaddle.setLocation(rPaddle.area.x, rPaddle.area.y+5);
        }
        
        
        if(Keyboard.isKeyDown(Keyboard.KEY_W) && lPaddle.area.y-5 >= 0) {
        	lPaddle.setLocation(lPaddle.area.x, lPaddle.area.y-5);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_S) && lPaddle.area.y+5+lPaddle.area.height <= getScreenHeight()) {
        	lPaddle.setLocation(lPaddle.area.x, lPaddle.area.y+5);
        }
    	
        
        
        if(ball.area.intersects(rPaddle.area)) {
            ball.trajectory = ball.trajectory.reflected(new Vector2D(-1,0));
        }
        if(ball.area.intersects(lPaddle.area)) {
            ball.trajectory = ball.trajectory.reflected(new Vector2D(1,0));
        }
        
        
        if(ball.area.intersectsLine(0, 0, 0, HEIGHT)) {  //left
        	reset();
        }
        if(ball.area.intersectsLine(0, 0, WIDTH, 0)) {  //top
            ball.trajectory = ball.trajectory.reflected(new Vector2D(0,1));
        }
        if(ball.area.intersectsLine(WIDTH, 0, WIDTH, HEIGHT)) {  //right
            reset();
        }
        if(ball.area.intersectsLine(0, HEIGHT, WIDTH, HEIGHT)) {  //bottom
            ball.trajectory = ball.trajectory.reflected(new Vector2D(0,-1));
        }
    }

    @Override
    public void onClose() {}
}
