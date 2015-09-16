package edu.neu.madcourse.rajatmalhotra.wordgame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import edu.neu.madcourse.rajatmalhotra.R;

public class WordGameMain extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_word_game_main);
		Button button = (Button)findViewById(R.id.resume_button);
		SharedPreferences prefs = getSharedPreferences("GAME_STATE",MODE_PRIVATE);
		Boolean pausePressed = prefs.getBoolean("paused", false);
		
		if (pausePressed == true) // check if the user press paused
		{
			button.setVisibility(0);
		}
		else
		{
			button.setVisibility(8);
		}
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Button button = (Button)findViewById(R.id.resume_button);
		
		SharedPreferences prefs = getSharedPreferences("GAME_STATE",MODE_PRIVATE);
		Boolean pausePressed = prefs.getBoolean("paused", false);
		
		if (pausePressed == true) // check if the user press paused
		{
			button.setVisibility(0);
		}
		else
		{
			button.setVisibility(8);
		}
		
	}
	@Override
	   public boolean onCreateOptionsMenu(Menu menu) {
	      super.onCreateOptionsMenu(menu);
	      MenuInflater inflater = getMenuInflater();
	      inflater.inflate(R.menu.wordgame_menu, menu);
	      return true;
	   }
	 @Override
	   public boolean onOptionsItemSelected(MenuItem item) {
	      switch (item.getItemId()) {
	      case R.id.settings:
	         startActivity(new Intent(this, PrefsWordGame.class));
	         return true;
	      // More items go here (if any) ...
	      }
	      return false;
	   }

	public void newGameClick(View view) {
		SharedPreferences prefs = getSharedPreferences("GAME_STATE",MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean("paused", false);
		editor.commit();
		openNewGameDialog();
	}
	public void resumeGameClick(View view) {
		SharedPreferences prefs = getSharedPreferences("GAME_STATE",MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean("paused", false);
		editor.commit();
		Intent intent = new Intent(this, WordGame.class);
		intent.putExtra("resumeGameClick", true);
		startActivity(intent);
	}
	
	public void instructionsClick(View view) {
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);
		
		alertDialogBuilder.setTitle("Instructions");
		alertDialogBuilder.setMessage(R.string.instructions_text);
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		try {
		      alertDialog.show();
		 } catch(Exception e){
				Log.e("e", "exception", e);
		}
	}

	public void acknowledgementsClick(View view) {
		Intent intent = new Intent(this, Acknowledgements.class);
		startActivity(intent);
	}
	
	public void quitClick(View view) {
		finish();
	}
	 private void openNewGameDialog() {
	     
		 try {
			 new AlertDialog.Builder(this)
	           .setTitle(R.string.new_game_title)
	           .setItems(R.array.difficulty,
	            new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialoginterface,
	                     int i) {
	                  startGame(i);
	               }
	            })
	           .show();
	   } catch(Exception e){
			Log.e("e", "exception", e);
		   // WindowManager$BadTokenException will be caught and the app would not display 
		   // the 'Force Close' message
		 }
	 }
	 private void startGame(int i) {
	      Intent intent = new Intent(this, WordGame.class);
	  	  intent.putExtra("newGameClick", true);
	      intent.putExtra(WordGame.KEY_DIFFICULTY, i);
	      startActivity(intent);
	   }
}
