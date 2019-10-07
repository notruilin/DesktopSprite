package com.example.desktopsprite;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import static android.content.Context.SENSOR_SERVICE;

public class SensorsManager implements SensorEventListener {
    final private DesktopSpriteService desktopSpriteService;

    private static SensorsManager instance;
    private SensorManager sensorManager;

    private float light = -1;
    private float proximity = -1;

    // If current too close event has reported
    private boolean tooCloseReport = false;

    public static SensorsManager getInstance() {
        return instance;
    }

    public SensorsManager(DesktopSpriteService desktopSpriteService) {
        instance = this;
        this.desktopSpriteService = desktopSpriteService;
        sensorManager = (SensorManager)MainActivity.getInstance().getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_LIGHT:
                //Log.w("myApp", "Detecting light is " + event.values[0]);
                light = event.values[0];
                break;
            case Sensor.TYPE_PROXIMITY:
                //Log.w("myApp", "Detecting proximity is " + event.values[0]);
                proximity = event.values[0];
                if (proximity < 3) {
                    // If hasn't reported
                    if (!tooCloseReport) {
                        tooCloseReport = true;
                        desktopSpriteService.tooClose(proximity);
                    }
                }
                else {
                    tooCloseReport = false;
                }
                break;
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    // Get current light sensor data
    public float getLight() {
        return light;
    }


    // Get current proximity sensor data
    public float getProximity() {
        return proximity;
    }

    public void getStepCounter() {

    }
}