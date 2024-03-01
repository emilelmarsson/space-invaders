

import animation.Animation;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Bullet extends Sprite{
	private long totalTimeParticles;
	private long particlesSpawnDelay;
	
	public Bullet(double x, double y, double width, double height, double dx, double dy, Animation animation) {
		super(x, y, width, height, dx, dy, animation);
		
		totalTimeParticles = 0;
		particlesSpawnDelay = (long)(50 + Math.random() * 25);
	}
	
	public Bullet(double x, double y, double width, double height, double dx, double dy, BufferedImage image) {
		super(x, y, width, height, dx, dy, image);
	}
	
	public void update(long elapsedTime){
		super.update(elapsedTime);
		
		if((System.nanoTime() - totalTimeParticles) / (1000 * 1000) >= particlesSpawnDelay){
			Particle p = new Particle(getX() + getWidth() / 2, getY() + getHeight() / 2, 2 + Math.random() * 2, 2 + Math.random() * 2, 0.1, 0.1);
			
			if(getDX() < 0){
				p.setFirstAngle(Math.toDegrees(Math.atan(getDY() / getDX())) - 22.5);
				p.setSecondAngle(Math.toDegrees(Math.atan(getDY() / getDX())) + 22.5);
			}else{
				p.setFirstAngle(Math.toDegrees(Math.atan(getDY() / getDX())) - 180 - 22.5);
				p.setSecondAngle(Math.toDegrees(Math.atan(getDY() / getDX())) - 180 + 22.5);
			}
			
			if(Sprite.bufferedImagesEqual(getImage(), GamePanel.getImage(0, 1, 16, 16)))
				p.setColor(new Color((int)(Math.random() * 100), 255, 0));
			else
				p.setColor(new Color(255, (int)(Math.random() * 220), 237));
			
			GamePanel.explosionParticles.add(p);
			
			particlesSpawnDelay = (long)(50 + Math.random() * 25);
			totalTimeParticles = System.nanoTime();
		}
	}
	
	public void render(Graphics2D g){
		g.drawImage(getImage(), (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight(), null);
	}
}
