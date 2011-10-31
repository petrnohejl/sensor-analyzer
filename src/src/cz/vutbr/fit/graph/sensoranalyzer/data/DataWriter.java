package cz.vutbr.fit.graph.sensoranalyzer.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.os.Environment;


/**
 * Storing data to file.
 * 
 * @author Petr Nohejl
 */
public class DataWriter 
{
	private static String DIR = "/sensors";
	private String PREFIXES[] = { "gps_", "acc_", "mag_", "gyr_" };
	
	public enum Type { GPS, ACCELEROMETER, MAGNETIC, GYROSCOPE };

	private File mFile = null;
	private BufferedWriter mBuffer = null;
	
	
	public DataWriter(DataWriter.Type type)
	{
		// nazev adresare
		File storage = Environment.getExternalStorageDirectory();
		String path = storage.getPath() + DIR;
		
		// vytvoreni adresare
		File directory = new File(path);
		if(!directory.exists()) directory.mkdirs();
		
		// nazev souboru
		String filename = PREFIXES[type.ordinal()] + System.currentTimeMillis() + ".txt";
		
		// vytvor novy soubor
		mFile = new File(path, filename);
		
		// vytvor buffer
		try 
		{
			FileWriter writer = new FileWriter(mFile, true);
			mBuffer = new BufferedWriter(writer);
		} 
		catch (IOException e) 
		{
			mBuffer = null;
			e.printStackTrace();
		}
	}
	
	
	public void writeValues(String data)
	{
		if (mFile.exists() && mBuffer!=null) 
		{
			try 
			{
				mBuffer.write(data);
			    mBuffer.newLine();
			    mBuffer.flush();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	
	public void close()
	{
		if (mBuffer!=null)
		{
			try 
			{
				mBuffer.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		
		mBuffer = null;
		mFile = null;
	}
}
