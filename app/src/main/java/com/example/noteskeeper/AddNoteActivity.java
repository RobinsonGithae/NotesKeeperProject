package com.example.noteskeeper;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.noteskeeper.adapters.CategoryAdapter;
import com.example.noteskeeper.models.Category;
import com.example.noteskeeper.models.Note;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class AddNoteActivity extends AppCompatActivity {
    private EditText edt_title, edt_content;
    private Spinner spinner_category;
    private ImageButton imgb_add_category;
    private Button btn_save, btn_cancel;
    private ArrayList<String> categoryList = new ArrayList<>();
    private CategoryAdapter adapter;
    private DatabaseReference noteDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);



        edt_title = findViewById(R.id.edt_note_title);
        edt_content = findViewById(R.id.edt_note);
        spinner_category = findViewById(R.id.spinner_category);
        imgb_add_category = findViewById(R.id.imgb_add_category);
        btn_save = findViewById(R.id.btn_save);
        btn_cancel = findViewById(R.id.btn_cancel);

        noteDB = FirebaseDatabase.getInstance().getReference("note");

        btn_save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });

        imgb_add_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog customDialog = new Dialog(AddNoteActivity.this);
                customDialog.setCancelable(false);
                customDialog.setContentView(R.layout.add_new_category);

                final EditText category_name = customDialog.findViewById(R.id.edt_category_name);
                Button btn_save_category = customDialog.findViewById(R.id.btn_category_save);
                Button btn_cancel_category = customDialog.findViewById(R.id.btn_category_cancel);

                btn_save_category.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveCategory(category_name.getText().toString().trim());
                        customDialog.dismiss();
                    }
                });

                btn_cancel_category.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.dismiss();
                    }
                });

                customDialog.show();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddNoteActivity.this);
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setCancelable(false);
                builder.setTitle(R.string.exit);
                builder.setMessage(R.string.want_to_leave);

                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                });

                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private String getCurrentDate(){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date calendar = Calendar.getInstance().getTime();
        return simpleDateFormat.format(calendar);
    }

    private void saveCategory(String name){
        noteDB = FirebaseDatabase.getInstance().getReference("category");

        if(!TextUtils.isEmpty(name)){
            String id = noteDB.push().getKey();
            Category category = new Category(id, name);
            noteDB.child(id).setValue(category);
            Toast.makeText(AddNoteActivity.this, R.string.category_saved, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(AddNoteActivity.this, R.string.fill_fields, Toast.LENGTH_SHORT).show();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void saveNote(){
        noteDB = FirebaseDatabase.getInstance().getReference("note");

        String title = edt_title.getText().toString().trim();
        String content = edt_content.getText().toString().trim();
        int position = spinner_category.getSelectedItemPosition();
        String selected_category = spinner_category.getItemAtPosition(position).toString();

        Log.d("TEST_SPINNER", "Spinner category value = " + selected_category);

        if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content)){

            String id = noteDB.push().getKey();
            Note note = new Note(id, title, content, getCurrentDate(), selected_category);
            Log.d("TEST_SPINNER", "Spinner category value = " + selected_category);
            noteDB.child(id).setValue(note);
            Toast.makeText(AddNoteActivity.this, R.string.note_saved, Toast.LENGTH_SHORT).show();
            finish();

        }else{
            Toast.makeText(AddNoteActivity.this, R.string.fill_fields, Toast.LENGTH_SHORT).show();
        }
    }


    private void loadCategory(){
        noteDB = FirebaseDatabase.getInstance().getReference("category");

        noteDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(categoryList.size() > 0){
                    categoryList.clear();
                }

                int i = 0;
                for (DataSnapshot categorySnapShot : dataSnapshot.getChildren()){
                    Category category = categorySnapShot.getValue(Category.class);
                    categoryList.add(category.getName());
                    Log.d("TEST_SPINNER", category.getId() + " " + category.getName() + " " + categoryList.get(i));
                    i = i + 1;
                }

                adapter = new CategoryAdapter(AddNoteActivity.this, categoryList);
                spinner_category.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AddNoteActivity.this, R.string.category_download_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        loadCategory();
    }
}



