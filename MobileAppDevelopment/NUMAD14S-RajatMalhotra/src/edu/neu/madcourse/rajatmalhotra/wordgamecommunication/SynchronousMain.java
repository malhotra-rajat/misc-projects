package edu.neu.madcourse.rajatmalhotra.wordgamecommunication;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import edu.neu.madcourse.rajatmalhotra.MyProperties;
import edu.neu.madcourse.rajatmalhotra.R;
import edu.neu.mhealth.api.KeyValueAPI;

public class SynchronousMain extends Activity {

	Boolean seekReceived = false;
	Timer timerSeek;
	Timer timerLogout;
	private boolean isInFront;
	
	private class ClearSeekTask  extends AsyncTask<String, Integer, Integer> {
		
		@Override
		protected void onProgressUpdate(Integer... progress) { //Post interim updates to UI thread; access UI
		// [... Update progress bar, Notification, or other UI element ...]
		
		}

		@Override
		protected void onPostExecute(Integer result) { //Run when doInBackground completed; access UI
		// [... Report results via UI update, Dialog, or notification ...]
			if (result == 1)
			{
				Toast.makeText(getApplicationContext(), "Unable to clear seek. Server not available!", Toast.LENGTH_SHORT).show();
			}
		}
		
		
		@Override
		protected Integer doInBackground(String... parameter) { //Background thread. Do not interact with UI
			
			if (KeyValueAPI.isServerAvailable())
			{
				KeyValueAPI.clearKey("RajatM", "thegreatone76", MyProperties.getInstance().getLoggedInUser()); //clear the seek
				KeyValueAPI.clearKey("RajatM", "thegreatone76", MyProperties.getInstance().getLoggedInUser() + "invitedBy");
				return 0;
			}
			else
			{
				return 1;
			}
		}
	}

	private class LogoutTask  extends AsyncTask<String, Integer, Integer> {

		@Override
		protected void onProgressUpdate(Integer... progress){}
		@Override
		protected void onPostExecute(Integer result) {

			if (result == 0)
			{
				Toast.makeText(getApplicationContext(), "Server not available!", Toast.LENGTH_SHORT).show();
			}
		}


		@Override
		protected Integer doInBackground(String... parameter) { //Background thread. Do not interact with UI
			String loggedInUserKey = MyProperties.getInstance().getLoggedInUserKey();
			int result = 0;
			//KeyValueAPI.clear("RajatM", "thegreatone76");
			if (KeyValueAPI.isServerAvailable())
			{
				//KeyValueAPI.clear("RajatM", "thegreatone76");
				KeyValueAPI.clearKey("RajatM", "thegreatone76", loggedInUserKey);
				result = 1;
			}
			return result;
		}
	}


	private class InviteToPlayTask extends AsyncTask<String, Integer, Integer> {

		@Override
		protected void onProgressUpdate(Integer... progress) { //Post interim updates to UI thread; access UI
			// [... Update progress bar, Notification, or other UI element ...]

		}

		@Override
		protected void onPostExecute(Integer result) { //Run when doInBackground completed; access UI
			// [... Report results via UI update, Dialog, or notification ...]
			if (result == 0)
			{

				Toast.makeText(getApplicationContext(), "You can't invite yourself to play!", Toast.LENGTH_SHORT).show();
			}

			if (result == 1)
			{
				Toast.makeText(getApplicationContext(), "Request Sent!", Toast.LENGTH_SHORT).show();
			}

			if (result == 2)
			{
				Toast.makeText(getApplicationContext(), "The invited user is not online!", Toast.LENGTH_SHORT).show();
			}

			if (result == 3)
			{
				Toast.makeText(getApplicationContext(), "Unable to connect!", Toast.LENGTH_SHORT).show();
			}

		}


		@Override
		protected Integer doInBackground(String... parameter) { //Background thread. Do not interact with UI
			
			String name = parameter[0].toString();
			
			if (name.equals(MyProperties.getInstance().getLoggedInUser()))
			{
				return 0;
			}
			else
			{

				String invitedBy = MyProperties.getInstance().getLoggedInUser();
				//check if the user is not inviting himself 
				//and check if the person who is invited is online or not. 
				//display and error if there is no internet connectivity

				int count = 1;
				Boolean invitedUserOnline = false;
				String user;

				if (KeyValueAPI.isServerAvailable())
				{
					while (	!(user = KeyValueAPI.get("RajatM", "thegreatone76", "loggedinUser" + Integer.toString(count))).equals("Error: No Such Key"))
					{
						if (name.equals(user))
						{
							invitedUserOnline = true;
							break;
						}
						//Log.d("d", user);
						count++;
					}

					if (invitedUserOnline == true)
					{
						KeyValueAPI.put("RajatM", "thegreatone76", name, "invited");
						KeyValueAPI.put("RajatM", "thegreatone76", name + "invitedBy", invitedBy);
						return 1;
					}

					else
					{
						return 2; //invited user not online
					}
				}

				else
				{
					return 3; //server not available
				}

			}
		}


	}

	private class CheckSeeksAsyncTask extends AsyncTask<String, Integer, Integer> {

		String invitedBy;

		@Override
		protected void onProgressUpdate(Integer... progress) { //Post interim updates to UI thread; access UI
			// [... Update progress bar, Notification, or other UI element ...]

		}


		@Override
		protected void onPostExecute(Integer result) { //Run when doInBackground completed; access UI
			// [... Report results via UI update, Dialog, or notification ...]
			if (result == 0)
			{
				Toast.makeText(getApplicationContext(),
						"Server not available!", Toast.LENGTH_SHORT).show();
			}
			else
			{
				if (result != 100)

				{

					if (result == 1 && seekReceived == true)
					{		//Toast.makeText(getApplicationContext(),
						//"Seek Received", Toast.LENGTH_SHORT).show();

						DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								switch (which){
								case DialogInterface.BUTTON_POSITIVE:
								{
									seekReceived = false; //remove later
									//Yes button clicked
									new ClearSeekTask().execute("");
									seekReceived = false;
									//callSeekTask(); //remove later;
									break;}

								case DialogInterface.BUTTON_NEGATIVE:
								{
									seekReceived = false; 
									new ClearSeekTask().execute("");
									seekReceived = false;
									//callSeekTask(); 
									break;}
								}
							}
						};

						AlertDialog.Builder builder = new AlertDialog.Builder(SynchronousMain.this);
						builder.setMessage(invitedBy + " wants to play a game with you. Play?").setPositiveButton("Yes", dialogClickListener)
						.setNegativeButton("No", dialogClickListener).show();

					}
				}}
		}

		@Override
		protected Integer doInBackground(String... parameter) { //Background thread. Do not interact with UI
			if (seekReceived == false)
			{


				if (KeyValueAPI.isServerAvailable())
				{	 
					if ((KeyValueAPI.get("RajatM", "thegreatone76", MyProperties.getInstance().getLoggedInUser()))
							.equals("invited"))
					{
						invitedBy = KeyValueAPI.get("RajatM", "thegreatone76", MyProperties.getInstance().getLoggedInUser() + "invitedBy");
						seekReceived = true;
					}
				}
				else
				{
					return 0;
				}
				return 1;
			}
			return 100;

		}
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_synchronous_main);
		callSeekTask();
	}

	public void inviteClick(View view) {
		/*Intent intent = new Intent(this, SynchronousLogin.class);
		startActivity(intent);*/
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Invite Someone to Play");
		alert.setMessage("Please enter the username of the player you wish to play with: ");

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("Send Request", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Editable value = input.getText();
				String invitedPlayer = value.toString();
				// Do something with value!
				new InviteToPlayTask().execute(invitedPlayer);
			}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled.
			}
		});

		alert.show();
		//pop a dialog box asking the player's name
	}

	public void instructionsClick(View view) {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);

		alertDialogBuilder.setTitle("Instructions");
		alertDialogBuilder.setMessage(R.string.instructions_text_sync);
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		try {
			alertDialog.show();
		} catch(Exception e){
			Log.e("e", "exception", e);
		}
	}

	public void logoutClick(View view) {
		//code to logout
		new LogoutTask().execute("logging out");
		Toast.makeText(getApplicationContext(),
				"Logged Out", Toast.LENGTH_SHORT).show();
		finish();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		new LogoutTask().execute("logging out");
		Toast.makeText(getApplicationContext(),
				"Logged Out", Toast.LENGTH_SHORT).show();
		super.onBackPressed();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//new LogoutTask().execute("logging out");
		timerSeek.cancel();
		isInFront = false;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		new LogoutTask().execute("logging out");
		timerSeek.cancel();
		super.onDestroy();
	}

	protected void onResume() {
		super.onResume();
		isInFront = true;
	}

	public void callSeekTask() {
		final Handler handler = new Handler();
		timerSeek = new Timer();
		TimerTask seekTask = new TimerTask() {       
			@Override
			public void run() {
				handler.post(new Runnable() {
					public void run() {       
						try {
							CheckSeeksAsyncTask csTask = new CheckSeeksAsyncTask();
							// PerformBackgroundTask this class is the class that extends AsynchTask 

							if (isInFront == true && seekReceived == false)
							{
								csTask.execute("Checking seeks");
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
						}
					}
				});
			}
		};

		timerSeek.schedule(seekTask, 0, 1500); 
	}

}
