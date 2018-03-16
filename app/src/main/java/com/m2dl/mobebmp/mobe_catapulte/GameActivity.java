package com.m2dl.mobebmp.mobe_catapulte;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {
    private  String score_name;
    private  String start_name;
    private TextView score;
    private TextView name;
    private SensorManager mySensorManager ;

    Chronometer simpleChronometer;
    private final SensorEventListener mySensorEventListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent se) {
            updateAccelParameters(se.values[0], se.values[1], se.values[2]);   // (1)
            if ((!shakeInitiated) && isAccelerationChanged()) {                                      // (2)
                shakeInitiated = true;
            }
            else if ((shakeInitiated) && isAccelerationChanged()) {                              // (3)
                executeShakeAction();
            } else if ((shakeInitiated) && (!isAccelerationChanged())) {                           // (4)
                shakeInitiated = false;
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
	    /* can be ignored in this example */
        }
    };
    /* Here we store the current values of acceleration, one for each axis */
    private float xAccel;
    private float yAccel;
    private float zAccel;

    /* And here the previous ones */
    private float xPreviousAccel;
    private float yPreviousAccel;
    private float zPreviousAccel;

    /* Used to suppress the first shaking */
    private boolean firstUpdate = true;

    /*What acceleration difference would we assume as a rapid movement? */
    private final float shakeThreshold = 5.0f;

    /* Has a shaking motion been started (one direction) */
    private boolean shakeInitiated = false;

    private int nbShake;

    private boolean finish = false;
    private long startTime = System.currentTimeMillis();
    private final long timeLimit = 9000L; // Limit to 10 hours
    private CountDownTimer timer;

    private TranslateAnimation moveLefttoRight = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 400.0f, Animation.RELATIVE_TO_SELF, -700.0f);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        if(getIntent() != null && getIntent().getExtras() != null){
            score_name = getIntent().getExtras().getString("nameFromScoreActivity");
            start_name = getIntent().getExtras().getString("nameFromStartActivity");
            if(score_name != null){
                name = findViewById(R.id.name_text_view_game_activity);
                name.setText(score_name);

            }else{
                name = findViewById(R.id.name_text_view_game_activity);
                name.setText(start_name);
            }
        }

        nbShake = 0;
        simpleChronometer = (Chronometer) findViewById(R.id.chronometer10);
        score = findViewById(R.id.score_text_view_game_activity);
        mySensorManager  = (SensorManager) getSystemService(SENSOR_SERVICE);

        mySensorManager.registerListener(mySensorEventListener, mySensorManager
                        .getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

        timer = new CountDownTimer(timeLimit, 61) {

            public void onTick(long r) {
                // Running
                long millis = System.currentTimeMillis() - startTime;
                String millisStr = String.valueOf(millis);

                // Display
                if (millisStr.length() > 3) {
                    String time = String.valueOf(millis / 1000) + "." + millisStr.substring(millisStr.length() - 2);
                }
            }

            public void onFinish() {

                simpleChronometer.stop();
                finish = true;
                startTime = System.currentTimeMillis();
                ImageView flyingMan = (ImageView)findViewById(R.id.flyingman);
                moveLefttoRight.setDuration(4000);
                moveLefttoRight.setFillAfter(true);
                flyingMan.startAnimation(moveLefttoRight);
                moveLefttoRight.setAnimationListener(new Animation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {}

                    @Override
                    public void onAnimationRepeat(Animation animation) {}

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        float distance = nbShake * 200;
                        ImageView flyingMan = (ImageView)findViewById(R.id.flyingman);
                        TranslateAnimation moveLefttoRightI = new TranslateAnimation(400.0f, (distance), -700.0f, 0.0f);
                        moveLefttoRightI.setDuration(6000);
                        moveLefttoRightI.setFillAfter(false);
                        flyingMan.startAnimation(moveLefttoRightI);
                        float scoreI = distance;
                        score = (TextView)findViewById(R.id.score_text_view_game_activity);
                        score.setText(""+distance);
                        moveLefttoRightI.setAnimationListener(new Animation.AnimationListener() {

                            @Override
                            public void onAnimationStart(Animation animation) {}

                            @Override
                            public void onAnimationRepeat(Animation animation) {}

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                Intent scoreActivityI = new Intent(GameActivity.this, ScoreActivity.class);
                                name = findViewById(R.id.name_text_view_game_activity);
                                score = findViewById(R.id.score_text_view_game_activity);
                                scoreActivityI.putExtra("nameFromGameActivity", name.getText());
                                scoreActivityI.putExtra("scoreFomGameActivity", score.getText());
                                startActivity(scoreActivityI);
                            }});

                    }
                });


            }
        };

    }

    public void goToresult(View view){
        Intent scoreActivity = new Intent(this, ScoreActivity.class);
        TextView name = (TextView) findViewById(R.id.name_text_view_game_activity);
        TextView score = (TextView) findViewById(R.id.score_text_view_game_activity);
        scoreActivity.putExtra("nameFromGameActivity", name.getText());
        scoreActivity.putExtra("scoreFomGameActivity", score.getText());
        startActivity(scoreActivity);
    }

    public void goToHome(View view){
        Intent starteActivityI = new Intent(this, StartActivity.class);
        startActivity(starteActivityI);
    }

    private void updateAccelParameters(float xNewAccel, float yNewAccel,
                                       float zNewAccel) {
                /* we have to suppress the first change of acceleration, it results from first values being initialized with 0 */
        if (firstUpdate) {
            xPreviousAccel = xNewAccel;
            yPreviousAccel = yNewAccel;
            zPreviousAccel = zNewAccel;
            firstUpdate = false;
        } else {
            xPreviousAccel = xAccel;
            yPreviousAccel = yAccel;
            zPreviousAccel = zAccel;
        }
        xAccel = xNewAccel;
        yAccel = yNewAccel;
        zAccel = zNewAccel;
    }

    private boolean isAccelerationChanged() {
        float deltaX = Math.abs(xPreviousAccel - xAccel);
        float deltaY = Math.abs(yPreviousAccel - yAccel);
        float deltaZ = Math.abs(zPreviousAccel - zAccel);
        return (deltaX > shakeThreshold && deltaY > shakeThreshold)
                || (deltaX > shakeThreshold && deltaZ > shakeThreshold)
                || (deltaY > shakeThreshold && deltaZ > shakeThreshold);
    }

    private void executeShakeAction() {
        if(!finish)
        {
            nbShake ++;
        }
    }

    public void catapult(View view){
        nbShake = 0;
        simpleChronometer.setBase(SystemClock.elapsedRealtime());
        simpleChronometer.start();
        timer.start();




    }

}
