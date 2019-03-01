package edu.neu.madcourse.rajatmalhotra.wordgamemultiplayer;

import java.util.ArrayList;
import java.util.Random;

import edu.neu.madcourse.rajatmalhotra.R;
import edu.neu.madcourse.rajatmalhotra.wordgamemultiplayer.ShakeDetector.OnShakeListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class SensorDetect extends Activity 

{

    private ShakeDetector mShakeDetector;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    Animation animShake;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_detect);
        animShake = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.wordgame_shake);     
        TextView myText = (TextView)findViewById(R.id.textViewShake);
        myText.startAnimation(animShake);
        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector(new OnShakeListener() {
            @Override
            public void onShake() {
            	//Toast.makeText(getApplicationContext(), "Nice shaking!", Toast.LENGTH_SHORT).show();
            	startRandomActivity();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }
    
    protected void startRandomActivity()
    {
    	ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
    	classes.add(RealTimePlayLogin.class);
    	classes.add(TurnBasedPlayMain.class);
    	Random r = new Random();
		int randomNumber = r.nextInt(2);
		//new ClearAllKeysTask().execute("");
    	Intent i = new Intent(this, classes.get(randomNumber));
    	startActivity(i);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }   
}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

