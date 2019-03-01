package edu.neu.madcourse.rajatmalhotra.finalproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;
import edu.neu.madcourse.rajatmalhotra.R;

public class SelectManuallyActivity extends Activity implements OnItemSelectedListener {

	/**
	 * The Database
	 */
	ExerciseDatabase exDb;

	/**
	 * The Ui Elements
	 */
	private Spinner spExerciseName;

	private NumberPicker npReps;
	private NumberPicker npWeights;

	//Intent i = new Intent(this, BeginWorkoutActivity.class);

	/**
	 * The TAG
	 */
	private static final String TAG = "SelectManuallyActivity";

	/**
	 * The list of all exercises (for auto complete text view
	 */
	private ArrayList<String> arrListExerciseNames = new ArrayList<String>();

	/**
	 * Variables for Ui element values
	 */
	String exerciseName = null;
	int nReps = -1;
	int nWeights = -1;

	/**
	 * Today's date
	 */
	Date date;


	String[] displayedValues;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_manually);
		exDb = ExerciseDatabase.getInstance(this);

		spExerciseName = (Spinner) findViewById(R.id.actvExerciseName);

		npReps = (NumberPicker) findViewById(R.id.npReps);
		npReps.setMinValue(0);
		npReps.setMaxValue(1000);
		npReps.setWrapSelectorWheel(false);
		npWeights = (NumberPicker) findViewById(R.id.npWeights);
		npWeights.setMinValue(0);
		npWeights.setMaxValue(10000);


		int NUMBER_OF_VALUES = 100; //num of values in the picker
		int PICKER_RANGE = 10;
		displayedValues  = new String[NUMBER_OF_VALUES];
		for(int i = 0; i < NUMBER_OF_VALUES; i++)
		{
			displayedValues[i] = String.valueOf(PICKER_RANGE * (i));
		}
		npWeights.setMinValue(0); 
		npWeights.setMaxValue(displayedValues.length-1);
		npWeights.setDisplayedValues(displayedValues);
		npWeights.setWrapSelectorWheel(false);

		try {
			getAllExercises();
		} catch (IOException e) {
			Log.d(TAG, "IoException: " + e.getMessage());
		}

		// Creating the instance of ArrayAdapter containing list of language
		// names
		Collections.sort(arrListExerciseNames);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.exercise_spinner_item, arrListExerciseNames);
		adapter.setDropDownViewResource(R.layout.exercise_dropdown_item);

		spExerciseName.setAdapter(adapter);
		spExerciseName.setOnItemSelectedListener(this);

		// Initialize today's date
		date = new Date(System.currentTimeMillis());
	}

	public void onItemSelected(AdapterView<?> parent, View view, 
			int pos, long id) {
		// An item was selected. You can retrieve the selected item using
		// parent.getItemAtPosition(pos)
		exerciseName = (String) parent.getItemAtPosition(pos);
	}

	public void onNothingSelected(AdapterView<?> parent) {
		// Another interface callback
		exerciseName = (String) parent.getItemAtPosition(0);

	}

	private void getAllExercises() throws IOException {
		InputStream stream = getResources().openRawResource(R.raw.exercises);
		BufferedReader br = new BufferedReader(new InputStreamReader(stream,
				"UTF8"));
		String line;

		line = br.readLine();

		while (line != null) {
			arrListExerciseNames.add(line);
			line = br.readLine();
		}
	}

	public void handleSaveClick(View view) {

		nReps = npReps.getValue();
		nWeights = Integer.parseInt(displayedValues[npWeights.getValue()]); 

		if (null != exerciseName) {
			if ((-1 != nReps) && (0 != nReps)) {
				if ((-1 != nWeights) && (0 != nWeights)) {
					Date date = new Date(System.currentTimeMillis());
					SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
					String todayDate = sdf.format(date);

					//TODO
					Log.d(TAG, todayDate);

					if (-1 != exDb.insertExercise(todayDate, exerciseName,
							nReps, nWeights)) {

						clear();

						Toast.makeText(getApplicationContext(),
								"Exercise details saved", Toast.LENGTH_SHORT)
								.show();


					} else {
						Toast.makeText(getApplicationContext(),
								"Error in saving exercise details",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getApplicationContext(),
							"Please Select Weights", Toast.LENGTH_LONG).show();
				}

			} else {
				Toast.makeText(getApplicationContext(),
						"Please Select Number Of Reps", Toast.LENGTH_LONG)
						.show();
			}
		} else {
			Toast.makeText(getApplicationContext(),
					"Please Select Exercise", Toast.LENGTH_SHORT).show();
		}
	}

	public void clear()
	{
		spExerciseName.setSelection(0);
		npReps.setValue(0);
		npWeights.setValue(0);

		exerciseName = null;
		nReps = -1;
		nWeights = -1;
	}

	public void handleClearClick(View view) {
		clear();
	}

	public void handleExitClick(View view) {

		finish();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
}
