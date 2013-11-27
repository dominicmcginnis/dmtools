/*
 * Created on Sep 16, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package dmtools.gameapi;

import java.applet.AudioClip;
import java.awt.Graphics;


/**
 * @author Dominic
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class Actor {

	//position placement variables
	private int xpos;
	private int ypos;

	//Default to 0 for no movement
	private int xSpeedFactor = 0;
	private int ySpeedFactor = 0;
	private int xVector = 0;
	private int yVector = 0;
	private boolean displayOnScreen = true;  //display actor on screen

	//The left, right, top, and bottom limits of the actor
	private int leftBounds;
	private int rightBounds;
	private int topBounds;
	private int bottomBounds;
	
	private String name = ""; //Name of the actor
	
	//An actors point value
	private int pointValue = 0;
	
	//Sound effects
	private AudioClip soundEffectsPlayer = null;
	private String soundEffectName = "";

	public Actor() {
		
	}
	
	//All subclasses need to define since their shape and collision
	//logic is unique
	public abstract void paint(Graphics g);
	public abstract boolean detectActorCollisions(Actor actor);
	public abstract boolean detectStageCollisions(Stage stage);

	//Update our movement
	public void move() {	
		//update x + y coordinates
		xpos += xVector;
		ypos += yVector;
	}
	
	//Accessors and modifiers
	public void setXPos(int pos) {
		xpos = pos;
	}
	public int getXPos() {
		return xpos;
	}
	public void setYPos(int pos) {
		ypos = pos;
	}
	public int getYPos() {
		return ypos;
	}
	public void setXSpeedFactor(int speed) {
		xSpeedFactor = speed;
	}
	public int getXSpeedFactor() {
		return xSpeedFactor;
	}
	public void setYSpeedFactor(int speed) {
		ySpeedFactor = speed;
	}
	public int getYSpeedFactor() {
		return ySpeedFactor;
	}		
	public void setXVector(int vector) {
		xVector = vector;
	}
	public int getXVector() {
		return xVector;
	}
	public void setYVector(int vector) {
		yVector = vector;
	}
	public int getYVector() {
		return yVector;
	}		
	public void setDisplayOnScreen(boolean b) {
		this.displayOnScreen = b;
	}
	public boolean getDisplayOnScreen() {
		return this.displayOnScreen;
	}
	public void setLeftBounds(int bounds) {
		this.leftBounds = bounds;
	}
	public int getLeftBounds() {
		return this.leftBounds;
	}
	public void setRightBounds(int bounds) {
		this.rightBounds = bounds;
	}
	public int getRightBounds() {
		return this.rightBounds;
	}
	public void setTopBounds(int bounds) {
		this.topBounds = bounds;
	}
	public int getTopBounds() {
		return this.topBounds;
	}
	public void setBottomBounds(int bounds) {
		this.bottomBounds = bounds;
	}
	public int getBottomBounds() {
		return this.bottomBounds;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return this.name;
	}
    public void setPointValue(int value) {
    	this.pointValue = value;
    }
    public int getPointValue() {
    	return this.pointValue;
    }
	public void setSoundEffectsPlayer(AudioClip ac) {
		soundEffectsPlayer = ac;
	}
	public void setSoundEffectName(String name) {
		soundEffectName = name;
	}
	public AudioClip getSoundEffectsPlayer() {
		return soundEffectsPlayer;
	}
	public String getSoundEffectName() {
		return soundEffectName;
	}	
	public void playSoundEffect() {
		if(this.soundEffectsPlayer != null) {
			this.soundEffectsPlayer.play();
		}
	}
	public void loopSoundEffect() {
		if(this.soundEffectsPlayer != null) {
			this.soundEffectsPlayer.loop();
		}
	}
	public void stopSoundEffect() {
		if(this.soundEffectsPlayer != null) {
			this.soundEffectsPlayer.stop();
		}
	}

}
