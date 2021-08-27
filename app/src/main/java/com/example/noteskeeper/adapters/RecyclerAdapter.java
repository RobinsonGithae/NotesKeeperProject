package com.example.noteskeeper.adapters;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteskeeper.NoteActivity;
import com.example.noteskeeper.R;
import com.example.noteskeeper.models.Note;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;



public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>{

    private Context context;
    private List<Note> noteList;

    public RecyclerAdapter(Context context, List<Note> noteList) {
        this.context = context;
        this.noteList = noteList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new RecyclerViewHolder(inflater.inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
        holder.txv_title.setText(noteList.get(position).getTitle());
        holder.txv_category.setText(noteList.get(position).getCategory());
        holder.txv_date.setText(noteList.get(position).getDate());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NoteActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id", noteList.get(position).getId());
                intent.putExtra("title", noteList.get(position).getTitle());
                intent.putExtra("content", noteList.get(position).getContent());
                intent.putExtra("date", noteList.get(position).getDate());
                intent.putExtra("category", noteList.get(position).getCategory());
                context.startActivity(intent);
            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                DatabaseReference noteDB;
                noteDB = FirebaseDatabase.getInstance().getReference("note");

                String id = noteList.get(position).getId();
                noteDB.child(id).removeValue();

                Note index = noteList.get(position);
                noteList.remove(index);
                noteList.clear();
                notifyDataSetChanged();
                Toast.makeText(context, R.string.note_removed, Toast.LENGTH_SHORT).show();

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder{
        private TextView txv_title, txv_category, txv_date;
        private CardView cardView;

        RecyclerViewHolder(View itemView) {
            super(itemView);

            txv_title = itemView.findViewById(R.id.txv_card_title);
            txv_category = itemView.findViewById(R.id.txv_card_category);
            txv_date = itemView.findViewById(R.id.txv_card_date);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}