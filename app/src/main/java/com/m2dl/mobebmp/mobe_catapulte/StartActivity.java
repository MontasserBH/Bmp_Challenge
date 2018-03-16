package com.m2dl.mobebmp.mobe_catapulte;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StartActivity extends AppCompatActivity {

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        final EditText name = findViewById(R.id.editText3);
        Button buttonStart = findViewById(R.id.button_start);

        String nom = name.getText().toString();
        if (nom.isEmpty() || nom.equals(" ")){
            buttonStart.setClickable(false);
        }
        else {
            buttonStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utilisateur utilisateur = new Utilisateur(name.toString(), 0);
                    databaseReference.child(name.getText().toString()).setValue(utilisateur);
                    Intent intent = new Intent(StartActivity.this, GameActivity.class);
                    intent.putExtra("nameFromStartActivity", name.getText().toString());
                    startActivity(intent);
                }
            });
        }
    }
}
