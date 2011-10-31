package cz.vutbr.fit.graph.sensoranalyzer.sensors;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import cz.vutbr.fit.graph.sensoranalyzer.data.DataWriter;


/**
 * Geolocation from GPS.
 * 
 * @author Petr Nohejl
 */
public class Geolocation 
{
	private LocationListener mListener = null;
	private LocationManager mLocationManager = null;	
	private Location mLocation = null;
	
    private static final float EARTH_RADIUS = 6378137;
    private static final float EARTH_CIRCUMFERENCE = (float) (2 * Math.PI * EARTH_RADIUS);
    public static final float DEGREES_TO_METERS = EARTH_CIRCUMFERENCE / 360;
    public static final float METERS_TO_DEGREES = 360 / EARTH_CIRCUMFERENCE;
    
    private DataWriter mGpsWriter = null; 
    private long mTime = 0;

	
	/**
	 * Constructor of Geolocation.
	 */	
	public Geolocation()
	{	
	}
	
	
	/**
	 * Set location listener.
	 */
	public void start(final Context context, long startTime) 
	{
		mTime = startTime;
		mListener = new LocationListener()
		{			
			// get location
			public void onLocationChanged(Location loc) 
			{
				// set current location
				mLocation = loc;

        	 	if(mGpsWriter!=null) 
    	 		{
        	 		String data = System.currentTimeMillis()-mTime + "," + mLocation.getLatitude() + "," + mLocation.getLongitude();
    	 			mGpsWriter.writeValues(data);
    	 		}
			}

			
			// this is called when the GPS is disabled in settings
			public void onProviderDisabled(String provider) 
			{

			}

			
			public void onProviderEnabled(String provider) 
			{
			
			}

			
			// this is called when the GPS status alters
			public void onStatusChanged(String provider, int status, Bundle extras) 
			{
				switch (status) 
				{
					case LocationProvider.OUT_OF_SERVICE:
						Log.v("SENSORANALYZER", "Status Changed: Out of Service");
						break;
					case LocationProvider.TEMPORARILY_UNAVAILABLE:
						Log.v("SENSORANALYZER", "Status Changed: Temporarily Unavailable");
						break;
					case LocationProvider.AVAILABLE:
						Log.v("SENSORANALYZER", "Status Changed: Available");
						break;
				}
			}
		};		
		
		
		// location manager
		mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		
		// location provider
		String provider = LocationManager.PASSIVE_PROVIDER;
		if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
		{
			provider = LocationManager.GPS_PROVIDER;
			mGpsWriter = new DataWriter(DataWriter.Type.GPS);
		}
			
		// register location listener
		mLocationManager.requestLocationUpdates(provider, 0, 0, mListener);
		Log.d("SENSORANALYZER", "Geolocation registerListener");
	}
	
	
	/**
	 * Unregister location listener.
	 */
	public void stop() 
	{ 
		if(mLocationManager != null && mListener != null)
			mLocationManager.removeUpdates(mListener);
	}
	
	
	public Location getLocation() { return this.mLocation; }
	public LocationManager getManager() { return this.mLocationManager; }
}
