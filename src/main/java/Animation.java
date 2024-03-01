

import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.util.ArrayList;

public class Animation {
	private ArrayList frames, brightenedFrames;
	private int currentFrameIndex;
	private long animationTime;
	private long totalDuration;
	
	private boolean ended;
	private boolean bright;
	
	public Animation(){
		frames = new ArrayList();
		brightenedFrames = new ArrayList();
		totalDuration = 0;
		bright = false;
		start();
	}
	
	public boolean hasEnded(){
		return ended;
	}
	
	public void setBright(boolean bright){
		this.bright = bright;
	}
	
	public boolean isBright(){
		return bright;
	}
	
	public animation.Animation getCopy(){
		animation.Animation animation = new animation.Animation();
		animation.setFrames(getFrames());
		animation.setBrightenedFrames(getBrightenedFrames());
		animation.setDuration(getDuration());
		
		return animation;
	}
	
	private ArrayList getFrames(){
		return frames;
	}
	
	private ArrayList getBrightenedFrames(){
		return brightenedFrames;
	}
	
	private void setFrames(ArrayList frames){
		this.frames = frames;
	}
	
	private void setBrightenedFrames(ArrayList brightenedFrames){
		this.brightenedFrames = frames;
	}
	
	private long getDuration(){
		return totalDuration;
	}
	
	private void setDuration(long totalDuration){
		this.totalDuration = totalDuration;
	}
	
	public synchronized void addFrame(BufferedImage image, long duration){
		totalDuration += duration;
		frames.add(new AnimationFrame(image, totalDuration));
		
		float scales[] = {2.0f, 2.0f, 2.0f, 1.0f}, offsets[] = new float[4];
		RescaleOp op = new RescaleOp(scales, offsets, null);
		brightenedFrames.add(new AnimationFrame(op.filter(image, null), totalDuration));
	}
	
	public synchronized void start(){
		animationTime = 0;
		currentFrameIndex = 0;
	}
	
	public synchronized void update(long elapsedTime){
		if(frames.size() > 1){
			ended = false;
			animationTime += elapsedTime;
			
			if(animationTime >= totalDuration){
				animationTime = animationTime % totalDuration;
				currentFrameIndex = 0;
				
				ended = true;
			}
			
			while(animationTime > getFrame(currentFrameIndex).endTime){
				currentFrameIndex++;
			}
		}
	}
	
	public synchronized BufferedImage getImage(){
		if(frames.size() == 0)
			return null;
		else
			return getFrame(currentFrameIndex).image;
	}
	
	public AnimationFrame getFrame(int i){
		if(bright)
			return (AnimationFrame) brightenedFrames.get(i);
		else
			return (AnimationFrame) frames.get(i);
	}
	
	private class AnimationFrame{
		BufferedImage image;
		long endTime;
		
		public AnimationFrame(BufferedImage image, long endTime){
			this.image = image;
			this.endTime = endTime;
		}
	}
}
