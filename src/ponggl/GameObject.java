
package ponggl;

import java.awt.geom.Rectangle2D;


public abstract class GameObject extends Rectangle2D.Float {
    protected GameEngine engine;
    
    public GameObject() {
        this(0,0,0,0);
    }
    
    public GameObject(float width, float height) {
        this(0, 0, width, height);
    }
    
    public GameObject(float x, float y, float width, float height) {
        super(x, y, width, height);
    }
    
    public void setPos(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    public void centerHorizontally() {
        setPos((engine.getScreenWidth() - width)/2, y);
    }
    
    public void centerVertically() {
       setPos(x, (engine.getScreenHeight() - height)/2);
    }
    
    public void center() {
        setPos((engine.getScreenWidth() - width)/2, (engine.getScreenHeight() - height)/2);
    }

    public GameEngine getEngine() {
        return engine;
    }

    public void setEngine(GameEngine engine) {
        this.engine = engine;
    }
    
    public abstract void draw();
    public abstract void think();
    public abstract void onRemove();
}
