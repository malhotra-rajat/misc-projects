package edu.neu.madcourse.rajatmalhotra.finalproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;

import edu.neu.madcourse.rajatmalhotra.R;
import android.app.ListActivity;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ViewAllExercisesActivity extends ListActivity {
	
	private static final String TAG = "ViewAllExercisesActivity";

	ArrayList<String> exercises = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_all_exercises);

		try {
			InputStream stream = getResources().openRawResource(R.raw.exercises);
			BufferedReader br = new BufferedReader(new InputStreamReader(stream,
					"UTF8"));
			String line;

			line = br.readLine();

			while (line != null) {
				exercises.add(line);
				line = br.readLine();
			}
		} catch (NotFoundException e) {
			Log.d(TAG, "NotFoundException" + e.getMessage());
		} catch (UnsupportedEncodingException e) {
			Log.d(TAG, "UnsupportedEncodingException" + e.getMessage());
		} catch (IOException e) {
			Log.d(TAG, "IOException" + e.getMessage());
		}
		
		Collections.sort(exercises);
		setListAdapter(new ArrayAdapter<String>(ViewAllExercisesActivity.this, R.layout.workout_entry_li, exercises));
		 
		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
	}
}
