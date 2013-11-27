/*
 * Created on Sep 18, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package dmtools.gameapi;

/**
 * @author Dominic
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Player {
	//Information about a player
	private int lives = 0;
	private int score = 0;
	
	public Player() {		
		
	}
	public Player(int lives) {		
		this.lives = lives;
	}
	//Accessors and modifiers
	public int getLives() {
		return this.lives;
	}
	public void setLives(int lives) {
		this.lives = lives;
	}
	public int getScore() {
		return this.score;
	}
	public void setScore(int score) {
		this.score = score;
	}
}
