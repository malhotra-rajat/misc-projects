package edu.neu.madcourse.rajatmalhotra.trickiestpart;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class FtsIndexedExerciseDatabase {
	private static final String TAG = "FtsIndexedExerciseDatabase";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "ExerciseDatabaseFts";
	private static final String TABLE_EXERCISE = "exerciseList";

	public static final String COLUMN_EXERCISE = "exercise";
	public static final String COLUMN_REPS = "reps";
	public static final String COLUMN_WEIGHTS = "weights";

	private static FtsIndexedExerciseDatabase instance;

	private DatabaseHelper databaseHelper;
	private SQLiteDatabase database;

	private FtsIndexedExerciseDatabase(Context context) {
		databaseHelper = new DatabaseHelper(context.getApplicationContext());
		database = databaseHelper.getWritableDatabase();
	}

	public static synchronized FtsIndexedExerciseDatabase getInstance(
			Context context) {
		if (instance == null) {
			instance = new FtsIndexedExerciseDatabase(
					context.getApplicationContext());
		}

		return instance;
	}

	public boolean isEmpty() {
		Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_EXERCISE,
				null);
		boolean isEmpty = (cursor.getCount() == 0);
		cursor.close();
		return isEmpty;
	}

	public void loadFrom(InputStream csvFile) throws IOException {
		BufferedReader is = new BufferedReader(new InputStreamReader(csvFile,
				"UTF8"));
		String line;

		line = is.readLine();
		while (line != null) {
			String[] parts = line.split(",");
			String exercise = parts[0];
			// float reps = Float.valueOf(parts[1]);
			// float weights = Float.valueOf(parts[2]);
			insertExercise(exercise);
			Log.d(TAG, "inserted: " + exercise);
			line = is.readLine();
		}
	}

	private long insertExercise(String exercise) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(COLUMN_EXERCISE, exercise);
		return database.insert(TABLE_EXERCISE, null, contentValues);
	}

	public void close() {
		synchronized (FtsIndexedExerciseDatabase.class) {
			databaseHelper.close();
			instance = null;
			database = null;
		}
	}

	public long insertExercise(String exercise, float reps, float weights) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(COLUMN_EXERCISE, exercise);
		contentValues.put(COLUMN_REPS, reps);
		contentValues.put(COLUMN_WEIGHTS, weights);
		return database.insert(TABLE_EXERCISE, null, contentValues);
	}

	public Cursor getAllExercises() {
		Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_EXERCISE,
				null);
		return cursor;
	}

	public void clean(Context context) {
		databaseHelper.dropTables(database);
		databaseHelper.createTables(database);
		instance = new FtsIndexedExerciseDatabase(
				context.getApplicationContext());
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
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISE + ";");
		}

		public void createTables(SQLiteDatabase db) {
			db.execSQL("CREATE VIRTUAL TABLE " + TABLE_EXERCISE
					+ " USING fts3(tokenize=porter," + BaseColumns._ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_EXERCISE
					+ " TEXT, " + COLUMN_REPS + " REAL, " + COLUMN_WEIGHTS
					+ " REAL);");
		}
	}
}
