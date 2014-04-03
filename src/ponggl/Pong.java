
package ponggl;

import java.awt.Color;
import lib2d.Vector2D;
import lib2d.Game;
import org.lwjgl.LWJGLException;


public class Pong extends Game {
    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;
    
    public static void main(String[] args) throws LWJGLException {
        Pong pong = new Pong();
        pong.start();
    }
    
    private final Ball ball;
    private final Paddle paddle;
    
    public Pong() throws LWJGLException {
        super(WIDTH, HEIGHT, "PongGL");
        
        ball = new Ball(0, 0, 20, new Vector2D(-4, 1.5f));
        registerObject(ball);
        ball.center();
        
        paddle = new Paddle(WIDTH-30, 100, 20, 200);
        registerObject(paddle);
        
        getGraphics().setBackgroundColor(Color.BLACK);
    }
    
    public void reset() {
        ball.center();
        ball.trajectory =  new Vector2D(-4, 1.5f);
        
        paddle.centerVertically();
    }
    
    @Override
    public void think() {
        if(ball.area.intersects(paddle.area)) {
            ball.trajectory = ball.trajectory.reflected(new Vector2D(-1,0));
        }
        
        if(ball.area.intersectsLine(0, 0, 0, HEIGHT)) {  //left
            ball.trajectory = ball.trajectory.reflected(new Vector2D(1,0));
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