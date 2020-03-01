import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.util.Random;
import java.util.Timer;
import java.awt.*;

//enemy object extends spritename

public class Enemy extends Sprite implements Runnable {
	private Boolean visible;
	private Boolean moving;
	private Thread t;
	private JLabel enemyLabel;
	private Random ran;
	private Dog dogger;
	private JLabel dogLabel;
	private JButton AnimationButton;
	private LifeCount lives;
	
	public Enemy() {
		super(0,0,"enemy.png",100, 50);
		visible = true;
		moving = false;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public Boolean getMoving() {
		return moving;
	}

	public void setMoving(Boolean moving) {
		this.moving = moving;
	}
	
	public void Display() {
		System.out.println("X,Y: " + spriteX + "," + spriteY + " / V: " + visible + " / M: " + moving);
	}
	
	public void show() {
		this.visible = true;
	}
	
	public void hide() {
		this.visible = false;
	}

	public void stopEnemy() {
		this.moving = false;
	}

	public void moveEnemy() {
		this.moving = true;
		t = new Thread(this, "row1");
		t.start();

	}

	public void setEnemyLabel(JLabel enemyLabel) {
		this.enemyLabel = enemyLabel;
	}

	public void setDog(Dog dogger) {
		this.dogger = dogger;
	}

	public void setDogLabel(JLabel dogLabel) {
		this.dogLabel = dogLabel;
	}

	public void setAnimationButton(JButton AnimationButton) {
		this.AnimationButton = AnimationButton;
	}

	public void setLifeCount(LifeCount lives) {
		this.lives = lives;
	}

	@Override
	public void run() {
		while (moving) {
			//seed has been made for each row so all cars in a row move at the same rate, 
			//but different rows have different rates
			
			ran = new Random(this.getSeed());
			int randomSpeed;
			int randomStep;
			//set seed for each pair of cars to be the same
			ran.setSeed(this.getSeed());
			randomSpeed = ran.nextInt(350) + 125;
			randomStep = ran.nextInt(12) + 2;
			//moving code
			int enemyY = this.spriteY;
			int enemyX = this.spriteX;

			enemyX -= GameProperties.ENEMY_STEP * randomStep;
			if (enemyX < 0 - this.getSpriteW()) {
				enemyX = GameProperties.SCREEN_WIDTH + this.spriteW;
			}
			this.setCoord(enemyX, enemyY);

			enemyLabel.setLocation(this.spriteX, this.spriteY);
			//detect collision
			Rectangle rDog = dogger.getRectangle();
			Rectangle rTraffic = this.r;
			if (rDog.intersects(rTraffic)) {
				//get current lives and update 1 less if collision happens
				lives.lostLife();
				lives.lifeUpdate();
				//hit image set to label
				dogLabel.setIcon(new ImageIcon(getClass().getResource("dogSprite-hit.png")));

				try {
					//delay hit sprite
					Thread.sleep(400);
				} catch (Exception e) {

				}

				dogger.setCoord(400,650);
				dogLabel.setIcon(new ImageIcon(getClass().getResource("dogSprite.png")));
				dogLabel.setLocation(dogger.getSpriteX(), dogger.getSpriteY());
				AnimationButton.setText("Run");
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


