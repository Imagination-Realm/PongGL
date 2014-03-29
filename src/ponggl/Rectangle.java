
package ponggl;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

public class Rectangle extends Rectangle2D.Float {
    public Color color;

    public Rectangle(float x, float y, float width, float height, Color color) {
        super(x, y, width, height);
        this.color = color;
    }

    public Rectangle(float x, float y, float width, float height) {
        super(x, y, width, height);
        this.color = Color.WHITE;
    }

    public Rectangle() {
        this(0, 0, 0, 0);
    }
}
