import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.*;

public class LifeCount extends Sprite {
    private JLabel lifeLabel;
    private int livesLeft;
    private Skateboard[][] boards;
    private Dog dogger;
    private boolean intersect;
    private JLabel dogLabel;
    private Player user;

    public LifeCount() {
        super(0,0,"lives-full.png",125,25);
        this.livesLeft = 3;
        this.intersect = false;
    }

    public void setLifeLabel(JLabel lifeLabel) {
		this.lifeLabel = lifeLabel;
    }

    public void setLivesLeft(int temp) {
        this.livesLeft = temp;
    }

    public int getLivesLeft() {
        return this.livesLeft;
    }

    public void lostLife() {
        this.livesLeft = this.livesLeft - 1;
    }
    
    public void setPlayer (Player user) {
    	this.user = user;
    }

    //lives are connected to the current score, if you lose all lives the timer
    //bonus isnt counted in the score
    public void lifeUpdate() {
        if (this.livesLeft == 3) {
            lifeLabel.setIcon(new ImageIcon(getClass().getResource("lives-full.png")));
        } else if (this.livesLeft == 2) {
            lifeLabel.setIcon(new ImageIcon(getClass().getResource("lives-2.png")));
        } else if (this.livesLeft == 1) {
            lifeLabel.setIcon(new ImageIcon(getClass().getResource("lives-1.png")));
        } else {
        	user.setTimeLeft(0);
			user.endGame();
            lifeLabel.setIcon(new ImageIcon(getClass().getResource("lives-0.png")));
        }
    }
    
    public void setBoards(Skateboard[][] boards) {

        this.boards = boards;
    }
    
    public void setDog(Dog dogger) {
        this.dogger = dogger;
    }

    public void setDogLabel(JLabel dogLabel) {
		this.dogLabel = dogLabel;
	}

    public boolean getIntersection() {
        return this.intersect;
    }

    //flag to say the dog has fallen off the skateboard
    public void setIntersection() {
        boolean intersect = false;
        Rectangle rDog = dogger.getRectangle();
		Rectangle rBoard;

        for( int i = 0; i < 4; i++) {
			for (int j = 0; j < 3; j++) {
                rBoard = boards[i][j].getRectangle();
				if (rDog.intersects(rBoard)) {
                    intersect = true;
                    dogger.setCoord(boards[i][j].getSpriteX(), boards[i][j].getSpriteY());
                    dogLabel.setLocation(dogger.getSpriteX(), dogger.getSpriteY());
                }
			}
        }
        
        this.intersect = intersect;
    }
    
}