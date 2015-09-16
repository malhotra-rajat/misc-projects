package edu.neu.madcourse.rajatmalhotra.wordgamecommunication;

import android.R;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import edu.neu.mhealth.api.KeyValueAPI;

public class GcmIntentService extends IntentService {
	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;
	static final String TAG = "GCM_Communication";
	public GcmIntentService() {
		super("GcmIntentService");
	}
	
	

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d(TAG, "  I'm reciving data....");
		String alertText = "";
		String titleText = "";
		String contentText = "";
//		if (Communication_Globals.mode == 1)
//			contentText = +"is online";
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		//String messageType = gcm.getMessageType(intent);
		Log.d(String.valueOf(extras.size()), extras.toString());
		if (!extras.isEmpty()) { // has effect of unparcelling Bundle
			alertText = KeyValueAPI.get("RajatM", "thegreatone76","alertText");
			titleText = KeyValueAPI.get("RajatM", "thegreatone76","titleText");
			contentText = KeyValueAPI.get("RajatM", "thegreatone76","contentText");
			sendNotification(alertText, titleText, contentText);
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	// Put the message into a notification and post it.
	// This is just one simple example of what you might choose to do with
	// a GCM message.
	public void sendNotification(String alertText, String titleText,
			String contentText) {
		Log.d(TAG, "???sendNotification???");
		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Intent notificationIntent;
		notificationIntent = new Intent(
				this,
				AsynchronousTest.class);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		notificationIntent.putExtra("show_response", "show_response");
		PendingIntent intent = PendingIntent.getActivity(this, 0, new Intent(
				this, AsynchronousTest.class), PendingIntent.FLAG_UPDATE_CURRENT);

		
		NotificationCompat.Builder mBuilder = 
				new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.ic_menu_info_details)
				.setContentTitle(titleText)
				.setStyle(new NotificationCompat.BigTextStyle().bigText(contentText))
				.setContentText(contentText)
				.setTicker(alertText)
				.setAutoCancel(true)
				.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
			mBuilder.setContentIntent(intent);
			mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	}

}