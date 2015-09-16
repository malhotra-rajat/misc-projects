package edu.neu.madcourse.rajatmalhotra.trickiestpart;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static DatabaseHandler sInstance;
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "exerciseManager";

	private static final String TABLE_EXERCISE = "exercise";
	private static final String TABLE_WORKOUT = "workout";

	// Exercise Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";

	// Workout Table Columns names
	private static final String WORKOUT_KEY_ID = "id";
	private static final String WORKOUT_KEY_EXERCISE_NAME = "name";
	private static final String WORKOUT_KEY_REPS = "reps";
	private static final String WORKOUT_KEY_WEIGHT = "weight";

	/*
	 * public static DatabaseHandler getInstance(Context context) {
	 * 
	 * // Use the application context, which will ensure that you // don't
	 * accidentally leak an Activity's context. // See this article for more
	 * information: http://bit.ly/6LRzfx if (sInstance == null) { sInstance =
	 * new DatabaseHandler(context.getApplicationContext()); } return sInstance;
	 * }
	 */

	DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// db.delete(TABLE_EXERCISE, null, null);
		String CREATE_EXERCISE_TABLE = "CREATE TABLE " + TABLE_EXERCISE + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT" + ")";

		String CREATE_WORKOUT_TABLE = "CREATE TABLE " + TABLE_WORKOUT + "("
				+ WORKOUT_KEY_ID + " INTEGER PRIMARY KEY,"
				+ WORKOUT_KEY_EXERCISE_NAME + " TEXT," + WORKOUT_KEY_REPS
				+ " INTEGER," + WORKOUT_KEY_WEIGHT + " INTEGER" + ")";

		db.execSQL(CREATE_EXERCISE_TABLE);
		db.execSQL(CREATE_WORKOUT_TABLE);

		// db.delete(TABLE_EXERCISE, null, null);

		populateExerciseTable(db);

		// db.close();

	}

	void populateExerciseTable(SQLiteDatabase db) {
		Log.d("Insert: ", "Inserting ..");
		ContentValues values = new ContentValues();

		values.put(KEY_NAME, "Bench Press"); // exercise name
		db.insert(TABLE_EXERCISE, null, values);

		values.put(KEY_NAME, "Squats");
		db.insert(TABLE_EXERCISE, null, values);

		values.put(KEY_NAME, "Deadlift");
		db.insert(TABLE_EXERCISE, null, values);

		values.put(KEY_NAME, "Skullcrusher");
		db.insert(TABLE_EXERCISE, null, values);

		values.put(KEY_NAME, "Dumbbell Curl");
		db.insert(TABLE_EXERCISE, null, values);

		// Inserting Row
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKOUT);

		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new exercise

	void addWorkoutEntry(WorkoutEntry wEntry) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(WORKOUT_KEY_EXERCISE_NAME, wEntry.getName());
		values.put(WORKOUT_KEY_REPS, wEntry.getReps());
		values.put(WORKOUT_KEY_WEIGHT, wEntry.getWeight());

		// Inserting Row
		db.insert(TABLE_WORKOUT, null, values);
	}

	void addExercise(Exercise exercise) {

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, exercise.getName()); // exercise name

		// Inserting Row
		db.insert(TABLE_EXERCISE, null, values);
		// db.close(); // Closing database connection
	}

	WorkoutEntry getWorkoutEntry(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_WORKOUT,
				new String[] { WORKOUT_KEY_ID, WORKOUT_KEY_EXERCISE_NAME,
						WORKOUT_KEY_REPS, WORKOUT_KEY_WEIGHT }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		WorkoutEntry wEntry = new WorkoutEntry(Integer.parseInt(cursor
				.getString(0)), cursor.getString(1), Integer.parseInt(cursor
				.getString(2)), Integer.parseInt(cursor.getString(3)));
		// return contact
		// db.close();
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return wEntry;
	}

	/*
	 * // Getting single exercise Exercise getExercise(int id) { SQLiteDatabase
	 * db = this.getReadableDatabase();
	 * 
	 * Cursor cursor = db.query(TABLE_EXERCISE, new String[] { KEY_ID,
	 * KEY_NAME}, KEY_ID + "=?", new String[] { String.valueOf(id) }, null,
	 * null, null, null);
	 * 
	 * if (cursor != null) cursor.moveToFirst();
	 * 
	 * Exercise exercise = new Exercise(Integer.parseInt(cursor.getString(0)),
	 * cursor.getString(1)); // return contact //db.close(); if(cursor != null
	 * && !cursor.isClosed()){ cursor.close(); } return exercise; }
	 */

	public List<WorkoutEntry> getAllWorkoutEntries() {

		List<WorkoutEntry> weList = new ArrayList<WorkoutEntry>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_WORKOUT;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				WorkoutEntry we = new WorkoutEntry();
				we.set_id((Integer.parseInt(cursor.getString(0))));
				we.setName(cursor.getString(1));
				we.setReps((Integer.parseInt(cursor.getString(2))));
				we.setWeight((Integer.parseInt(cursor.getString(3))));

				// Adding Workout Entry to list
				weList.add(we);
			} while (cursor.moveToNext());
		}

		// return contact list
		// db.close();
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		return weList;
	}

	// Getting All exercises
	public List<Exercise> getAllExercises() {

		List<Exercise> exerciseList = new ArrayList<Exercise>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_EXERCISE;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Exercise exercise = new Exercise();
				exercise.setId(Integer.parseInt(cursor.getString(0)));
				exercise.setName(cursor.getString(1));
				// Adding exercise to list
				exerciseList.add(exercise);
			} while (cursor.moveToNext());
		}

		// return contact list
		// db.close();
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		return exerciseList;
	}

	/*
	 * // Updating single exercise public int updateExercise(Exercise exercise)
	 * { SQLiteDatabase db = this.getWritableDatabase();
	 * 
	 * ContentValues values = new ContentValues(); values.put(KEY_NAME,
	 * exercise.getName());
	 * 
	 * 
	 * // updating row int result = db.update(TABLE_EXERCISE, values, KEY_ID +
	 * " = ?", new String[] { String.valueOf(exercise.getId()) }); //db.close();
	 * return result;
	 * 
	 * }
	 */

	// Deleting single exercise
	public void deleteWorkoutEntry(WorkoutEntry we) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_WORKOUT, KEY_ID + " = ?",
				new String[] { String.valueOf(we.get_id()) });
		// db.close();
	}

	// Deleting single exercise
	public void deleteExercise(Exercise exercise) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_EXERCISE, KEY_ID + " = ?",
				new String[] { String.valueOf(exercise.getId()) });
		// db.close();
	}

	/*
	 * // Getting exercise Count public int getExerciseCount() { String
	 * countQuery = "SELECT  * FROM " + TABLE_EXERCISE; SQLiteDatabase db =
	 * this.getReadableDatabase(); Cursor cursor = db.rawQuery(countQuery,
	 * null);
	 * 
	 * // db.close(); // return count int result = cursor.getCount(); if(cursor
	 * != null && !cursor.isClosed()){ cursor.close(); }
	 * 
	 * return result; }
	 */

}
