package com.m2dl.mobebmp.mobe_catapulte;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {
    private  String score_name;
    private  String start_name;
    private TextView score;
    private TextView name;
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
}
