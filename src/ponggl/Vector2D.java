
package ponggl;

import java.awt.geom.Point2D;


public class Vector2D extends Point2D.Double {
    public Vector2D() {
        this(0, 0);
    }
    
    public Vector2D(double x, double y) {
        super(x, y);
    }
    
    public double lengthSquared() {
        return Math.pow(x, 2) + Math.pow(y, 2);
    }

    public double length() {
	return Math.sqrt(lengthSquared());
    }

    public double angle() {
	return angle(0);
    }
    
    public double angle(double lowerBound) {  //in radians
	return Math.atan2(y, x);
    }

    public Vector2D negated() {
        return new Vector2D(-x,-y);
    }

    public Vector2D normalized() {
	double len = length();
	if (len > 0) {
            return new Vector2D(x/len, y/len);
	} else {
            return new Vector2D(x, y);
        }
    }
    
    public Vector2D scaled(double s) {
        return new Vector2D(x * s, y * s);
    }
    
    public Vector2D reflected(Vector2D normal) {
        Vector2D normComp = normal.scaled(dot(this, normal) / normal.lengthSquared());
        Vector2D tanComp = sum(this, normComp.negated());  //take out the normal component from the vector
        return sum(tanComp, normComp.negated());  //and put it back, but reversed
    }
    
    public static double dot(Vector2D v1, Vector2D v2) {
	return v1.x * v2.x + v1.y * v2.y;
    }

    public static Vector2D sum(Vector2D v1, Vector2D v2) {
	return new Vector2D(v1.x + v2.x, v1.y + v2.y);
    }
}
