import javax.swing.*;
import java.awt.*;

public class Test_Frame extends JFrame{
    private static JFrame frame;

    public Test_Frame() {
        this.frame = new JFrame();
        this.frame.setDefaultCloseOperation(3);
        this.frame.setSize(800,800);
        this.frame.setLayout(new GridLayout());
    }
}
