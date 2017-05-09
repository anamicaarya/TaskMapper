package com.comp3617.assignment2.anamicakartik;

/**
 * Created by Kartik on 16-07-21.
 */
public class CommonConstants {

    public CommonConstants() {

        // don't allow the class to be instantiated
    }

    // Milliseconds in the snooze duration, which translates
    // to 20 seconds.
    public static final int SNOOZE_DURATION = 20000;
    public static final int DEFAULT_TIMER_DURATION = 10000;
    public static final String ACTION_SNOOZE = "com.comp3617.assignment2.anamicakartik.ACTION_SNOOZE";
    public static final String ACTION_DISMISS = "com.comp3617.assignment2.anamicakartik.ACTION_DISMISS";
    public static final String ACTION_PING = "com.comp3617.assignment2.anamicakartik.ACTION_PING";
    public static final String EXTRA_MESSAGE= "com.comp3617.assignment2.anamicakartik.EXTRA_MESSAGE";
    public static final String EXTRA_TIMER = "com.comp3617.assignment2.anamicakartik.EXTRA_TIMER";
    public static final int NOTIFICATION_ID = 001;
    public static final String DEBUG_TAG = "Remind me";

    // Location updates intervals in sec
    public static int UPDATE_INTERVAL = 10000; // 10 sec
    public static int FASTEST_INTERVAL = 5000; // 5 sec
    public static int DISPLACEMENT = 10; // 10 meters
    public static final int LOCATION_REQUEST_PERMISSION = 101;
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
}
