import java.awt.*;
import java.awt.geom.Point2D;


@SuppressWarnings("unused")
public class Ball {
    private Vector pos;
    private double radius;
    private Vector vel;
    private final Color color;

    public Ball(double x, double y, double r, Vector v, Color c) {
        pos = new Vector(x,y);
        radius = r;
        vel = v.copy();
        color = c.darker().darker();
    }

    public void updatePos(double stepMult) {
        pos = Vector.add(pos, Vector.mult(vel,stepMult));
    }

    public void setVel(Vector v) { vel = v.copy(); }
    public void setRadius(double r) { radius = r; }

    public Vector getVel() { return vel.copy(); }
    public Vector getPos() { return pos.copy(); }

    // it checks if its colliding with a wall (and it works! Y(°o°)Y )
    public boolean checkCollision(CollisionShape wall, boolean resolve) {
        double angle = wall.getAngle();
        Vector testPos = pos.copy();

        // rotate ball to match box rotation
        if (angle != 0) {
            Vector center = wall.getCenter();
            Vector relative = Vector.sub(pos, center);
            Vector localRelative = relative.rotated(-angle);
            testPos = Vector.add(center, localRelative);
        }

        // closest corner
        double closestX = Math.clamp(testPos.x, wall.left, wall.right);
        double closestY = Math.clamp(testPos.y, wall.up, wall.down);

        // distance
        double dx = testPos.x - closestX;
        double dy = testPos.y - closestY;
        double distSq = dx * dx + dy * dy;

        // collision check (use square of distance for faster calculation)
        if (distSq >= radius * radius) return false;

        // calculations for resolution
        if (resolve) {
            double dist = Math.sqrt(distSq);

            // normal vector
            double nx, ny;

            // ball is inside/on the edge of hitbox
            if (dist == 0) {
                // find closest edge
                double overlapX = Math.min(testPos.x - wall.left, wall.right - testPos.x);
                double overlapY = Math.min(testPos.y - wall.up, wall.down - testPos.y);

                // check if x or y wall is closer
                // inside, it sets the normal based on which side wall is closer
                if (overlapX < overlapY) {
                    nx = (testPos.x < (wall.left + wall.right) / 2) ? -1 : 1;
                    ny = 0;
                } else {
                    nx = 0;
                    ny = (testPos.y < (wall.up + wall.down) / 2) ? -1 : 1;
                }
                // epsilon (prevents div by 0)
                //dist = 0.0001;
            } else {
                nx = dx / dist;
                ny = dy / dist;
            }

            // rotate normal vector back
            if (angle != 0) {
                Vector worldNormal = new Vector(nx, ny).rotated(angle);
                nx = worldNormal.x;
                ny = worldNormal.y;
            }

            resolveCollision(nx, ny, dist);
        }

        return true;
    }

    private void resolveCollision(double nx, double ny, double dist) {
        // overlap from center
        double overlap = radius - dist;
        pos.addTo(new Vector(nx * overlap, ny * overlap));

        // get dot product and use to calculate new velocity
        double dot = vel.x * nx + vel.y * ny;
        vel.x -= 2 * dot * nx;
        vel.y -= 2 * dot * ny;
    }

    public void render(Graphics2D g) {
        float cx = (float) pos.x;
        float cy = (float) pos.y;
        float r = (float) radius;

        // drop shadow
        g.setColor(new Color(0, 0, 0, 80));
        g.fillOval((int) (cx - r - 3), (int) (cy - r + 5), (int) (r * 2), (int) (r * 2));

        Point2D cent = new Point2D.Float(cx, cy);
        Point2D focus = new Point2D.Float(cx + r * 0.15f, cy - r * 0.5f);

        float[] lerpPts = {0f, 0.7f, 1f};
        Color[] shades = {
            color.brighter().brighter(),    // highlight color at focus
            color,                          // base color in middle
            color.darker().darker()         // shadow color on edges
        };
        RadialGradientPaint shaderPaint = new RadialGradientPaint(cent, r, focus, lerpPts, shades, MultipleGradientPaint.CycleMethod.NO_CYCLE);
        Paint old = g.getPaint();

        g.setPaint(shaderPaint);
        g.fillOval((int) (cx-r), (int) (cy-r), (int) (r*2), (int) (r*2));
        g.setPaint(old);
    }
}
