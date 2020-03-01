import java.awt.Color;
import java.awt.Container;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.Random;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//implement thread for overall game counter
//set flag 
public class GamePrep extends JFrame implements ActionListener, KeyListener, Runnable {
	
	private Thread tGame;
	//declare the sprite objects (dog and enemy)
	private Dog dogger;
	private Enemy[][] traffic;
	private Skateboard[][] boards;
	private LifeCount lives;
	private Bone bone;
	private Score score;
	
	//image icons
	private ImageIcon DogImage;
	private ImageIcon[][] EnemyImage;
	private ImageIcon backgroundImage;
	private ImageIcon LifeImage;
	private ImageIcon[][] SkateboardImage;
	private ImageIcon BoneImage;
	private ImageIcon ScoreImage;

	//labels to display image icons
	private JLabel DogLabel;
	private JLabel[][] EnemyLabel;
	private JLabel backgroundLabel;
	private JLabel LifeLabel;
	private JLabel[][] SkateboardLabel;
	private JLabel BoneLabel;
	private JLabel ScoreLabel;
	private JLabel timeLabel;
	
	//button to control the enemy
	private JButton BtnEndGame, BtnAnimation;
	
	//player
	private Player user;
	
	private Container content;
	
	public GamePrep() {
		//initialize the variables

		//player
		user = new Player();
		timeLabel = new JLabel();
		timeLabel.setSize(50, 50);
		timeLabel.setLocation(GameProperties.SCREEN_WIDTH-50, 0);
		timeLabel.setText(String.valueOf(200));
		user.setTimeLabel(timeLabel);
		add(timeLabel);
		

		//score/coins
		score = new Score();
		ScoreLabel = new JLabel();
		ScoreImage = new ImageIcon(getClass().getResource(score.getSpriteName()));
		score.setCoord(0, 30);
		ScoreLabel.setIcon(ScoreImage);
		ScoreLabel.setSize(score.getSpriteW(), score.getSpriteH());
		ScoreLabel.setLocation(score.getSpriteX(), score.getSpriteY());
		score.setScoreLabel(ScoreLabel);
		add(ScoreLabel);
		//bone
		bone = new Bone();
		BoneLabel = new JLabel();
		BoneImage = new ImageIcon(getClass().getResource(bone.getSpriteName()));
		bone.setCoord(450, 0);
		BoneLabel.setIcon(BoneImage);
		BoneLabel.setSize(bone.getSpriteW(), bone.getSpriteH());
		BoneLabel.setLocation(bone.getSpriteX(), bone.getSpriteY());
		bone.setBoneLabel(BoneLabel);
		add(BoneLabel);

		//lifecount
		lives = new LifeCount();
		LifeLabel = new JLabel();
		LifeImage = new ImageIcon(getClass().getResource(lives.getSpriteName()));
		lives.setCoord(0, 0);
		LifeLabel.setIcon(LifeImage);
		LifeLabel.setSize(lives.getSpriteW(), lives.getSpriteH());
		LifeLabel.setLocation(lives.getSpriteX(), lives.getSpriteY());
		lives.setLifeLabel(LifeLabel);
		add(LifeLabel);

		//dog
		dogger = new Dog();
		DogLabel = new JLabel();
		DogImage = new ImageIcon(getClass().getResource(dogger.getSpriteName()));

		BtnEndGame = new JButton();
		BtnAnimation = new JButton();
		
		//traffic
		traffic = new Enemy[5][3];
		EnemyLabel = new JLabel[5][3];
		EnemyImage = new ImageIcon[5][3];
		for( int i = 0; i < 5; i++) {
			for (int j = 0; j < 3; j++ ) {
				traffic[i][j] = new Enemy();
				EnemyLabel[i][j] = new JLabel();
				//randomize images for enemies
				Random ranImg = new Random();
				int chooseImg;
				chooseImg = ranImg.nextInt(900) + 0;
				if (chooseImg < 300) {
					traffic[i][j].setSpriteName("enemy.png");
				} else if (chooseImg < 600 && chooseImg > 300) {
					traffic[i][j].setSpriteName("enemy2.png");
				} else {
					traffic[i][j].setSpriteName("enemy3.png");
				}
				EnemyImage[i][j] = new ImageIcon(getClass().getResource(traffic[i][j].getSpriteName()));
			}
		}

		//skateboards
		boards = new Skateboard[4][3];
		SkateboardLabel = new JLabel[4][3];
		SkateboardImage = new ImageIcon[4][3];
		for( int i = 0; i < 4; i++) {
			for (int j = 0; j < 3; j++ ) {
				boards[i][j] = new Skateboard();
				SkateboardLabel[i][j] = new JLabel();
				SkateboardImage[i][j] = new ImageIcon(getClass().getResource(boards[i][j].getSpriteName()));
			}
		}

		//set dog whereabouts in traffic
		for( int i = 0; i < 5; i++) {
			for (int j = 0; j < 3; j++ ) {
				traffic[i][j].setDog(dogger);
				traffic[i][j].setDogLabel(DogLabel);
				traffic[i][j].setAnimationButton(BtnAnimation);
				//set current lives left
				traffic[i][j].setLifeCount(lives);
			}
		}

		//set dog whereabouts in skateboards
		for( int i = 0; i < 4; i++) {
			for (int j = 0; j < 3; j++ ) {
				boards[i][j].setDog(dogger);
				boards[i][j].setDogLabel(DogLabel);
				boards[i][j].setLifeCount(lives);
			}
		}

		//set up GUI
		content = getContentPane();
		setSize(GameProperties.SCREEN_WIDTH, GameProperties.SCREEN_HEIGHT_CONTENT);
		content.setBackground(Color.DARK_GRAY);
		//no layout to allow graphics to be set w absolute positioning
		setLayout(null);
		
		//set up dog initial pos (upper left hand corner position)
		dogger.setCoord(400,650);
		DogLabel.setIcon(DogImage);
		DogLabel.setSize(dogger.getSpriteW(), dogger.getSpriteH());
		DogLabel.setLocation(dogger.getSpriteX(), dogger.getSpriteY());
		dogger.setDogLabel(DogLabel);
		add(DogLabel);
		
		//set up enemy initial position 
		//set up seeds
		for( int i = 0; i < 5; i++) {
			Random sd = new Random();
			int seed;
			seed = sd.nextInt();
			for (int j =0; j < 3; j++) {
				int x;
				int y;
				Random ran = new Random();
				int randomSpace;
				//random number generated for space between vehicles
				if (j == 0) {
					randomSpace = ran.nextInt(250);
					x = GameProperties.SCREEN_WIDTH + randomSpace;
				}
				else {
					randomSpace = ran.nextInt(250) + (traffic[i][j-1].getSpriteX() + 150);
					x = randomSpace;
				}
				//lane position (each 50 high)
				y = 100 + (i * 50);
				traffic[i][j].setCoord(x, y);
				traffic[i][j].setSeed(seed);
			}
		}

		//set up skateboard initial positions with random generated seeds
		for( int i = 0; i < 4; i++) {
			Random sk = new Random();
			int seed;
			seed = sk.nextInt();
			for (int j =0; j < 3; j++) {
				int x;
				int y;
				Random ran = new Random();
				int randomSpace;
				//random number generated for space between vehicles
				if (j == 0) {
					randomSpace = ran.nextInt(250);
					x = GameProperties.SCREEN_WIDTH + randomSpace;
				}
				else {
					randomSpace = ran.nextInt(200) + (boards[i][j-1].getSpriteX() + 100);
					x = randomSpace;
				}
				//lane position (each 50 high)
				y = 400 + (i * 50);
				boards[i][j].setCoord(x, y);
				boards[i][j].setSeed(seed);
			}
		}
		
		//set traffic to the screen
		for( int i = 0; i < 5; i++) {
			for (int j = 0; j < 3; j++) {
				EnemyLabel[i][j].setIcon(EnemyImage[i][j]);
				EnemyLabel[i][j].setSize(traffic[i][j].getSpriteW(), traffic[i][j].getSpriteH());
				EnemyLabel[i][j].setLocation(traffic[i][j].getSpriteX(), traffic[i][j].getSpriteY());
				traffic[i][j].setMoving(false);
				traffic[i][j].setVisible(true);
				traffic[i][j].setEnemyLabel(EnemyLabel[i][j]);
				add(EnemyLabel[i][j]);
			}
		}

		//set skateboards to the screen
		for( int i = 0; i < 4; i++) {
			for (int j = 0; j < 3; j++) {
				SkateboardLabel[i][j].setIcon(SkateboardImage[i][j]);
				SkateboardLabel[i][j].setSize(boards[i][j].getSpriteW(), boards[i][j].getSpriteH());
				SkateboardLabel[i][j].setLocation(boards[i][j].getSpriteX(), boards[i][j].getSpriteY());
				boards[i][j].setMoving(false);
				boards[i][j].setSkateboardLabel(SkateboardLabel[i][j]);
				add(SkateboardLabel[i][j]);
			}
		}

		//set skateboard whereabouts in lifecount
		lives.setDog(dogger);
		lives.setDogLabel(DogLabel);
		lives.setBoards(boards);
		lives.setPlayer(user);

		//set dog/score in bone
		bone.setDog(dogger);
		bone.setScore(score);

		//set lives in dog
		dogger.setLifeCount(lives);
		
		//set coins in Player
		user.setScore(score);
		
		//set user in Score
		score.setPlayer(user);

			
		//set up start game button
		BtnAnimation.setLocation(GameProperties.SCREEN_WIDTH/2, GameProperties.SCREEN_HEIGHT/2);
		BtnAnimation.setSize(100, 100);
		BtnAnimation.setText("START");
		BtnAnimation.addActionListener(this);
		//remove focus
		BtnAnimation.setFocusable(false);
		add(BtnAnimation);
		
		//set up end game button
		BtnEndGame.setLocation(GameProperties.SCREEN_WIDTH-70, GameProperties.SCREEN_HEIGHT-30);
		BtnEndGame.setSize(70,30);
		BtnEndGame.setText("END");
		BtnEndGame.addActionListener(this);
		BtnEndGame.setFocusable(false);
		add(BtnEndGame);
		
		//add key listener to the screen to move sprite by arrow keys
		//maintain focus of program on content to maintain the keys answerable
		content.addKeyListener(this);
		content.setFocusable(true);
	

		//define background as label
		backgroundLabel = new JLabel();
		backgroundImage = new ImageIcon("bin/GameBackground.png");
		backgroundLabel.setIcon(backgroundImage);
		backgroundLabel.setSize(800, 700);
		backgroundLabel.setLocation(0,0);
		add(backgroundLabel);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	
	public static void main(String [] args) {
		GamePrep theGame = new GamePrep();
		theGame.setVisible(true);
		//the popup to grab player info
		
	    theGame.setPlayerName();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if(e.getSource() == BtnAnimation) {
			//start timer 
			user.setGameRunning(true);
			tGame = new Thread(this, "timer");
			tGame.start();
			//move enemy/boards/start game
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 3; j++) {
					if (traffic[i][j].getMoving()) {
						//if enemy is moving, then stop it
						traffic[i][j].stopEnemy();
						BtnAnimation.setText("Start");
					} else {
						//if enemy isnt moving, then move it
						traffic[i][j].moveEnemy();
						BtnAnimation.setLocation(GameProperties.SCREEN_WIDTH+100, GameProperties.SCREEN_HEIGHT);
					}
				}
			}
			
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 3; j++) {
					if (boards[i][j].getMoving()) {
						//if enemy is moving, then stop it
						boards[i][j].stopSkateboard();
						BtnAnimation.setText("Start");
					} else {
						//if enemy isnt moving, then move it
						boards[i][j].moveSkateboard();
						BtnAnimation.setText("Stop");
					}
				}
			}
			if (bone.getMoving()) {
				bone.stopBone();
			} else {
				bone.moveBone();
			}
		} else if(e.getSource() == BtnEndGame) {
			//button to end game else if btn == end game
			user.setGameRunning(false);
			user.setTimeLeft(0);

			//set new score and save name and score in database, display the results
			user.endGame();
			
			//save to database with player name
			System.exit(0);
		}
		
	} 

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.isActionKey()) {
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		dogger.moveDog(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
	
	public void setPlayerName() {
		//should show highest score with playr name as current no 1 rank
		String playerName = JOptionPane.showInputDialog(user.pullFromDatabase() + "\nPlease enter your player name:");
	    user.setPlayerName(playerName);
	}

	@Override
	public void run() {
		while(user.getGameRunning()) {
			int time = user.getTimeLeft();
			time--;
			user.setTimeLeft(time);
			//counter for every second that passes
			
			//put time in JLabel on screen and pass this variable to object variable
			user.updateTimeLabel(user.getTimeLeft());
			//sleep thread every 1 second
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				
			}
		}
	}
}

