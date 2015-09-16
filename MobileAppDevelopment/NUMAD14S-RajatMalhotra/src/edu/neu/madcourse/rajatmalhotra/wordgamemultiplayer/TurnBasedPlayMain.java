package edu.neu.madcourse.rajatmalhotra.wordgamemultiplayer;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import edu.neu.madcourse.rajatmalhotra.MyProperties;
import edu.neu.madcourse.rajatmalhotra.R;
import edu.neu.mhealth.api.KeyValueAPI;

public class TurnBasedPlayMain extends Activity{

	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	public static final String PROPERTY_ALERT_TEXT = "alertText";
	public static final String PROPERTY_TITLE_TEXT = "titleText";
	public static final String PROPERTY_CONTENT_TEXT = "contentText";
	public static final String PROPERTY_NTYPE = "nType";

	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	static final String TAG = "GCM_Communication";

	GoogleCloudMessaging gcm;
	AtomicInteger msgId = new AtomicInteger();
	SharedPreferences prefs;
	Context context;
	String regid;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_turn_based_play_main);
		gcm = GoogleCloudMessaging.getInstance(this);
		context = getApplicationContext();
		MyProperties.getInstance().setOpponentTB(null);
		/*if (checkPlayServices()) {
	        // If this check succeeds, proceed with normal processing.
	        // Otherwise, prompt user to get valid Play Services APK.
			Toast.makeText(getApplicationContext(),
					"Please download Google Play Services!", Toast.LENGTH_SHORT).show();

	    }*/

	}

	@SuppressLint("NewApi")
	private String getRegistrationId(Context context) {

		final SharedPreferences prefs = getGCMPreferences(context);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		if (registrationId.isEmpty()) {
			Log.i(TAG, "Registration not found.");
			return "";
		}
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
				Integer.MIN_VALUE);
		Log.i(TAG, String.valueOf(registeredVersion));
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i(TAG, "App version changed.");
			return "";
		}
		return registrationId;
	}

	private SharedPreferences getGCMPreferences(Context context) {
		return getSharedPreferences(TurnBasedPlayMain.class.getSimpleName(),
				Context.MODE_PRIVATE);
	}

	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	private void registerInBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String name = ((EditText)findViewById(R.id.usernameTextTB)).getText().toString();
				String msg = "";
				try {
					if (!name.equals(""))
					{
						if (gcm == null) {
							gcm = GoogleCloudMessaging.getInstance(context);
						}
						
						regid = gcm.register(Communication_Globals.GCM_SENDER_ID);
						
						KeyValueAPI.put("RajatM", "thegreatone76", "alertText",
								"Login Notification");
						KeyValueAPI.put("RajatM", "thegreatone76", "titleText",
								"Login");
						KeyValueAPI.put("RajatM", "thegreatone76", "contentText",
								"Login Successful");
						/*regid = GCMRegistrar.getRegistrationId(TurnBasedPlayMain.this);
						if (regid.equals(""))
						{
							regid = gcm.register(Communication_Globals.GCM_SENDER_ID);
						}
						else
						{
							msg = "Device already registered!";
							return msg;
						}

					*/	if (KeyValueAPI.isServerAvailable()) {

							KeyValueAPI.put("RajatM", "thegreatone76", "regid"
									+ name, regid);

							MyProperties.getInstance().setLoggedInUserTB(name);
							msg = "Logged In!";
							storeRegistrationId(context, regid);
							return msg;
						}

						else {
							//msg = "Error :" + "Backup Server is not available";
							msg = "Server Error";
							return msg;
						}
					}
					else
					{
						msg = "Please enter a username!";
						return msg;
					}



				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					return msg;
				}
				
			}

			@Override
			protected void onPostExecute(String msg) {

				if (msg.equals("Logged In!"))
				{
					Toast.makeText(getApplicationContext(),
							"Logged In!", Toast.LENGTH_SHORT).show();
					Intent i = new Intent(TurnBasedPlayMain.this, TurnBasedPlayMainScreen.class);
					startActivity(i);
				}
				else if (msg.equals("Please enter a username!"))
				{
					Toast.makeText(getApplicationContext(),
							"Please enter a username!", Toast.LENGTH_SHORT).show();
				}
				else if (msg.equals("Server Error"))
				{
					Toast.makeText(getApplicationContext(),
							"Server Error!", Toast.LENGTH_SHORT).show();
				}
				else if (msg.contains("Error :"))
				{
					Toast.makeText(getApplicationContext(),
							"Error: IOException", Toast.LENGTH_SHORT).show();
				}
				else if (msg.equals("Device already registered!"))
				{
					Toast.makeText(getApplicationContext(),
							"Please logout previous user!", Toast.LENGTH_SHORT).show();
				}
				else
				{}
			}
		}.execute(null, null, null);
	}

	private void storeRegistrationId(Context context, String regId) {

		final SharedPreferences prefs = getGCMPreferences(context);
		int appVersion = getAppVersion(context);
		Log.i(TAG, "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}

	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.i(TAG, "This device is not supported.");
				finish();
			}
			return false;
		}
		return true;
	}

	private void unregister() {
		Log.d(Communication_Globals.TAG, "UNREGISTER USERID: " + regid);
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					msg = "Logging out previous user";
					KeyValueAPI.put("RajatM", "thegreatone76", "alertText",
							"Logout");
					KeyValueAPI.put("RajatM", "thegreatone76", "titleText",
							"Logging out");
					KeyValueAPI.put("RajatM", "thegreatone76", "contentText",
							"Logout Successful");

					KeyValueAPI.clearKey("RajatM", "thegreatone76", "regid"
							+ 	MyProperties.getInstance().getLoggedInUserTB());

					gcm.unregister();
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				removeRegistrationId(getApplicationContext());
				//Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
				/*((TextView) findViewById(R.id.communication_display))
						.setText(regid);*/
			}
		}.execute();
	}

	private void removeRegistrationId(Context context) {
		final SharedPreferences prefs = getGCMPreferences(context);
		int appVersion = getAppVersion(context);
		Log.i(Communication_Globals.TAG, "Removing regId on app version "
				+ appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.remove(PROPERTY_REG_ID);
		editor.commit();
		regid = null;
	}
	public void loginClickTB(View view) {

		if (checkPlayServices()) {
			regid = getRegistrationId(context);
			if (TextUtils.isEmpty(regid)) {
				//unregister();
				registerInBackground();
			}
		}
		else
		{
			Toast.makeText(context, "Please install Google Play Services!", Toast.LENGTH_SHORT).show();
		}


	}

	public void logoutPrevClick(View view) {

		unregister();
	}


}









