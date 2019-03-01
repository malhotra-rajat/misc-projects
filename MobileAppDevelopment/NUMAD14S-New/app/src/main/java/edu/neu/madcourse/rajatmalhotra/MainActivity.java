package edu.neu.madcourse.rajatmalhotra;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import edu.neu.madcourse.rajatmalhotra.dictionary.Dictionary;
import edu.neu.madcourse.rajatmalhotra.finalproject.ProjectDescriptionActivity;
import edu.neu.madcourse.rajatmalhotra.sudoku.Sudoku;
import edu.neu.madcourse.rajatmalhotra.trickiestpart.MultiTurnExerciseDialogActivity;
import edu.neu.madcourse.rajatmalhotra.wordgame.WordGameMain;
import edu.neu.madcourse.rajatmalhotra.wordgamecommunication.Communication;
import edu.neu.madcourse.rajatmalhotra.wordgamemultiplayer.TwoPlayerWordGame;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		setTitle(getResources().getText(R.string.author_name));
	}

	// methods for all the button click events
	public void aboutClick(View view) {
		Intent i = new Intent(this, AboutMe.class);
		startActivity(i);
	}

	public void sudokuClick(View view) {
		Intent intent = new Intent(this, Sudoku.class);
		startActivity(intent);
	}

	public void dictionaryClick(View view) {
		Intent intent = new Intent(this, Dictionary.class);
		startActivity(intent);
	}

	public void wordGameClick(View view) {
		Intent intent = new Intent(this, WordGameMain.class);
		startActivity(intent);
	}

	public void communicationClick(View view) {
		Intent intent = new Intent(this, Communication.class);
		startActivity(intent);
	}

	public void twoPlayerDabbleClick(View view) {
		Intent intent = new Intent(this, TwoPlayerWordGame.class);
		startActivity(intent);
	}

	public void trickiestPartClick(View view) {
		Intent intent = new Intent(this, MultiTurnExerciseDialogActivity.class);
		// Intent intent = new Intent(this, SpeechActivationServicePlay.class);
		startActivity(intent);
	}

	public void genErrorClick(View view) {
		throw new RuntimeException();
	}

	public void finalProjectClick(View view) {
		Intent finalProjectIntent = new Intent(this,
				ProjectDescriptionActivity.class);
		startActivity(finalProjectIntent);
	}

	public void quitClick(View view) {
		finish();
	}
	
}
