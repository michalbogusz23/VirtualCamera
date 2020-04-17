import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Screen extends JPanel implements KeyListener {
    private final int screenWidth = 640;
    private final int screenHeight = 480;
    private int cameraViewAngle = 90;
    private final int step = 2;
    private final int rotStep = 1;
    private final int fps = 30;
    private double sleepTime = 1000 / fps, lastRefresh = 0;
    private Point3d[] firstCube = new Point3d[]{
            new Point3d(-200, 100, 350),
            new Point3d(-100, 100, 350),
            new Point3d(-100, 100, 450),
            new Point3d(-200, 100, 450),
            new Point3d(-200, -100, 350),
            new Point3d(-100, -100, 350),
            new Point3d(-100, -100, 450),
            new Point3d(-200, -100, 450),
    };
    private Point3d[] secCube = new Point3d[8];
    private Point3d[] thirdCube = new Point3d[8];
    private Point3d[] fourthCube = new Point3d[8];
    private Point3d[] allVertices = new Point3d[32];
    // Point[] firstDrawableCube = new Point[8];

    public Screen() {
        addKeyListener(this);
        setFocusable(true);
        setPreferredSize(new Dimension(640, 480));
        duplicateCubes();
        mergeCubes();
    }

    public void mergeCubes() {
        System.arraycopy(firstCube, 0, allVertices, 0, 8);
        System.arraycopy(secCube, 0, allVertices, 8, 8);
        System.arraycopy(thirdCube, 0, allVertices, 16, 8);
        System.arraycopy(fourthCube, 0, allVertices, 24, 8);
    }

    public void duplicateCubes() {
        secCube = firstCube.clone();
        thirdCube = firstCube.clone();
        fourthCube = firstCube.clone();

        secCube = Calculator.translateVertices(secCube, 300, 0, 0);
        thirdCube = Calculator.translateVertices(thirdCube, 300, 0, 200);
        fourthCube = Calculator.translateVertices(fourthCube, 0, 0, 200);
    }

    public void paintCube(Graphics g, Point[] drawableCube, int i) {
        int shift = i * 8;
        //top edges
        g.drawLine((int) drawableCube[0 + shift].x, (int) drawableCube[0 + shift].y, (int) drawableCube[1 + shift].x, (int) drawableCube[1 + shift].y);
        g.drawLine((int) drawableCube[1 + shift].x, (int) drawableCube[1 + shift].y, (int) drawableCube[2 + shift].x, (int) drawableCube[2 + shift].y);
        g.drawLine((int) drawableCube[3 + shift].x, (int) drawableCube[3 + shift].y, (int) drawableCube[2 + shift].x, (int) drawableCube[2 + shift].y);
        g.drawLine((int) drawableCube[0 + shift].x, (int) drawableCube[0 + shift].y, (int) drawableCube[3 + shift].x, (int) drawableCube[3 + shift].y);
        //bottom edges
        g.drawLine((int) drawableCube[4 + shift].x, (int) drawableCube[4 + shift].y, (int) drawableCube[5 + shift].x, (int) drawableCube[5 + shift].y);
        g.drawLine((int) drawableCube[5 + shift].x, (int) drawableCube[5 + shift].y, (int) drawableCube[6 + shift].x, (int) drawableCube[6 + shift].y);
        g.drawLine((int) drawableCube[6 + shift].x, (int) drawableCube[6 + shift].y, (int) drawableCube[7 + shift].x, (int) drawableCube[7 + shift].y);
        g.drawLine((int) drawableCube[7 + shift].x, (int) drawableCube[7 + shift].y, (int) drawableCube[4 + shift].x, (int) drawableCube[4 + shift].y);
        //side edges
        g.drawLine((int) drawableCube[4 + shift].x, (int) drawableCube[4 + shift].y, (int) drawableCube[0 + shift].x, (int) drawableCube[0 + shift].y);
        g.drawLine((int) drawableCube[5 + shift].x, (int) drawableCube[5 + shift].y, (int) drawableCube[1 + shift].x, (int) drawableCube[1 + shift].y);
        g.drawLine((int) drawableCube[6 + shift].x, (int) drawableCube[6 + shift].y, (int) drawableCube[2 + shift].x, (int) drawableCube[2 + shift].y);
        g.drawLine((int) drawableCube[7 + shift].x, (int) drawableCube[7 + shift].y, (int) drawableCube[3 + shift].x, (int) drawableCube[3 + shift].y);
    }

    public void paintComponent(Graphics g) {
        g.clearRect(0, 0, screenWidth, screenHeight);
        g.drawString(System.currentTimeMillis() + "", 20, 20);
        Point[] drawablePoints = Calculator.map3Dto2d(allVertices, screenWidth, screenHeight, cameraViewAngle);
        for (int i = 0; i < 4; i++)
            paintCube(g, drawablePoints, i);
        sleepAndRefresh();
    }

    void sleepAndRefresh() {
        while (true) {
            if ((System.currentTimeMillis() - lastRefresh) > sleepTime) {
                lastRefresh = System.currentTimeMillis();
                repaint();
                break;
            } else {
                try {
                    Thread.sleep((long) (sleepTime - (System.currentTimeMillis() - lastRefresh)));
                } catch (Exception e) {

                }
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT)
            Calculator.translateVertices(allVertices, step, 0, 0);
        if (e.getKeyCode() == KeyEvent.VK_RIGHT)
            Calculator.translateVertices(allVertices, -step, 0, 0);
        if (e.getKeyCode() == KeyEvent.VK_UP)
            Calculator.translateVertices(allVertices, 0, 0, -step);
        if (e.getKeyCode() == KeyEvent.VK_DOWN)
            Calculator.translateVertices(allVertices, 0, 0, step);
        if (e.getKeyCode() == KeyEvent.VK_A)
            Calculator.translateVertices(allVertices, 0, -step, 0);
        if (e.getKeyCode() == KeyEvent.VK_Z)
            Calculator.translateVertices(allVertices, 0, step, 0);
        if (e.getKeyCode() == KeyEvent.VK_NUMPAD4)
            Calculator.rotateVertices(allVertices, -rotStep, 'z');
        if (e.getKeyCode() == KeyEvent.VK_NUMPAD6)
            Calculator.rotateVertices(allVertices, rotStep, 'z');
        if (e.getKeyCode() == KeyEvent.VK_NUMPAD8)
            Calculator.rotateVertices(allVertices, -rotStep, 'x');
        if (e.getKeyCode() == KeyEvent.VK_NUMPAD2)
            Calculator.rotateVertices(allVertices, rotStep, 'x');
        if (e.getKeyCode() == KeyEvent.VK_NUMPAD7)
            Calculator.rotateVertices(allVertices, -rotStep, 'y');
        if (e.getKeyCode() == KeyEvent.VK_NUMPAD9)
            Calculator.rotateVertices(allVertices, rotStep, 'y');
        if (e.getKeyCode() == KeyEvent.VK_ADD)
            cameraViewAngle--;
        if (e.getKeyCode() == KeyEvent.VK_SUBTRACT)
            cameraViewAngle++;

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
