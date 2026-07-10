import java.awt.*;

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

    public void updatePos() {
        pos = Vector.add(pos, vel);
    }

    public void setVel(Vector v) {
        vel = v.copy();
    }

    public Vector getVel() { return vel.copy(); }
    public Vector getPos() { return pos.copy(); }

    public void render(Graphics2D g) {
        g.setColor(color);
        int drawX = (int) (pos.x - radius);
        int drawY = (int) (pos.y - radius);
        g.fillOval(drawX, drawY, (int) (radius * 2), (int) (radius * 2));
    }

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
}
