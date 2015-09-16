/**
 * 
 */
package edu.neu.madcourse.rajatmalhotra.finalproject;

import edu.neu.madcourse.rajatmalhotra.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * @author Arpit
 *
 */
public class ProjectDescriptionActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_project_description);
	}
	
	public void handleGoToAppClick(View view) {
		Intent goToAppIntent = new Intent(this, LtwMainActivity.class);
		startActivity(goToAppIntent);
	}
	
	public void handleAckClick(View view) {
		Intent ackIntent = new Intent(this, LtwAckActivity.class);
		startActivity(ackIntent);
	}
	
	public void handleExitClick(View view) {
		finish();
	}
}
