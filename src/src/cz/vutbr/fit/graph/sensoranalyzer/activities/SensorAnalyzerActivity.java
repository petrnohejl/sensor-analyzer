package cz.vutbr.fit.graph.sensoranalyzer.activities;

import android.app.Activity;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import cz.vutbr.fit.graph.sensoranalyzer.R;
import cz.vutbr.fit.graph.sensoranalyzer.sensors.Geolocation;
import cz.vutbr.fit.graph.sensoranalyzer.sensors.Orientation;


/**
 * Main Activity.
 * 
 * @author Petr Nohejl
 */
public class SensorAnalyzerActivity extends Activity 
{
	private boolean mRunning = false;
	private Geolocation mGeolocation = null;
	private Orientation mOrientation = null;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setButton();
        
        // sensors init
        mGeolocation = new Geolocation();
        mOrientation = new Orientation();
    }
	
    
    @Override
    public void onPause()
    {
		super.onPause();
		
		final Button button = (Button) findViewById(R.id.button_start);
		button.setText(R.string.button_start);
		
		if(mGeolocation!=null) mGeolocation.stop();
		if(mOrientation!=null) mOrientation.stop();
		mRunning = false;
		checkSensors();
    }
    
    
    private void setButton()
    {
    	final Button button = (Button) findViewById(R.id.button_start);
        button.setOnClickListener(new View.OnClickListener() 
        {        	
            public void onClick(View v) 
            {        
            	// zastav mereni
            	if(mRunning)
            	{
            		button.setText(R.string.button_start);
            		mGeolocation.stop();
            		mOrientation.stop();
            	}
            	
            	// zapni mereni
            	else
            	{
            		button.setText(R.string.button_stop);
            		long startTime = System.currentTimeMillis();
            		mGeolocation.start(SensorAnalyzerActivity.this, startTime);
            		mOrientation.start(SensorAnalyzerActivity.this, startTime);
            	}
            	
            	mRunning = !mRunning;
            	checkSensors();
            }
        });
    }
    
    
    private void checkSensors()
    {
    	final TextView textLocation = (TextView) findViewById(R.id.text_location);
    	final TextView textOrientation = (TextView) findViewById(R.id.text_orientation);
    	
    	if(mRunning)
    	{
    		boolean gpsAvailable = mGeolocation.getManager().isProviderEnabled(LocationManager.GPS_PROVIDER);
    		textLocation.setText("GPS available: " + gpsAvailable + "\n");
    		
    		textLocation.setText(textLocation.getText() + "Accelerometer available: " + mOrientation.getAccelerometerAvailable() + "\n");
    		textLocation.setText(textLocation.getText() + "Magnetic sensor available: " + mOrientation.getMagneticAvailable() + "\n");
    		textLocation.setText(textLocation.getText() + "Gyroscope available: " + mOrientation.getGyroscopeAvailable() + "\n");
    	}
    	else
    	{
    		textLocation.setText("");
    		textOrientation.setText("");
    	}
    }
}
