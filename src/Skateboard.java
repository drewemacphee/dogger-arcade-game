import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.util.Random;
import java.util.Timer;

import java.awt.*;

//enemy object extends spritename

public class Skateboard extends Sprite implements Runnable {
	private Boolean moving;
    private Thread t2;
    private JLabel skateboardLabel;
    private Random ran;
	private Dog dogger;
	private JLabel dogLabel;
	private LifeCount lives;
	
	public Skateboard() {
		super(0,0,"skateboard.png",100, 50);
		moving = false;
    }

	public Boolean getMoving() {
		return moving;
	}

	public void setMoving(Boolean moving) {
		this.moving = moving;
	}
	
	public void Display() {
		System.out.println("X,Y: " + spriteX + "," + spriteY + " /  M: " + moving);
	}
    
	public void stopSkateboard() {
		this.moving = false;
	}

	public void moveSkateboard() {
		this.moving = true;
		t2 = new Thread(this, "concrete");
		t2.start();
	}
    
    public void setSkateboardLabel(JLabel skateboardLabel) {
		this.skateboardLabel = skateboardLabel;
	}

	public void setDog(Dog dogger) {
		this.dogger = dogger;
	}

	public void setDogLabel(JLabel dogLabel) {
		this.dogLabel = dogLabel;
	}

	public void setLifeCount(LifeCount lives) {
		this.lives = lives;
	}
	

    @Override
	public void run() {
		while (moving) {

			ran = new Random(this.getSeed());
			int randomSpeed;
			int randomStep;
			//set seed for each array of skateboards to be the same, but different from other rows
			ran.setSeed(this.getSeed());
			randomSpeed = ran.nextInt(500) + 200;
			randomStep = ran.nextInt(10) + 2;
			//moving code
			int skateY = this.spriteY;
			int skateX = this.spriteX;

			skateX -= GameProperties.ENEMY_STEP * randomStep;
			if (skateX < 0 - this.getSpriteW()) {
				skateX = GameProperties.SCREEN_WIDTH + this.spriteW;
			}
			this.setCoord(skateX, skateY);

			skateboardLabel.setLocation(this.spriteX, this.spriteY);
            //detect collision
            //if sprite is on concrete block, check if intersecting with board, else, lifelost
            if ((dogger.getSpriteX() >= 0 && dogger.getSpriteX() <= (GameProperties.SCREEN_WIDTH - dogger.getSpriteW())) && (dogger.getSpriteY() >= 400 && dogger.getSpriteY() < 600)) {
			   lives.setIntersection();
			   if (!(lives.getIntersection()) || ( dogger.getSpriteX() < 0)) {
					//get current lives and update 1 less if collision happens
					
					dogger.setCoord(400, 650);
					dogger.accident();

					try {
						//delay hit sprite
						Thread.sleep(250);
					} catch (Exception e) {
	
					}
					
					dogger.revived();
			   } 
            }

			//pause
			try {
				//time delay between cycles
				Thread.sleep(randomSpeed);
			} catch (Exception e) {

			}
        }
    }
    

}