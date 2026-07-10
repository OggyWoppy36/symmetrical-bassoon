

public class MouseTracker {
    private boolean isLeftPressed;
    private boolean isRightPressed;
    private boolean onScreen;
    private Vector downPos;
    // private Vector upPos;
    private Vector currPos;
    
    public MouseTracker() {
        isLeftPressed = false;
        isRightPressed = false;
        onScreen = false;
        downPos = Vector.ZERO;
        currPos = Vector.ZERO;
    }

    public void setLeft(boolean b) { isLeftPressed = b; }
    public void setRight(boolean b) { isRightPressed = b; }
    public void setDownPos(Vector v) {
        downPos = v.copy();
        int w = Game.DIMS;
        onScreen = !(v.x > w || v.x < 0 || v.y > w || v.y < 0);
    }
    public void setPos(Vector v) {
        currPos = v.copy();
        int w = Game.DIMS;
        onScreen = !(v.x > w || v.x < 0 || v.y > w || v.y < 0);
    }

    // public void update() {
    //     if (!isLeftPressed) return;
    //     drawSelection(downPos,currPos);
    // }

    // public void drawSelection(Vector a, Vector b) {
        
    // }

    public boolean leftPress() { return isLeftPressed; }
    public boolean rightPress() { return isRightPressed; }
    public boolean onScreen() { return onScreen; }
    public Vector getDownPos() { return downPos; }
    public Vector getPos() { return currPos; }
    
    
}
