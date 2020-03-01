//dog object extends sprite for set up

import java.awt.event.*;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Dog extends Sprite implements Runnable {

	private JLabel dogLabel;
	private boolean hit;
	private Thread t;
	private LifeCount lives;

	public Dog() {
		super(0,0,"dogSprite.png",50,50);
		hit = false;
	}

	//pass label in to set it during keyevents
	public void setDogLabel(JLabel dogLabel) {
		this.dogLabel = dogLabel;
	}

	//if hit, set flag to true
	public Boolean getHit() {
		return hit;
	}

	public void setHit(Boolean hit) {
		this.hit = hit;
	}

	//get lives
	public void setLifeCount(LifeCount lives) {
		this.lives = lives;
	}

	//dog loses a life
	public void accident() {
		this.hit = true;

		t = new Thread(this, "hit");
		t.start();
	}

	public void revived() {
		this.hit = false;

		this.setCoord(400,650);
		dogLabel.setIcon(new ImageIcon(getClass().getResource("dogSprite.png")));
		dogLabel.setLocation(this.getSpriteX(), this.getSpriteY());
	}

	public void moveDog (KeyEvent e) {
		int dogX = this.spriteX;
		int dogY = this.spriteY;

		//restrict dog to move only inside screen content, cannot
		//move outside of designated area or switch side to side/top to bottom
		//also change img if moving left or right
		if (e.getKeyCode()== KeyEvent.VK_DOWN) {
			if (this.spriteY == (GameProperties.SCREEN_HEIGHT - this.getSpriteH())) {

			} else {
				dogY += GameProperties.CHARACTER_STEP;
			}
		} else if (e.getKeyCode()== KeyEvent.VK_UP) {
			if (this.spriteY == 0) {

			} else {
				dogY -= GameProperties.CHARACTER_STEP;
			}
		} else if (e.getKeyCode()== KeyEvent.VK_LEFT) {
			if (this.spriteX <= 0) {

			} else {
				dogLabel.setIcon(new ImageIcon(getClass().getResource("dogSprite.png")));
				dogX -= GameProperties.CHARACTER_STEP;
			}
		} else if (e.getKeyCode()== KeyEvent.VK_RIGHT) {
			if (this.spriteX >= (GameProperties.SCREEN_WIDTH - this.getSpriteW())) {

			} else {
				dogLabel.setIcon(new ImageIcon(getClass().getResource("dogSprite-right.png")));
				dogX += GameProperties.CHARACTER_STEP;
			}
		}

		this.setCoord(dogX, dogY);
		dogLabel.setLocation(this.spriteX, this.spriteY);
	}
	@Override
	public void run() {
		if (hit) {
			this.hit = false;
			lives.lostLife();
			lives.lifeUpdate();
			dogLabel.setIcon(new ImageIcon(getClass().getResource("dogSprite-hit.png")));
			
			try {
				Thread.sleep(400);
				
				
			} catch (Exception e) {

			}
			
			//reset hit flag
			this.revived();
		}
	}

}