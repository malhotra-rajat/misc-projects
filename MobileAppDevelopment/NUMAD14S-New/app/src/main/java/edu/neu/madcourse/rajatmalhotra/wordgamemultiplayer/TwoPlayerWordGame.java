package edu.neu.madcourse.rajatmalhotra.wordgamemultiplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import edu.neu.madcourse.rajatmalhotra.R;

public class TwoPlayerWordGame extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_two_player_word_game);
	}

	public void rtClick(View view) {
		Intent intent = new Intent(this, RealTimePlayLogin.class);
		startActivity(intent);
	}
	
	public void tbClick(View view) {
		
		//new ClearAllKeysTask().execute("");
		Intent intent = new Intent(this, TurnBasedPlayMain.class);
		startActivity(intent);
	}
	
	public void shakeClick(View view) {
		
		//new ClearAllKeysTask().execute("");
		Intent intent = new Intent(this, SensorDetect.class);
		startActivity(intent);
	}
	
	public void ackClick(View view) {
		
		//new ClearAllKeysTask().execute("");
		Intent intent = new Intent(this, Acknowledgements.class);
		startActivity(intent);
	}
	
	public void leaderboardClick(View view) {
		
		//new ClearAllKeysTask().execute("");
		Intent intent = new Intent(this, Leaderboard.class);
		startActivity(intent);
	}
	

}
