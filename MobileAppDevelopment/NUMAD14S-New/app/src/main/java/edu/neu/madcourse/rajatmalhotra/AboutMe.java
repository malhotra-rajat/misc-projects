package edu.neu.madcourse.rajatmalhotra;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.widget.TextView;

public class AboutMe extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_about_me);

		/*TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		TextView text = (TextView) findViewById(R.id.imei);
		text.setText("IMEI: " + telephonyManager.getDeviceId());*/
	}
}
