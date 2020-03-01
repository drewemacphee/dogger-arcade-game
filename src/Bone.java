import java.awt.*;

import javax.swing.JLabel;
import java.util.Random;

public class Bone extends Sprite implements Runnable {
    private Boolean moving;
    private JLabel boneLabel;
    private Thread t;
    private Dog dogger;
    private Score score;
    private Random ran;

    public Bone() {
        super(0,0,"bone.png",50,50);
        moving = false;
    }

    public void setBoneLabel(JLabel boneLabel) {
		this.boneLabel = boneLabel;
    }

    //extend dog to get coords
    public void setDog(Dog dogger) {
		this.dogger = dogger;
	}

    public Boolean getMoving() {
		return moving;
	}

	public void setMoving(Boolean moving) {
		this.moving = moving;
	}

    
    //extend score to update score
    public void setScore(Score score) {
		this.score = score;
	}

    //check intersection with thread?
    public void stopBone() {
		this.moving = false;
	}

	public void moveBone() {
		this.moving = true;
		t = new Thread(this, "grass");
		t.start();
    }
    
    @Override
	public void run() {
		while (moving) {
            //check if bone and dog have intersected
            ran = new Random();
            int boneLocation = ran.nextInt(200) + 500;
            Rectangle rBone = this.getRectangle();
            Rectangle rDog = dogger.getRectangle();
            if(rDog.intersects(rBone)) {
                //reset game and add score and change label
                score.coinAchieved();
                score.coinUpdate();
                dogger.revived();
                this.setCoord(boneLocation, 0);
                boneLabel.setLocation(this.getSpriteX(), this.getSpriteY());
            }

            //pause
			try {
				//time delay between cycles
				Thread.sleep(200);
			} catch (Exception e) {

			}
        }
    }

}