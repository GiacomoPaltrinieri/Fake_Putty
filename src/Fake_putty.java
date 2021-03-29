import javax.swing.*;
import java.awt.*;

public class Fake_putty {

    /** public Attributes **/
    public static final Dimension MAIN_FRAME_DIMENSION = new Dimension(600, 450);

    /** private Attributes **/


    /** public methods **/

    /**
     * Create new frame
     *
     * @param user_interface
     * @return JFrame object
     *
     * **/
    public static JFrame new_frame(GUI user_interface){

        JFrame frame= new JFrame();
        frame.setVisible(true);
        frame.setContentPane(user_interface.getPannello_main());

        return frame;
    }

    /** main method **/
    public static void main(String[] args) {

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }

            JFrame frame = new_frame(new GUI());
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setSize(Fake_putty.MAIN_FRAME_DIMENSION);
            ImageIcon foto = new ImageIcon("fake_putty.png");
            frame.setIconImage(foto.getImage());
            frame.setTitle("Fake_Putty");
            frame.setAlwaysOnTop(true);

        } catch (IllegalAccessException | UnsupportedLookAndFeelException | InstantiationException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
