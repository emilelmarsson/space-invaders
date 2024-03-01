

import animation.Animation;

import java.awt.image.BufferedImage;

public class Sprite{
	private double x;
	private double y;
	
	private double dx;
	private double dy;
	
	private double speed;
	
	private double width;
	private double height;
	
	private BufferedImage image;
	
	private Animation animation;
	
	private boolean firing;
	
	private int health;
	
	private double angle;
	
	public Sprite(double x, double y){
		this.x = x;
		this.y = y;
		
		firing = false;
	}
	
	public Sprite(double x, double y, double width, double height, double dx, double dy){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.dx = dx;
		this.dy = dy;
		
		firing = false;
	}
	
	public Sprite(double x, double y, double width, double height, double dx, double dy, Animation animation){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.dx = dx;
		this.dy = dy;
		this.animation = animation;
		
		firing = false;
	}
	
	public Sprite(double x, double y, double width, double height, double dx, double dy, BufferedImage image){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.dx = dx;
		this.dy = dy;
		this.image = image;
		
		firing = false;
	}
	
	public Sprite(Animation animation){
		this.animation = animation;
		
		firing = false;
	}
	
	public Sprite(BufferedImage image){
		this.image = image;
		
		firing = false;
	}
	
	public static boolean bufferedImagesEqual(BufferedImage img1, BufferedImage img2){
	    if (img1.getWidth() == img2.getWidth() && img1.getHeight() == img2.getHeight()) {
	        for (int x = 0; x < img1.getWidth(); x++) {
	            for (int y = 0; y < img1.getHeight(); y++) {
	                if (img1.getRGB(x, y) != img2.getRGB(x, y))
	                    return false;
	            }
	        }
	    } else {
	        return false;
	    }
	    return true;
	}
	
	public void update(long elapsedTime){
		x += dx * elapsedTime;
		y += dy * elapsedTime;
		
		if(animation != null)
			animation.update(elapsedTime);
	}
	
	public boolean isOutOfBounds(){
		return getX() + getWidth() < 0 || getX() + getWidth() > GamePanel.WIDTH + getWidth() || getY() + getHeight() < 0 || getY() + getHeight() > GamePanel.HEIGHT + getHeight();
	}
	
	public boolean isInsideScreen(){
		return getX() > 0 && getX() + getWidth() < GamePanel.WIDTH && getY() > 0 && getY() + getHeight() < GamePanel.HEIGHT;
	}
	
	public void slow(double slowFactor){
		setDX(getDX() * slowFactor);
		setDY(getDY() * slowFactor);
	}
	
	public void normalSpeed(){
		double dx = getDX(), dy = getDY();
		
		if(getDX() > 0 && getDY() > 0){
			setDX(getSpeed() * Math.cos(Math.atan(dy / dx)));
			setDY(getSpeed() * Math.sin(Math.atan(dy / dx)));
		}else if(getDX() < 0 && getDY() > 0){
			setDX(getSpeed() * Math.cos(Math.atan(dy / dx) + Math.PI));
			setDY(getSpeed() * Math.sin(Math.atan(dy / dx) + Math.PI));
		}else if(getDX() < 0 && getDY() < 0){
			setDX(getSpeed() * Math.cos(Math.atan(dy / dx) + Math.PI));
			setDY(getSpeed() * Math.sin(Math.atan(dy / dx) + Math.PI));
		}else if(getDX() > 0 && getDY() < 0){
			setDX(getSpeed() * Math.cos(Math.atan(dy / dx)));
			setDY(getSpeed() * Math.sin(Math.atan(dy / dx)));
		}
	}
	
	public double getX(){
		return x;
	}
	
	public void setX(double x){
		this.x = x;
	}
	
	public double getY(){
		return y;
	}
	
	public void setY(double y){
		this.y = y;
	}
	
	public double getSpeed(){
		return speed;
	}
	
	public void setSpeed(double speed){
		this.speed = speed;
	}
	
	public double getDX(){
		return dx;
	}
	
	public void setDX(double dx){
		this.dx = dx;
	}
	
	public double getDY(){
		return dy;
	}
	
	public void setDY(double dy){
		this.dy = dy;
	}
	
	public double getWidth(){
		return width;
	}
	
	public void setWidth(double width){
		this.width = width;
	}
	
	public double getHeight(){
		return height;
	}
	
	public void setHeight(double height){
		this.height = height;
	}
	
	public BufferedImage getImage(){
		if(animation == null)
			return image;
		else
			return animation.getImage();
	}
	
	public void setImage(BufferedImage image){
		this.image = image;
	}
	
	public Animation getAnimation(){
		return animation;
	}
	
	public void setAnimation(Animation animation){
		this.animation = animation;
	}
	
	public int getHealth(){
		return health;
	}
	
	public void setHealth(int health){
		this.health = health;
	}
	
	public void increaseHealth(){
		if(getHealth() < 6)
			setHealth(getHealth() + 1);
	}
	
	public boolean isDead(){
		return health <= 0;
	}
	
	public boolean isFiring(){
		return firing;
	}
	
	public void setFiring(boolean firing){
		this.firing = firing;
	}
	
	public void setAngle(double angle){
		this.angle = angle;
	}
	
	public double getAngle(){
		return angle;
	}
}
