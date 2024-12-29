import javax.swing.JFrame;

public class GameFrame extends JFrame {

    GameFrame() {
        this.add(new GamePanel()); // add GamePanel to GameFrame
        this.setTitle("Snake Game"); // set title
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // set close operation
        this.setResizable(false); // set resizable
        this.pack(); // pack
        this.setVisible(true); // set visible
        this.setLocationRelativeTo(null); // set location
    }
}
