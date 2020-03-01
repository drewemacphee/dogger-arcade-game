import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Player {
	
	private String playerName;
	private int playerScore;
	private boolean gameRunning;
	private JLabel timeLabel;
	private int timeLeft;
	
	//get coins
	private Score score;
	
	public Player() {
		playerName = "";
		playerScore = 0;
		gameRunning = false;
		timeLeft = 200;
	}
	
	//set up count for time remaining
	public void setTimeLeft(int timeLeft) {
		this.timeLeft = timeLeft;
	}
	
	public int getTimeLeft() {
		return this.timeLeft;
	}
	
	public void setTimeLabel(JLabel timeLabel) {
		this.timeLabel = timeLabel;
	}
	
	public void updateTimeLabel(int timeLeft) {
		this.timeLabel.setText(String.valueOf(timeLeft));
	}
	
	public void setGameRunning(boolean gameRunning) {
		this.gameRunning = gameRunning;
	}
	
	public boolean getGameRunning() {
		return this.gameRunning;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	
	public String getPlayerName() {
		return this.playerName;
	}
	//this is the final computed score ( coins * time remaining)
	public void setPlayerScore () {
		// build calculation of time remaining * coins = player score and make it a void set function
		//multiply the coins by the timer so if theres 0 coins u get no points
		this.playerScore = this.getPlayerScore() + (score.getCoins() * this.getTimeLeft());
	}
	
	public int getPlayerScore() {
		return this.playerScore;
	}
	
	// setters for coins and time objects
	public void setScore(Score score) {
        this.score = score;
    }
	
	//DATABASE STUFF
	public String pullFromDatabase() {
		Connection conn = null;
        Statement stmt = null;
        String highscore = "";

        try {
            //load the database driver
            Class.forName("org.sqlite.JDBC");
            String dbURL = "jdbc:sqlite:playerScores.db";
            conn = DriverManager.getConnection(dbURL);
            if (conn != null) {
                System.out.println("Connection established");
                //dont allow the statements to be commited upon calling function
                conn.setAutoCommit(false);
                //get info about the database
                //driver info
                DatabaseMetaData dm = (DatabaseMetaData)conn.getMetaData();
                
                //statement
                stmt =  conn.createStatement();
                String sql = "";
                ResultSet rs = null;
                
                
                //selecting max values
                sql = "SELECT * FROM playerInfo WHERE score=(SELECT max(score) FROM playerinfo)";
                rs = stmt.executeQuery(sql);
                highscore = "Current HISCORE - " + rs.getString("name") + ": " + rs.getInt("score");
             
                
                conn.close();
            } else {
                System.out.println("Cannot establish connection");
            }

        } catch (ClassNotFoundException e) {
            //cant connect
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //cleanup code
        }
        return highscore;
	}
	
	
	public void storeInDatabase() {
		Connection conn = null;
        Statement stmt = null;
        

        try {
            //load the database driver
            Class.forName("org.sqlite.JDBC");
            String dbURL = "jdbc:sqlite:playerScores.db";
            conn = DriverManager.getConnection(dbURL);
            if (conn != null) {
                System.out.println("Connection established");
                //dont allow the statements to be committed upon calling function
                conn.setAutoCommit(false);
                //get info about the database
                //driver info
                DatabaseMetaData dm = (DatabaseMetaData)conn.getMetaData();
                
                //statement
                stmt =  conn.createStatement();
                String sql = "";
                
                //CREATE TABLE IF NOT EXIST
                sql = "CREATE TABLE IF NOT EXISTS playerInfo (id INTEGER PRIMARY KEY, name TEXT NOT NULL, score INT NOT NULL)";
                stmt.execute(sql);
                conn.commit();
            
                //INSERT DATA player name and score

                sql = "INSERT INTO playerInfo (name, score) VALUES ('" + this.playerName + "', '" + this.playerScore + "' )";
                stmt.execute(sql);
                conn.commit();
                
                conn.close();
            } else {
                System.out.println("Cannot establish connection");
            }

        } catch (ClassNotFoundException e) {
            //cant connect
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //cleanup code
        }
	}
	
	
	public void endGame() {
		this.setPlayerScore();
		this.storeInDatabase();
		
		//display current score
		JOptionPane.showMessageDialog(null, "Thanks for playing, "+ this.getPlayerName() + "!\nYour score was " + this.getPlayerScore(), "You tried your best" , JOptionPane.INFORMATION_MESSAGE);
		System.exit(0);
	}
}
