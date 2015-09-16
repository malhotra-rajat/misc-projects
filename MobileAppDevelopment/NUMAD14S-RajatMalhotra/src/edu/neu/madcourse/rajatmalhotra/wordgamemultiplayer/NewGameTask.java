package edu.neu.madcourse.rajatmalhotra.wordgamemultiplayer;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import edu.neu.madcourse.rajatmalhotra.MyProperties;
import edu.neu.mhealth.api.KeyValueAPI;

public class NewGameTask extends AsyncTask<String, Integer, Integer> {

	@Override
	protected void onProgressUpdate(Integer... progress) { //Post interim updates to UI thread; access UI
		// [... Update progress bar, Notification, or other UI element ...]

	}

	@Override
	protected void onPostExecute(Integer result) { //Run when doInBackground completed; access UI
		// [... Report results via UI update, Dialog, or notification ...]
		
		if (result == 3)
		{
			Log.d("app", "Connection Error!");
		}

	}


	@Override
	protected Integer doInBackground(String... parameter) { //Background thread. Do not interact with UI
		
		
		
		
			if (KeyValueAPI.isServerAvailable())
			{
			
				
				//KeyValueAPI.put("RajatM", "thegreatone76", userToInvite, "invited");
				KeyValueAPI.clearKey("RajatM", "thegreatone76", "GameExited");
				KeyValueAPI.put("RajatM", "thegreatone76", 
						MyProperties.getInstance().getLoggedInUserTB()+"movesLeft", "6");
				
				KeyValueAPI.put("RajatM", "thegreatone76", 
						MyProperties.getInstance().getLoggedInUserTB()+"Score", 
						"0");
				

				KeyValueAPI.put("RajatM", "thegreatone76", 
						MyProperties.getInstance().getOpponentTB()+"Score", 
						"0");
				/*
				KeyValueAPI.put("RajatM", "thegreatone76", 
						"oppScore", 
						"0");*/
				KeyValueAPI.put("RajatM", "thegreatone76", MyProperties.getInstance().getOpponentTB()+"movesLeft", "6");
				
				KeyValueAPI.put("RajatM", "thegreatone76", "puzzle", "ANDHENATIGERSHCHICKENPIZZAZRBURGERS");
				
				
				return 1;
			}

			else
			{
				return 3; //server not available
			}

		}
	}