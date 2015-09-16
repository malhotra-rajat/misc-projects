package edu.neu.madcourse.rajatmalhotra.finalproject;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class ExerciseDatabase {
	/**
	 * The Database TAG
	 */
	private static final String TAG = "ExerciseDatabase";

	/**
	 * The Database Version
	 */
	private static final int DATABASE_VERSION = 16;

	/**
	 * The Database Name
	 */
	private static final String DATABASE_NAME = "ExerciseDatabase";

	/**
	 * The Database Tables
	 */
	private static final String TABLE_WORKOUT = "workout";

	/**
	 * The Database Columns
	 */
	public static final String COLUMN_EXERCISE = "name";
	public static final String COLUMN_REPS = "reps";
	public static final String COLUMN_WEIGHTS = "weight";
	public static final String COLUMN_DATE = "date";
	/**
	 * The singleton database instance
	 */
	private static ExerciseDatabase instance;

	private DatabaseHelper databaseHelper;
	private SQLiteDatabase database;

	private ExerciseDatabase(Context context) {
		databaseHelper = new DatabaseHelper(context.getApplicationContext());
		database = databaseHelper.getWritableDatabase();
	}

	public static synchronized ExerciseDatabase getInstance(Context context) {
		if (instance == null) {
			instance = new ExerciseDatabase(context.getApplicationContext());
		}

		return instance;
	}

	public String getDateColumnName() {
		return COLUMN_DATE;
	}

	public String getExerciseColumnName() {
		return COLUMN_EXERCISE;
	}

	public String getRepsColumnName() {
		return COLUMN_REPS;
	}

	public String getWightsColumnName() {
		return COLUMN_WEIGHTS;
	}

	public boolean isWorkoutsTableEmpty() {
		Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_WORKOUT,
				null);
		boolean isEmpty = (cursor.getCount() == 0);
		cursor.close();
		return isEmpty;
	}


	public void close() {
		synchronized (ExerciseDatabase.class) {
			databaseHelper.close();
			instance = null;
			database = null;
		}
	}

	public long insertExercise(String date, String exercise, int reps,
			int weights) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(COLUMN_DATE, date);
		contentValues.put(COLUMN_EXERCISE, exercise);
		contentValues.put(COLUMN_REPS, reps);
		contentValues.put(COLUMN_WEIGHTS, weights);
		return database.insert(TABLE_WORKOUT, null, contentValues);
	}

	public Cursor getAllExercises() {
		Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_WORKOUT,
				null);
		return cursor;
	}

	public Cursor getAllDates() {
		Cursor cursor = database.rawQuery("SELECT DISTINCT " + COLUMN_DATE
				+ " FROM " + TABLE_WORKOUT + " ORDER BY " + COLUMN_DATE
				+ " DESC", null);
		return cursor;
	}

	public void clean(Context context) {
		databaseHelper.dropTables(database);
		databaseHelper.createTables(database);
		instance = new ExerciseDatabase(context.getApplicationContext());
	}

	private static final class DatabaseHelper extends SQLiteOpenHelper {
		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			createTables(db);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			dropTables(db);
			createTables(db);
		}

		public void dropTables(SQLiteDatabase db) {
			// Drop Exercise Table
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKOUT + ";");
		}

		public void createTables(SQLiteDatabase db) {
			try {
				db.execSQL("CREATE VIRTUAL TABLE " + TABLE_WORKOUT
						+ " USING fts3(tokenize=porter," + BaseColumns._ID
						+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_DATE
						+ " TEXT, " + COLUMN_EXERCISE + " TEXT, " + COLUMN_REPS
						+ " INTEGER, " + COLUMN_WEIGHTS + " INTEGER);");
			} catch (SQLException e) {
				Log.d(TAG, e.getMessage());
			}
		}
	}

	public List<WorkoutEntry> getAllWorkoutEntriesByDate(String exerciseDate) {
		List<WorkoutEntry> weList = new ArrayList<WorkoutEntry>();
		// Select All Query
		String selectQuery = "SELECT * FROM " + TABLE_WORKOUT + " WHERE "
				+ COLUMN_DATE + " = \"" + exerciseDate + "\";";

		Log.d(TAG, selectQuery);

		// String selectQuery = "SELECT * FROM " + TABLE_WORKOUT;
		Cursor cursor = database.rawQuery(selectQuery, null);

		Log.d(TAG, Integer.toString(cursor.getCount()));

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				WorkoutEntry we = new WorkoutEntry();

				we.setName(cursor.getString(2));
				we.setReps((Integer.parseInt(cursor.getString(3))));
				we.setWeight((Integer.parseInt(cursor.getString(4))));

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

	public int increaseInWeightsForExercise(String exercise, int weights) {
		Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_WORKOUT
				+ " WHERE " + COLUMN_EXERCISE + " = \"" + exercise + "\" ORDER BY " + COLUMN_WEIGHTS + " DESC;", null);
		boolean isEmpty = (cursor.getCount() == 0);
		
		if(true == isEmpty) {
			return -1;
		}
		else {
			cursor.moveToFirst();
			int maxWt = Integer.parseInt(cursor.getString(4));
			
			if(maxWt < weights) {
				return weights - maxWt;
			}
			else {
				return -1;
			}
		}		
	}
}
