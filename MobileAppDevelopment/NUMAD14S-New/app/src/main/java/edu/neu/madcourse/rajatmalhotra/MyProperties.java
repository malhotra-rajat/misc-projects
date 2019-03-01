package edu.neu.madcourse.rajatmalhotra;

public class MyProperties {

	private static MyProperties mInstance = null;

	//Synchronous Play
	Boolean hintModeEnabled = false;
	String loggedInUser;
	String loggedInUserKey;
	String invitedUser;
	String invitedBy;
	String gameId;
	Boolean generatedGameId;
	int myScore;
	int opponentScore;
	Boolean haveUpdatedBoard;
	Boolean invited;


	//Turn-based Play
	String loggedInUserTB;
	Boolean hintModeEnabledTB = false;
	String invitedUserTB;
	String invitedByTB;
	String gameIdTB;
	Boolean generatedGameIdTB;
	int myScoreTB;
	int opponentScoreTB;
	Boolean haveUpdatedBoardTB;
	Boolean invitedTB;
	int movesLeft;
	Boolean gameOver;

	String puzzle = "ANDHENATIGERSHCHICKENPIZZAZRBURGERS";


	int id=5;

	/*boolean isListening;



	public boolean isListening() {
		return isListening;
	}
	public void setListening(boolean isListening) {
		this.isListening = isListening;
	}*/
	
	protected MyProperties()
	{

	}
	public static synchronized MyProperties getInstance()
	{
		if(null == mInstance)
		{
			mInstance = new MyProperties();
		}
		return mInstance;
	}

	public Boolean getHintModeEnabled() 
	{
		return hintModeEnabled;
	}

	public void setHintModeEnabled(Boolean hintModeEnabled) 
	{
		this.hintModeEnabled = hintModeEnabled;
	}

	public String getLoggedInUser() 
	{
		return loggedInUser;
	}

	public void setLoggedInUser(String loggedInUser) 
	{
		this.loggedInUser = loggedInUser;
	}

	public String getLoggedInUserKey() 
	{
		return loggedInUserKey;
	}

	public void setLoggedInUserKey(String loggedInUserKey) 
	{
		this.loggedInUserKey = loggedInUserKey;
	}

	public String getInvitedUser() {
		return invitedUser;
	}
	public void setInvitedUser(String invitedUser) {
		this.invitedUser = invitedUser;
	}

	public String getInvitedBy() {
		return invitedBy;
	}
	public void setInvitedBy(String invitedBy) {
		this.invitedBy = invitedBy;
	}

	public String getGameId() {
		return gameId;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public Boolean getGeneratedGameId() {
		return generatedGameId;
	}
	public void setGeneratedGameId(Boolean generatedGameId) {
		this.generatedGameId = generatedGameId;
	}

	public int getMyScore() {
		return myScore;
	}
	public void setMyScore(int myScore) {
		this.myScore = myScore;
	}

	public int getOpponentScore() {
		return opponentScore;
	}
	public void setOpponentScore(int opponentScore) {
		this.opponentScore = opponentScore;
	}

	public Boolean getHaveUpdatedBoard() {
		return haveUpdatedBoard;
	}

	public void setHaveUpdatedBoard(Boolean haveUpdatedBoard) {
		this.haveUpdatedBoard = haveUpdatedBoard;
	}

	public Boolean getInvited() {
		return invited;
	}

	public void setInvited(Boolean invited) {
		this.invited = invited;
	}

	public String getLoggedInUserTB() {
		return loggedInUserTB;
	}

	public void setLoggedInUserTB(String loggedInUserTB) {
		this.loggedInUserTB = loggedInUserTB;
	}
	public Boolean getHintModeEnabledTB() {
		return hintModeEnabledTB;
	}
	public void setHintModeEnabledTB(Boolean hintModeEnabledTB) {
		this.hintModeEnabledTB = hintModeEnabledTB;
	}
	public String getInvitedUserTB() {
		return invitedUserTB;
	}
	public void setInvitedUserTB(String invitedUserTB) {
		this.invitedUserTB = invitedUserTB;
	}
	public String getInvitedByTB() {
		return invitedByTB;
	}
	public void setInvitedByTB(String invitedByTB) {
		this.invitedByTB = invitedByTB;
	}
	public String getGameIdTB() {
		return gameIdTB;
	}
	public void setGameIdTB(String gameIdTB) {
		this.gameIdTB = gameIdTB;
	}
	public Boolean getGeneratedGameIdTB() {
		return generatedGameIdTB;
	}
	public void setGeneratedGameIdTB(Boolean generatedGameIdTB) {
		this.generatedGameIdTB = generatedGameIdTB;
	}
	public int getMyScoreTB() {
		return myScoreTB;
	}
	public void setMyScoreTB(int myScoreTB) {
		this.myScoreTB = myScoreTB;
	}
	public int getOpponentScoreTB() {
		return opponentScoreTB;
	}
	public void setOpponentScoreTB(int opponentScoreTB) {
		this.opponentScoreTB = opponentScoreTB;
	}
	public Boolean getHaveUpdatedBoardTB() {
		return haveUpdatedBoardTB;
	}
	public void setHaveUpdatedBoardTB(Boolean haveUpdatedBoardTB) {
		this.haveUpdatedBoardTB = haveUpdatedBoardTB;
	}
	public Boolean getInvitedTB() {
		return invitedTB;
	}
	public void setInvitedTB(Boolean invitedTB) {
		this.invitedTB = invitedTB;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getMovesLeft() {
		return movesLeft;
	}
	public void setMovesLeft(int movesLeft) {
		this.movesLeft = movesLeft;
	}
	public String getPuzzle() {
		return puzzle;
	}
	public void setPuzzle(String puzzle) {
		this.puzzle = puzzle;
	}


	String opponentTB;



	public String getOpponentTB() {
		return opponentTB;
	}
	public void setOpponentTB(String opponentTB) {
		this.opponentTB = opponentTB;
	};

	public Boolean getGameOver() {
		return gameOver;
	}
	public void setGameOver(Boolean gameOver) {
		this.gameOver = gameOver;
	}


}