package com.m2dl.mobebmp.mobe_catapulte;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StartActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        name = findViewById(R.id.editText3);
        Button buttonStart = findViewById(R.id.button_start);
    }

    public void goToGameActivity(View view){
        String nom = name.getText().toString();
        if (nom.length() > 2){
            Utilisateur utilisateur = new Utilisateur(name.toString(), 0);
            databaseReference.child(name.getText().toString()).setValue(utilisateur);
            Intent intent = new Intent(StartActivity.this, GameActivity.class);
            intent.putExtra("nameFromStartActivity", name.getText().toString());
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "Nom trop court. Saisissez au moins 3 charactères", Toast.LENGTH_LONG).show();
        }
    }
}
