import java.awt.*;
import java.awt.geom.Point2D;


@SuppressWarnings("unused")
public class Ball {
    private Vector pos;
    private double radius;
    private Vector vel;
    private Color color;

    public Ball(double x, double y, double r, Vector v, Color c) {
        pos = new Vector(x,y);
        radius = r;
        vel = v.copy();
        color = c;
    }

    public void updatePos(double stepMult) {
        pos = Vector.add(pos, Vector.mult(vel,stepMult));
    }

    public void setVel(Vector v) {
        vel = v.copy();
    }

    public Vector getVel() { return vel.copy(); }
    public Vector getPos() { return pos.copy(); }

    public boolean checkCollision(CollisionShape wall, boolean resolve) {
        
        double closestX = Math.clamp(pos.x, wall.left, wall.right);
        double closestY = Math.clamp(pos.y, wall.up, wall.down);

        double dx = pos.x - closestX;
        double dy = pos.y - closestY;
        double distSq = dx * dx + dy * dy;

        if (distSq >= radius * radius) return false;

        if (resolve) {
            double dist = Math.sqrt(distSq);
            double nx, ny;

            if (dist == 0) {
                double overlapX = Math.min(pos.x - wall.left, wall.right - pos.x);                                  
                double overlapY = Math.min(pos.y - wall.up, wall.down - pos.y);
                if (overlapX < overlapY) {
                    nx = (pos.x < (wall.left + wall.right) / 2) ? -1 : 1;
                    ny = 0;
                } else {
                    nx = 0;
                    ny = (pos.y < (wall.up + wall.down) / 2) ? -1 : 1;
                }
                dist = 0.0001;
            } else {
                nx = dx / dist;
                ny = dy / dist;
            }

            resolveCollision(nx, ny, dist);
        }

        return true;
    }

    private void resolveCollision(double nx, double ny, double dist) {
        double overlap = radius - dist;
        pos.addTo(new Vector(nx * overlap, ny * overlap));

        double dot = vel.x * nx + vel.y * ny;
        vel.x -= 2 * dot * nx;
        vel.y -= 2 * dot * ny;
    }

    public void render(Graphics2D g) {
        float cx = (float) pos.x;
        float cy = (float) pos.y;
        float r = (float) radius;

        g.setColor(new Color(0, 0, 0, 80));
        g.fillOval((int) (cx - r - 3), (int) (cy - r + 5), (int) (r * 2), (int) (r * 2));

        // Light source offset toward upper-left, same direction as your wall bevels
        Point2D cent = new Point2D.Float(cx, cy);
        Point2D focus = new Point2D.Float(cx + r * 0.15f, cy - r * 0.5f);

        float[] fracts = {0f, 0.7f, 1f};
        Color[] shades = {
            color.brighter().brighter(),
            color,                       
            color.darker().darker()      
        };

        RadialGradientPaint lightPaint = new RadialGradientPaint(cent, r, focus, fracts, shades, MultipleGradientPaint.CycleMethod.NO_CYCLE);
        Paint old = g.getPaint();

        g.setPaint(lightPaint);
        g.fillOval((int) (cx-r), (int) (cy-r), (int) (r*2), (int) (r*2));
        g.setPaint(old); // restore, so this doesn't leak into other draw calls
    }
}
