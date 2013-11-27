/*
 * Created on Sep 16, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package dmtools.gameapi;

import java.util.List;
import java.awt.Graphics;

import dmtools.gameapi.Actor;
import dmtools.gameapi.Stage;

/**
 * @author Dominic
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StageDirector {
	private List actorList;  //The list of actors
	private Stage stage;	//The stage set
	private int roundScore = 0;  //This is the score for each round of collission detection
	
	public StageDirector() {
		
	}
	
	//Paint all of the actors onto the screen
	public void paintStage(Graphics g) {
		for(int i = 0; i < this.actorList.size(); i++) {
			Actor actor = (Actor)actorList.get(i);
			if(actor.getDisplayOnScreen()) {
				actor.paint(g);
			}
		}		
	}
	
	//Detecte collisions between all registered and visible actors
	//and their interaction with each other as well as 
	//the stage.  Also update their move.
	public void updateStage() {
		this.roundScore = 0;
		for(int i = 0; i < actorList.size(); i++) {
			Actor actor1 = (Actor)actorList.get(i);
			if(actor1.getDisplayOnScreen()) {
				if(!actor1.detectStageCollisions(this.stage)) {
					for(int j = 0; j < actorList.size(); j ++) {
						//Do not detect collisions with ourself
						if(i != j) {
							Actor actor2 = (Actor)actorList.get(j);
							if(actor2.getDisplayOnScreen() && actor1.detectActorCollisions(actor2)) {
								//Collision detected so top searching the list 
								//and update the score if there is any
								this.roundScore += actor1.getPointValue();
								actor2.playSoundEffect();
								actor1.playSoundEffect();
								break;
							}
						}
					}
				} else {
					//Collision was detected so top searching the list 
					//and update the score if there is any
					this.roundScore += actor1.getPointValue();	
					actor1.playSoundEffect();
				}
				//move our actor
				actor1.move();
			}
		}
	}
	
	//Accessors and modifiers
	public void setActorList(List actorList) {
		this.actorList = actorList;
	}	
	public List getActorList() {
		return this.actorList;
	}
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	public Stage getStage() {
		return this.stage;
	}
	public int getRoundScore() {
		return this.roundScore;
	}
	public void setRoundScore(int score) {
		this.roundScore = score;
	}
	
}
