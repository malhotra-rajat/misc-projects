package edu.neu.madcourse.rajatmalhotra.trickiestpart;

import java.util.ArrayList;
import java.util.List;
import edu.neu.madcourse.rajatmalhotra.R;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class WorkoutEntries extends ListActivity {

	
	ArrayList<String> workoutEntries = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_workout_entries);
		
		
		List<WorkoutEntry> weList = new DatabaseHandler(this).getAllWorkoutEntries();
		

		for (WorkoutEntry we : weList) 
		{
			workoutEntries.add("Exercise Name: " + we.getName() + "\nReps: " + we.getReps() + "\nWeight: " + we.getWeight());
			
		}
		
		setListAdapter(new ArrayAdapter<String>(WorkoutEntries.this, R.layout.workout_entry_li, workoutEntries));
		 
		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
	}
	
	

}




