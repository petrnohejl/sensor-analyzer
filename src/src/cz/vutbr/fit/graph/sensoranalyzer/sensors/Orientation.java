package cz.vutbr.fit.graph.sensoranalyzer.sensors;

import cz.vutbr.fit.graph.sensoranalyzer.data.DataWriter;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


/**
 * Orientation from compass, accelerometer and gyroscope.
 * 
 * @author Petr Nohejl
 */
public class Orientation
{
	private SensorEventListener mListener;
	private SensorManager mSensorManager; 
	private Sensor mSensorAccelerometer;
	private Sensor mSensorMagnetic;
	private Sensor mSensorGyroscope;
	
	private boolean mAccelerometerAvailable = false;
	private boolean mMagneticAvailable = false;
	private boolean mGyroscopeAvailable = false; 
	
	private DataWriter mAccelerometerWriter = null;
	private DataWriter mMagneticWriter = null;
	private DataWriter mGyroscopeWriter = null; 
	
	private long mTime = 0;
	
	public static final double RADIANS_TO_DEGREES = 180 / Math.PI;
	public static final double DEGREES_TO_RADIANS = Math.PI / 180;
	
	
	/**
	 * Constructor of Orientation.
	 */	
	public Orientation() 
	{
	}
	
	
	/**
	 * Set orientation listener.
	 */
	public void start(Context context, long startTime) 
	{
		mTime = startTime;
		mListener = new SensorEventListener() 
        {
        	public void onAccuracyChanged(Sensor sensor, int accuracy) {}       	
        	
        	// compute orientation
        	public void onSensorChanged(SensorEvent event) 
        	{
        	 	Sensor sensor = event.sensor;
        	 	String data = System.currentTimeMillis()-mTime + "," + event.values[0] + "," + event.values[1] + "," + event.values[2];
        	 	
        	 	if(sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        	 	{
        	 		if(mAccelerometerWriter!=null) mAccelerometerWriter.writeValues(data);
        	 	}
        	 	else if(sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
        	 	{
        	 		if(mMagneticWriter!=null) mMagneticWriter.writeValues(data);
        	 	}
        	 	else if(sensor.getType() == Sensor.TYPE_GYROSCOPE)
        	 	{
        	 		if(mGyroscopeWriter!=null) mGyroscopeWriter.writeValues(data);
        	 	}
        	}
        };
   
        // sensor manager
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);       
        mSensorAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorMagnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mSensorGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);       
        
        // register listener
        mAccelerometerAvailable = mSensorManager.registerListener(mListener, mSensorAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        mMagneticAvailable = mSensorManager.registerListener(mListener, mSensorMagnetic, SensorManager.SENSOR_DELAY_FASTEST);
        mGyroscopeAvailable = mSensorManager.registerListener(mListener, mSensorGyroscope, SensorManager.SENSOR_DELAY_FASTEST);     
        
        // vytvor vystupni soubory
        if(mAccelerometerAvailable) mAccelerometerWriter = new DataWriter(DataWriter.Type.ACCELEROMETER);
        if(mMagneticAvailable) mMagneticWriter = new DataWriter(DataWriter.Type.MAGNETIC);
        if(mGyroscopeAvailable) mGyroscopeWriter = new DataWriter(DataWriter.Type.GYROSCOPE);
	}
	
	
	/**
	 * Unregister orientation listener.
	 */
	public void stop() 
	{ 
		if(mSensorManager!=null && mListener!=null)
			mSensorManager.unregisterListener(mListener);
		
		if(mAccelerometerWriter!=null) mAccelerometerWriter.close();
		if(mMagneticWriter!=null) mMagneticWriter.close();
		if(mGyroscopeWriter!=null) mGyroscopeWriter.close();
		
		mAccelerometerWriter = null;
		mMagneticWriter = null;
		mGyroscopeWriter = null;
	}
	
	
	public SensorManager getManager() { return this.mSensorManager; }
	public Boolean getAccelerometerAvailable() { return this.mAccelerometerAvailable; }
	public Boolean getMagneticAvailable() { return this.mMagneticAvailable; }
	public Boolean getGyroscopeAvailable() { return this.mGyroscopeAvailable; }
}
