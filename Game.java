import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class Game extends JPanel implements ActionListener {                                                                                                                                                            
    public enum directions {
        UP,
        LEFT,
        RIGHT,
        DOWN
    }
    private Ball ball;
    private ArrayList<CollisionShape> walls = new ArrayList<>();
    public static final int DIMS = 800;
    public static int STEPS_PER_FRAME = 1;
    private Timer timer;
    private MouseTracker mouse;

    
    public Game() {
        setPreferredSize(new Dimension(DIMS, DIMS));
        setBackground(Color.BLACK);
        setFocusable(true);
        requestFocusInWindow();
        
        ball = new Ball(DIMS/2.0,DIMS/4.0,30,new Vector(2,2),Color.CYAN);
        walls.add(new CollisionShape(Vector.ZERO,DIMS,-50,Color.RED));
        walls.add(new CollisionShape(Vector.mult(Vector.DOWN,DIMS),DIMS,50,Color.RED));
        walls.add(new CollisionShape(Vector.ZERO,-50,DIMS,Color.RED));
        walls.add(new CollisionShape(Vector.mult(Vector.RIGHT,DIMS),50,DIMS,Color.RED));
        //walls.add(new CollisionShape(new Vector(50,120), 200, 100, Color.RED));
        //walls.add(new CollisionShape(new Vector(600,300), 50, 350, Color.MAGENTA));
        System.out.println(walls.get(2));

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_RIGHT -> {
                        ball.setVel(Vector.mult(ball.getVel(),1.25f));
                    }
                    case KeyEvent.VK_LEFT -> {
                        ball.setVel(Vector.mult(ball.getVel(),0.8f));
                    }
                    case KeyEvent.VK_P -> pause();
                }
            }
        });

        mouse = new MouseTracker();
        MouseAdapter mouseHandler = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouse.setDownPos(new Vector(e.getX(),e.getY()));
                if (e.getButton() == MouseEvent.BUTTON1) {
                    mouse.setLeft(true);
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    mouse.setRight(true);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    mouse.setPos(new Vector(e.getX(),e.getY()));
                    createRect(mouse);
                    mouse.setLeft(false);
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    mouse.setRight(false);
                    makeRect();
                }
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double rot = e.getPreciseWheelRotation();
                
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                mouse.setPos(new Vector(e.getX(), e.getY()));
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                mouse.setPos(new Vector(e.getX(), e.getY()));
            }

            
        };
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);

        timer = new Timer(16, this);
        timer.start();
    }

    public void makeRect() {
        CollisionShape.setType(CollisionShape.types.CENTER);
        CollisionShape w = new CollisionShape(mouse.getPos(),45,80,  randColor());
        walls.add(w);
        CollisionShape.setType(CollisionShape.types.CORNER);
    }

    public void createRect(MouseTracker mouse) {
        CollisionShape w = new CollisionShape(mouse.getDownPos(), mouse.getPos(), randColor());
        if (ball.checkCollision(w, false)) {
            return;
        }
        walls.add(w);
    }

    public void pause() {
        if (timer.isRunning()) {
            timer.stop();
        }
        else timer.start();
    }

    
    @Override
    public void actionPerformed(ActionEvent e) {
        updateGame();
        repaint();
    }

    private void updateGame() {
        for (int i=0; i<STEPS_PER_FRAME; i++) {
            ball.updatePos(1.0/STEPS_PER_FRAME);
            for (CollisionShape wall : walls) {
                ball.checkCollision(wall,true);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(0,0,DIMS,DIMS);
        for (CollisionShape wall : walls) {
            wall.render(g2);
        }
        ball.render(g2);

        if (mouse.leftPress()) {
            drawSelectionBox(g2);
        }
    }

    public void drawSelectionBox(Graphics2D g2) {
        int x = (int) Math.min(mouse.getDownPos().x, mouse.getPos().x);
        int y = (int) Math.min(mouse.getDownPos().y, mouse.getPos().y);
        int w = (int) Math.abs(mouse.getDownPos().x - mouse.getPos().x);
        int h = (int) Math.abs(mouse.getDownPos().y - mouse.getPos().y);

        CollisionShape test = new CollisionShape(new Vector(x,y),w,h,Color.WHITE);
        if (!ball.checkCollision(test, false)) {
            g2.setColor(new Color(255, 255, 255, 100));
        } else {
            g2.setColor(new Color(255,20,20,100));
        }
        g2.fillRect(x, y, w, h);
        g2.setColor(Color.WHITE);
        g2.drawRect(x, y, w, h);
    }

    public static Color randColor() {
        float hue = (float) Math.random();
        float saturation = 0.8f;
        float brightness = 0.9f;
        return Color.getHSBColor(hue, saturation, brightness);
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("My Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new Game());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}