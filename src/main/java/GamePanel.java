import animation.Animation;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable, KeyListener, MouseListener, MouseMotionListener{
	public static int WIDTH = 512;
	public static int HEIGHT = 512;
	
	private Thread thread;
	private boolean running;
	private static boolean paused;
	
	private BufferedImage image;
	private Graphics2D g;
	
	public static Player player;
	public static Sprite rocketBlast;
	private static BufferedImage life;
	private static BufferedImage halfLife;
	private static BufferedImage emptyLife;
	private static BufferedImage power;
	private static BufferedImage powerEmpty;

	public static ArrayList<Bullet> playerBullets;
	public static ArrayList<Bullet> enemyBullets;
	public static ArrayList<Enemy> enemies;
	public static ArrayList<Explosion> explosions;
	public static ArrayList<Text> texts;
	public static ArrayList<PowerUp> powerUps;
	public static ArrayList<Particle> playerParticles;
	public static ArrayList<Particle> explosionParticles;
	public static ArrayList<Particle> bulletParticles;
	
	public static Animation explosion;
	public static Animation shootingEnemyAnimation;
	
	public static BufferedImage sprites[];
	
	private int FPS = 60;
	
	private Cursor defaultCursor;
	private Cursor clickCursor;
	
	private long waveTimer;
	private long waveDelay;
	private int waveNumber;
	private boolean waveTimerSet;
	
	private long totalTimeFire;
	private long rapidFireDuration;
	private long adjustPauseFire;
	
	private long totalTimeSlow;
	private long slowDownDuration;
	private long adjustPauseSlow;
	
	private long totalTimePlayerParticles;
	private long playerParticlesSpawnDelay;
	
	public GamePanel(){
		super();
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();
		
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public void addNotify(){
		super.addNotify();
		if(thread == null){
			thread = new Thread(this);
			thread.start();
		}
	}
	
	public void loadSprites(){
		try{
			BufferedImage bigImage = ImageIO.read(this.getClass().getResource("spritesheet.png"));
			
			sprites = new BufferedImage[16 * 16];
			
			for(int x = 0; x < 16; x++){
				for(int y = 0; y < 16; y++){
					sprites[(x * 16) + y] = bigImage.getSubimage(y * 16, x * 16, 16, 16);
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static BufferedImage getImage(int x, int y, int width, int height){
		try{
			BufferedImage bigImage = ImageIO.read(GamePanel.class.getResource("spritesheet.png"));
			
			return bigImage.getSubimage(x * 16, y * 16, width, height);
		}catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean isPaused(){
		return paused;
	}
	
	public void run(){
		running = true;
		paused = false;
		
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		loadSprites();
		
		Animation playerAnimation = new Animation();
		playerAnimation.addFrame(sprites[0], 150);
		playerAnimation.addFrame(sprites[1], 150);
		playerAnimation.addFrame(sprites[2], 150);
		playerAnimation.addFrame(sprites[3], 150);
		
		Animation rocketAnimation = new Animation();
		rocketAnimation.addFrame(sprites[32], 100);
		rocketAnimation.addFrame(sprites[33], 100);
		rocketAnimation.addFrame(sprites[34], 100);
		rocketAnimation.addFrame(sprites[35], 100);
		
		explosion = new Animation();
		explosion.addFrame(getImage(12, 0, 32, 32), 100);
		explosion.addFrame(getImage(14, 0, 32, 32), 100);
		explosion.addFrame(getImage(12, 2, 32, 32), 100);
		explosion.addFrame(getImage(14, 2, 32, 32), 75);
		explosion.addFrame(getImage(12, 4, 32, 32), 75);
		
		shootingEnemyAnimation = new Animation();
		shootingEnemyAnimation.addFrame(GamePanel.getImage(8, 2, 16, 16), 200);
		shootingEnemyAnimation.addFrame(GamePanel.getImage(9, 2, 16, 16), 200);
		shootingEnemyAnimation.addFrame(GamePanel.getImage(10, 2, 16, 16), 200);
		shootingEnemyAnimation.addFrame(GamePanel.getImage(11, 2, 16, 16), 200);
		shootingEnemyAnimation.addFrame(GamePanel.getImage(8, 3, 16, 16), 200);
		shootingEnemyAnimation.addFrame(GamePanel.getImage(9, 3, 16, 16), 200);
		
		defaultCursor = Toolkit.getDefaultToolkit().createCustomCursor((Image)sprites[17], new Point(16, 16), "Crosshair");
		clickCursor = Toolkit.getDefaultToolkit().createCustomCursor((Image)sprites[18], new Point(16, 16), "Crosshair");
		setCursor(defaultCursor);
		
		player = new Player(WIDTH / 2 - 39 / 2, HEIGHT / 2 - 39 / 2, 39, 39, 0, 0, playerAnimation);
		rocketBlast = new Sprite(WIDTH / 2, HEIGHT / 2, 39, 39, 0, 0, rocketAnimation);
		life = getImage(4, 2, 16, 16);
		halfLife = getImage(4, 3, 16, 16);
		emptyLife = getImage(5, 2, 16, 16);
		power = getImage(5, 3, 16, 16);
		powerEmpty = getImage(6, 3, 16, 16);
		
		playerBullets = new ArrayList<Bullet>();
		enemyBullets = new ArrayList<Bullet>();
		enemies = new ArrayList<Enemy>();
		explosions = new ArrayList<Explosion>();
		texts = new ArrayList<Text>();
		powerUps = new ArrayList<PowerUp>();
		playerParticles = new ArrayList<Particle>();
		explosionParticles = new ArrayList<Particle>();
		bulletParticles = new ArrayList<Particle>();
		
		waveTimer = System.nanoTime();
		waveDelay = 2500;
		waveNumber = 0;
		waveTimerSet = false;
		
		totalTimeFire = 0;
		rapidFireDuration = 3000;
		adjustPauseFire = 0;
		
		totalTimeSlow = 0;
		slowDownDuration = 3000;
		adjustPauseSlow = 0;
		
		totalTimePlayerParticles = 0;
		playerParticlesSpawnDelay = (long)(50 + Math.random() * 25);
		
		long startTime = System.nanoTime();
		long frame = 1000 * 1000 * 1000 / FPS;
		long elapsedTime = 0;
		
		while(running){
			if(!paused)
				update(elapsedTime);
			render();
			draw();
			
			elapsedTime = System.nanoTime() - startTime;
			if(elapsedTime < frame){
				int sleep = (int)((frame - elapsedTime) / (1000 * 1000));
				try{
					Thread.sleep(sleep);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}
			
			startTime += elapsedTime;
		}
	}
	
	public void update(long elapsedTime){
		// UPDATING PLAYER
		player.update(elapsedTime / (1000 * 1000));
		
		// UPDATING PLAYER ROCKET ANIMATION
		rocketBlast.update(elapsedTime / (1000 * 1000));
		
		// SPAWNING ENEMIES
		spawnEnemies();
		
		// UPDATING BULLETS
		for(Bullet b : playerBullets)
			b.update(elapsedTime / (1000 * 1000));
		
		// UPDATING ENEMY BULLETS
		for(Bullet b : enemyBullets)
			b.update(elapsedTime / (1000 * 1000));
		
		// REMOVING PLAYER PARTICLES
		for(int x = 0; x < playerParticles.size(); x++)
			if(playerParticles.get(x).isOutOfBounds() || playerParticles.get(x).hasEnded()){
				playerParticles.remove(x);
				x--;
			}
		
		// REMOVING BULLET PARTICLES
		for(int x = 0; x < bulletParticles.size(); x++)
			if(bulletParticles.get(x).isOutOfBounds() || bulletParticles.get(x).hasEnded()){
				bulletParticles.remove(x);
				x--;
			}
		
		// REMOVING EXPLOSION PARTICLES
		for(int x = 0; x < explosionParticles.size(); x++)
			if(explosionParticles.get(x).isOutOfBounds() || explosionParticles.get(x).hasEnded()){
				explosionParticles.remove(x);
				x--;
			}
		
		// REMOVING BULLETS
		for(int x = 0; x < playerBullets.size(); x++)
			if(playerBullets.get(x).isOutOfBounds()){
				playerBullets.remove(x);
				x--;
			}
		
		// REMOVING ENEMY BULLETS
		for(int x = 0; x < enemyBullets.size(); x++)
			if(enemyBullets.get(x).isOutOfBounds()){
				enemyBullets.remove(x);
				x--;
			}
		
		// UPDATING ENEMIES
		for(Enemy e : enemies)
			e.update(elapsedTime / (1000 * 1000));
		
		for(Explosion e : explosions)
			e.update(elapsedTime / (1000 * 1000));
		
		// TESTING IF ANY ENEMIES ARE DEAD AND ADDING POWERUPS
		for(int x = 0; x < enemies.size(); x++)
			if(enemies.get(x).isDead()){
				explosions.add(new Explosion(enemies.get(x).getX() - ((106 - enemies.get(x).getWidth()) / 2), enemies.get(x).getY() - ((106 - enemies.get(x).getHeight()) / 2), 106, 106, 0, 0, explosion.getCopy()));
				if(enemies.get(x).getRank() == 2)
					for(int y = 0; y < enemies.get(x).getType() + 1; y++)
						Enemy.spawnChild(enemies.get(x).getType(), 1, enemies.get(x));
				
				double random = Math.random();
				if(random < 0.2)
					powerUps.add(new PowerUp(enemies.get(x).getX() + enemies.get(x).getWidth() / 2 - 29 / 2, enemies.get(x).getY() + enemies.get(x).getHeight() / 2 - 29 / 2, 29, 29, 0, 0, null));
				
				enemies.remove(x);
				x--;
			}
		
		// TESTING IF ANY BULLETS HAVE COLLIDED WITH ANY ENEMIES
		for(int x = 0; x < playerBullets.size(); x++){
			Bullet b = playerBullets.get(x);
			double bulletX = b.getX() + b.getWidth() / 2, bulletY = b.getY() + b.getHeight() / 2;
			
			for(int y = 0; y < enemies.size(); y++){
				Enemy e = enemies.get(y);
				
				if(e.isInsideScreen()){
					double enemyX = e.getX() + e.getWidth() / 2, enemyY = e.getY() + e.getHeight() / 2;
				
					double distance = Math.sqrt((bulletX - enemyX) * (bulletX - enemyX) + (bulletY - enemyY) * (bulletY - enemyY));
				
					if(distance < e.getWidth() / 2){
						e.startBlinkTimer();
						
						e.setHealth(enemies.get(y).getHealth() - 1);
						playerBullets.remove(x);
						x--;
						break;
					}
				}
			}
		}
		
		// TESTING IF THE PLAYER HAS COLLIDED WITH ANY ENEMIES
		double playerX = player.getX() + player.getWidth() / 2, playerY = player.getY() + player.getHeight() / 2;
		for(int x = 0; x < enemies.size(); x++){
			Enemy e = enemies.get(x);
			double enemyX = e.getX() + e.getWidth() / 2, enemyY = e.getY() + e.getHeight() / 2;
			
			double distance = Math.sqrt((playerX - enemyX) * (playerX - enemyX) + (playerY - enemyY) * (playerY - enemyY));
			
			if(distance < player.getWidth() / 2 + e.getWidth() / 2 && !player.isDamaged()){
				player.setDamage();
				break;
			}
		}
		
		// TESTING IF THE PLAYER HAS COLLIDED WITH ANY OF THE ENEMIES' BULLETS
		for(int x = 0; x < enemyBullets.size(); x++){
			Bullet b = enemyBullets.get(x);
			double bulletX = b.getX() + b.getWidth() / 2, bulletY = b.getY() + b.getHeight() / 2;
			
			double distance = Math.sqrt((playerX - bulletX) * (playerX - bulletX) + (playerY - bulletY) * (playerY - bulletY));
			if(distance < player.getWidth() / 2 + b.getWidth() / 2 && !player.isDamaged()){
				player.setDamage();
				enemyBullets.remove(x);
				break;
			}
		}
		
		// TESTING IF THE PLAYER HAS PICKED UP ANY POWERUPS AND ADDING POWER BUFFS
		for(int x = 0; x < powerUps.size(); x++){
			PowerUp p = powerUps.get(x);
			
			double powerX = p.getX() + p.getWidth() / 2, powerY = p.getY() + p.getHeight() / 2;
			
			double distance = Math.sqrt((playerX - powerX) * (playerX - powerX) + (playerY - powerY) * (playerY - powerY));
			if(distance < player.getWidth() / 2 + p.getWidth() / 2){
				
				if(powerUps.get(x).getType() == 1)
					player.increaseHealth();
				if(powerUps.get(x).getType() == 2)
					player.increasePowerIndex();
				else if(powerUps.get(x).getType() == 3){
					PowerUp.setRapidFire(true);
					totalTimeFire = System.nanoTime();
				}else if(powerUps.get(x).getType() == 4){
					for(int y = 0; !PowerUp.getSlowDown() && y < enemies.size(); y++){
						enemies.get(y).slow(0.2);
					}
					
					PowerUp.setSlowDown(true);
					
					totalTimeSlow = System.nanoTime();
				}
				//if(powerUps.get(x).getType() == 5)
				
				powerUps.remove(x);
				x--;
			}
		}
		
		// UPDATING POWERUPS
		for(int x = 0; x < powerUps.size(); x++)
			powerUps.get(x).update(elapsedTime / (1000 * 1000));
		
		// UPDATING TEXTS
		for(int x = 0; x < texts.size(); x++)
			texts.get(x).update(elapsedTime / (1000 * 1000));
		
		// TESTING IF ANY EXPLOSIONS HAVE ENDED
		for(int x = 0; x < explosions.size(); x++)
			if(explosions.get(x).getAnimation().hasEnded()){
				explosions.remove(x);
				x--;
			}
		
		// REMOVING POWERUPS
		for(int x = 0; x < powerUps.size(); x++)
			if(powerUps.get(x).hasEnded()){
				powerUps.remove(x);
				x--;
			}
		
		// TESTING IF ANY TEXT ANIMATIONS HAVE ENDED
		for(int x = 0; x < texts.size(); x++)
			if(texts.get(x).hasEnded()){
				texts.remove(x);
				x--;
			}
		
		// UPDATING PLAYER PARTICLES
		for(int x = 0; x < playerParticles.size(); x++)
			playerParticles.get(x).update(elapsedTime / (1000 * 1000));
		
		// UPDATING EXPLOSION PARTICLES
		for(int x = 0; x < explosionParticles.size(); x++)
			explosionParticles.get(x).update(elapsedTime / (1000 * 1000));
		
		// UPDATING BULLET PARTICLES
		for(int x = 0; x < bulletParticles.size(); x++)
			bulletParticles.get(x).update(elapsedTime / (1000 * 1000));
		
		// TESTING IF RAPID FIRE POWERUP HAS ENDED
		if(PowerUp.getRapidFire() && (System.nanoTime() - totalTimeFire) / (1000 * 1000) >= rapidFireDuration){
			totalTimeFire = 0;
			PowerUp.setRapidFire(false);
		}
		
		// SPAWNING NEW PLAYER PARTICLES
		if((System.nanoTime() - totalTimePlayerParticles) / (1000 * 1000) >= playerParticlesSpawnDelay){
			double firstAngle = 0, secondAngle = 0;
			
			if(player.isMovingUp() && player.isMovingLeft()){
				firstAngle = 22.5;
				secondAngle = 67.5;
			}else if(player.isMovingUp() && player.isMovingRight()){
				firstAngle = 112.5;
				secondAngle = 157.5;
			}else if(player.isMovingDown() && player.isMovingRight()){
				firstAngle = 202.5;
				secondAngle = 247.5;
			}else if(player.isMovingDown() && player.isMovingLeft()){
				firstAngle = 292.5;
				secondAngle = 337.5;
			}else if(player.isMovingUp()){
				firstAngle = 67.5;
				secondAngle = 112.5;
			}else if(player.isMovingDown()){
				firstAngle = 247.5;
				secondAngle = 292.5;
			}else if(player.isMovingLeft()){
				firstAngle = 337.5;
				secondAngle = 382.5;
			}else if(player.isMovingRight()){
				firstAngle = 157.5;
				secondAngle = 202.5;
			}
			
			Particle p = new Particle(player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2, 0, 0, 0, 0);
			p.setFirstAngle(firstAngle);
			p.setSecondAngle(secondAngle);
			p.setColor(new Color(255, (int)(Math.random() * 255), 0));
			
			playerParticles.add(p);
			
			playerParticlesSpawnDelay = (long)(50 + Math.random() * 25);
			totalTimePlayerParticles = System.nanoTime();
		}
		
		// TESTING IF SLOW DOWN POWERUP HAS ENDED
		if(PowerUp.getSlowDown() && (System.nanoTime() - totalTimeSlow) / (1000 * 1000) >= slowDownDuration){
			totalTimeSlow = 0;
			
			for(int x = 0; x < enemies.size(); x++)
				enemies.get(x).normalSpeed();
			
			PowerUp.setSlowDown(false);
		}
		
		// GAME OVER
		if(player.isDead())
			running = false;
	}
	
	public void render(){
		// RENDERING THE BACKGROUND
		g.setColor(new Color(0, 0, 0, 100));
		g.fillRect(0, 0, WIDTH, HEIGHT);
			
		// RENDERING ENEMIES
		for(Enemy e : enemies)
			e.render(g);
		
		// RENDERING EXPLOSIONS
		for(Explosion e : explosions)
			e.render(g);
		
		// RENDERING ROCKET PARTICLES
		for(Particle p : playerParticles)
			p.render(g);
		
		// RENDERING EXPLOSION PARTICLES
		for(Particle p : explosionParticles)
			p.render(g);
		
		// RENDERING BULLET PARTICLES
		for(Particle p : bulletParticles)
			p.render(g);
		
		// RENDERING THE ROCKET BLAST
		if(!paused){
			AffineTransform at = new AffineTransform();
		
			double degrees = 0;
			double xOffset = player.getWidth() / 2, yOffset = player.getHeight() / 2;
			boolean draw = true;
			if(player.isMovingUp() && player.isMovingLeft())
				degrees = 225;
			else if(player.isMovingUp() && player.isMovingRight()){
				degrees = 315;
				xOffset = -xOffset;
			}else if(player.isMovingDown() && player.isMovingLeft()){
				degrees = 135;
				yOffset = -yOffset;
			}else if(player.isMovingDown() && player.isMovingRight()){
				degrees = 45;
				xOffset = -xOffset;
				yOffset = -yOffset;
			}else if(player.isMovingUp()){
				degrees = 270;
				xOffset = 0;
			}else if(player.isMovingDown()){
				degrees = 90;
				xOffset = 0;
				yOffset = -player.getHeight() / 2;
			}else if(player.isMovingLeft()){
				degrees = 180;
				xOffset = player.getWidth() / 2;
				yOffset = 0;
			}else if(player.isMovingRight()){
				degrees = 0;
				xOffset = -player.getWidth() / 2;
				yOffset = 0;
			}else
				draw = false;
		
			if(draw){
				at.setToTranslation(player.getX() + xOffset, player.getY() + yOffset);
				at.rotate(Math.toRadians(degrees), player.getWidth() / 2, player.getHeight() / 2);
				at.scale(player.getWidth() / player.getImage().getWidth(null), player.getHeight() / player.getImage().getHeight(null));
				g.drawImage(rocketBlast.getImage(), at, null);
			}
		}
		
		// RENDERING POWERUPS
		for(int x = 0; x < powerUps.size(); x++)
			powerUps.get(x).render(g);
	
		// RENDERING THE PLAYER
		player.render(g);
		
		// RENDERING THE THE PLAYER'S BULLETS
		for(Bullet b : playerBullets)
			b.render(g);
		
		// RENDERING THE ENEMIES' BULLETS
		for(Bullet b : enemyBullets)
			b.render(g);
		
		// RENDERING THE PLAYER'S HEALTH
		int x;
		for(x = 0; x < (player.getHealth() % 2 == 0 ? player.getHealth() : player.getHealth() - 1); x++)
			if(x % 2 == 0)
				g.drawImage(life, 10 + x / 2 * 35, 10, 30, 30, null);
		
		if(player.getHealth() % 2 != 0)
			for(;x < player.getHealth(); x++)
				g.drawImage(halfLife, 10 + x / 2 * 35, 10, 30, 30, null);
		
		for(;x < 6; x++)
			if(x % 2 == 0)
				g.drawImage(emptyLife, 10 + x / 2 * 35, 10, 30, 30, null);
		
		// RENDERING POWER METER
		for(x = 0; x < player.getPowerIndex(); x++)
			g.drawImage(power, 10 + x * 20, 50, 15, 15, null);
		
		for(;x < Player.getMaxPowerIndex(); x++)
			g.drawImage(powerEmpty, 10 + x * 20, 50, 15, 15, null);
		
		// RENDERING POWERUP METERS
		if(PowerUp.getRapidFire()){
			g.setColor(new Color(94, 255, 110));
			g.setStroke(new BasicStroke(4));
			g.drawRect(400, 10, 102, 15);
			
			if(!paused)
				g.fillRect(400, 10, (int)(102 * ((rapidFireDuration - ((System.nanoTime() - totalTimeFire) / (1000 * 1000))) / (double)rapidFireDuration)), 15);
			else
				g.fillRect(400, 10, (int)(102 * ((rapidFireDuration - ((System.nanoTime() - totalTimeFire) / (1000 * 1000))) / (double)rapidFireDuration)), 15);
		}
		
		if(PowerUp.getSlowDown()){
			g.setColor(new Color(255, 106, 0));
			g.setStroke(new BasicStroke(4));
			
			int yOffset = PowerUp.getRapidFire() ? 30 : 0;
			
			g.drawRect(400, 10 + yOffset, 102, 15);
			
			if(!paused)
				g.fillRect(400, 10 + yOffset, (int)(102 * ((slowDownDuration - ((System.nanoTime() - totalTimeSlow) / (1000 * 1000))) / (double)slowDownDuration)), 15);
			else
				g.fillRect(400, 10 + yOffset, (int)(102 * ((slowDownDuration - ((System.nanoTime() - totalTimeSlow) / (1000 * 1000))) / (double)slowDownDuration)), 15);
		}

		// RENDERING TEXTS AND POWERUP FOREGROUNDS
		if(!paused){
			for(Text t : texts)
				t.render(g);
			
			if(PowerUp.getRapidFire()){
				g.setColor(new Color(94, 255, 110, 32));
				g.fillRect(0, 0, WIDTH, HEIGHT);
			}if(PowerUp.getSlowDown() && !PowerUp.getRapidFire()){
				g.setColor(new Color(255, 106, 0, 32));
				g.fillRect(0, 0, WIDTH, HEIGHT);
			}if(!PowerUp.getRapidFire() && !PowerUp.getSlowDown()){
				g.setColor(new Color(255, 255, 255, 12));
				g.fillRect(0, 0, WIDTH, HEIGHT);
			}
			
			adjustPauseFire = 0;
			adjustPauseSlow = 0;
		}
		
		// PREVENTING GAUGES FROM GOING ON WHEN THE GAME IS PAUSED
		if(paused){
			if(adjustPauseFire == 0)
				adjustPauseFire = System.nanoTime() - totalTimeFire;
			if(adjustPauseSlow == 0)
				adjustPauseSlow = System.nanoTime() - totalTimeSlow;
			
			totalTimeFire = System.nanoTime() - adjustPauseFire;
			totalTimeSlow = System.nanoTime() - adjustPauseSlow;
		}
		
		// RENDERING PAUSE FOREGROUND AND TEXT
		if(paused){
			Dimension d = this.getSize();
			
			g.setColor(new Color(255, 255, 255, 32));
			g.fillRect(0, 0, WIDTH, HEIGHT);
			
			Font f = new Font("Century Gothic", Font.BOLD, 18);
			g.setColor(new Color(255, 255, 255, 255));
			g.setFont(f);
			drawCenteredString("Game Paused", d.width, d.height, g);
		}
	}
	
	public void spawnEnemies(){
		if(enemies.size() == 0){
			if(!waveTimerSet){
				waveTimer = System.nanoTime();
				waveNumber++;
				waveTimerSet = true;
				
				if(waveNumber <= 8)
					texts.add(new Text("-  W A V E  " + waveNumber + "  -", 2250));
			}
			
			if((System.nanoTime() - waveTimer) / (1000 * 1000) >= waveDelay){
				if(waveNumber == 1){
					for(int x = 0; x < 30; x++)
						enemies.add(new Enemy(0, 0, 0, 0, 0, 0, Enemy.images[0][0]));
				}
				
				if(waveNumber == 2){
					for(int x = 0; x < 4; x++)
						enemies.add(new Enemy(0, 0, 0, 0, 0, 0, Enemy.images[0][1]));
					
					for(int x = 0; x < 8; x++)
						enemies.add(new Enemy(0, 0, 0, 0, 0, 0, Enemy.images[0][0]));
				}
				
				if(waveNumber == 3){
					for(int x = 0; x < 16; x++)
						enemies.add(new Enemy(0, 0, 0, 0, 0, 0, Enemy.images[0][0]));
					
					for(int x = 0; x < 5; x++)
						enemies.add(new Enemy(0, 0, 0, 0, 0, 0, Enemy.images[1][0]));
				}
				
				if(waveNumber == 4){
					for(int x = 0; x < 16; x++)
						enemies.add(new Enemy(0, 0, 0, 0, 0, 0, Enemy.images[1][0]));
				}
				
				if(waveNumber == 5){
					for(int x = 0; x < 5; x++)
						enemies.add(new Enemy(0, 0, 0, 0, 0, 0, Enemy.images[1][1]));
					
					for(int x = 0; x < 5; x++)
						enemies.add(new Enemy(0, 0, 0, 0, 0, 0, Enemy.images[1][0]));
				}
		
				if(waveNumber == 6){
					for(int x = 0; x < 10; x++)
						enemies.add(new Enemy(0, 0, 0, 0, 0, 0, Enemy.images[0][0]));
					
					for(int x = 0; x < 10; x++)
						enemies.add(new Enemy(0, 0, 0, 0, 0, 0, Enemy.images[1][0]));
				}
		
				if(waveNumber == 7){
					for(int x = 0; x < 4; x++)
						enemies.add(new Enemy(0, 0, 0, 0, 0, 0, shootingEnemyAnimation));
					
					for(int x = 0; x < 3; x++)
						enemies.add(new Enemy(0, 0, 0, 0, 0, 0, Enemy.images[2][1]));
				}
				
				if(waveNumber == 8){
					for(int x = 0; x < 5; x++)
						enemies.add(new Enemy(0, 0, 0, 0, 0, 0, Enemy.images[2][1]));
				}
				
				waveTimerSet = false;
			}
		}
	}
	
	public void drawCenteredString(String s, double w, double h, Graphics g){
		FontMetrics fm = g.getFontMetrics();
	    double x = (w - fm.stringWidth(s)) / 2;
	    double y = (fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2);
	    g.drawString(s, (int)x, (int)y);
	}
	
	public void draw(){
		Graphics g2 = this.getGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();
	}

	public void mousePressed(MouseEvent e){
		setCursor(clickCursor);
		int button = e.getButton();
		if(button == MouseEvent.BUTTON1){
			player.setFiring(true);
			player.setMouseX(e.getX());
			player.setMouseY(e.getY());
		}
	}

	public void mouseReleased(MouseEvent e){
		setCursor(defaultCursor);
		int button = e.getButton();
		if(button == MouseEvent.BUTTON1){
			player.setFiring(false);
		}
	}
	
	public void mouseMoved(MouseEvent e){
	}
	
	public void mouseDragged(MouseEvent e){
		player.setMouseX(e.getX());
		player.setMouseY(e.getY());
	}
	
	public void mouseClicked(MouseEvent e){
	}

	public void mouseEntered(MouseEvent e){
	}

	public void mouseExited(MouseEvent e){
	}
	
	public void keyPressed(KeyEvent e){
		int keyCode = e.getKeyCode();
		if(keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W)
			player.setMovingUp(true);
		if(keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A)
			player.setMovingLeft(true);
		if(keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S)
			player.setMovingDown(true);
		if(keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D)
			player.setMovingRight(true);
		if(keyCode == KeyEvent.VK_ESCAPE || keyCode == KeyEvent.VK_P)
			paused = paused ? false : true;
	}
	
	public void keyReleased(KeyEvent e){
		int keyCode = e.getKeyCode();
		if(keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W)
			player.setMovingUp(false);
		if(keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A)
			player.setMovingLeft(false);
		if(keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S)
			player.setMovingDown(false);
		if(keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D)
			player.setMovingRight(false);
	}
	
	public void keyTyped(KeyEvent e){
	}
}