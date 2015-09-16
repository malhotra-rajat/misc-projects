package edu.neu.madcourse.rajatmalhotra.wordgamecommunication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import edu.neu.madcourse.rajatmalhotra.R;

public class Communication extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_communication_select);
	}
	
	
	public void synchronousGameClick(View view) {
		Intent intent = new Intent(this, SynchronousLogin.class);
		startActivity(intent);
	}
	
	public void asynchronousGameClick(View view) {
		Intent intent = new Intent(this, AsynchronousTest.class);
		startActivity(intent);
	}
	
	public void acknowledgementsClick(View view) {
		Intent intent = new Intent(this, AcknowledgementsCommunication.class);
		startActivity(intent);
	}
	
	public void quitClick(View view) {
		finish();
	}
}
