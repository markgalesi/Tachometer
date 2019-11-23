import java.awt.EventQueue;
import javax.swing.JFrame;


public class Client extends JFrame
{
    public Client()
    {
		Game g = new Game();
        add(new Game());
        setResizable(false);
        pack();
        setTitle("Game");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    public static void main(String[] args)
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                JFrame ex = new Client();
                ex.setVisible(true);
            }
        });
    }
}