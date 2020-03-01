
import java.awt.*;

public class Sprite {
	//DEFINE ATTRIBUTES
	
	//upper left coord
	protected int spriteX, spriteY, seed;
	protected String spriteName;
	//width and height
	protected int spriteW, spriteH;
	//rec for collision
	protected Rectangle r;
	
	//DEFINE SETS AND GETS
	public Rectangle getRectangle () {
		return r;
	}

	//used to assign random number to each row to 
	//generate same speed & step for elements in same row
	void setSeed(int s) {
		this.seed = s;
	}
	public int getSeed() {
		return seed;
	}
	void setCoord (int x, int y) {
		spriteX = x;
		spriteY = y;
		r.y = this.spriteY;
		r.x = this.spriteX;
	}
	
	public int getSpriteX() {
		return spriteX;
	}

	public int getSpriteY() {
		return spriteY;
	}

	public String getSpriteName() {
		return spriteName;
	}

	public void setSpriteName(String spriteName) {
		this.spriteName = spriteName;
	}

	public int getSpriteW() {
		return spriteW;
	}

	public void setSpriteW(int spriteW) {
		this.spriteW = spriteW;
		r.width = this.spriteW;
	}

	public int getSpriteH() {
		return spriteH;
	}

	public void setSpriteH(int spriteH) {
		this.spriteH = spriteH;
		r.height = this.spriteH;
	}
	
	//CONSTRUCTORS
	
	public Sprite() {
		super();
		r = new Rectangle(0,0,0,0);
	}
	
	public Sprite(int spriteX, int spriteY, String spriteName, int spriteW, int spriteH) {
		super();
		this.spriteX = spriteX;
		this.spriteY = spriteY;
		this.spriteName = spriteName;
		this.spriteW = spriteW;
		this.spriteH = spriteH;
		r = new Rectangle(spriteX,spriteY,spriteW,spriteH);
	}
	
	public void Display() {
		System.out.println("X,Y: " + spriteX + "," + spriteY);
	}
}
