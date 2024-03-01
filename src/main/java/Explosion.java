

import animation.Animation;

import java.awt.Color;
import java.awt.Graphics2D;

public class Explosion extends Sprite{
	private long totalTimeParticles;
	private long particlesSpawnDelay;
	
	public Explosion(double x, double y, double width, double height, double dx, double dy, Animation animation){
		super(x, y, width, height, dx, dx, animation);
		
		totalTimeParticles = 0;
		particlesSpawnDelay = (long)(25 + Math.random() * 12.5);
	}
	
	public void update(long elapsedTime){
		if((System.nanoTime() - totalTimeParticles) / (1000 * 1000) >= particlesSpawnDelay){
			Particle p = new Particle(getX() + getWidth() / 2, getY() + getHeight() / 2, 0, 0, 0, 0);
			p.setFirstAngle(0);
			p.setSecondAngle(0);
			p.setColor(new Color(255, (int)(Math.random() * 255), 0));
			
			GamePanel.explosionParticles.add(p);
			
			particlesSpawnDelay = (long)(25 + Math.random() * 12.5);
			totalTimeParticles = System.nanoTime();
		}
		
		getAnimation().update(elapsedTime);
	}
	
	public void render(Graphics2D g){
		g.drawImage(getImage(), (int) getX(), (int) getY(), (int) getWidth(), (int) getHeight(), null);
	}
}
