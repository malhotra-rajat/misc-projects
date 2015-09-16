package edu.neu.madcourse.rajatmalhotra.wordgamecommunication;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import edu.neu.madcourse.rajatmalhotra.MyProperties;
import edu.neu.madcourse.rajatmalhotra.R;
import edu.neu.mhealth.api.KeyValueAPI;

public class SynchronousLogin extends Activity {
	
	
		private class loginAsyncTask extends AsyncTask<String, Integer, Integer> {
		
		@Override
		protected void onProgressUpdate(Integer... progress) { //Post interim updates to UI thread; access UI
		// [... Update progress bar, Notification, or other UI element ...]
		
		}
	
		@Override
		protected void onPostExecute(Integer result) { //Run when doInBackground completed; access UI
		// [... Report results via UI update, Dialog, or notification ...]
			if (result == 0)
			{
				Intent intent = new Intent(SynchronousLogin.this, SynchronousMain.class);
				startActivity(intent);
			}
			if (result == 1)
			{
				Toast.makeText(getApplicationContext(),
						"Please enter your username", Toast.LENGTH_SHORT).show();
			}
			if (result == 2)
			{
				Toast.makeText(getApplicationContext(),
						"Username already exists. Please enter a different value.", Toast.LENGTH_SHORT).show();
			}
			if (result == 3)
			{
				Toast.makeText(getApplicationContext(),
						"Server not available!", Toast.LENGTH_SHORT).show();
			}
			
		}
		@Override
		protected Integer doInBackground(String... parameter) { //Background thread. Do not interact with UI
			
			String name = ((EditText)findViewById(R.id.usernameText)).getText().toString();
			int count = 1;
			Boolean usernameAlreadyExists = false;
			String user;
			if (KeyValueAPI.isServerAvailable())
			{
			
				while (	!(user = KeyValueAPI.get("RajatM", "thegreatone76", "loggedinUser" + Integer.toString(count))).equals("Error: No Such Key"))
				{
					if (name.equals(user))
					{
						usernameAlreadyExists = true;
					}
					//Log.d("d", user);
					count++;
				}
			}
			else
			{
				return 3;
			}
			
			if (!usernameAlreadyExists)
			{
			
			
				if (! (name.equals("")))
				{
			
				KeyValueAPI.put("RajatM", "thegreatone76", "loggedinUser" + Integer.toString(count), name);
				MyProperties.getInstance().setLoggedInUser(name);
				MyProperties.getInstance().setLoggedInUserKey("loggedinUser" + Integer.toString(count));
				return 0;
				}
				else
				{
					return 1;
				}
			}
			else
			{
				return 2;
			}
			
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_synchronous);
	}
	
	public void loginClick(View view) {
		
		new loginAsyncTask().execute("logging in");
		
	}

}
