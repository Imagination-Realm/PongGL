
package ponggl;

import java.awt.geom.Rectangle2D;

public abstract class GameObject extends Rectangle2D.Float {
    public GameObject() {
        this(0,0,0,0);
    }
    
    public GameObject(float width, float height) {
        this(0, 0, width, height);
    }
    
    public GameObject(float x, float y, float width, float height) {
        super(x, y, width, height);
    }
    
    public abstract void draw();
    public abstract void think();
    public abstract void onRemove();
}
