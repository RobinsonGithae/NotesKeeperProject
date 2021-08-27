package com.example.noteskeeper;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.noteskeeper.adapters.RecyclerAdapter;
import com.example.noteskeeper.models.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerAdapter recyclerAdapter;
    private RecyclerView recyclerView;
    private DatabaseReference noteDB;
    private List<Note> noteList = new ArrayList<>();
    private FloatingActionButton actionButton;





    public void showExitAppPrompt(){

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("LOG OFF");
        builder.setMessage("Are you sure you want to get signed out?");
        builder.setPositiveButton("YES!!!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                finish();

            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.create().show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        showExitAppPrompt();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        noteDB = FirebaseDatabase.getInstance().getReference("note");

        recyclerView = findViewById(R.id.noteList);
        actionButton = findViewById(R.id.fab_add_note);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddNoteActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        retrieveNotes();
    }

    private void retrieveNotes(){

        noteList.clear();
        noteDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot notes : dataSnapshot.getChildren()){
                    Note note = notes.getValue(Note.class);
                    noteList.add(note);
                }
                recyclerAdapter = new RecyclerAdapter(getApplicationContext(), noteList);
                recyclerView.setAdapter(recyclerAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, R.string.notes_download_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
