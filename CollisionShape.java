import java.awt.*;
import java.awt.geom.AffineTransform;

@SuppressWarnings("unused")
public class CollisionShape {
    private Color color;
    private Vector pos;
    public enum types { CORNER, CENTER }
    public static types type = types.CORNER;

    private double width;
    private double height;
    private double angle = 0;

    public double up, left, right, down;

    public CollisionShape(Vector p, double w, double h, Color c) {
        color = c;
        pos = p.copy();
        if (type == types.CENTER) {
            pos = Vector.add(pos, Vector.mult(new Vector(w,h), -0.5));
        }
        width = w;
        height = h;
        

        left  = Math.min(pos.x, pos.x + w);
        right = Math.max(pos.x, pos.x + w);
        up    = Math.min(pos.y, pos.y + h);
        down  = Math.max(pos.y, pos.y + h);
    }

    public CollisionShape(Vector a, Vector b, Color c) {
        this(a, Vector.sub(b, a).x, Vector.sub(b,a).y, c);
    }

    public Vector getCenter() {
        return new Vector((left+right) / 2.0, (up+down) / 2.0);
    }

    public double getHalfWidth() { return (right - left) / 2.0; }
    public double getHalfHeight() { return (down - up) / 2.0; }
    public double getAngle() { return angle; }
    /**
     * @param ang   angle in radians
     */
    public void setAngle(double ang) { this.angle = ang; }

    public void render(Graphics2D g) {
        g.setColor(color);
        if (angle == 0) {
            g.fillRect((int) left, (int) up, (int) (right-left), (int) (down-up));
        } else {
            Vector cent = getCenter();
            AffineTransform old = g.getTransform();
            g.rotate(angle, cent.x, cent.y);
            g.fillRect((int) left, (int) up, (int) (right-left), (int) (down-up));
            g.setTransform(old);
        }

    }

    public static void setType(types t) { type = t; }
}