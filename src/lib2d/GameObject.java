
package lib2d;

import java.awt.geom.Rectangle2D;


public abstract class GameObject {
	public Rectangle2D.Float area;
    protected Game engine;
    
    public GameObject() {
        this(0,0,0,0);
    }
    
    public GameObject(float width, float height) {
        this(0, 0, width, height);
    }
    
    public GameObject(float x, float y, float width, float height) {
    	area = new Rectangle2D.Float(x, y, width, height);
    }
    
    public void setLocation(float x, float y) {
    	area.x = x;
    	area.y = y;
    }
    
    public void centerHorizontally() {
        setLocation((engine.getScreenWidth() - area.width)/2, area.y);
    }
    
    public void centerVertically() {
       setLocation(area.x, (engine.getScreenHeight() - area.height)/2);
    }
    
    public void center() {
        setLocation((engine.getScreenWidth() - area.width)/2, (engine.getScreenHeight() - area.height)/2);
    }

    public Game getEngine() {
        return engine;
    }

    public void setEngine(Game engine) {
        this.engine = engine;
    }
    
    public abstract void draw();
    public abstract void think();
    public abstract void onRemove();
}
