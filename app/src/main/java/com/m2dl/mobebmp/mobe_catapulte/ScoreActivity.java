package com.m2dl.mobebmp.mobe_catapulte;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ScoreActivity extends AppCompatActivity{

    Button home;
    Button replay;
    TextView scoreTextView;
    TextView nameTextView;
    String scoreValue;
    String nameValue;

    private DatabaseReference databaseScores;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        nameTextView = (TextView) findViewById(R.id.scoreUserName);
        scoreTextView = (TextView) findViewById(R.id.scoreUserScore);
        home = (Button) findViewById(R.id.boutonHome);
        replay = (Button) findViewById(R.id.boutonReplay);

        databaseScores = FirebaseDatabase.getInstance().getReferenceFromUrl("https://challenge-projet.firebaseio.com/");

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

        ScoreAdatper adapter = new ScoreAdatper(getApplicationContext(), R.layout.activity_score, populateScores());
        ListView listeRecords = (ListView) findViewById(R.id.scoreListeRecords);
        listeRecords.setAdapter(adapter);
    }

    public void goToHome(View v)
    {
        Intent intentStart = new Intent(ScoreActivity.this, StartActivity.class);
        startActivity(intentStart);
    }

    public void goToGame(View v)
    {
        Intent intentGame = new Intent(ScoreActivity.this, GameActivity.class);
        intentGame.putExtra("nameFromScoreActivity", nameValue);
        startActivity(intentGame);
    }

    public LinkedList<Score> populateScores()
    {
        final LinkedList<Score> scoresList = new LinkedList<>();
        databaseScores.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                        Score score = new Score(((Map) ds.getValue()).get("nom").toString(), ((Long) ((Map) ds.getValue()).get("score")));
                        scoresList.add(score);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return scoresList;
    }

}
