
package edu.neu.madcourse.rajatmalhotra.dictionary;

import android.content.Context;
import android.media.MediaPlayer;
// This class is used for playing beep sound when a word is found in the 
// dictionary
public class DictionaryMusic {
   private static MediaPlayer mp = null;

   /** Stop old song and start new one */
   
   public static void play(Context context, int resource) {
         stop(context);
         mp = MediaPlayer.create(context, resource);
         mp.setLooping(false);
         mp.start();
      }
    /** Stop the music */
   public static void stop(Context context) { 
      if (mp != null) {
         mp.stop();
         mp.release();
         mp = null;
      }
   }
}
