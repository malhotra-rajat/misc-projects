package edu.neu.madcourse.rajatmalhotra.wordgamemultiplayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import edu.neu.madcourse.rajatmalhotra.R;
import edu.neu.mhealth.api.KeyValueAPI;

public class Leaderboard extends ListActivity {

	
	ArrayList<String> userScores = new ArrayList<String>();
	HashMap<Integer, String> scoresNamesHashMap; 
	TreeMap<Integer, String> scoresNamesTreeMap; 
	ArrayList<String> userScoresToDisplay = new ArrayList<String>();
	
	void getScoresOnline()
	{
		
		new AsyncTask<String, Integer, Integer>()
		{
			ProgressDialog pd;
			
			protected void onPreExecute() {
				pd = ProgressDialog.show (Leaderboard.this,"","Loading highest scores...", false);
				
			};
			protected void onProgressUpdate(Integer... progress) { //Post interim updates to UI thread; access UI
				// [... Update progress bar, Notification, or other UI element ...]
				
				}

				@Override
				protected void onPostExecute(Integer result) { //Run when doInBackground completed; access UI
				// [... Report results via UI update, Dialog, or notification ...]
					scoresNamesHashMap = new HashMap<Integer, String>();
					if (userScores.isEmpty() == false)
					{
					
						for (String s : userScores)
						{
							//int indexStart = 0;
							int indexEnd = s.indexOf("scoreishere");
							String userName = s.substring(0, indexEnd);
							int score = Integer.parseInt(s.substring(indexEnd + 11, s.length()));
							
							
							scoresNamesHashMap.put(score, userName);
							
							
						}
					}
					
					scoresNamesTreeMap = new TreeMap<Integer, String>(Collections.reverseOrder());
					scoresNamesTreeMap.putAll(scoresNamesHashMap);
					
					Set s = scoresNamesTreeMap.entrySet();
				    Iterator it = s.iterator();
				    while ( it.hasNext() ) {
				       Map.Entry entry = (Map.Entry) it.next();
				       Integer score = (Integer) entry.getKey();
				       String name = (String) entry.getValue();
				       userScoresToDisplay.add(name + " : " + score + " points");
				    }
				
					setListAdapter(new ArrayAdapter<String>(Leaderboard.this, R.layout.leaderboard_score, userScoresToDisplay));
			 
					ListView listView = getListView();
					listView.setTextFilterEnabled(true);
					pd.dismiss();
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
						String userScore;
						
						while (!(userScore = (KeyValueAPI.get("RajatM", "thegreatone76", "userScore" + Integer.toString(count)))).
								equals("Error: No Such Key"))
						{
							userScores.add(userScore);
							count++;
						}
						return 0;
					}
					else
				    {
						return 1;
					}
				}
				
			}.execute("");
		
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		getScoresOnline();
	}
}
