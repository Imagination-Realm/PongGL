
package ponggl;

public class GameLogic {
    public Ball ball;
    public Paddle paddle;
            
    public GameLogic(int screenWidth, int screenHeight) {
        reset();
    }
    
    public void reset() {
        ball = new Ball(0,0);
        paddle = new Paddle();
        
        ball.trajectory.x = -1;
        ball.trajectory.y = 0.5;
    }
    
    public void update() {
        ball.updatePos();
    }
}
