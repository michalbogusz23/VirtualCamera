
import javax.swing.*;
import java.awt.*;
//import java.awt.geomjava.Line2D;

public class Display extends JFrame {

    Screen screen = new Screen();

    public Display() {
        add(screen);
        setVisible(true);
        pack();
    }

    public static void main(String[] args) {
        JFrame frame = new Display();
    }
}
