

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class PowerUp extends Sprite{
	private long totalTime = 0;
	private long startBlinkingTime = 5000;
	private long duration = 7000;
	
	private int type;
	
	private boolean visible = true;
	
	public static BufferedImage healthImage = GamePanel.getImage(0, 3, 16, 16);
	public static BufferedImage powerImage = GamePanel.getImage(1, 3, 16, 16);
	public static BufferedImage fireImage = GamePanel.getImage(2, 3, 16, 16);
	public static BufferedImage timeImage = GamePanel.getImage(3, 3, 16, 16);
	public static BufferedImage missileImage = GamePanel.getImage(0, 4, 16, 16);
	
	private static boolean rapidFire = false;
	private static boolean slowDown = false;
	
	public static void setRapidFire(boolean rapidFire){
		PowerUp.rapidFire = rapidFire;
	}
	
	public static boolean getRapidFire(){
		return rapidFire;
	}
	
	public static void setSlowDown(boolean slowDown){
		PowerUp.slowDown = slowDown;
	}
	
	public static boolean getSlowDown(){
		return slowDown;
	}
	
	public PowerUp(double x, double y, double width, double height, double dx, double dy, BufferedImage image){
		super(x, y, width, height, dx, dy, image);
		
		setStats();
	}
	
	private void setStats(){
		setHeight(29);
		setWidth(29);
		
		randomizeType();
		
		if(getType() == 1) // EXTRA HEALTH
			setImage(healthImage);
		
		if(getType() == 2) // POWER
			setImage(powerImage);
		
		if(getType() == 3) // RAPID FIRE
			setImage(fireImage);
		
		if(getType() == 4) // SLOW DOWN TIME
			setImage(timeImage);
		
		if(getType() == 5) // MISSILE
			setImage(missileImage);
	}
	
	public void setType(int type){
		this.type = type;
	}
	
	public int getType(){
		return type;
	}
	
	public void randomizeType(){
		setType((int)(Math.random() * 4 + 1));
	}
	
	public boolean hasEnded(){
		return totalTime >= duration;
	}
	
	public void update(long elapsedTime){
		totalTime += elapsedTime;
		
		int period = (int)(totalTime - startBlinkingTime) / 200;
		visible = period % 2 == 0 && period >= 0 ? false : true;
	}
	
	public void render(Graphics2D g){
		if(visible)
			g.drawImage(getImage(), (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight(), null);
	}
}
