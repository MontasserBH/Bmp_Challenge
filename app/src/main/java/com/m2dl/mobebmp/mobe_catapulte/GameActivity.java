package com.m2dl.mobebmp.mobe_catapulte;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private  String score_name;
    private  String start_name;
    private TextView score;
    private TextView name;
    private SensorManager mySensorManager ;
    private final SensorEventListener mySensorEventListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent se) {
            updateAccelParameters(se.values[0], se.values[1], se.values[2]);   // (1)
            if ((!shakeInitiated) && isAccelerationChanged()) {                                      // (2)
                shakeInitiated = true;
            } else if ((shakeInitiated) && isAccelerationChanged()) {                              // (3)
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
    private long startTimeBonus = System.currentTimeMillis();
    private final long timeLimit = 9000L;
    private final long timeLimitBonus = 900L; // Limit to 10 hours
    private CountDownTimer timer;
    private CountDownTimer timerBonus;
    private List<Bonus> bonusList;
    private Context context;

    private TranslateAnimation moveLefttoRight = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 400.0f, Animation.RELATIVE_TO_SELF, -700.0f);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        context = this;
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
                finish = true;
            }
        }.start();

        timerBonus = new CountDownTimer(timeLimitBonus, 61) {
            public void onTick(long r) {
                ImageView flyingman = findViewById(R.id.flyingman);

                for (Bonus bonus : bonusList)
                {
                    ImageView bonusView = findViewById(bonus.getId());
                    if (collision(flyingman, bonusView))
                    {
                        // Ajouter du temps ou augmenter score

                        bonusList.remove(bonus);
                        RelativeLayout imageLayout = findViewById(R.id.imageLayout);
                        imageLayout.removeView(bonusView);
                    }
                }

            }

            public void onFinish() {
                finish = true;
                startTimeBonus = System.currentTimeMillis();
                Random r = new Random();
                int x = 200 + r.nextInt(1500 - 200);
                int y = 200 + r.nextInt(1500 - 200);

                Bonus bonus = new Bonus(x, y, R.drawable.etoile, "life");
                bonusList = new ArrayList<>();
                bonusList.add(bonus);
                RelativeLayout imageLayout = findViewById(R.id.imageLayout);
                ImageView bonusView = new ImageView(context);
                bonusView.setImageResource(bonus.getImage());
                bonusView.setX(bonus.getX());
                bonusView.setY(bonus.getY());
                bonusView.setId(bonus.getId());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(50, 50);
                bonusView.setLayoutParams(layoutParams);
                imageLayout.addView(bonusView);
                this.start();
            }
        }.start();



    }

    public void goToresult(View view){
        Intent scoreActivity = new Intent(this, ScoreActivity.class);
        name = findViewById(R.id.name_text_view_game_activity);
        score = findViewById(R.id.score_text_view_game_activity);
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
                ImageView flyingMan = (ImageView)findViewById(R.id.flyingman);
                TranslateAnimation moveLefttoRightI = new TranslateAnimation(400.0f, 1200.0f, -700.0f, 0.0f);
                moveLefttoRightI.setDuration(2000);
                moveLefttoRightI.setFillAfter(false);
                flyingMan.startAnimation(moveLefttoRightI);
            }
        });

    }

    private boolean collision(View flyingman, View bonus)
    {
        boolean collision = false;
        if (flyingman.getX() >= bonus.getX()-10  && flyingman.getX() <= bonus.getX()-10 && flyingman.getY() >= bonus.getY()-10  && flyingman.getY() <= bonus.getY()-10)
        {
            collision = true;
        }
        return collision;
    }

}
