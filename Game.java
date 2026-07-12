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
    public static Ball ball;
    public ArrayList<CollisionShape> walls = new ArrayList<>();
    public static final int DIMS = 800;
    public static int STEPS_PER_FRAME = 5;
    private final Timer timer;
    private boolean paused = false;
    private MouseTracker mouse;

    
    public Game() {
        setPreferredSize(new Dimension(DIMS, DIMS));
        setBackground(Color.DARK_GRAY);
        setFocusable(true);
        requestFocusInWindow();
        
        ball = new Ball(DIMS/2.0,DIMS/3.0,30,new Vector(2,2),Color.CYAN);
        walls.add(new CollisionShape(Vector.ZERO,DIMS,-50,Color.RED));
        walls.add(new CollisionShape(Vector.mult(Vector.DOWN,DIMS),DIMS,50,Color.RED));
        walls.add(new CollisionShape(Vector.ZERO,-50,DIMS,Color.RED));
        walls.add(new CollisionShape(Vector.mult(Vector.RIGHT,DIMS),50,DIMS,Color.RED));

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
                    case KeyEvent.VK_ALT -> {
                        e.consume();
                    }
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
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    CollisionShape w = mouse.createRect();
                    if (ball.checkCollision(w, false)) {
                        return;
                    }
                    walls.add(w);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    mouse.setPos(new Vector(e.getX(),e.getY()));
                    mouse.setLeft(false);
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    mouse.setRight(false);
                }
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double x = e.getPreciseWheelRotation();
                if (e.isAltDown()) {
                    mouse.scrollWidth(x);
                } else if (e.isShiftDown()) {
                    mouse.scrollHeight(x);
                } else {
                    mouse.scrollRot(x);
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                mouse.setPos(new Vector(e.getX(), e.getY()));
            }

            
        };
        addMouseWheelListener(mouseHandler);
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);

        timer = new Timer(16, this);
        timer.start();
    }

    public void pause() {
        if (timer.isRunning()) {
            //timer.stop();
            mouse.setVisible(false);
            timer.stop(); 
            paused = true;
            repaint();
        }
        else {
            mouse.setVisible(true);
            paused = false;
            timer.start();
        }
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
        
        ball.render(g2);
        for (CollisionShape wall : walls) {
            wall.render(g2);
        }
        mouse.render(g2);
        if (paused) {
            g2.setFont(new Font("Arial", Font.BOLD, 50));
            g2.setColor(Color.WHITE);
            g2.drawString("Game Paused",(DIMS-332)/2,60);
        }
    }

    public static boolean checkBallCollision(CollisionShape c) { 
        return ball.checkCollision(c, false); 
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