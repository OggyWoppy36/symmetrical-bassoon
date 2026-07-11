import java.awt.*;
import java.awt.geom.AffineTransform;


public class CollisionShape extends Object {
    private Color color;
    private Vector pos;
    public enum types { CORNER, CENTER }
    public static types type = types.CORNER;

    private double width;
    private double height;
    private double angle = 0;

    public double up, left, right, down;

    /** Length 4 array of centers for drawing
     * 0: Top Center (y)
     * 1: Left Center (x)
     * 2: Right Center (x)
     * 3: Bottom Center (y)
     */
    private double[] centers;

    public int timer;

    public CollisionShape(Vector p, double w, double h, Color c) {
        color = c;
        pos = p.copy();
        if (type == types.CENTER) {
            pos = Vector.add(pos, Vector.mult(new Vector(w,h), -0.5));
        }
        width = w;
        height = h;
        
        timer = -1;

        left  = Math.min(pos.x, pos.x + w);
        right = Math.max(pos.x, pos.x + w);
        up    = Math.min(pos.y, pos.y + h);
        down  = Math.max(pos.y, pos.y + h);
        centers = populateCenters();
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
    public void setAngle(double ang) { angle = ang; }
    public void setTimer(int t) { timer = t; }

    public boolean update() {
        if (timer > 0) timer -= 1;
        if (timer == 0) {
            return true;
        }
        return false;
    }

    public void render(Graphics2D g) {
        if (angle == 0) {
            renderDropShadow(g);

            g.setColor(color);
            g.fillRect((int) left, (int) up, (int) (right-left), (int) (down-up));

            renderShading(g);
        } else {
            Vector cent = getCenter();
            AffineTransform old = g.getTransform();
            g.rotate(angle, cent.x, cent.y);

            renderDropShadow(g);

            g.setColor(color);
            g.fillRect((int) left, (int) up, (int) (right-left), (int) (down-up));

            renderShading(g);

            g.setTransform(old);
        }
    }

    public void renderDropShadow(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 80));
        g.fillRect((int) (pos.x - 3), (int) (pos.y + 5), (int) (width), (int) (height));
    }

    public void renderShading(Graphics2D g) {
        g.setColor(color.darker());
        int[] xpoints = {(int) left, (int) centers[1], (int) centers[1], (int) left};
        int[] ypoints = {(int) up, (int) centers[0], (int) centers[3], (int) down};
        g.fillPolygon(xpoints, ypoints, 4);

        g.setColor(color.darker().darker());
        xpoints = new int[]{(int) left, (int) centers[1], (int) centers[2], (int) right};
        ypoints = new int[]{(int) down, (int) centers[3], (int) centers[3], (int) down};
        g.fillPolygon(xpoints, ypoints, 4);

        g.setColor(color.brighter());
        xpoints = new int[]{(int) left, (int) centers[1], (int) centers[2], (int) right};
        ypoints = new int[]{(int) up, (int) centers[0], (int) centers[0], (int) up};
        g.fillPolygon(xpoints, ypoints, 4);

        g.setColor(lerp(color,color.brighter(),0.5));
        xpoints = new int[]{(int) right, (int) centers[2], (int) centers[2], (int) right};
        ypoints = new int[]{(int) up, (int) centers[0], (int) centers[3], (int) down};
        g.fillPolygon(xpoints, ypoints, 4);
    }

    public static double lerp(double a, double b, double percent) {
        return a+percent*(b-a);
    }

    public static Color lerp(Color a, Color b, double percent) {
        int r = (int) lerp(a.getRed(),b.getRed(),percent);
        int g = (int) lerp(a.getGreen(),b.getGreen(),percent);
        int bb = (int) lerp(a.getBlue(),b.getBlue(),percent);
        int aa = (int) lerp(a.getAlpha(),b.getAlpha(),percent);
        return new Color(r,g,bb,aa);
    }

    public final double[] populateCenters() {
        double[] c = new double[4];
        double d = 0.25*Math.min((right - left),(down-up));
        c[0] = up + d;
        c[1] = left + d;
        c[2] = right - d;
        c[3] = down - d;
        return c;
    } 

    public static void setType(types t) { type = t; }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("{");

        return s.toString();
    }
}