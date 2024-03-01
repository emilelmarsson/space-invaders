

import java.awt.Color;
import java.awt.Graphics2D;

public class Particle extends Sprite{
	private double firstAngle;
	private double secondAngle;
	private double randomAngle;
	private double alpha;
	private double alphaDecayRate;
	
	private Color color;
	private Color colors[];
	
	public Particle(double x, double y, double width, double height, double dx, double dy){
		super(x, y, width, height, dx, dy);
		
		setStats();
	}
	
	private void setStats(){
		setWidth(5 + Math.random() * 2);
		setHeight(5 + Math.random() * 2);
		
		if(getDX() == 0 && getDY() == 0)
			setSpeed(0.15 + Math.random() * 0.1);
		else
			setSpeed(getDX());
		
		setAlpha(255);
		alphaDecayRate = 1.5 + 0.25 * Math.random();
		
		setAngles();
	}
	
	private void setAngles(){
		if(secondAngle > firstAngle){
			setAngle(firstAngle + (secondAngle - firstAngle) * Math.random());
			
			setDX(getSpeed() * Math.cos(Math.toRadians(getAngle())));
			setDY(getSpeed() * Math.sin(Math.toRadians(getAngle())));
		}else{
			if(randomAngle == 0)
				randomAngle = Math.random() * 360;
			setAngle(randomAngle);
			
			setDX(getSpeed() * Math.cos(Math.toRadians(getAngle())));
			setDY(getSpeed() * Math.sin(Math.toRadians(getAngle())));
		}
	}
	
	public boolean hasEnded(){
		return alpha <= 0;
	}
	
	public void setAlpha(double alpha){
		this.alpha = alpha;
	}
	
	public double getAlpha(){
		return alpha;
	}
	
	public void setColor(Color color){
		this.color = color;
	}
	
	public Color getColor(){
		return color;
	}
	
	public void setColors(Color colors[]){
		this.colors = colors.clone();
		
		if(colors != null)
			color = colors[(int)(Math.random() * (colors.length - 1))];
	}
	
	public Color[] getColors(){
		return colors;
	}
	
	public void setFirstAngle(double firstAngle){
		this.firstAngle = firstAngle;
	}
	
	public double getFirstAngle(){
		return firstAngle;
	}
	
	public void setSecondAngle(double secondAngle){
		this.secondAngle = secondAngle;
	}
	
	public double getSecondAngle(){
		return secondAngle;
	}
	
	public void update(long elapsedTime){
		super.update(elapsedTime);
		
		if(getAlpha() - 2 > 0)
			alpha -= alphaDecayRate;
		
		setAngles();
	}
	
	public void render(Graphics2D g){
		if(color == null)
			color = new Color((int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255));
		else
			color = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)getAlpha());
		
		g.setColor(color);
		g.fillRect((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
	}
}
