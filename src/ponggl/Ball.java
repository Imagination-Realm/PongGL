
package ponggl;

public class Ball extends Rectangle {
    public static float DEFAULT_WIDTH = 0.2f;
    public static float DEFAULT_HEIGHT = 0.2f;
    
    
    public Vector2D trajectory;

    public Ball(float x, float y) {
        super(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        trajectory = new Vector2D();
    }
    
    public void updatePos() {
        x += trajectory.x;
        y += trajectory.y;
    }
}
