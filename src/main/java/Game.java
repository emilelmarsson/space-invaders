import javax.swing.*;

public class Game{
	public static void main(String[] args){
		JFrame window = new JFrame("Space Invaders");
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		window.setContentPane(new GamePanel());
		window.setResizable(false);
		window.pack();
		window.setVisible(true);
		window.setLocationRelativeTo(null);
	}
}