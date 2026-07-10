public class Vector {
    public double x;
    public double y;

    public static final Vector UP = new Vector(0,-1);
    public static final Vector RIGHT = new Vector(1,0);
    public static final Vector LEFT = new Vector(-1,0);
    public static final Vector DOWN = new Vector(0,1);
    public static final Vector ZERO = new Vector(0,0);

    public Vector(double xp, double yp) {
        x = xp;
        y = yp;
    }

    public Vector copy() {
        return new Vector(x,y);
    }

    /**
     * Calculates the difference of Vector b from Vector a (a-b). Non Mutator 
     * @param a     Vector subtracted from 
     * @param b     Vector being subtracted
     **/
    public static Vector sub(Vector a, Vector b) {
        return new Vector(a.x-b.x, a.y-b.y);
    }

    //non Mutator
    public static Vector add(Vector a, Vector b) {
        return new Vector(a.x+b.x, a.y+b.y);
    }

    //Mutator
    public Vector addTo(Vector v) {
        x += v.x;
        y += v.y;
        return this;
    }

    //non Mutator
    public static Vector mult(Vector v, double m) {
        return new Vector(v.x*m,v.y*m);
    }

    public double magnitude() {
        return Math.sqrt(x*x + y*y);
    }

    public static Vector setMag(Vector v, double m) {
        return Vector.mult(v.normalized(), m);
    }

    public Vector normalized() {
        double mag = magnitude();
        if (mag == 0) return new Vector(0,0);
        return new Vector(x/mag,y/mag);
    }

    //angle in radians
    public Vector rotated(double ang) {
        double cos = Math.cos(ang);
        double sin = Math.sin(ang);
        return new Vector(cos*x - sin*y, sin*x - cos*y);
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }
}