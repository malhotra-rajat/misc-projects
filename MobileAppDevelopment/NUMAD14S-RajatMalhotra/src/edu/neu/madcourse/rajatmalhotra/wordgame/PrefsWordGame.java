package edu.neu.madcourse.rajatmalhotra.wordgame;
import edu.neu.madcourse.rajatmalhotra.R;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class PrefsWordGame extends PreferenceActivity {
   // Option names and default values
   private static final String OPT_MUSIC = "music_word_game";
   private static final boolean OPT_MUSIC_DEF = true;

   @SuppressWarnings("deprecation")
@Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      addPreferencesFromResource(R.xml.wordgame_settings);
   }
   
   
   /** Get the current value of the music option */
   
   public static boolean getMusic(Context context) {
      return PreferenceManager.getDefaultSharedPreferences(context)
            .getBoolean(OPT_MUSIC, OPT_MUSIC_DEF);
   }
}
