package edu.neu.madcourse.rajatmalhotra.finalproject;

import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.rajatmalhotra.R;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ViewExercisesOnDateActivity extends ListActivity {
	/**
	 * The Activity TAG
	 */
	private static final String TAG = "ViewExercisesOnDateActivity";

	/**
	 * The Date for which details are to be displyed
	 */
	private String exerciseDate = null;

	/**
	 * ExerciseDatabase Instance
	 */
	ExerciseDatabase exerciseDb;

	ArrayList<String> workoutEntries = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_exercises_on_date);

		// Init Exercise Date
		exerciseDate = getIntent().getExtras().getString("DATE");

		// Init db
		exerciseDb = ExerciseDatabase.getInstance(this);

		List<WorkoutEntry> weList = exerciseDb.getAllWorkoutEntriesByDate(exerciseDate);

		for (WorkoutEntry we : weList) {
			workoutEntries.add("Exercise Name: " + we.getName() + "\nReps: "
					+ we.getReps() + "\nPounds: " + we.getWeight());

		}

		setListAdapter(new ArrayAdapter<String>(ViewExercisesOnDateActivity.this,
				R.layout.workout_entry_li, workoutEntries));

		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
	}
}
