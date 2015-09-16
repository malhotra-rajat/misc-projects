package edu.neu.madcourse.rajatmalhotra.wordgamecommunication;

public class Communication_Globals
{
    public static final String TAG = "GCM_Globals";
   // public static final String GCM_SENDER_ID = "248102853395";
    public static final String GCM_SENDER_ID = "162593625177";
    public static final String BASE_URL = "https://android.googleapis.com/gcm/send";
    public static final String PREFS_NAME = "GCM_Communication";
    //public static final String GCM_API_KEY = "AIzaSyCh6nwdjr33tm5iJtt9nhnncxDJ_oMTSgQ";
    public static final String GCM_API_KEY = "AIzaSyCK1B4mHfIIVRT67kRxA9djI9kcg-yt4qM";
    public static final int SIMPLE_NOTIFICATION = 22;
    public static final long GCM_TIME_TO_LIVE = 60L * 60L * 24L * 7L * 4L; // 4 Weeks
    public static int mode = 0;
}