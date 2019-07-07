package me.custodio.Veever.manager;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import me.custodio.Veever.Events.UpdateDemoBeaconEvent;
import me.custodio.Veever.GeoDirections;
import me.custodio.Veever.MainActivity;
import me.custodio.Veever.R;

import org.greenrobot.eventbus.EventBus;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by Andrews on 17,May,2019
 */

public class VeeverSensorManager implements SensorEventListener {

    private static final String TAG = "VeeverSensor";
    private static VeeverSensorManager ourInstance;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private float[] mR = new float[9];
    private float[] mOrientation = new float[3];
    private float mCurrentDegree = 0f;

    private GeoDirections geoDirection;
    private GeoDirections lastGeoDirection;

    public MainActivity mainActivity;

    public boolean inDemo = false;

    public static VeeverSensorManager getInstance() {
        return ourInstance;
    }

    private VeeverSensorManager(Context context) {

        mSensorManager = (SensorManager)context.getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

    }

    public static void initialise(Context context) {

        ourInstance = new VeeverSensorManager(context);

    }

    public void register(MainActivity activity) {
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_GAME);
        mainActivity = activity;
    }

    public void unRegister() {
        mSensorManager.unregisterListener(this, mAccelerometer);
        mSensorManager.unregisterListener(this, mMagnetometer);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor == mAccelerometer) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor == mMagnetometer) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(mR, mOrientation);
            float azimuthInRadians = mOrientation[0];
            float azimuthInDegress = (float)(Math.toDegrees(azimuthInRadians)+360)%360;

            // TODO log orientation

            mCurrentDegree = azimuthInDegress;
            // set geo direction and update the activity for new direction info
            setGeoAndDialog();
        }
    }

    private void setGeoAndDialog() {

         lastGeoDirection = geoDirection;

        if (mCurrentDegree >= 337.5 || mCurrentDegree <= 22.5) {
            geoDirection = GeoDirections.NORTH;
        } else if(mCurrentDegree >= 22.5 && mCurrentDegree <= 67.5){
            geoDirection = GeoDirections.NORTH_EAST;
        }else if(mCurrentDegree >= 67.5 && mCurrentDegree <= 112.5){
            geoDirection = GeoDirections.EAST;
        }else if(mCurrentDegree >= 112.5 && mCurrentDegree <= 157.5){
            geoDirection = GeoDirections.SOUTH_EAST;
        }else if(mCurrentDegree >= 157.5 && mCurrentDegree <= 202.5){
            geoDirection = GeoDirections.SOUTH;
        }else if(mCurrentDegree >= 202.5 && mCurrentDegree <= 247.5){
            geoDirection = GeoDirections.SOUTH_WEST;
        }else if(mCurrentDegree >= 247.5 && mCurrentDegree <= 292.5){
            geoDirection = GeoDirections.WEST;
        }else if(mCurrentDegree >= 292.5 && mCurrentDegree <= 337.5){
            geoDirection = GeoDirections.NORTH_WEST;
        }

        if (geoDirection != lastGeoDirection) {
            mainActivity.showDialog();
            if (inDemo) {
                EventBus.getDefault().post(new UpdateDemoBeaconEvent());
            }
            Log.e(TAG, "onSensorChanged: current direction: " + geoDirection.name());
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

    public GeoDirections getGeoDirection() {
        return geoDirection;
    }

    public String getDirectionText(Context context) {

            switch (geoDirection) {
                case NORTH:
                    return context.getString(R.string.direction_north);
                case NORTH_EAST:
                    return context.getString(R.string.direction_northeast);
                case EAST:
                    return context.getString(R.string.direction_east);
                case SOUTH_EAST:
                    return context.getString(R.string.direction_southeast);
                case SOUTH:
                    return context.getString(R.string.direction_south);
                case SOUTH_WEST:
                    return context.getString(R.string.direction_southwest);
                case WEST:
                    return context.getString(R.string.direction_west);
                case NORTH_WEST:
                    return context.getString(R.string.direction_northwest);
                default:
                    return null;

            }
    }

    public void setDemo(boolean isDemo) {
        this.inDemo = isDemo;
    }
}
