package edu.neu.madcourse.rajatmalhotra.wordgame;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import edu.neu.madcourse.rajatmalhotra.R;

public class WordGame extends Activity{

	private WordGameView wordGameView;

	public static final String KEY_DIFFICULTY =
			"edu.neu.madcourse.rajatmalhotra.wordgame.difficulty";
	public static final int DIFFICULTY_EASY = 0;
	public static final int DIFFICULTY_MEDIUM = 1;
	public static final int DIFFICULTY_HARD = 2;
	private char puzzle[][];

	BufferedReader br;
	ArrayList<Coordinates> indexesSelected = new ArrayList<Coordinates>();
	HashSet<String> hash_word_list = new HashSet<String>();
	HashSet<String> hash_word_list_for_hints = new HashSet<String>();
	int finalScore;

	private  final char easyPuzzle[][] =
		{{'A' , 'N' , 'D' , 'H' , 'E' , 'N', 'A'},
			{'T' , 'I' , 'G' , 'E' , 'R' , 'S', 'H'},
			{'C' , 'H' , 'I' , 'C' , 'K' , 'E', 'N'},
			{'P' , 'I' , 'Z' , 'Z' , 'A' , 'Z', 'R'},
			{'B' , 'U' , 'R' , 'G' , 'E' , 'R', 'S'}};

	private  final char mediumPuzzle[][] =
		{{'I' , 'A' , 'T' , 'Y' , 'I' , 'L', 'A'},
			{'A' , 'B' , 'N' , 'P' , 'T' , 'I', 'S'},
			{'O' , 'E' , 'U' , 'A' , 'S' , 'F', 'A'},
			{'N' , 'H' , 'E' , 'P' , 'M' , 'E', 'M'},
			{'N' , 'A' , 'E' , 'O' , 'Y' , 'Z', 'I'}};

	private  final char hardPuzzle[][] =
		{{'L' , 'Y' , 'I' , 'N' , 'L' , 'S', 'M'},
			{'A' , 'I' , 'S' , 'G' , 'O' , 'R', 'E'},
			{'T' , 'B' , 'O' , 'S' , 'D' , 'R', 'V'},
			{'E' , 'E' , 'L' , 'L' , 'P' , 'A', 'O'},
			{'L' , 'E' , 'N' , 'E' , 'E' , 'L', 'L'}};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		int diff = getIntent().getIntExtra(KEY_DIFFICULTY, DIFFICULTY_EASY);
		PuzzleObject po;
		super.onCreate(savedInstanceState);
		SharedPreferences prefs = getSharedPreferences("GAME_STATE",MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		Bundle extras = getIntent().getExtras();
		Boolean resumeGameClick = extras.getBoolean("resumeGameClick");
		Boolean newGameClick = extras.getBoolean("newGameClick");



		if (newGameClick == true)
		{
			editor.putInt("timer", 45000);
			editor.putInt("score", 0);
			puzzle = getPuzzle(diff);
		}
		else if (resumeGameClick == true)
		{
			try {
				FileInputStream fi = openFileInput("wordPuzzle.ser");
				ObjectInputStream ois = new ObjectInputStream(fi);
				po = (PuzzleObject)ois.readObject();
				puzzle = po.getPuzzle();
				fi.close();

			}  catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				Log.e("e", "exception", e);
			} 
			catch (StreamCorruptedException e) {
				// TODO Auto-generated catch block
				Log.e("e", "exception", e);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e("e", "exception", e);
			}

			if ((prefs.getInt("timer", 45000) != 45000)) {
				editor.putInt("timer", (prefs.getInt("timer", 47000)));
			}
			if ((prefs.getInt("score", 0) != 0)) {
				editor.putInt("score", (prefs.getInt("score", 0)));
			}
		}
		else
		{}

		editor.commit();
		wordGameView = new WordGameView(this);
		setContentView(wordGameView);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MusicWordGame.play(this, R.raw.word_game_background);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MusicWordGame.stop(this);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	/** Given a difficulty level, come up with a new puzzle */
	private char[][] getPuzzle(int diff) {
		char[][] puz;
		switch (diff) {

		case DIFFICULTY_HARD:
			puz = hardPuzzle;
			break;
		case DIFFICULTY_MEDIUM:
			puz = mediumPuzzle;
			break;
		case DIFFICULTY_EASY:
		default:
			puz = easyPuzzle;
			break;
		}

		return puz;
	}

	private char getTile(int x, int y) {
		return puzzle[x][y];
	}

	/** Return a string for the tile at the given coordinates */
	protected String getTileString(int x, int y) {
		return Character.toString(getTile(x, y));
	}

	protected void pausePressedGoToMain()
	{
		PuzzleObject po = new PuzzleObject();
		po.setPuzzle(puzzle);
		try {
			OutputStream fo = openFileOutput("wordPuzzle.ser", Context.MODE_PRIVATE);
			OutputStream buffer = new BufferedOutputStream(fo);
			ObjectOutput output = new ObjectOutputStream(buffer);
			output.writeObject(po);
			output.close();
			fo.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e("e", "exception", e);
		}
		SharedPreferences prefs = getSharedPreferences("GAME_STATE", MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean("paused", true);
		editor.commit();
		finish();
	}

	@SuppressLint("DefaultLocale")
	protected boolean isWordFound (String word_entered)
	{
		boolean found = false;
		word_entered = word_entered.toLowerCase();

		AssetManager am = getAssets();

		if (word_entered.length() >= 2) //copy the file contents into a HashSet when word length reaches 2
		{
			try
			{
				br = new BufferedReader(new InputStreamReader(am.open("dict_files/" + 
						word_entered.charAt(0) + word_entered.charAt(1) + ".txt")));
				hash_word_list.clear();
				String word;
				while ((word = br.readLine()) != null)
				{
					hash_word_list.add(word);
				}

			}
			catch (FileNotFoundException fne)
			{
				Log.e("Exception", "e", fne);
			} 
			catch (IOException ioe)
			{
				Log.e("Exception", "e", ioe);
			} 
		}
		if (word_entered.length()>=3 
				&& hash_word_list.contains(word_entered)) //search if the word entered is in the HashSet or not
		{
			found = true;
			try {
				br.close();
			} catch (IOException ioe) {
				Log.e("Exception", "e", ioe);
			}
		}
		return found;
	}

	protected void changeLetters()
	{
		int xIndex;
		int yIndex;

		for (Coordinates c : indexesSelected)
		{
			xIndex = c.getX();
			yIndex = c.getY();
			Random r = new Random();
			char ch = (char)(r.nextInt(26) + 'A');
			puzzle[xIndex][yIndex] = ch; 
		}
	}

	protected void timeUp()
	{
		MusicWordGame.stop(this);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);

		alertDialogBuilder.setTitle("Time Up!");
		alertDialogBuilder.setMessage("Your final score is: " + finalScore);

		alertDialogBuilder.setPositiveButton("Go to Main Menu", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		try
		{
			alertDialog.show();
		}
		catch(Exception e){
			Log.e("e", "exception", e);
		}
	}
}


