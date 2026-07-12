import java.awt.Graphics2D;

// hoo boy whyd i make this class half of it is unused anyway T-T
public class MouseTracker {
    private boolean isLeftPressed;
    private boolean isRightPressed;
    private boolean onScreen;
    private Vector downPos;
    private Vector currPos;
    private RectMaker selection;
    private boolean visible = true;
    
    public MouseTracker() {
        isLeftPressed = false;
        isRightPressed = false;
        onScreen = false;
        downPos = Vector.ZERO;
        currPos = Vector.ZERO;
        selection = new RectMaker(Vector.ZERO.copy(), 50, 50, 0);
    }

    public void setVisible(boolean b) { visible = b; selection.setVisible(b);}
    public void setLeft(boolean b) { isLeftPressed = b; }
    public void setRight(boolean b) { isRightPressed = b; }
    public void checkOnScreen() {
        Vector v = currPos.copy();
        int w = Game.DIMS;
        onScreen = !(v.x > w || v.x < 0 || v.y > w || v.y < 0);
        setVisible(visible);
    }


    public void setDownPos(Vector v) {
        downPos = v.copy();
        checkOnScreen();
    }

    public void setPos(Vector v) {
        currPos = v.copy();
        checkOnScreen();
        selection.setPos(currPos);
    }

    public void render(Graphics2D g) {
        if (!visible) return;
        selection.render(g);
    }

    public void scrollHeight(double x) {
        double h = selection.getHeight() - x*7.5;
        selection.setHeight(h);
    }

    public void scrollWidth(double x) {
        double w = selection.getWidth() - x*7.5;
        selection.setWidth(w);
    }

    public void scrollRot(double x) {
        double a = selection.getAngle();
        a = a + x*5;
        selection.setAngle(a);
    }

    public CollisionShape createRect() {
        return selection.createRect();
    }

    public boolean isVisible() { return visible; }
    public boolean leftPress() { return isLeftPressed; }
    public boolean rightPress() { return isRightPressed; }
    public boolean isOnScreen() { return onScreen; }
    public Vector getDownPos() { return downPos; }
    public Vector getPos() { return currPos; }
}
