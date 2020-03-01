import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.*;

public class Score extends Sprite {
    private int coins;
    private JLabel scoreLabel;
    private Player user;

    public Score() {
        super(0,0,"score-0.png",125,25);
        this.coins = 0;
    }
    
    public void setPlayer (Player user) {
    	this.user = user;
    }
    
    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getCoins() {
        return this.coins;
    }

    public void setScoreLabel(JLabel scoreLabel) {
		this.scoreLabel = scoreLabel;
    }

    public void coinAchieved() {
        this.coins = this.coins + 1;
    }

    //this will also be responsible for updating the current player score as it is connected
    //to generating the score
    public void coinUpdate() {
        if (this.coins == 3) {
        	user.endGame();
            scoreLabel.setIcon(new ImageIcon(getClass().getResource("score-3.png")));
        } else if (this.coins == 2) {
        	user.setPlayerScore();
        	user.setTimeLeft(200);
			user.updateTimeLabel(user.getTimeLeft());
            scoreLabel.setIcon(new ImageIcon(getClass().getResource("score-2.png")));
        } else if (this.coins == 1) {
        	user.setPlayerScore();
        	user.setTimeLeft(200);
			user.updateTimeLabel(user.getTimeLeft());
            scoreLabel.setIcon(new ImageIcon(getClass().getResource("score-1.png")));
        } else {
            scoreLabel.setIcon(new ImageIcon(getClass().getResource("score-0.png")));
        }
    }
    
		
}