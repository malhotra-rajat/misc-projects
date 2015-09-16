package edu.neu.madcourse.rajatmalhotra.wordgamecommunication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import edu.neu.madcourse.rajatmalhotra.R;
import edu.neu.mhealth.api.KeyValueAPI;

public class AsynchronousTest extends Activity implements OnClickListener {

	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	public static final String PROPERTY_ALERT_TEXT = "alertText";
	public static final String PROPERTY_TITLE_TEXT = "titleText";
	public static final String PROPERTY_CONTENT_TEXT = "contentText";
	public static final String PROPERTY_NTYPE = "nType";
	public String IMEI;

	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	static final String TAG = "GCM_Communication";
	TextView mDisplay;
	EditText mMessage;
	//EditText mUsername;
	GoogleCloudMessaging gcm;
	AtomicInteger msgId = new AtomicInteger();
	SharedPreferences prefs;
	Context context;
	String regid;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_asynchronous_test);
		
		mDisplay = (TextView) findViewById(R.id.communication_display);
		mMessage = (EditText) findViewById(R.id.communication_edit_message);
		//mUsername = (EditText) findViewById(R.id.communication_edit_username);
		
		gcm = GoogleCloudMessaging.getInstance(this);
		context = getApplicationContext();
		TelephonyManager tm = (TelephonyManager) this
				.getSystemService(TELEPHONY_SERVICE);
		IMEI = tm.getDeviceId();
	
		
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
		return getSharedPreferences(AsynchronousTest.class.getSimpleName(),
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
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					KeyValueAPI.put("RajatM", "thegreatone76", "alertText",
							"Registration Notification");
					KeyValueAPI.put("RajatM", "thegreatone76", "titleText",
							"Registration");
					KeyValueAPI.put("RajatM", "thegreatone76", "contentText",
							"Registration Successful");
					regid = gcm.register(Communication_Globals.GCM_SENDER_ID);
					int cnt = 0;
					if (KeyValueAPI.isServerAvailable()) {
						if (!KeyValueAPI.get("RajatM", "thegreatone76", "cnt")
								.contains("Error")) {
							Log.d("????", KeyValueAPI.get("RajatM", "thegreatone76",
									"cnt"));
							cnt = Integer.parseInt(KeyValueAPI.get("RajatM", "thegreatone76"
									,"cnt"));
						}
						String getString;
						boolean flag = false;
						for (int i = 1; i <= cnt; i++) {
							getString = KeyValueAPI.get("RajatM", "thegreatone76",
									"regid" + String.valueOf(i));
							Log.d(String.valueOf(i), getString);
							if (getString.equals(regid))
								flag = true;
						}
						if (!flag) {
							KeyValueAPI.put("RajatM", "thegreatone76", "cnt",
									String.valueOf(cnt + 1));
							KeyValueAPI.put("RajatM", "thegreatone76", "regid"
									+ String.valueOf(cnt + 1), regid);
						}
						msg = "Device registered, registration ID =" + regid;
					} else {
						msg = "Error :" + "Backup Server is not available";
						return msg;
					}
					//sendRegistrationIdToBackend();
					storeRegistrationId(context, regid);
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				mDisplay.append(msg + "\n");
			}
		}.execute(null, null, null);
	}
	
	/*private void sendRegistrationIdToBackend() {
		// Your implementation here.
	}*/

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
	
	@Override
	public void onClick(final View view) {
		
		//Log.d("click", "am I click?");
		if (view == findViewById(R.id.communication_send)) {
			String message = ((EditText) findViewById(R.id.communication_edit_message))
					.getText().toString();
			if (message != "") {
				sendMessage(message);
			} else {
				Toast.makeText(context, "Sending Context Empty!",
						Toast.LENGTH_LONG).show();
			}
		} else if (view == findViewById(R.id.communication_clear)) {
			mMessage.setText("");
		} else if (view == findViewById(R.id.communication_unregister_button)) {
			unregister();}
			else if (view == findViewById(R.id.communication_register_button)) {
				if (checkPlayServices()) {
					regid = getRegistrationId(context);
					if (TextUtils.isEmpty(regid)) {
						registerInBackground();
					}
				}
			}

		}


	private void unregister() {
		Log.d(Communication_Globals.TAG, "UNREGISTER USERID: " + regid);
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					msg = "Unregistration Sent";
					KeyValueAPI.put("RajatM", "thegreatone76", "alertText",
							"Notification");
					KeyValueAPI.put("RajatM", "thegreatone76", "titleText",
							"Unregistration");
					KeyValueAPI.put("RajatM", "thegreatone76", "contentText",
							"Unregistration Successful");
					gcm.unregister();
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				removeRegistrationId(getApplicationContext());
				Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
				((TextView) findViewById(R.id.communication_display))
						.setText(regid);
			}
		}.execute();
	}
	private void removeRegistrationId(Context context) {
		final SharedPreferences prefs = getGCMPreferences(context);
		int appVersion = getAppVersion(context);
		Log.i(Communication_Globals.TAG, "Removig regId on app version "
				+ appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.remove(PROPERTY_REG_ID);
		editor.commit();
		regid = null;
	}
	
	@SuppressLint("NewApi")
	private void sendMessage(final String message) {
		if (regid == null || regid.equals("")) {
			Toast.makeText(this, "You must register first", Toast.LENGTH_LONG)
					.show();
			return;
		}
		if (message.isEmpty()) {
			Toast.makeText(this, "Empty Message", Toast.LENGTH_LONG).show();
			return;
		}

		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				int cnt = 0;
				if (!KeyValueAPI.get("RajatM", "thegreatone76", "cnt").contains(
						"Error"))
					cnt = Integer.parseInt(KeyValueAPI.get("RajatM", "thegreatone76", "cnt"));
				else {
					msg = KeyValueAPI.get("RajatM", "thegreatone76", "cnt");
					return msg;
				}
				List<String> regIds = new ArrayList<String>();
				String reg_device = regid;
				int nIcon = R.drawable.ic_stat_cloud;
				int nType = Communication_Globals.SIMPLE_NOTIFICATION;
				Map<String, String> msgParams;
				
				msgParams = new HashMap<String, String>();
				msgParams.put("data.alertText", "Notification");
				msgParams.put("data.titleText", "Notification Title");
				msgParams.put("data.contentText", message);
				msgParams.put("data.nIcon", String.valueOf(nIcon));
				msgParams.put("data.nType", String.valueOf(nType));
				
				KeyValueAPI.put("RajatM", "thegreatone76", "alertText",
						"Message Notification");
				KeyValueAPI.put("RajatM", "thegreatone76", "titleText",
						"Sending Message");
				KeyValueAPI.put("RajatM", "thegreatone76", "contentText", message);
				KeyValueAPI.put("RajatM", "thegreatone76", "nIcon",
						String.valueOf(nIcon));
				KeyValueAPI.put("RajatM", "thegreatone76", "nType",
						String.valueOf(nType));
				
				GcmNotification gcmNotification = new GcmNotification();
				for (int i = 1; i <= cnt; i++) {
					regIds.clear();
					reg_device = KeyValueAPI.get("RajatM", "thegreatone76", "regid"
							+ String.valueOf(i));
					Log.d(String.valueOf(i), reg_device);
					regIds.add(reg_device);
					gcmNotification
							.sendNotification(
									msgParams,
									regIds,
									AsynchronousTest.this);
					Log.d(String.valueOf(i), regIds.toString());
				}
				msg = "Sending Information...";
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
			}
		}.execute(null, null, null);
	}

}
