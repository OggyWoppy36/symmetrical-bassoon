
public class RectMaker {
    private boolean visible = true;
    
    private Vector pos; //center
    private double width;
    private double height;
    private double angle;

    public RectMaker(Vector p, double w, double h, double a) {
        pos = p.copy();
        width = w;
        height = h;
        angle = a;
    }






    public void setPos(Vector p) { pos = p.copy(); }
    public void setWidth(double w) { width = w; }
    public void setHeight(double h) { height = h; }
    
}
