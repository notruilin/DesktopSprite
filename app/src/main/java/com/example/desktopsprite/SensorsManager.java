/*
 * Project - Desktop Sprite
 * COMP90018 Mobile Computing Systems Programming
 * Author - Yao Wang, Tong He, Dinghao Yong, Jianyu Yan, Ruilin Liu
 * Oct 2019, Semester 2
 */

package com.example.desktopsprite;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/*
 * This class manages the data of sensors
 * Including accelerometer, light, proximity and step counter
 */

import static android.content.Context.SENSOR_SERVICE;

public class SensorsManager implements SensorEventListener {
    final private DesktopSpriteService desktopSpriteService;

    private static SensorsManager instance;
    private SensorManager sensorManager;

    private float light = -1;
    private float proximity = -1;

    // If current too close event has reported
    private boolean tooCloseReport = false;

    private boolean tooDarkReport = false;
    private boolean tooLightfulReport = false;

    private static final long ACCELEROMETER_THRESHOLD = 3;
    private long lastAccelerometerCheckTime;
    private int accelerometerCount;
    private float lastX, lastY, lastZ;

    private int stepCount;

    public static SensorsManager getInstance() {
        return instance;
    }

    public SensorsManager(DesktopSpriteService desktopSpriteService) {
        instance = this;
        this.desktopSpriteService = desktopSpriteService;
        sensorManager = (SensorManager)MainActivity.getInstance().getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    // Get current light sensor data
    public float getLight() {
        return light;
    }

    // Get current proximity sensor data
    public float getProximity() {
        return proximity;
    }

    // Get last accelerometer sensor data
    public float[] getLastAccelerometer() {
        return new float[]{lastX, lastY, lastZ};
    }

    // Get step count since service opened
    public int getStepCounter() {
        return stepCount;
    }

    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_LIGHT:
                detectLight(event);
                break;
            case Sensor.TYPE_PROXIMITY:
                detectProximity(event);
                break;
            case Sensor.TYPE_ACCELEROMETER:
                detectAccelerometer(event);
                break;
            case Sensor.TYPE_STEP_COUNTER:
                detectStepCounter(event);
                break;
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void detectLight(SensorEvent event) {
        light = event.values[0];
        if (light < 100){
            if(!tooDarkReport){
                tooDarkReport = true;
                desktopSpriteService.tooDark(light);
            }
        }
        if (light > 20000){
            if(!tooLightfulReport){
                tooLightfulReport = true;
                desktopSpriteService.tooLightful(light);
            }
        }
        if (light>=100 & light<=20000){
            if(tooDarkReport){
                desktopSpriteService.normalLight(light);
            }
            tooDarkReport = false;
            tooLightfulReport = false;
        }

    }

    private void detectProximity(SensorEvent event) {
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
    }

    private void detectAccelerometer(SensorEvent event) {
        long curTime = System.currentTimeMillis();
        long duration = curTime - lastAccelerometerCheckTime;
        if (duration > 1) {
            float speed = Math.abs(event.values[0] - lastX) + Math.abs(event.values[1] - lastY) + Math.abs(event.values[2] - lastZ) / duration * 1000;
            if (speed > ACCELEROMETER_THRESHOLD) {
                accelerometerCount += 1;
                if (accelerometerCount > 5) {
                    desktopSpriteService.keepShaking(accelerometerCount);
                }
            }
            else {
                accelerometerCount = 0;
            }
            lastX = event.values[0];
            lastY = event.values[1];
            lastZ = event.values[2];
            lastAccelerometerCheckTime = curTime;
        }
    }

    private void detectStepCounter(SensorEvent event) {
        stepCount = (int)event.values[0];
    }

    public void resetLightReminder(){
        tooDarkReport = false;
        tooLightfulReport = false;
    }
}