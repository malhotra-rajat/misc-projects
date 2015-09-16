package edu.neu.madcourse.rajatmalhotra.trickiestpart;

import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.rajatmalhotra.R;
import edu.neu.madcourse.rajatmalhotra.wordgamemultiplayer.TwoPlayerWordGame;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ManualInput extends Activity {

	DatabaseHandler db = new DatabaseHandler(this);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manual_input);

	
	
		ArrayList<String> exNames =  new ArrayList<String>();


		//Log.d("Reading: ", "Reading all exercises..");
		List<Exercise> exercises = db.getAllExercises();      

		for (Exercise ex : exercises) 
		{
			exNames.add(ex.getName());
		}
		//   Log.d("Reading: Name: ", log);}


		//Creating the instance of ArrayAdapter containing list of language names  
		ArrayAdapter<String> adapter = new ArrayAdapter<String>  
		(this,android.R.layout.select_dialog_item,exNames);  

		//Getting the instance of AutoCompleteTextView  
		AutoCompleteTextView actv = 
				(AutoCompleteTextView)findViewById(R.id.exName);  
		actv.setThreshold(1);//will start working from first character  
		actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView  
		//actv.setTextColor(color.white);  

	}
	
	public void addWorkoutEntry(View view) {
		
		
	    String exName = ((AutoCompleteTextView)findViewById(R.id.exName)).getText().toString();
	    String reps = ((EditText)findViewById(R.id.reps)).getText().toString();
	    String weight = ((EditText)findViewById(R.id.weight)).getText().toString();
	    
	    if (exName.equals("") || reps.equals("") || weight.equals(""))
	    {
	    	Toast.makeText(getApplicationContext(), "Please enter information in all the fields", Toast.LENGTH_SHORT);
	    }
	    else
	    {
	    	WorkoutEntry we = new WorkoutEntry(exName, Integer.parseInt(reps), Integer.parseInt(weight));
	    	db.addWorkoutEntry(we);
			Intent intent = new Intent(this, WorkoutEntries.class);
			startActivity(intent);
	    }
	}

}
