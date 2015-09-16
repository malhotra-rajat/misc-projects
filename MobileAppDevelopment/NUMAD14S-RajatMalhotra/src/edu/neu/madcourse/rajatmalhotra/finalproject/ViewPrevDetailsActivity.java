package edu.neu.madcourse.rajatmalhotra.finalproject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import edu.neu.madcourse.rajatmalhotra.R;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ViewPrevDetailsActivity extends Activity {

	/**
	 * The TAG
	 */
	private static final String TAG = "ViewPrevDetailsActivity";

	/**
	 * The Instance of ExerciseDatabase
	 */
	ExerciseDatabase exDb;

	/**
	 * The Dates Cursor
	 */
	Cursor dateCursor;

	/**
	 * The List of All Dates
	 */
	ArrayList<String> datesList;

	/**
	 * The ListView for dates
	 */
	ListView lvDates;

	/**
	 * Calender Instance
	 */
	Calendar calender;

	/**
	 * HashMap of days of the week
	 */
	HashMap<Integer, String> dayOfWeek = new HashMap<Integer, String>();

	String dates[];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_prev_details);

		dayOfWeek.put(Calendar.SUNDAY, "Sunday");
		dayOfWeek.put(Calendar.MONDAY, "Monday");
		dayOfWeek.put(Calendar.TUESDAY, "Tuesday");
		dayOfWeek.put(Calendar.WEDNESDAY, "Wednesday");
		dayOfWeek.put(Calendar.THURSDAY, "Thursday");
		dayOfWeek.put(Calendar.FRIDAY, "Friday");
		dayOfWeek.put(Calendar.SATURDAY, "Saturday");

		// Init DB
		exDb = ExerciseDatabase.getInstance(getApplicationContext());

		// Init ArrayList of Dates
		datesList = new ArrayList<String>();

		// Init ListView
		lvDates = (ListView) findViewById(R.id.lvDates);

		// Init Calender
		calender = Calendar.getInstance();

		dateCursor = exDb.getAllDates();

		dateCursor.moveToFirst();

		String dateColumName = exDb.getDateColumnName();
		int count = dateCursor.getCount();
		dates = new String[count];
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		Date date = null;

		for (int i = 0; i < count; i++) {
			try {
				date = sdf.parse(dateCursor.getString(dateCursor
						.getColumnIndex(dateColumName)));
			} catch (ParseException e) {
				Log.d(TAG, "Error in parsing date");
			}

			calender.setTime(date);

			Integer dow = new Integer(calender.get(Calendar.DAY_OF_WEEK));

			// TODO
			Log.d(TAG, "Day of week: " + dow);

			dates[i] = dayOfWeek.get(dow)
					+ ", "
					+ dateCursor.getString(dateCursor
							.getColumnIndex(dateColumName));
			dateCursor.moveToNext();
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, dates);

		lvDates.setAdapter(adapter);

		lvDates.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// The selected item
				String selectedDate = dates[position];
				String parts[] = selectedDate.split(", ");
				String dow = parts[0];
				String dateStr = parts[1];
				
				displayExercisesOnDate(dateStr);
			}
		});
	}
	
	private void displayExercisesOnDate(String dateStr) {
		//TODO
		Intent viewExercisesIntent = new Intent(this, ViewExercisesOnDateActivity.class);
		viewExercisesIntent.putExtra("DATE", dateStr);
		startActivity(viewExercisesIntent);
	}
	
	public void handleExitClick(View view) {
		finish();
	}
}
