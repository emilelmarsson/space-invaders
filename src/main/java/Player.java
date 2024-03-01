

import animation.Animation;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Player extends Sprite{	
	private boolean movingUp;
	private boolean movingDown;
	private boolean movingLeft;
	private boolean movingRight;
	
	private BufferedImage bulletImage;
	private long firingTimer;
	private long firingDelay;
	
	private int mouseX;
	private int mouseY;
	
	private boolean hit;
	private long invincibleDelay;
	private long invincibleTimer;
	
	private int powerIndex;
	private int powerLevel;
	
	private static int maxPowerIndex = 8;
	private static int maxPowerLevel = 2;
	
	public Player(double x, double y, double width, double height, double dx, double dy, Animation animation){
		super(x, y, width, height, dx, dy, animation);
		
		bulletImage = GamePanel.getImage(0, 1, 16, 16);
		
		firingDelay = 200;
		firingTimer = System.currentTimeMillis();
		
		setHealth(6);
		invincibleDelay = 2250;
		invincibleTimer = System.currentTimeMillis();
		hit = false;
		
		powerIndex = 0;
		powerLevel = 0;
	}
	
	public static int getMaxPowerIndex(){
		return maxPowerIndex;
	}
	
	public static int getMaxPowerLevel(){
		return maxPowerLevel;
	}
	
	public void increasePowerIndex(){
		if(powerIndex < maxPowerIndex)
			powerIndex++;
		else{
			if(getPowerLevel() != getMaxPowerLevel())
				powerIndex = 1;
			increasePowerLevel();
		}	
	}
	
	public int getPowerIndex(){
		return powerIndex;
	}
	
	public void increasePowerLevel(){
		if(powerLevel < maxPowerLevel)
			powerLevel++;
	}
	
	public int getPowerLevel(){
		return powerLevel;
	}
	
	public void setDamage(){
		setHealth(getHealth() - 1);
		invincibleTimer = System.currentTimeMillis();
		hit = true;
	}
	
	public boolean isDamaged(){
		return System.currentTimeMillis() - invincibleTimer < invincibleDelay && hit;
	}
	
	public int getMouseX(){
		return mouseX;
	}
	
	public void setMouseX(int mouseX){
		this.mouseX = mouseX;
	}
	
	public int getMouseY(){
		return mouseY;
	}
	
	public void setMouseY(int mouseY){
		this.mouseY = mouseY;
	}
	
	public boolean isMovingUp(){
		return movingUp;
	}
	
	public void setMovingUp(boolean movingUp){
		this.movingUp = movingUp;
	}
	
	public boolean isMovingDown(){
		return movingDown;
	}
	
	public void setMovingDown(boolean movingDown){
		this.movingDown = movingDown;
	}
	
	public boolean isMovingLeft(){
		return movingLeft;
	}
	
	public void setMovingLeft(boolean movingLeft){
		this.movingLeft = movingLeft;
	}
	
	public boolean isMovingRight(){
		return movingRight;
	}
	
	public void setMovingRight(boolean movingRight){
		this.movingRight = movingRight;
	}
	
	public boolean isMoving(){
		return isMovingUp() || isMovingDown() || isMovingLeft() || isMovingRight();
	}
	
	public void update(long elapsedTime){
		// SETTING SPEED
		if(isMovingUp())
			setDY(-0.25);
		else if(isMovingDown())
			setDY(0.25);
		else
			setDY(0);
	
		if(isMovingLeft())
			setDX(-0.25);
		else if(isMovingRight())
			setDX(0.25);
		else
			setDX(0);
		
		// COLLISION DETECTION - PLAYER AND MAP
		if(getX() >= 0 && getX() <= GamePanel.WIDTH - getWidth()){
			setX(getX() + getDX() * elapsedTime);
		}else if(getDX() < 0 && getX() > GamePanel.WIDTH - getWidth()){
			setDX(0);
			setX(GamePanel.WIDTH - getWidth());
		}else if(getDX() > 0 && getX() < 0){
			setDX(0);
			setX(0);
		}
		
		if(getY() >= 0 && getY() <= GamePanel.HEIGHT - getHeight()){
			setY(getY() + getDY() * elapsedTime);
		}else if(getDY() < 0 && getY() > GamePanel.HEIGHT - getHeight()){
			setDY(0);
			setY(GamePanel.HEIGHT - getHeight());
		}else if(getDY() > 0 && getY() < 0){
			setDY(0);
			setY(0);
		}
		
		if(PowerUp.getRapidFire())
			firingDelay = 75;
		else
			firingDelay = 200;
		
		// PREVENTING AN OVERLOAD OF BULLETS AND SETTING BULLET SPEED
		if(isFiring() && System.currentTimeMillis() - firingTimer >= firingDelay){
			double distanceX = mouseX - (getX() + getWidth() / 2);
			double distanceY = (getY() + getHeight() / 2) - mouseY;
			
			double angle;
			if(distanceX >= 0)
				angle = -Math.atan(distanceY / distanceX);
			else
				angle = (Math.PI - Math.atan(distanceY / distanceX));
			
			if(getPowerLevel() == 0){
				Bullet b = new Bullet(getX() + getWidth() / 4, getY() + getHeight() / 4, getWidth() / 2, getHeight() / 2, 0.7 * Math.cos(angle), 0.6 * Math.sin(angle), bulletImage);
				GamePanel.playerBullets.add(b);
			}else if(getPowerLevel() == 1){
				Bullet b1 = new Bullet(getX() + getWidth() / 4, getY() + getHeight() / 4, getWidth() / 2, getHeight() / 2, 0.7 * Math.cos(angle + 0.1), 0.6 * Math.sin(angle + 0.1), bulletImage),
				       b2 = new Bullet(getX() + getWidth() / 4, getY() + getHeight() / 4, getWidth() / 2, getHeight() / 2, 0.7 * Math.cos(angle - 0.1), 0.6 * Math.sin(angle - 0.1), bulletImage);
			
				GamePanel.playerBullets.add(b1);
				GamePanel.playerBullets.add(b2);
			}else if(getPowerLevel() == 2){
				Bullet b1 = new Bullet(getX() + getWidth() / 4, getY() + getHeight() / 4, getWidth() / 2, getHeight() / 2, 0.7 * Math.cos(angle + 0.1), 0.6 * Math.sin(angle + 0.1), bulletImage),
					   b2 = new Bullet(getX() + getWidth() / 4, getY() + getHeight() / 4, getWidth() / 2, getHeight() / 2, 0.7 * Math.cos(angle), 0.6 * Math.sin(angle), bulletImage),
					   b3 = new Bullet(getX() + getWidth() / 4, getY() + getHeight() / 4, getWidth() / 2, getHeight() / 2, 0.7 * Math.cos(angle - 0.1), 0.6 * Math.sin(angle - 0.1), bulletImage);
				
				GamePanel.playerBullets.add(b1);
				GamePanel.playerBullets.add(b2);
				GamePanel.playerBullets.add(b3);
			}
			
			firingTimer = System.currentTimeMillis();
		}
		
		getAnimation().update(elapsedTime);
	}
	
	public void render(Graphics2D g){
		if(!GamePanel.isPaused()){
			if(isDamaged())
				getAnimation().setBright(true);
			else
				getAnimation().setBright(false);
		}
		
		g.drawImage(getImage(), (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight(), null);
	}
}
