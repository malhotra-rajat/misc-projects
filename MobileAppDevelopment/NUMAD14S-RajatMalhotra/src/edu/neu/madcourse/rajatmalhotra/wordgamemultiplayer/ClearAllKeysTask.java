package edu.neu.madcourse.rajatmalhotra.wordgamemultiplayer;

import android.os.AsyncTask;
import edu.neu.mhealth.api.KeyValueAPI;

public class ClearAllKeysTask extends AsyncTask<String, Integer, Integer> {

		@Override
		protected void onProgressUpdate(Integer... progress){}
		@Override
		protected void onPostExecute(Integer result) {

			if (result == 0)
			{
				//Toast.makeText(getApplicationContext(), "Server not available!", Toast.LENGTH_SHORT).show();
			}
		}


		@Override
		protected Integer doInBackground(String... parameter) { //Background thread. Do not interact with UI
			//String loggedInUserKey = MyProperties.getInstance().getLoggedInUserKey();
			int result = 0;
			//KeyValueAPI.clear("RajatM", "thegreatone76");
			if (KeyValueAPI.isServerAvailable())
			{
				//KeyValueAPI.clear("RajatM", "thegreatone76");
				KeyValueAPI.clear("RajatM", "thegreatone76");
				result = 1;
			}
			return result;
		}
	}

