package edu.neu.madcourse.rajatmalhotra.wordgamemultiplayer;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import edu.neu.madcourse.rajatmalhotra.MyProperties;
import edu.neu.madcourse.rajatmalhotra.R;
import edu.neu.mhealth.api.KeyValueAPI;

public class RealTimePlayTP extends Activity {

	Boolean seekReceived = false;
	Boolean seekAccepted = false;
	private boolean isInFront;

	final int SERVER_NOT_AVAILABLE = 0;
	final int SUCCESS = 1;
	final int SEEK_ALREADY_ACCEPTED = 2;
	final int INVITE_HIMSELF = 3;
	final int INVITED_USER_NOT_ONLINE = 4;
	final int CONNECTION_ERROR = 5;
	final int SEEK_ALREADY_RECEIVED = 6;

	boolean checkSeekTaskExecuting = false;
	boolean checkAcceptedSeekTaskExecuting = false;
	String invitedUser;

	private class AcceptedSeekTask extends AsyncTask<String, Integer, Integer> {

		@Override
		protected void onProgressUpdate(Integer... progress) { //Post interim updates to UI thread; access UI
			// [... Update progress bar, Notification, or other UI element ...]

		}

		@Override
		protected void onPostExecute(Integer result) { //Run when doInBackground completed; access UI
			// [... Report results via UI update, Dialog, or notification ...]
			if (result == SERVER_NOT_AVAILABLE)
			{
				Toast.makeText(getApplicationContext(), "Unable to send the message that you accepted the seek. Server not available!", Toast.LENGTH_SHORT).show();
			}
		}


		@Override
		protected Integer doInBackground(String... parameter) { //Background thread. Do not interact with UI

			if (KeyValueAPI.isServerAvailable())
			{
				KeyValueAPI.put("RajatM", "thegreatone76", MyProperties.getInstance().getLoggedInUser()+
						MyProperties.getInstance().getInvitedBy()+"seekAccepted", "true"); //remove the seek on the game page
				return SUCCESS;
			}
			else
			{
				return SERVER_NOT_AVAILABLE;
			}
		}
	}

	protected void clearExitedKey()
	{
		new AsyncTask<String, Integer, Integer>() {

			@Override
			protected void onProgressUpdate(Integer... progress) { //Post interim updates to UI thread; access UI
				// [... Update progress bar, Notification, or other UI element ...]

			}

			@Override
			protected void onPostExecute(Integer result) { //Run when doInBackground completed; access UI
				// [... Report results via UI update, Dialog, or notification ...]
				if (result == SERVER_NOT_AVAILABLE)
				{
					Toast.makeText(getApplicationContext(), "Server not available!", Toast.LENGTH_SHORT).show();
				}
			}


			@Override
			protected Integer doInBackground(String... parameter) { //Background thread. Do not interact with UI

				if (KeyValueAPI.isServerAvailable())
				{
					KeyValueAPI.clearKey("RajatM", "thegreatone76", MyProperties.getInstance().getLoggedInUser()+"exitedGame");
					return SUCCESS;
				}
				else
				{
					return SERVER_NOT_AVAILABLE;
				}
			}
		}.execute("");
	}

	private class CheckAcceptedSeekTask extends AsyncTask<String, Integer, Integer> {

		@Override
		protected void onProgressUpdate(Integer... progress) { //Post interim updates to UI thread; access UI
			// [... Update progress bar, Notification, or other UI element ...]

		}

		@Override
		protected void onPostExecute(Integer result) { //Run when doInBackground completed; access UI
			// [... Report results via UI update, Dialog, or notification ...]

			/*if (result == SERVER_NOT_AVAILABLE)
			{
				Toast.makeText(getApplicationContext(), "Unable to check accepted seeks. Server not available!", Toast.LENGTH_SHORT).show();
			}*/
			if (result == SUCCESS)
			{
				//Toast.makeText(getApplicationContext(), "Seek Accepted", Toast.LENGTH_SHORT).show();
				seekAccepted = true;
				Intent i = new Intent(RealTimePlayTP.this, WordGameTP.class);
				startActivity(i);

			}
			checkAcceptedSeekTaskExecuting = false;
		}


		@Override
		protected Integer doInBackground(String... parameter) { //Background thread. Do not interact with UI
			checkAcceptedSeekTaskExecuting = true;
			if (seekAccepted == false)
			{
				if (KeyValueAPI.isServerAvailable())
				{
					if (KeyValueAPI.get("RajatM", "thegreatone76", MyProperties.getInstance().getInvitedUser()+
							MyProperties.getInstance().getLoggedInUser()+"seekAccepted").equals("true")
							||
							KeyValueAPI.get("RajatM", "thegreatone76", MyProperties.getInstance().getLoggedInUser()+
									MyProperties.getInstance().getInvitedBy()+"seekAccepted").equals("true")
							) 

					{
						seekAccepted = true;
						KeyValueAPI.put("RajatM", "thegreatone76", MyProperties.getInstance().getLoggedInUser()+"status", "onTheGameScreen");
						return SUCCESS;
					}

					/*KeyValueAPI.clearKey("RajatM", "thegreatone76", MyProperties.getInstance().getLoggedInUser()); //clear the seek
				KeyValueAPI.clearKey("RajatM", "thegreatone76", MyProperties.getInstance().getLoggedInUser() + "invitedBy");*/
					return 0; //do nothing
				}
				else
				{
					return SERVER_NOT_AVAILABLE; //server not available
				}
			}


			else
			{
				return SEEK_ALREADY_ACCEPTED; //seek already accepted
			}
		}
	}

	private class ClearSeekTask  extends AsyncTask<String, Integer, Integer> {

		@Override
		protected void onProgressUpdate(Integer... progress) { //Post interim updates to UI thread; access UI
			// [... Update progress bar, Notification, or other UI element ...]

		}

		@Override
		protected void onPostExecute(Integer result) { //Run when doInBackground completed; access UI
			// [... Report results via UI update, Dialog, or notification ...]
			if (result == SERVER_NOT_AVAILABLE)
			{
				Toast.makeText(getApplicationContext(), "Unable to clear seek. Server not available!", Toast.LENGTH_SHORT).show();
			}
		}


		@Override
		protected Integer doInBackground(String... parameter) { //Background thread. Do not interact with UI

			if (KeyValueAPI.isServerAvailable())
			{
				try
				{
					KeyValueAPI.clearKey("RajatM", "thegreatone76", MyProperties.getInstance().getLoggedInUser()); 
					KeyValueAPI.clearKey("RajatM", "thegreatone76", invitedUser);//clear the seek
					KeyValueAPI.clearKey("RajatM", "thegreatone76", MyProperties.getInstance().getLoggedInUser() + "invitedBy");
				}
				catch (Exception e)
				{
					Log.e("Exception", e.toString());
				}
				return SUCCESS;
			}
			else
			{
				return SERVER_NOT_AVAILABLE;
			}
		}
	}

	private class LogoutTask  extends AsyncTask<String, Integer, Integer> {

		@Override
		protected void onProgressUpdate(Integer... progress){}
		@Override
		protected void onPostExecute(Integer result) {

			if (result == SERVER_NOT_AVAILABLE)
			{
				Toast.makeText(getApplicationContext(), "Server not available!", Toast.LENGTH_SHORT).show();
			}
		}


		@Override
		protected Integer doInBackground(String... parameter) { //Background thread. Do not interact with UI

			String loggedInUserKey = MyProperties.getInstance().getLoggedInUserKey();

			if (KeyValueAPI.isServerAvailable())
			{
				//KeyValueAPI.clear("RajatM", "thegreatone76");
				KeyValueAPI.clearKey("RajatM", "thegreatone76", loggedInUserKey);
				return SUCCESS;
			}
			else
			{
				return SERVER_NOT_AVAILABLE;
			}

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
			if (result == INVITE_HIMSELF)
			{

				Toast.makeText(getApplicationContext(), "You can't invite yourself to play!", Toast.LENGTH_SHORT).show();
			}

			if (result == SUCCESS)
			{
				Toast.makeText(getApplicationContext(), "Match Request Sent. If your partner accepts, the game will be started automatically!", Toast.LENGTH_SHORT).show();
			}

			if (result == INVITED_USER_NOT_ONLINE)
			{
				Toast.makeText(getApplicationContext(), "The invited user is not online!", Toast.LENGTH_SHORT).show();
			}

			if (result == SERVER_NOT_AVAILABLE)
			{
				Toast.makeText(getApplicationContext(), "Unable to connect!", Toast.LENGTH_SHORT).show();
			}

		}


		@Override
		protected Integer doInBackground(String... parameter) { //Background thread. Do not interact with UI

			invitedUser = parameter[0].toString();

			if (invitedUser.equals(MyProperties.getInstance().getLoggedInUser()))
			{
				return INVITE_HIMSELF;
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
						if (invitedUser.equals(user))
						{
							invitedUserOnline = true;
							break;
						}
						//Log.d("d", user);
						count++;
					}

					if (invitedUserOnline == true)
					{
						KeyValueAPI.put("RajatM", "thegreatone76", invitedUser, "invited");
						KeyValueAPI.put("RajatM", "thegreatone76", invitedUser + "invitedBy", invitedBy);
						MyProperties.getInstance().setInvitedUser(invitedUser);
						return SUCCESS;
					}

					else
					{
						return INVITED_USER_NOT_ONLINE; //invited user not online
					}
				}

				else
				{
					return SERVER_NOT_AVAILABLE; //server not available
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
			if (result == SERVER_NOT_AVAILABLE)
			{
				Toast.makeText(getApplicationContext(),
						"Server not available!", Toast.LENGTH_SHORT).show();
			}
			else if (result == CONNECTION_ERROR)
			{
				Toast.makeText(getApplicationContext(),
						"Connection Error!", Toast.LENGTH_SHORT).show();
				new ClearSeekTask().execute("");
			}
			else if (result == SEEK_ALREADY_RECEIVED) //seek already received .. do nothing
			{

			}
			else
			{
				if (result == SUCCESS && seekReceived == true)
				{		
					DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							switch (which){
							case DialogInterface.BUTTON_POSITIVE:
							{
								seekReceived = false; //remove later
								//Yes button clicked
								new ClearSeekTask().execute("");

								MyProperties.getInstance().setInvited(true);
								MyProperties.getInstance().setInvitedBy(invitedBy);


								storeGameId();

								new AcceptedSeekTask().execute("");
								//seekReceived = false;
								//callSeekTask(); //remove later;
								break;}

							case DialogInterface.BUTTON_NEGATIVE:
							{
								seekReceived = false; 
								new ClearSeekTask().execute("");
								//seekReceived = false;
								//callSeekTask(); 
								break;}
							}
						}
					};

					AlertDialog.Builder builder = new AlertDialog.Builder(RealTimePlayTP.this);
					builder.setMessage(invitedBy + " wants to play a game with you. Play?").setPositiveButton("Yes", dialogClickListener)
					.setNegativeButton("No", dialogClickListener).show();

				}
			}
			checkSeekTaskExecuting = false;
		}



		@Override
		protected Integer doInBackground(String... parameter) { //Background thread. Do not interact with UI
			checkSeekTaskExecuting = true;
			if (seekReceived == false)
			{
				if (KeyValueAPI.isServerAvailable())
				{	 
					if ((KeyValueAPI.get("RajatM", "thegreatone76", MyProperties.getInstance().getLoggedInUser()))
							.equals("invited"))
					{
						invitedBy = KeyValueAPI.get("RajatM", "thegreatone76", MyProperties.getInstance().getLoggedInUser() + "invitedBy");
						if (!invitedBy.contains("Error"))
						{
							seekReceived = true;
							return SUCCESS; //
						}
						else
						{
							return CONNECTION_ERROR; //
						}
					}
				}
				else
				{
					return SERVER_NOT_AVAILABLE; //server not available
				}
			}
			return SEEK_ALREADY_RECEIVED; //seek already received

		}
	}

	protected void storeGameId()
	{
		new AsyncTask<String, Integer, Integer>() {

			@Override
			protected void onProgressUpdate(Integer... progress) { //Post interim updates to UI thread; access UI
				// [... Update progress bar, Notification, or other UI element ...]

			}

			@Override
			protected void onPostExecute(Integer result) { //Run when doInBackground completed; access UI
				// [... Report results via UI update, Dialog, or notification ...]

				if (result == SERVER_NOT_AVAILABLE)
				{
					Toast.makeText(getApplicationContext(), "Server not available!", Toast.LENGTH_SHORT).show();
				}
			}


			@Override
			protected Integer doInBackground(String... parameter) { //Background thread. Do not interact with UI


				if (KeyValueAPI.isServerAvailable())
				{
					KeyValueAPI.put("RajatM", "thegreatone76", MyProperties.getInstance().getInvitedBy() + 
							MyProperties.getInstance().getLoggedInUser()+"gameId", MyProperties.getInstance().getInvitedBy() + 
							MyProperties.getInstance().getLoggedInUser()+"gameId");

					MyProperties.getInstance().setGeneratedGameId(true);

					MyProperties.getInstance().setGameId(MyProperties.getInstance().getInvitedBy() + 
							MyProperties.getInstance().getLoggedInUser()+"gameId");

					Log.d("gameid for: "+ MyProperties.getInstance().getLoggedInUser() ,  MyProperties.getInstance().getInvitedBy() + 
							MyProperties.getInstance().getLoggedInUser()+"gameId");


					return SUCCESS;
				}
				else
				{
					return SERVER_NOT_AVAILABLE;
				}
			}
		}.execute("");
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_real_time_play_main);
		TextView loggedInUser = (TextView)findViewById(R.id.loggedInUser);
		loggedInUser.setText("Logged in as: " + MyProperties.getInstance().getLoggedInUser());
		seekAccepted = false;
		MyProperties.getInstance().setGeneratedGameId(false);
		MyProperties.getInstance().setInvited(false);

		useCheckSeeksHandler();
		useCheckAcceptedSeeksHandler();
		//callCheckAcceptedSeekTask();
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
		alertDialogBuilder.setMessage(R.string.instructions_sync);
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		try {
			alertDialog.show();
		} catch(Exception e){
			Log.e("e", "exception", e);
		}
	}

	public void leaderboardClick(View view) {
		Intent i = new Intent(this, Leaderboard.class);
		startActivity(i);
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
		checkSeeksHandler.removeCallbacks(checkSeeksRunnable);
		checkAcceptedSeeksHandler.removeCallbacks(checkAcceptedSeeksRunnable);
		Toast.makeText(getApplicationContext(),
				"Logged Out", Toast.LENGTH_SHORT).show();
		super.onBackPressed();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		//timerSeek.cancel();
		checkSeeksHandler.removeCallbacks(checkSeeksRunnable);
		checkAcceptedSeeksHandler.removeCallbacks(checkAcceptedSeeksRunnable);
		//timerCheckAcceptedSeek.cancel();
		isInFront = false;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		new LogoutTask().execute("logging out");
		//timerSeek.cancel();
		checkSeeksHandler.removeCallbacks(checkSeeksRunnable);
		checkAcceptedSeeksHandler.removeCallbacks(checkAcceptedSeeksRunnable);
		//timerCheckAcceptedSeek.cancel();
		super.onDestroy();
	}

	protected void onResume() {
		super.onResume();
		isInFront = true;
		seekAccepted = false;

		MyProperties.getInstance().setGeneratedGameId(false);
		MyProperties.getInstance().setInvited(false);

		useCheckSeeksHandler();
		useCheckAcceptedSeeksHandler();

	}

	Handler checkAcceptedSeeksHandler;

	public void useCheckAcceptedSeeksHandler() {
		checkAcceptedSeeksHandler = new Handler();
		checkAcceptedSeeksHandler.postDelayed(checkAcceptedSeeksRunnable, 1100);
	}

	private Runnable checkAcceptedSeeksRunnable = new Runnable() {

		@Override
		public void run() {
			Log.e("checking if seek is accepted", "checking if seek is accepted");
			
			// PerformBackgroundTask this class is the class that extends AsynchTask 
			if (!checkAcceptedSeekTaskExecuting)
			{
				CheckAcceptedSeekTask casTask = new CheckAcceptedSeekTask();
				if (isInFront == true && seekAccepted == false)
				{
					casTask.execute("Checking accepted seeks");
				}
			}
			checkAcceptedSeeksHandler.postDelayed(checkAcceptedSeeksRunnable, 1100);
		}
	};

	Handler checkSeeksHandler;

	public void useCheckSeeksHandler() {
		checkSeeksHandler = new Handler();
		checkSeeksHandler.postDelayed(checkSeeksRunnable, 1800);
	}

	private Runnable checkSeeksRunnable = new Runnable() {

		@Override
		public void run() {
			Log.e("checking seeks", "checking seeks");
			
			if (!checkSeekTaskExecuting)
			{	CheckSeeksAsyncTask csTask = new CheckSeeksAsyncTask();
				if (isInFront == true && seekReceived == false)
				{
					csTask.execute("Checking seeks");
				}
			}
			checkSeeksHandler.postDelayed(checkSeeksRunnable, 1800);
		}
	};

}
