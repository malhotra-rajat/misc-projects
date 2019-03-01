package edu.neu.madcourse.rajatmalhotra.dictionary;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import edu.neu.madcourse.rajatmalhotra.R;
import edu.neu.madcourse.rajatmalhotra.wordgamemultiplayer.ClearAllKeysTask;


public class Dictionary extends Activity {

	ArrayList<String> words_already_displayed = new ArrayList<String>();
	BufferedReader  br = null;
	HashSet<String> hash_word_list = new HashSet<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dictionary);
		EditText searchTo = (EditText)findViewById(R.id.word_entry);
		
		searchTo.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

				String word_entered = s.toString();
				TextView words_found = (TextView) findViewById(R.id.words_found);
				AssetManager am = getAssets();
				
				if (word_entered.length() >= 2) //copy the file contents into a HashSet when word length reaches 2
				{
					try
					{
						br = new BufferedReader(new InputStreamReader(am.open("dict_files/" + 
								word_entered.charAt(0) + word_entered.charAt(1) + ".txt")));
						
						hash_word_list.clear();
						String word;
						while ((word = br.readLine()) != null)
						{
							hash_word_list.add(word);
						}
					}
					catch (FileNotFoundException fne)
					{
						Log.d("Exception", "e", fne);
					} 
					catch (IOException ioe)
					{
						Log.d("Exception", "e", ioe);
					} 
				}
				if (word_entered.length()>=3 && words_already_displayed.contains(word_entered)==false
						&& hash_word_list.contains(word_entered)) //search if the word entered is in the HashSet or not
				{
					words_found.append(word_entered + "\n");
					DictionaryMusic.play(Dictionary.this, R.raw.dictionary_beep);
					words_already_displayed.add(word_entered);
					
					try {
						br.close();
					} catch (IOException ioe) {
						Log.d("Exception", "e", ioe);
					}
				}
			}});}
	
	public void clearClick(View view) {
		EditText word_text_field = (EditText) findViewById(R.id.word_entry);
		word_text_field.setText("");
		TextView words_found = (TextView) findViewById(R.id.words_found);
		words_found.setText("");
		words_already_displayed.clear();
	}

	public void returnToMenuClick(View view) {
		finish();
	}

	public void acknowledgementsClick(View view) {
		new ClearAllKeysTask().execute(""); //SBR
		Intent i = new Intent(this, Acknowledgements.class);
		startActivity(i);
	}
}
