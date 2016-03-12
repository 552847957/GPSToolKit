package com.srmn.xwork.androidlib.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class MyOrientationListener implements SensorEventListener {

    private static final String TAG = "sensor";
    float[] accelerometerValues = new float[3];
    float[] magneticFieldValues = new float[3];
    final SensorEventListener myListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent sensorEvent) {

            if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
                magneticFieldValues = sensorEvent.values;
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                accelerometerValues = sensorEvent.values;
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    private Context context;
    private SensorManager sm;
    //需要两个Sensor
    private Sensor aSensor;
    private Sensor mSensor;
    private float lastX;
    private OnOrientationListener onOrientationListener;

    public MyOrientationListener(Context context) {
        Log.i(TAG, "初始化方向传感器");
        this.context = context;
        sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        aSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensor = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    // 开始
    public void start() {
        Log.i(TAG, "开始方向传感器");

        sm.registerListener(myListener, aSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(myListener, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }


    // 停止检测
    public void stop() {
        Log.i(TAG, "停止方向传感器");
        sm.unregisterListener(myListener);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.i(TAG, "捕获传感器事件");
        // 这里我们可以得到数据，然后根据需要来处理
        float x = calculateOrientation();

        if (Math.abs(x - lastX) > 1.0) {
            onOrientationListener.onOrientationChanged(x);
        }

        lastX = x;
    }

    private float calculateOrientation() {
        float[] values = new float[3];
        float[] R = new float[9];
        SensorManager.getRotationMatrix(R, null, accelerometerValues, magneticFieldValues);
        SensorManager.getOrientation(R, values);

        // 要经过一次数据格式的转换，转换为度
        values[0] = (float) Math.toDegrees(values[0]);

        return values[0];
    }

    public void setOnOrientationListener(OnOrientationListener onOrientationListener) {
        this.onOrientationListener = onOrientationListener;
    }


    public interface OnOrientationListener {
        void onOrientationChanged(float x);
    }

}

