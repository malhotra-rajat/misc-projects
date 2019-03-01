package edu.neu.madcourse.rajatmalhotra.wordgamecommunication;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;
import androidx.legacy.content.WakefulBroadcastReceiver;
import android.content.Context;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("GCMBROADCASTRECIEVER: ","I'm a broadcast receiver!!!");
		ComponentName comp = new ComponentName(context.getPackageName(),
				GcmIntentService.class.getName());
		startWakefulService(context, (intent.setComponent(comp)));
		setResultCode(Activity.RESULT_OK);
	}

}
