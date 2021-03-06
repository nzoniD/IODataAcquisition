package it.unipi.dii.iodataacquisition;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.Log;

/*This class is responsible to contains the data acquired*/
public class SensorData implements Parcelable
{
	private static final String TAG = SensorData.class.getName();
	private final String sensorName;
	private final int sensorType;
	private final long timestamp;
	private final float value;
	private final Integer accuracy;

	public static final Parcelable.Creator<SensorData> CREATOR
		= new Parcelable.Creator<SensorData>() {
		public SensorData createFromParcel(Parcel source)
		{
			return new SensorData(source);
		}

		public SensorData[] newArray(int size)
		{
			return new SensorData[size];
		}
	};

	private SensorData(String sensorName, int sensorType, long timestamp, float value, Integer accuracy)
	{
		this.sensorName = sensorName;
		this.sensorType = sensorType;
		this.timestamp = timestamp;
		this.value = value;
		this.accuracy = accuracy;
		Log.d(TAG, "New SensorData {sensorName: " + sensorName +
			", sensorType: " + sensorType +
			", timestamp: " + timestamp +
			", value: " + value +
			", accuracy: " + accuracy + "}.");
	}

	public SensorData(String sensorName, long timestamp, float value, Integer accuracy)
	{
		this(sensorName, Sensor.TYPE_ALL, timestamp, value, accuracy);
	}

	public SensorData(String sensorName, long timestamp, float value)
	{
		this(sensorName, timestamp, value, null);
	}

	public SensorData(String sensorName, float value)
	{
		this(sensorName, System.currentTimeMillis(), value);
	}

	public SensorData(Sensor sensor, long timestamp, float value, Integer accuracy)
	{
		this(sensor.getName(), sensor.getType(), timestamp, value, accuracy);
	}

	public SensorData(SensorEvent event)
	{
		this(event.sensor,
			System.currentTimeMillis() - (SystemClock.elapsedRealtimeNanos() - event.timestamp)/(1000*1000),
			event.values[0], event.accuracy);
	}

	@SuppressLint("ParcelClassLoader")
	private SensorData(Parcel source)
	{
		this(source.readString(), source.readInt(), source.readLong(), source.readFloat(), (Integer)source.readValue(null));
	}

	/*-----------------------------------------GETTER-----------------------------------------*/
	public String getSensorName()
	{
		return sensorName;
	}

	public int getSensorType()
	{
		return sensorType;
	}

	public long getTimestamp()
	{
		return timestamp;
	}

	public float getValue()
	{
		return value;
	}
	/*----------------------------------------------------------------------------------------*/

	public String[] toStringArray()
	{
		String[] array = new String[5];
		array[0] = Long.toString(timestamp);
		array[1] = Integer.toString(sensorType);
		array[2] = sensorName;
		array[3] = Float.toString(value);
		array[4] = accuracy != null ? accuracy.toString() : "";
		return array;
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(sensorName);
		dest.writeInt(sensorType);
		dest.writeLong(timestamp);
		dest.writeFloat(value);
		dest.writeValue(accuracy);
	}
}
