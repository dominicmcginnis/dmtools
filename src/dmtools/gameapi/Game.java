package dmtools.gameapi;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import java.net.URL;

import dmtools.gameapi.StageDirector;
import dmtools.gameapi.Stage;
import dmtools.gameapi.GameState;

/**
 * @author Dominic
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class Game extends Applet implements Runnable {

	//The games stage and director
	private StageDirector director = new StageDirector();
	private Stage stage = new Stage();

	//current state of the game
	private int gameState = GameState.INIT; 

	//double buffering variables
	private Image dbImage = null;
	private Graphics dbg = null;
	
	//Size of the screen
	private int appWidth = 400;  //default
	private int appHeight = 500; //default
	
	//Background
	private String backgroundImageName = "";
	private Color backgroundColor = Color.black; //default

	public Game () {
		super();
		init();
	}
	
	//init functions
	public void init() { 
		//Set the applets size
		setSize(appWidth, appHeight);	
		
		//Re-initialize everything back to its defaults
		gameState = GameState.INIT; 

		//Initialize the stage		
		stage.setLeftBounds(0);
		stage.setRightBounds(appWidth);
		stage.setBottomBounds(appHeight);
		stage.setTopBounds(21);
		loadBackground();

		director.setStage(stage);


		//Any other initialization information that the sub-class needs to do
		initEx();
		
		//Finish initializing the actors
		setupActors();		
	} 

	public abstract void setupActors();
		
	//To be overriden by any extended initialization that the sub-class needs
	public void initEx() {

	}


	public void destroy() { 
		stage.stopSoundEffect();		
	} 

	// Graphics functions
	public void update(Graphics g) {
		//Do double buffering logic
		//Only create the image buffer once
		if(dbImage == null) {
			dbImage = createImage(this.getSize().width, this.getSize().height);
			dbg = dbImage.getGraphics();
		}
		//first clear the screen in background so we are working
		//with a clear slate
		dbg.setColor(getBackground()); 
		dbg.fillRect(0, 0, this.getSize().width, this.getSize().height); 

		//draw the image to the buffer
		dbg.setColor(getForeground()); 
		paint(dbg); 

		//flip the image from the buffer to the screen 
		g.drawImage(dbImage, 0, 0, this); 
	}	

	public void paint(Graphics g) {
		//set the background to an image one exists
		if(this.stage.getBackgroundImage() != null) {
			g.drawImage (this.stage.getBackgroundImage(), 0, 0, appWidth, appHeight, this); 
		} else {
			setBackground(this.stage.getBackgroundColor());
		} 

		if(gameState == GameState.INIT) {
			drawIntro(g);
		} else if(gameState == GameState.ENDLEVEL) {
			drawNextLevel(g);
		} else if(gameState == GameState.GAMEWINNER) {
			drawGameWinner(g);
		} else if(gameState == GameState.GAMEOVER) {
			drawGameOver(g);
		}

		//Used for passing graphics to overriden sub-class if needed
		drawMisc(g);

		//Have the director draw all of the actors
		director.paintStage(g);		
		
	}

	public void drawMisc(Graphics g) {
		//Used as an override in case the sub-class needs access to the
		//parents paint class for graphics drawing
	}

	public void loadBackground() {
		stage.setBackground(this.backgroundColor);		
		try {
			stage.setBackground(getImage(new URL(getCodeBase(), this.backgroundImageName)));
		} catch (Exception e) {
			setBackground(null);
			e.printStackTrace();
		} 

	}

	public abstract void drawIntro(Graphics g);
	public abstract void drawNextLevel(Graphics g);
	public abstract void drawGameWinner(Graphics g);
	public abstract void drawGameOver(Graphics g);


	//Accessors/Mutators
	public void setBackgroundImageName(String name) {
	   	this.backgroundImageName = name;
	} 
	public void setBackgroundColor(Color c) {
		this.backgroundColor = c;
	}
	public void setAppWidth(int w) {
		this.appWidth = w;
	}
	public void setAppHeight(int h) {
		this.appHeight = h;
	}
	public Stage getStage() {
		return this.stage;
	}
	public StageDirector getStageDirector() {
		return this.director;
	}
	public int getGameState() {
		return this.gameState;
	}
	public void setGameState(int s) {
		this.gameState = s;
	}
	
}

