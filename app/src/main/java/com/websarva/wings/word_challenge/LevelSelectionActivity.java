package com.websarva.wings.word_challenge;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LevelSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_selection);

        Button buttonDb1 = findViewById(R.id.buttonDb1);
        Button buttonDb2 = findViewById(R.id.buttonDb2);

        buttonDb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LevelSelectionActivity.this, MainActivity.class);
                intent.putExtra("DB_TYPE", 1);
                startActivity(intent);
            }
        });

        buttonDb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LevelSelectionActivity.this, MainActivity.class);
                intent.putExtra("DB_TYPE", 2);
                startActivity(intent);
            }
        });
    }
}
