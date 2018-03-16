package com.m2dl.mobebmp.mobe_catapulte;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity{

    Button home;
    Button replay;
    TextView scoreTextView;
    TextView nameTextView;
    String scoreValue;
    String nameValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        nameTextView = (TextView) findViewById(R.id.scoreUserName);
        scoreTextView = (TextView) findViewById(R.id.scoreUserScore);
        home = (Button) findViewById(R.id.boutonHome);
        replay = (Button) findViewById(R.id.boutonReplay);

        if (getIntent() != null && getIntent().getExtras() != null)
        {
            nameValue = getIntent().getExtras().getString("nameFromGameActivity");
            scoreValue = getIntent().getExtras().getString("scoreFromGameActivity");
        }
        if (nameValue != null && scoreValue != null)
        {
            nameTextView.setText(nameValue);
            scoreTextView.setText(scoreValue);
        }

        home.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentStart = new Intent(ScoreActivity.this, StartActivity.class);
                intentStart.putExtra("nameFromScoreActivity", nameValue);
                startActivity(intentStart);
            }
        });
        replay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentGame = new Intent(ScoreActivity.this, GameActivity.class);
                intentGame.putExtra("nameFromScoreActivity", nameValue);
                startActivity(intentGame);
            }
        });
    }

}
