

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class Text{
	private String text;
	private long totalTime;
	private long duration;
	private long alpha;
	private boolean increasing;
	
	private static Font font = new Font("Century Gothic", Font.BOLD, 18);
	
	public Text(String text, long duration){
		setText(text);
		setDuration(duration);
		
		alpha = 0;
		totalTime = 0;
		increasing = true;
	}
	
	public void setText(String text){
		this.text = text;
	}
	
	public String getText(){
		return text;
	}
	
	public void setDuration(long duration){
		this.duration = duration;
	}
	
	public long getDuration(){
		return duration;
	}
	
	public boolean hasEnded(){
		return totalTime >= duration;
	}
	
	public void update(long elapsedTime){
		totalTime += elapsedTime;
		
		if(increasing){
			double test = ((double)totalTime / (duration / 2)) * 255;
			alpha = (long)test;
		}else{
			double test = (((duration / 2) - (totalTime - (duration / 2))) / (double)(duration / 2)) * 255;
			alpha = (long)test;
		}
		
		if(alpha >= 255){
			alpha = 255;
			increasing = false;
		}else if(alpha < 0)
			alpha = 0;
	}
	
	public void render(Graphics2D g){
		g.setColor(new Color(255, 255, 255, (int)alpha));
		g.setFont(font);
		
		FontMetrics fm = g.getFontMetrics();
	    double x = (GamePanel.WIDTH - fm.stringWidth(text)) / 2;
	    double y = (fm.getAscent() + (GamePanel.HEIGHT - (fm.getAscent() + fm.getDescent())) / 2);
	    g.drawString(text, (int)x, (int)y);
	}
}
