
package ponggl;

public class GameLogic {
    public Ball ball;
    public Paddle paddle;
    private int screenWidth;
    private int screenHeight;
            
    public GameLogic(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        
        reset();
    }
    
    public void reset() {
        ball = new Ball(screenWidth/2,screenHeight/2);
        paddle = new Paddle();
        
        ball.trajectory.x = -2.5;
        ball.trajectory.y = 1;
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
        
        ball.updatePos();
    }
}
