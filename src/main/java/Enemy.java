

import animation.Animation;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

public class Enemy extends Sprite{
	private boolean ready;
	private boolean hit;
	private boolean brightened;
	private boolean rotates;
	private boolean shoots;
	
	private long blinkTimer = 0;
	private long blinkDelay = 100;
	
	private double rotationAngle;
	
	private BufferedImage bulletImage = GamePanel.getImage(3, 1, 16, 16);
	private BufferedImage originalImage;
	private BufferedImage hitImage;
	
	private int type;
	private int rank;
	
	private double rotationIndex;
	
	public static BufferedImage images[][] = {{GamePanel.getImage(4, 0, 16, 16), GamePanel.getImage(6, 1, 32, 32)}, {GamePanel.getImage(5, 0, 16, 16), GamePanel.getImage(8, 0, 32, 32)}, {GamePanel.getImage(6, 0, 16, 16), GamePanel.getImage(10, 0, 32, 32)}}; 
	
	public static int tiles[][] = {{4, 0}, {5, 0}, {6, 0}};
	
	public Enemy(double x, double y, double width, double height, double dx, double dy, Animation animation){
		super(x, y, width, height, dx, dy, animation);
		
		if(animation == GamePanel.shootingEnemyAnimation){
			setType(4);
			setRank(1);
		}
		
		setStats();
	}
	
	public Enemy(double x, double y, double width, double height, double dx, double dy, BufferedImage image){
		super(x, y, width, height, dx, dy, image);
		
		for(int v = 0; v < images.length; v++){
			for(int u = 0; u < images[v].length; u++){
				if(Sprite.bufferedImagesEqual(getImage(), images[v][u])){
					setType(v + 1);
					setRank(u + 1);
				}
			}
		}
		
		originalImage = getImage();
		
		float scales[] = {2f, 2f, 2f, 1f}, offsets[] = new float[4];
		RescaleOp op = new RescaleOp(scales, offsets, null);
		
		hitImage = op.filter(getImage(), null);
		
		setStats();
	}
	
	public void setStats(){
		brightened = false;
		ready = false;
		rotationIndex = Math.random();
		
		setX(Math.random() * GamePanel.WIDTH / 4 + GamePanel.WIDTH / 4);
		
		if(getType() == 1){
			rotates = true;
			shoots = false;
			rotationAngle = Math.random() * 90;
			
			if(getRank() == 1){
				setWidth(40);
				setHeight(40);
				setHealth(2);
				
				setY(0 - getHeight());
				
				setAngle(Math.random() * 90 + 45);
				
				setSpeed(0.2375 + Math.random() * 0.025);
				
				setDX(getSpeed() * Math.cos(Math.toRadians(getAngle())));
				setDY(getSpeed() * Math.sin(Math.toRadians(getAngle())));
			}
			
			if(getRank() == 2){
				setWidth(80);
				setHeight(80);
				setHealth(4);
				
				setY(0 - getHeight());
				
				setAngle(Math.random() * 90 + 35);
				
				setSpeed(0.1875 + Math.random() * 0.025);
				
				setDX(getSpeed() * Math.cos(Math.toRadians(getAngle())));
				setDY(getSpeed() * Math.sin(Math.toRadians(getAngle())));
			}
		}
		
		if(getType() == 2){
			rotates = true;
			shoots = false;
			rotationAngle = Math.random() * 90;
			
			if(getRank() == 1){
				setWidth(40);
				setHeight(40);
				setHealth(3);
				
				setY(0 - getHeight());
				
				setAngle(Math.random() * 90 + 45);
				
				setSpeed(0.2875 + Math.random() * 0.025);
				
				setDX(getSpeed() * Math.cos(Math.toRadians(getAngle())));
				setDY(getSpeed() * Math.sin(Math.toRadians(getAngle())));
			}
			
			if(getRank() == 2){
				setWidth(80);
				setHeight(80);
				setHealth(5);
				
				setY(0 - getHeight());
				
				setAngle(Math.random() * 90 + 35);
				
				setSpeed(0.2375 + Math.random() * 0.025);
				
				setDX(getSpeed() * Math.cos(Math.toRadians(getAngle())));
				setDY(getSpeed() * Math.sin(Math.toRadians(getAngle())));
			}
		}
		
		if(getType() == 3){
			rotates = true;
			shoots = false;
			rotationAngle = Math.random() * 90;
			
			if(getRank() == 1){
				setWidth(40);
				setHeight(40);
				setHealth(4);
				
				setY(0 - getHeight());
				
				setAngle(Math.random() * 90 + 45);
				
				setSpeed(0.2875 + Math.random() * 0.025);
				
				setDX(getSpeed() * Math.cos(Math.toRadians(getAngle())));
				setDY(getSpeed() * Math.sin(Math.toRadians(getAngle())));
			}
			
			if(getRank() == 2){
				setWidth(80);
				setHeight(80);
				setHealth(5);
				
				setY(0 - getHeight());
				
				setAngle(Math.random() * 90 + 35);
				
				setSpeed(0.2375 + Math.random() * 0.025);
				
				setDX(getSpeed() * Math.cos(Math.toRadians(getAngle())));
				setDY(getSpeed() * Math.sin(Math.toRadians(getAngle())));
			}
		}
		
		if(getType() == 4){
			rotates = false;
			shoots = true;
			rotationAngle = 0;
			
			blinkDelay = 300;
			
			setWidth(40);
			setHeight(40);
			setHealth(5);
			
			setY(0 - getHeight());
			
			setAngle(Math.random() * 90 + 45);
			
			setSpeed(0.2875 + Math.random() * 0.025);
			
			setDX(getSpeed() * Math.cos(Math.toRadians(getAngle())));
			setDY(getSpeed() * Math.sin(Math.toRadians(getAngle())));
			
			setAnimation(GamePanel.shootingEnemyAnimation.getCopy());
		}
		
		if(PowerUp.getSlowDown())
			slow(0.2);
	}
	
	public void setType(int type){
		this.type = type;
	}
	
	public int getType(){
		return type;
	}
	
	public void setRank(int rank){
		this.rank = rank;
	}
	
	public int getRank(){
		return rank;
	}
	
	public void startBlinkTimer(){
		blinkTimer = System.nanoTime();
	}
	

	public static void spawnChild(int type, int rank, Enemy parent){
		Enemy child = new Enemy(0, 0, 0, 0, 0, 0, GamePanel.getImage(tiles[type-1][0], tiles[type-1][1], 16, 16));
		child.setAngle(Math.random() * 360);
		double dx = child.getDX(), dy = child.getDY();
		child.setDX(Math.sqrt((dx * dx) + (dy * dy)) * Math.cos(Math.toRadians(child.getAngle())));
		child.setDY(Math.sqrt((dx * dx) + (dy * dy)) * Math.sin(Math.toRadians(child.getAngle())));
		child.setX(parent.getX());
		child.setY(parent.getY());
		child.setReady(true);
		
		if(PowerUp.getSlowDown())
			child.slow(0.2);
		
		GamePanel.enemies.add(child);
	}
	
	public void shoot(){
		double distanceX = (GamePanel.player.getX() + GamePanel.player.getWidth() / 2) - (getX() + getWidth() / 2);
		double distanceY = (getY() + getHeight() / 2) - (GamePanel.player.getY() + GamePanel.player.getHeight() / 2);
		
		double angle;
		if(distanceX >= 0)
			angle = -Math.atan(distanceY / distanceX);
		else
			angle = (Math.PI - Math.atan(distanceY / distanceX));
		
		Bullet b = new Bullet(getX() + getWidth() / 4, getY() + getHeight() / 4, getWidth() / 2, getHeight() / 2, 0.5 * Math.cos(angle), 0.5 * Math.sin(angle), bulletImage);
		GamePanel.enemyBullets.add(b);
	}
	
	public void setReady(boolean ready){
		this.ready = ready;
	}
	
	public boolean isHit(){
		return hit;
	}
	
	public void setHit(boolean hit){
		this.hit = hit;
	}
	
	public void update(long elapsedTime){
		setX(getX() + getDX() * elapsedTime);
		setY(getY() + getDY() * elapsedTime);
		
		if(isInsideScreen())
			ready = true;
		
		if(ready){
			if(getX() <= 0){
				setX(0);
				setDX(-getDX());
			}if(getX() + getWidth() >= GamePanel.WIDTH){
				setX(GamePanel.WIDTH - getWidth());
				setDX(-getDX());
			}
			
			if(getY() <= 0){
				setY(0);
				setDY(-getDY());
			}if(getY() + getHeight() >= GamePanel.HEIGHT){
				setY(GamePanel.HEIGHT - getHeight());
				setDY(-getDY());
			}
			
			if(getAnimation() != null){
				if(shoots && getAnimation().hasEnded())
					shoot();
			}
		}
		
		if(getAnimation() != null){
			getAnimation().update(elapsedTime);
		}
		
		if(rotates){
			if(PowerUp.getSlowDown())
				rotationAngle += 2 * rotationIndex * 0.2;
			else
				rotationAngle += 2 * rotationIndex;
			if(rotationAngle >= 360)
				rotationAngle -= 360;
		}
	}
	
	public void render(Graphics2D g){
		if(((System.nanoTime() - blinkTimer) / (1000 * 1000)) < blinkDelay && !brightened){
			if(getAnimation() == null)
				setImage(hitImage);
			else
				getAnimation().setBright(true);
			
			brightened = true;
		}else if(((System.nanoTime() - blinkTimer) / (1000 * 1000)) >= blinkDelay && brightened){
			if(getAnimation() == null)
				setImage(originalImage);
			else
				getAnimation().setBright(false);
			
			brightened = false;
		}
		
	    AffineTransform at = new AffineTransform();
		at.setToTranslation(getX(), getY());
		at.rotate(Math.toRadians(rotationAngle), getWidth() / 2, getHeight() / 2);
		at.scale(getWidth() / getImage().getWidth(null), getHeight() / getImage().getHeight(null));
		
		g.drawImage(getImage(), at, null);
	}
}
