import java.awt.*;
import java.awt.geom.AffineTransform;

public class RectMaker {
    private boolean visible = true;
    
    private Vector pos; //center
    private double width;
    private double height;
    private double angle; //DEGREES
    private static final double MIN_DIM = 30;

    public RectMaker(Vector p, double w, double h, double a) {
        pos = p.copy();
        width = w;
        height = h;
        angle = a;
    }

    public void render(Graphics2D g) {
        if (!visible) return;
        CollisionShape c = createRect();
        if (Game.checkBallCollision(c)) {
            g.setColor(new Color(255,20,20,100));
        } else {
            g.setColor(new Color(255, 255, 255, 100));
        }
        

        Vector cent = pos.copy();
        AffineTransform old = g.getTransform();
        g.rotate(Math.toRadians(angle), cent.x, cent.y);

        int x = (int) (pos.x-width/2);
        int y = (int) (pos.y-height/2);
        g.fillRect(x, y, (int) (width), (int) (height));
        g.setColor(Color.WHITE);
        g.drawRect(x, y, (int) width, (int) height);
        g.setTransform(old);
    }

    public CollisionShape createRect() {
        CollisionShape.setType(CollisionShape.types.CENTER);
        CollisionShape c = new CollisionShape(pos, width, height, Game.randColor());
        c.setAngle(Math.toRadians(angle));
        CollisionShape.setType(CollisionShape.types.CORNER);
        return c;
    }

    public void setVisible(boolean b) { visible = b; }
    public void setPos(Vector p) { pos = p.copy(); }
    public void setWidth(double w) { width = Math.max(w,MIN_DIM); }
    public void setHeight(double h) { height = Math.max(h,MIN_DIM); }
    /**
     * Changes the angle of the rectangle to match the input. May swap width/height + rotate 90
     * @param a angle to set in DEGREES
     */
    // worst method ever???? (it cheeses the rectangle shadows by constraining the angle)
    public void setAngle(double a) { 
        angle = a;
        if (angle >= 45.0) {
            angle -= 90;
            swapDims();
        } else if (angle < -50.0) {
            angle += 90;
            swapDims();
        }
    }

    public void swapDims() {
        double temp = width;
        width = height;
        height = temp;
    }

    public Vector getPos() { return pos.copy(); }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    /**
     * @return Returns angle in DEGREES
     */
    public double getAngle() { return angle; }
    public boolean isVisible() { return visible; }
}
