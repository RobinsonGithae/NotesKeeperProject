package com.example.noteskeeper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class NoteActivity extends AppCompatActivity {
    private TextView txv_title, txv_content, txv_date, txv_category;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        txv_title = findViewById(R.id.txv_note_title);
        txv_content = findViewById(R.id.txv_note_content);
        txv_category = findViewById(R.id.txv_note_category);
        txv_date = findViewById(R.id.txv_note_date);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        String date = intent.getStringExtra("date");
        String category = intent.getStringExtra("category");

        txv_title.setText(title);
        txv_content.setText(content);
        txv_date.setText(date);
        txv_category.setText(category);


    }
}
