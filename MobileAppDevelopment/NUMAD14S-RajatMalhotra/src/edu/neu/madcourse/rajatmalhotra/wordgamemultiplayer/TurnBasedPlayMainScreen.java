package edu.neu.madcourse.rajatmalhotra.wordgamemultiplayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import edu.neu.madcourse.rajatmalhotra.MyProperties;
import edu.neu.madcourse.rajatmalhotra.R;
import edu.neu.mhealth.api.KeyValueAPI;

public class TurnBasedPlayMainScreen extends Activity {

	
	Boolean seekReceived = false;
	Boolean seekAccepted = false;

	

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
				Toast.makeText(getApplicationContext(), "Match Request Sent. " +
						"If your partner is logged in, he will receive a notification about your request!", Toast.LENGTH_SHORT).show();
			}

			if (result == 3)
			{
				Toast.makeText(getApplicationContext(), "Unable to connect!", Toast.LENGTH_SHORT).show();
			}

		}


		@Override
		protected Integer doInBackground(String... parameter) { //Background thread. Do not interact with UI
			
			String userToInvite = parameter[0].toString();
			
			if (userToInvite.equals(MyProperties.getInstance().getLoggedInUserTB()))
			{
				return 0;
			}
			else
			{

				String invitedBy = MyProperties.getInstance().getLoggedInUserTB();
				//check if the user is not inviting himsel9f 
				//and check if the person who is invited is online or not. 
				//display and error if there is no internet connectivity


				if (KeyValueAPI.isServerAvailable())
				{
				
					
					//KeyValueAPI.put("RajatM", "thegreatone76", userToInvite, "invited");
					
					KeyValueAPI.put("RajatM", "thegreatone76", userToInvite + "invitedBy", invitedBy);
					MyProperties.getInstance().setInvitedUserTB(userToInvite);
					
					KeyValueAPI.put("RajatM", "thegreatone76", "gameidForTB", userToInvite+invitedBy);
					
					MyProperties.getInstance().setGameIdTB(userToInvite+invitedBy);
					Log.d("aap",userToInvite+invitedBy);
					//MyProperties.getInstance().setGameId(gameId)(userToInvite);	
					
					sendMessage(userToInvite, "Invite", "Invite" , MyProperties.getInstance().getLoggedInUserTB() + " has invited you to play!");
					MyProperties.getInstance().setOpponentTB(userToInvite);
					KeyValueAPI.put("RajatM", "thegreatone76", "yourOpp", invitedBy);
					/*ProgressDialog  mdialog = new ProgressDialog(TurnBasedPlayMainScreen.this);*/
					
					new NewGameTask().execute("");
					
					
					return 1;
				}

				else
				{
					return 3; //server not available
				}

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
					//String nameToSend = ((EditText)findViewById(R.id.usernameToSend)).getText().toString();
					reg_device = KeyValueAPI.get("RajatM", "thegreatone76", "regid"
							+ usernameToSend);
				
				
				
				//Log.d(String.valueOf(i), reg_device);
					regIds.add(reg_device);
					gcmNotification
						.sendNotification(
								msgParams,
								regIds,
								TurnBasedPlayMainScreen.this);
				//Log.d(String.valueOf(i), regIds.toString());
				
				
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
				Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
			}
		}.execute(null, null, null);
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_turn_based_play_main_screen);
		TextView loggedInUser = (TextView)findViewById(R.id.loggedInUserTB);
		loggedInUser.setText("Logged in as: " + MyProperties.getInstance().getLoggedInUserTB());
		MyProperties.getInstance().setGameOver(false);
		
	}

	public void inviteClick(View view) {
	
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
		alertDialogBuilder.setMessage(R.string.instructions_tb);
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		try {
			alertDialog.show();
		} catch(Exception e){
			Log.e("e", "exception", e);
		}
	}
	
}
