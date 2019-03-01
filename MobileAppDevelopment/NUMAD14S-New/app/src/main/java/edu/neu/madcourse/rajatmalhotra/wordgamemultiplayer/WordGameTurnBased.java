package edu.neu.madcourse.rajatmalhotra.wordgamemultiplayer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;
import edu.neu.madcourse.rajatmalhotra.MyProperties;
import edu.neu.madcourse.rajatmalhotra.R;
import edu.neu.mhealth.api.KeyValueAPI;

public class WordGameTurnBased extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (MyProperties.getInstance().getOpponentTB() == null)
		{
			new getOpponentTask().execute("");
		}
		
		try
		{
			puzzle = StringToPuzzleArray(MyProperties.getInstance().getPuzzle());
		}
		catch (Exception e)
		{
			Log.d ("Exception: ", e.toString());
		}
		//useSyncrhonizeHandler();

		wordGameView = new WordGameViewTurnBased(this);
		setContentView(wordGameView);
		wordGameView.invalidate();
		}
	
	
	private WordGameViewTurnBased wordGameView;

	private char puzzle[][];
	
	BufferedReader br;
	ArrayList<CoordinatesTP> indexesSelected = new ArrayList<CoordinatesTP>();
	HashSet<String> hash_word_list = new HashSet<String>();
	HashSet<String> hash_word_list_for_hints = new HashSet<String>();

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//new ClearAcceptedSeekAndPutPuzzleValueTask().execute("");
		if (MyProperties.getInstance().getOpponentTB() == null)
		{
			new getOpponentTask().execute("");
		}
		try
		{
			puzzle = StringToPuzzleArray(MyProperties.getInstance().getPuzzle());
		}
		catch (Exception e)
		{
			Log.d ("Exception: ", e.toString());
		}
		
		new downloadPuzzleMovesScoresTask().execute("");
		MusicWordGameTP.play(this, R.raw.word_game_background);
		
		wordGameView.invalidate();
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		wordGameView.invalidate();
	}
	
	@Override
	protected void onDestroy() {
		//synchronizeHandler.removeCallbacks(synchronizeRunnable);
		super.onDestroy();
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//synchronizeHandler.removeCallbacks(synchronizeRunnable);
		MusicWordGameTP.stop(this);
	}
	
	
	private class getOpponentTask extends AsyncTask<String, Integer, Integer> {
		ProgressDialog pd1; 
		
		@Override
		protected void onPreExecute() {
			pd1 = ProgressDialog.show(WordGameTurnBased.this,"","Synchronizing Game. Please wait!",false); 
			//pd1.setCancelable(true);
		};
		
		@Override
		protected void onProgressUpdate(Integer... progress) { //Post interim updates to UI thread; access UI
			// [... Update progress bar, Notification, or other UI element ...]

		}

		@Override
		protected void onPostExecute(Integer result) { //Run when doInBackground completed; access UI
			// [... Report results via UI update, Dialog, or notification ...]
			pd1.dismiss();
			if (result == 3)
			{
				Toast.makeText(getApplicationContext(), "Unable to connect!", Toast.LENGTH_SHORT).show();
			}

		}
		@Override
		protected Integer doInBackground(String... parameter) { //Background thread. Do not interact with UI
				if (KeyValueAPI.isServerAvailable())
				{
					MyProperties.getInstance().setOpponentTB((
							KeyValueAPI.get("RajatM", "thegreatone76", "yourOpp")));
					
					return 1;
				}

				else
				{
					return 3; //server not available
				}

			}
		}

	private class downloadPuzzleMovesScoresTask extends AsyncTask<String, Integer, Integer> {

		@Override
		protected void onPreExecute() {
			//pd2 = ProgressDialog.show(WordGameTurnBased.this,"","Synchronizing Game. Please wait!",false); 
		}
		
		@Override
		protected void onProgressUpdate(Integer... progress) { //Post interim updates to UI thread; access UI
			// [... Update progress bar, Notification, or other UI element ...]

		}

		@Override
		protected void onPostExecute(Integer result) { //Run when doInBackground completed; access UI
			// [... Report results via UI update, Dialog, or notification ...]
			
		//	pd2.dismiss();
			if (result == 3)
			{
				Toast.makeText(getApplicationContext(), "Unable to connect!", Toast.LENGTH_SHORT).show();
			}

		}


		@Override
		protected Integer doInBackground(String... parameter) { //Background thread. Do not interact with UI
				if (KeyValueAPI.isServerAvailable())
				{
					try
					{
					
					int ml = Integer.parseInt(KeyValueAPI.get("RajatM", "thegreatone76",  MyProperties.getInstance().getLoggedInUserTB()+"movesLeft"));
					MyProperties.getInstance().setMovesLeft(ml);
					
					int myscr = Integer.parseInt(KeyValueAPI.get("RajatM", "thegreatone76", MyProperties.getInstance().getLoggedInUserTB()+"Score"));
					MyProperties.getInstance().setMyScoreTB(myscr);
					
					MyProperties.getInstance().setPuzzle(
							KeyValueAPI.get("RajatM", "thegreatone76", "puzzle"));
					
					try
					{
						puzzle = StringToPuzzleArray(MyProperties.getInstance().getPuzzle());
					}
					catch (Exception e)
					{
						Log.d ("Exception: ", e.toString());
					}
							
					int opscr = Integer.parseInt(KeyValueAPI.get("RajatM", "thegreatone76", MyProperties.getInstance().getOpponentTB()+"score"));
					
					MyProperties.getInstance().setOpponentScoreTB(opscr);
							
					}
					catch (Exception e)
					{
						Log.d ("Exception: ", e.toString());
					}
					
					
					return 1;
				}

				else
				{
					return 3; //server not available
				}

			}
		}
	
	private class uploadPuzzleMovesScoresTask extends AsyncTask<String, Integer, Integer> {
		ProgressDialog pd3;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			pd3 = ProgressDialog.show(WordGameTurnBased.this,"","Sending Move. Please wait!",false); 
		}
		
		@Override
		protected void onProgressUpdate(Integer... progress) { //Post interim updates to UI thread; access UI
			// [... Update progress bar, Notification, or other UI element ...]

		}

		@Override
		protected void onPostExecute(Integer result) { //Run when doInBackground completed; access UI
			// [... Report results via UI update, Dialog, or notification ...]
			pd3.dismiss();
			if (result == 3)
			{
				Toast.makeText(getApplicationContext(), "Unable to connect!", Toast.LENGTH_SHORT).show();
			}

		}

		@Override
		protected Integer doInBackground(String... parameter) { //Background thread. Do not interact with UI
			if (KeyValueAPI.isServerAvailable())
				{
					KeyValueAPI.put("RajatM", "thegreatone76", 
							MyProperties.getInstance().getLoggedInUserTB()+"movesLeft", 
							Integer.toString(MyProperties.getInstance().getMovesLeft()));
					
					KeyValueAPI.put("RajatM", "thegreatone76", 
							MyProperties.getInstance().getLoggedInUserTB()+"Score", 
							Integer.toString(MyProperties.getInstance().getMyScoreTB()));
					
					KeyValueAPI.put("RajatM", "thegreatone76", "puzzle", MyProperties.getInstance().getPuzzle());
					
					return 1;
				}

				else
				{
					return 3; //server not available
				}

			}
		}
	
	
	private class GameExitedTask extends AsyncTask<String, Integer, Integer> {

		ProgressDialog pd2;
		@Override
		protected void onPreExecute() {
			pd2 = ProgressDialog.show(WordGameTurnBased.this,"","Please wait...",false); 
		}
		
		@Override
		protected void onProgressUpdate(Integer... progress) { //Post interim updates to UI thread; access UI
			// [... Update progress bar, Notification, or other UI element ...]

		}

		@Override
		protected void onPostExecute(Integer result) { //Run when doInBackground completed; access UI
			// [... Report results via UI update, Dialog, or notification ...]
			
		    pd2.dismiss();
			if (result == 3)
			{
				Toast.makeText(getApplicationContext(), "Unable to connect!", Toast.LENGTH_SHORT).show();
			}

		}


		@Override
		protected Integer doInBackground(String... parameter) { //Background thread. Do not interact with UI
				if (KeyValueAPI.isServerAvailable())
				{
					KeyValueAPI.put("RajatM", "thegreatone76", "GameExited", "true");
					return 1;
				}

				else
				{
					return 3; //server not available
				}

			}
		}
	
	public void sendMessage(final String usernameToSend, final String alertText, final String titleText, final String message) {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				
				List<String> regIds = new ArrayList<String>();
				String reg_device;
				int nIcon = R.drawable.ic_stat_cloud;
				int nType = Communication_Globals.SIMPLE_NOTIFICATION;
				Map<String, String> msgParams;
				
				msgParams = new HashMap<String, String>();
				msgParams.put("data.alertText", "Notification");
				msgParams.put("data.titleText", "Notification Title");
				msgParams.put("data.contentText", message);
				msgParams.put("data.nIcon", String.valueOf(nIcon));
				msgParams.put("data.nType", String.valueOf(nType));
				
				if (KeyValueAPI.isServerAvailable())
				{
				
					KeyValueAPI.put("RajatM", "thegreatone76", "alertText",
						titleText);
					KeyValueAPI.put("RajatM", "thegreatone76", "titleText",
						alertText);
					KeyValueAPI.put("RajatM", "thegreatone76", "contentText", message);
					KeyValueAPI.put("RajatM", "thegreatone76", "nIcon",
						String.valueOf(nIcon));
					KeyValueAPI.put("RajatM", "thegreatone76", "nType",
						String.valueOf(nType));
				
					GcmNotification gcmNotification = new GcmNotification();
					regIds.clear();
					reg_device = KeyValueAPI.get("RajatM", "thegreatone76", "regid"
							+ usernameToSend);
			
					regIds.add(reg_device);
					gcmNotification
						.sendNotification(
								msgParams,
								regIds,
								WordGameTurnBased.this);
				
				
					msg = "Sending Information...";
					
				}
				else
				{
					msg = "Connection Error!";
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				//Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
			}
		}.execute(null, null, null);
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
			MyProperties.getInstance().setPuzzle(PuzzleArrayToString(puzzle));
		}
		catch(Exception e)
		{
			Log.d ("Exception: ", e.toString());
		}
		
		new uploadPuzzleMovesScoresTask().execute("");
		if (!(MyProperties.getInstance().getMovesLeft() < 1))
		{
			sendMessage(MyProperties.getInstance().getOpponentTB(), "Move", "Move" , MyProperties.getInstance().getLoggedInUserTB() + " has made a move!");
		}
	}
	
	protected void movesUp()
	{
		MusicWordGameTP.stop(this);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);
		
		alertDialogBuilder.setTitle("Moves Over!");
		String message;
		int pointDifference = MyProperties.getInstance().getMyScoreTB() - MyProperties.getInstance().getOpponentScoreTB();
		
		if (pointDifference > 0)
		{
			message = "Awesome! You won by: " +  Integer.toString(pointDifference) + " points";
			sendMessage(MyProperties.getInstance().getOpponentTB(), "Game Over!" ,  "Game Over", "You lost by " + pointDifference + " points!");
		}
		else if (pointDifference < 0)
		{
			message = "Aww! You lost by: " + Integer.toString(Math.abs(pointDifference)) + " points";
			sendMessage(MyProperties.getInstance().getOpponentTB(), "Game Over!" ,  "Game Over", "You won by " + pointDifference + " points!");
		}
		else
		{
			message = "Game Drawn";
			sendMessage(MyProperties.getInstance().getOpponentTB(), "Game Over!" ,  "Game Drawn", "You lost by " + pointDifference + " points!");
		}
		alertDialogBuilder.setMessage(message + "\nYour final score is: " + MyProperties.getInstance().getMyScoreTB());
				
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
				new GameExitedTask().execute("");
				
	}

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
	
	/*  Handler synchronizeHandler;
		 
	  public void useSyncrhonizeHandler() {
		  synchronizeHandler = new Handler();
		  synchronizeHandler.postDelayed(synchronizeRunnable, 1000);
	  }

	  private Runnable synchronizeRunnable = new Runnable() {

	    @Override
	    public void run() {
	      Log.e("Handlers", "Calls");
	      downloadPuzzleMovesScoresTask csTask = new downloadPuzzleMovesScoresTask();
			// PerformBackgroundTask this class is the class that extends AsynchTask 
			csTask.execute("Checking seeks");
			wordGameView.invalidate();
			synchronizeHandler.postDelayed(synchronizeRunnable, 4000);
	    }
	  };*/

}
