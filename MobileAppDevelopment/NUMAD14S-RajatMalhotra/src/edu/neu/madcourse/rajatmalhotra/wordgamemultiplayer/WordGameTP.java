package edu.neu.madcourse.rajatmalhotra.wordgamemultiplayer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import edu.neu.madcourse.rajatmalhotra.MyProperties;
import edu.neu.madcourse.rajatmalhotra.R;
import edu.neu.mhealth.api.KeyValueAPI;

public class WordGameTP extends Activity{

	private WordGameViewTP wordGameView;


	private char puzzle[][];
	String puzzleString;
	BufferedReader br;
	ArrayList<CoordinatesTP> indexesSelected = new ArrayList<CoordinatesTP>();
	HashSet<String> hash_word_list = new HashSet<String>();
	HashSet<String> hash_word_list_for_hints = new HashSet<String>();
	int finalScore;
	boolean updateBoardTaskExecuting = false;
	boolean exitSeekTaskCheckExecuting = false;

	/*private  final char easyPuzzle[][] =
			{{'A' , 'N' , 'D' , 'H' , 'E' , 'N', 'A'},
			 {'T' , 'I' , 'G' , 'E' , 'R' , 'S', 'H'},
			 {'C' , 'H' , 'I' , 'C' , 'K' , 'E', 'N'},
			 {'P' , 'I' , 'Z' , 'Z' , 'A' , 'Z', 'R'},
			 {'B' , 'U' , 'R' , 'G' , 'E' , 'R', 'S'}};*/

	private final String easyPuzzleString = "ANDHENATIGERSHCHICKENPIZZAZRBURGERS";


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		new ClearAcceptedSeekAndPutPuzzleValueTask().execute("");

		try
		{
			puzzle = StringToPuzzleArray(easyPuzzleString);
		}
		catch (Exception e)
		{
			Log.d ("Exception: ", e.toString());
		}

		puzzleString = easyPuzzleString;
		if (MyProperties.getInstance().getGeneratedGameId() == false)
		{
			getGameId();
		}
		MyProperties.getInstance().setMyScore(0);
		MyProperties.getInstance().setOpponentScore(0);
		MyProperties.getInstance().setHaveUpdatedBoard(false);
		//useCheckExitHandler();
		//uploadPuzzleAndScoreForSync();
		usePuzzleSyncHandler();

		wordGameView = new WordGameViewTP(this);
		setContentView(wordGameView);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		new ClearAcceptedSeekAndPutPuzzleValueTask().execute("");


		try
		{
			puzzle = StringToPuzzleArray(easyPuzzleString);
		}
		catch (Exception e)
		{
			Log.d ("Exception: ", e.toString());
		}

		puzzleString = easyPuzzleString;
		if (MyProperties.getInstance().getGeneratedGameId() == false)
		{
			getGameId();
		}
		MyProperties.getInstance().setMyScore(0);
		MyProperties.getInstance().setOpponentScore(0);
		MyProperties.getInstance().setHaveUpdatedBoard(false);
		//uploadPuzzleAndScoreForSync();
		usePuzzleSyncHandler();

		//useCheckExitHandler();
		MusicWordGameTP.play(this, R.raw.word_game_background);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		clearPuzzleAndScoreAndGameId();
		//checkExitHandler.removeCallbacks(checkExitRunnable);
		puzzleSyncHandler.removeCallbacks(puzzleSyncRunnable);
		MusicWordGameTP.stop(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		//exitPressed();
		//checkExitHandler.removeCallbacks(checkExitRunnable);
		clearPuzzleAndScoreAndGameId();
		puzzleSyncHandler.removeCallbacks(puzzleSyncRunnable);
		//timerCheckAcceptedSeek.cancel();
		super.onDestroy();
	}

	protected void clearPuzzleAndScoreAndGameId()
	{
		new AsyncTask<String, Integer, Integer>() {

			@Override
			protected void onProgressUpdate(Integer... progress) { //Post interim updates to UI thread; access UI
				// [... Update progress bar, Notification, or other UI element ...]

			}

			@Override
			protected void onPostExecute(Integer result) { //Run when doInBackground completed; access UI
				// [... Report results via UI update, Dialog, or notification ...]

				if (result == 1)
				{
					Toast.makeText(getApplicationContext(), "Server not available!", Toast.LENGTH_SHORT).show();
				}

			}


			@Override
			protected Integer doInBackground(String... parameter) { //Background thread. Do not interact with UI


				if (KeyValueAPI.isServerAvailable())
				{
					KeyValueAPI.clearKey("RajatM", "thegreatone76", MyProperties.getInstance().getGameId()+
							MyProperties.getInstance().getLoggedInUser()+"puzzle"+"haveUpdatedBoard");

					KeyValueAPI.clearKey("RajatM", "thegreatone76", MyProperties.getInstance().getGameId()+
							MyProperties.getInstance().getLoggedInUser()+"puzzle");
					KeyValueAPI.clearKey("RajatM", "thegreatone76", MyProperties.getInstance().getGameId()+ 
							MyProperties.getInstance().getLoggedInUser()+"score");

					KeyValueAPI.clearKey("RajatM", "thegreatone76", MyProperties.getInstance().getGameId());

					return 0;
				}
				else
				{
					return 1;
				}
			}}.execute("");
	}

	@Override
	public void onBackPressed() {
		exitPressed();
		super.onBackPressed();
	}

	private class CheckPlayerExitedTask extends AsyncTask<String, Integer, Integer> {

		@Override
		protected void onProgressUpdate(Integer... progress) { //Post interim updates to UI thread; access UI
			// [... Update progress bar, Notification, or other UI element ...]

		}

		@Override
		protected void onPostExecute(Integer result) { //Run when doInBackground completed; access UI
			// [... Report results via UI update, Dialog, or notification ...]

			if (result == 1)
			{
				Toast.makeText(getApplicationContext(), "Server not available!", Toast.LENGTH_SHORT).show();
			}
			if (result == 0)
			{
				Toast.makeText(getApplicationContext(), "Game Over! The other player has exited the game.", Toast.LENGTH_SHORT).show();
				finish();
			}
			exitSeekTaskCheckExecuting = false;
		}


		@Override
		protected Integer doInBackground(String... parameter) { //Background thread. Do not interact with UI

			exitSeekTaskCheckExecuting = true;
			if (KeyValueAPI.isServerAvailable())
			{
				if(KeyValueAPI.get("RajatM", "thegreatone76", MyProperties.getInstance().getInvitedUser()+"exitedGame").equals("true")
						||
						(KeyValueAPI.get("RajatM", "thegreatone76", MyProperties.getInstance().getInvitedBy()+"exitedGame").equals("true")
								))
				{
					Log.d(KeyValueAPI.get("RajatM", "thegreatone76", MyProperties.getInstance().getInvitedUser()+"exitedGame"),
							KeyValueAPI.get("RajatM", "thegreatone76", MyProperties.getInstance().getInvitedBy()+"exitedGame"));

					KeyValueAPI.clearKey("RajatM", "thegreatone76", MyProperties.getInstance().getInvitedUser()+"exitedGame");
					KeyValueAPI.clearKey("RajatM", "thegreatone76", MyProperties.getInstance().getInvitedBy()+"exitedGame");
					return 0;
				}
				return 2;
			}
			else
			{
				return 1;
			}
		}
	}



	private class ClearAcceptedSeekAndPutPuzzleValueTask extends AsyncTask<String, Integer, Integer> {

		@Override
		protected void onProgressUpdate(Integer... progress) { //Post interim updates to UI thread; access UI
			// [... Update progress bar, Notification, or other UI element ...]

		}

		@Override
		protected void onPostExecute(Integer result) { //Run when doInBackground completed; access UI
			// [... Report results via UI update, Dialog, or notification ...]

			if (result == 1)
			{
				Toast.makeText(getApplicationContext(), "Unable to clear accepted seek. Server not available!", Toast.LENGTH_SHORT).show();
			}
		}


		@Override
		protected Integer doInBackground(String... parameter) { //Background thread. Do not interact with UI


			if (KeyValueAPI.isServerAvailable())
			{
				KeyValueAPI.put("RajatM", "thegreatone76", MyProperties.getInstance().getGameId()+
						MyProperties.getInstance().getLoggedInUser()+"puzzle"+"haveUpdatedBoard", "false");



				if (KeyValueAPI.get("RajatM", "thegreatone76", MyProperties.getInstance().getInvitedUser()+
						MyProperties.getInstance().getLoggedInUser()+"seekAccepted").equals("true")
						||
						KeyValueAPI.get("RajatM", "thegreatone76", MyProperties.getInstance().getLoggedInUser()+
								MyProperties.getInstance().getInvitedBy()+"seekAccepted").equals("true")
						) 

				{
					if ((KeyValueAPI.get("RajatM", "thegreatone76", 
							MyProperties.getInstance().getLoggedInUser()+"status").equals("onTheGameScreen") &&
							KeyValueAPI.get("RajatM", "thegreatone76", 
									MyProperties.getInstance().getInvitedUser()+"status").equals("onTheGameScreen"))

									||

									(KeyValueAPI.get("RajatM", "thegreatone76", 
											MyProperties.getInstance().getLoggedInUser()+"status").equals("onTheGameScreen") &&
											KeyValueAPI.get("RajatM", "thegreatone76", 
													MyProperties.getInstance().getInvitedBy()+"status").equals("onTheGameScreen"))


							)
					{
						KeyValueAPI.clearKey("RajatM", "thegreatone76", MyProperties.getInstance().getInvitedUser()+
								MyProperties.getInstance().getLoggedInUser()+"seekAccepted"); //do this on the game screen

						KeyValueAPI.clearKey("RajatM", "thegreatone76", MyProperties.getInstance().getLoggedInUser()+
								MyProperties.getInstance().getInvitedBy()+"seekAccepted"); //do this on the game screen

						return 2;
					}}

				/*KeyValueAPI.clearKey("RajatM", "thegreatone76", MyProperties.getInstance().getLoggedInUser()); //clear the seek
				KeyValueAPI.clearKey("RajatM", "thegreatone76", MyProperties.getInstance().getLoggedInUser() + "invitedBy");*/
				return 0;
			}
			else
			{
				return 1;
			}
		}
	}






	private char getTile(int x, int y) {
		return puzzle[x][y];
	}

	/** Return a string for the tile at the given coordinates */
	protected String getTileString(int x, int y) {
		return Character.toString(getTile(x, y));
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
		//puzzleSyncHandler.removeCallbacks(puzzleSyncRunnable);
		int xIndex;
		int yIndex;

		for (CoordinatesTP c : indexesSelected)
		{
			xIndex = c.getX();
			yIndex = c.getY();
			Random r = new Random();
			char ch = (char)(r.nextInt(26) + 'A');
			puzzle[xIndex][yIndex] = ch; 
		}
		try
		{
			puzzleString = PuzzleArrayToString(puzzle);
		}
		catch(Exception e)
		{
			Log.d ("Exception: ", e.toString());
		}


		uploadPuzzleAndScoreForSync();
		//usePuzzleSyncHandler();

	}

	protected void timeUp()
	{
		MusicWordGameTP.stop(this);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);

		alertDialogBuilder.setTitle("Time Up!");
		String message;
		int pointDifference =  MyProperties.getInstance().getMyScore() - MyProperties.getInstance().getOpponentScore();

		if (pointDifference > 0)
		{
			message = "Awesome! You won by: " +  Integer.toString(pointDifference) + " points";
		}
		else if (pointDifference < 0)
		{
			message = "Aww! You lost by: " + Integer.toString(Math.abs(pointDifference)) + " points";
		}
		else
		{
			message = "Game Drawn";
		}
		alertDialogBuilder.setMessage(message + "\nYour final score is: " + finalScore);

		alertDialogBuilder.setPositiveButton("Go to Main Menu", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}});

		submitScoreOnline();

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

	protected void exitPressed()
	{
		new AsyncTask<String, Integer, Integer>() {

			@Override
			protected void onProgressUpdate(Integer... progress) { //Post interim updates to UI thread; access UI
				// [... Update progress bar, Notification, or other UI element ...]

			}

			@Override
			protected void onPostExecute(Integer result) { //Run when doInBackground completed; access UI
				// [... Report results via UI update, Dialog, or notification ...]

				if (result == 1)
				{
					Toast.makeText(getApplicationContext(), "Server not available!", Toast.LENGTH_SHORT).show();
				}
			}


			@Override
			protected Integer doInBackground(String... parameter) { //Background thread. Do not interact with UI


				if (KeyValueAPI.isServerAvailable())
				{
					KeyValueAPI.put("RajatM", "thegreatone76", MyProperties.getInstance().getLoggedInUser()+"exitedGame", "true");
					return 0;
				}
				else
				{
					return 1;
				}
			}
		}.execute("");
	}

	protected void getGameId()
	{
		new AsyncTask<String, Integer, Integer>() {

			@Override
			protected void onProgressUpdate(Integer... progress) { //Post interim updates to UI thread; access UI
				// [... Update progress bar, Notification, or other UI element ...]

			}

			@Override
			protected void onPostExecute(Integer result) { //Run when doInBackground completed; access UI
				// [... Report results via UI update, Dialog, or notification ...]

				if (result == 1)
				{
					Toast.makeText(getApplicationContext(), "Server not available!", Toast.LENGTH_SHORT).show();
				}
			}


			@Override
			protected Integer doInBackground(String... parameter) { //Background thread. Do not interact with UI


				if (KeyValueAPI.isServerAvailable())
				{

					String gameId = KeyValueAPI.get("RajatM", "thegreatone76", MyProperties.getInstance().getLoggedInUser()+ 
							MyProperties.getInstance().getInvitedUser()+"gameId");

					Log.d("gameid for: "+ MyProperties.getInstance().getLoggedInUser() ,  gameId);

					MyProperties.getInstance().setGameId(gameId);

					return 0;
				}
				else
				{
					return 1;
				}
			}
		}.execute("");
	}

	protected void uploadPuzzleAndScoreForSync()
	{
		new AsyncTask<String, Integer, Integer>() {

			@Override
			protected void onProgressUpdate(Integer... progress) { //Post interim updates to UI thread; access UI
				// [... Update progress bar, Notification, or other UI element ...]

			}

			@Override
			protected void onPostExecute(Integer result) { //Run when doInBackground completed; access UI
				// [... Report results via UI update, Dialog, or notification ...]

				if (result == 1)
				{
					Toast.makeText(getApplicationContext(), "Server not available!", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			protected Integer doInBackground(String... parameter) { //Background thread. Do not interact with UI


				if (KeyValueAPI.isServerAvailable())
				{

					//MyProperties.getInstance().setHaveUpdatedBoard(true);

					if 	(KeyValueAPI.get("RajatM", "thegreatone76", MyProperties.getInstance().getGameId()+
							MyProperties.getInstance().getLoggedInUser()+"puzzle"+"haveUpdatedBoard")
							.equals("false"))
					{
						KeyValueAPI.put("RajatM", "thegreatone76", MyProperties.getInstance().getGameId()+
								MyProperties.getInstance().getLoggedInUser()+"puzzle", puzzleString);

						KeyValueAPI.put("RajatM", "thegreatone76", MyProperties.getInstance().getGameId()+
								MyProperties.getInstance().getLoggedInUser()+"puzzle"+"haveUpdatedBoard", "true");


					}

					KeyValueAPI.put("RajatM", "thegreatone76", MyProperties.getInstance().getGameId()+ 
							MyProperties.getInstance().getLoggedInUser()+"score", 
							Integer.toString(MyProperties.getInstance().getMyScore()));

					return 0;
				}
				else
				{
					return 1;
				}
			}
		}.execute("");
	}

	private class UpdatePuzzleAndScoreTask extends AsyncTask<String, Integer, Integer> {

		@Override
		protected void onProgressUpdate(Integer... progress) { //Post interim updates to UI thread; access UI
			// [... Update progress bar, Notification, or other UI element ...]

		}

		@Override
		protected void onPostExecute(Integer result) { //Run when doInBackground completed; access UI
			// [... Report results via UI update, Dialog, or notification ...]

			if (result == 1)
			{
				Toast.makeText(getApplicationContext(), "Server not available!", Toast.LENGTH_SHORT).show();
			}
			updateBoardTaskExecuting = false;
		}


		@Override
		protected Integer doInBackground(String... parameter) { //Background thread. Do not interact with UI

			updateBoardTaskExecuting = true;
			if (KeyValueAPI.isServerAvailable())
			{
				/*	MyProperties.getInstance().setHaveUpdatedBoard(true);
					KeyValueAPI.put("RajatM", "thegreatone76", MyProperties.getInstance().getGameId()+"puzzle", puzzleString);
					KeyValueAPI.put("RajatM", "thegreatone76", MyProperties.getInstance().getGameId()+ 
							MyProperties.getInstance().getLoggedInUser()+"score", 
							Integer.toString(MyProperties.getInstance().getMyScore()));*/

				if (MyProperties.getInstance().getInvited() == true)
				{
					if (KeyValueAPI.get("RajatM", "thegreatone76", MyProperties.getInstance().getGameId()+
							MyProperties.getInstance().getInvitedBy()+"puzzle"+"haveUpdatedBoard").equals("true"))
					{

						puzzleString = KeyValueAPI.get("RajatM", "thegreatone76",  
								MyProperties.getInstance().getGameId()+MyProperties.getInstance().getInvitedBy()
								+"puzzle");

						KeyValueAPI.put("RajatM", "thegreatone76", MyProperties.getInstance().getGameId()+
								MyProperties.getInstance().getInvitedBy()+"puzzle"+"haveUpdatedBoard", "false");

					}

				}
				else
				{
					if (KeyValueAPI.get("RajatM", "thegreatone76", MyProperties.getInstance().getGameId()+
							MyProperties.getInstance().getInvitedUser()+"puzzle"+"haveUpdatedBoard").equals("true"))
					{

						puzzleString = KeyValueAPI.get("RajatM", "thegreatone76",  
								MyProperties.getInstance().getGameId()+MyProperties.getInstance().getInvitedUser()
								+"puzzle");
						KeyValueAPI.put("RajatM", "thegreatone76", MyProperties.getInstance().getGameId()+
								MyProperties.getInstance().getInvitedUser()+"puzzle"+"haveUpdatedBoard", "false");
					}
				}








				//puzzleString = KeyValueAPI.get("RajatM", "thegreatone76",  MyProperties.getInstance().getGameId()+"puzzle");
				//Log.d("puzzle", puzzleString);

				try
				{
					puzzle = StringToPuzzleArray(puzzleString);
				}
				catch (Exception e)
				{
					Log.d ("Exception: ", e.toString());
				}

				if (MyProperties.getInstance().getInvited() == true)
				{
					try
					{
						int opponentScore = Integer.parseInt(KeyValueAPI.get("RajatM", "thegreatone76", MyProperties.getInstance().getGameId()+ 
								MyProperties.getInstance().getInvitedBy()+"score"));
						MyProperties.getInstance().setOpponentScore(opponentScore);
					}
					catch (Exception e)
					{
						Log.d("Exception", e.toString());
					}
				}

				else 
				{
					try
					{
						int opponentScore = Integer.parseInt(KeyValueAPI.get("RajatM", "thegreatone76", MyProperties.getInstance().getGameId()+ 
								MyProperties.getInstance().getInvitedUser()+"score"));

						MyProperties.getInstance().setOpponentScore(opponentScore);
					}
					catch (Exception e)
					{
						Log.d("Exception", e.toString());
					}
				}




				return 0;
			}
			else
			{
				return 1;
			}
		}
	}


	void submitScoreOnline()
	{

		new AsyncTask<String, Integer, Integer>()
		{


			protected void onProgressUpdate(Integer... progress) { //Post interim updates to UI thread; access UI
				// [... Update progress bar, Notification, or other UI element ...]

			}

			@Override
			protected void onPostExecute(Integer result) { //Run when doInBackground completed; access UI
				// [... Report results via UI update, Dialog, or notification ...]

				if (result == 1)
				{
					Toast.makeText(getApplicationContext(), "Server not available!", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			protected Integer doInBackground(String... parameter) { //Background thread. Do not interact with UI

				int count = 1;
				if (KeyValueAPI.isServerAvailable())
				{

					while (	!(KeyValueAPI.get("RajatM", "thegreatone76", "userScore" + Integer.toString(count))).
							equals("Error: No Such Key"))
					{

						count++;
					}
					KeyValueAPI.put("RajatM", "thegreatone76", "userScore" + Integer.toString(count), 
							MyProperties.getInstance().getLoggedInUser()+"scoreishere"+ MyProperties.getInstance().getMyScore());
					return 0;
				}
				else
				{
					return 1;
				}
			}

		}.execute("");


	}



	Handler checkExitHandler;

	public void useCheckExitHandler() {
		checkExitHandler = new Handler();
		checkExitHandler.postDelayed(checkExitRunnable, 2500);
	}

	private Runnable checkExitRunnable = new Runnable() {

		@Override
		public void run() {
			Log.e("Exited", "Checking if opponent has exited");
			if (!exitSeekTaskCheckExecuting)
			{
				CheckPlayerExitedTask exitTask = new CheckPlayerExitedTask();
				exitTask.execute("Checking seeks");
			}
			checkExitHandler.postDelayed(checkExitRunnable, 2500);
		}
	};


	Handler puzzleSyncHandler;

	public void usePuzzleSyncHandler() {
		puzzleSyncHandler = new Handler();
		puzzleSyncHandler.postDelayed(puzzleSyncRunnable, 1200);
	}

	private Runnable puzzleSyncRunnable = new Runnable() {

		@Override
		public void run() {
			Log.e("Updating puzzle", "Updating puzzle");
			if (!updateBoardTaskExecuting)
			{
				UpdatePuzzleAndScoreTask upTask = new UpdatePuzzleAndScoreTask();
				upTask.execute("Checking seeks");
			}
			puzzleSyncHandler.postDelayed(puzzleSyncRunnable, 1200);
		}
	};

	char[][] StringToPuzzleArray(String str)
	{
		char puzzle[][] = new char[5][7];

		char ch[] = str.toCharArray();

		for (int i = 0; i <= 28; i = i+7)
		{
			for (int j = 0; j <= 6; j++)
			{
				puzzle[i/7][j] = ch[i + j];
				// System.out.print(puzzle[i/7][j] + " ");

			}
			//System.out.println();
		}
		return puzzle;
	}


	String PuzzleArrayToString(char[][] charArray)
	{
		String puzzleString = "";


		for (int i = 0; i <= 28; i=i+7) {
			for (int j = 0; j <= 6; j++) {
				puzzleString = puzzleString.concat(Character.toString(charArray[i/7][j]));

			}
		}

		return puzzleString;

	}
}


