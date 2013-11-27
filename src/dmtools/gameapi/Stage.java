/*
 * Created on Sep 16, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package dmtools.gameapi;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import dmtools.gameapi.Actor;

/**
 * @author Dominic
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Stage extends Actor {
	//defaults
	private Color backgroundColor = Color.white;
	private Image backgroundImage = null;

	
	public Stage() {
		//Bounding box of screen
		setLeftBounds(0);
		setRightBounds(300);  //width of screen
		setTopBounds(0);  
		setBottomBounds(400); //height of screen	
		setName("Stage");
	}

	//Accessors and modifiers
	public void setBackground(Color color) {
		this.backgroundColor = color;
	}
	public void setBackground(Image image) {
		this.backgroundImage = image;
	}
	public Color getBackgroundColor() {
		return this.backgroundColor;
	}
	public Image getBackgroundImage() {
		return this.backgroundImage;
	}
	
	//Include the base class overrides but we are not using so do nothing
	public void paint(Graphics g) {}
	public boolean detectActorCollisions(Actor actor) {return false; }
	public boolean detectStageCollisions(Stage stage) {return false; }
	
	
}
