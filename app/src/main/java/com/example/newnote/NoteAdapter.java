package com.example.newnote;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private List<Note> noteList;  // List to hold notes

    public NoteAdapter(List<Note> noteList) {
        this.noteList = noteList;
    }

    // Method to update the list of notes and refresh the RecyclerView
    public void updateNoteList(List<Note> newNoteList) {
        this.noteList = newNoteList;
        notifyDataSetChanged();  // Notify the adapter that the data has changed
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = noteList.get(position);  // Get the current note
        int accentColour = (note.getColour() & 0xFF000000) |
                ((int)(((note.getColour() >> 16) & 0xFF) * 0.5) << 16) |
                ((int)(((note.getColour() >> 8) & 0xFF) * 0.5) << 8) |
                (int)((note.getColour() & 0xFF) * 0.5);

        holder.title.setText(note.getTitle());
        holder.subtitle.setText(note.getSubtitle());
        holder.subtitle.setTextColor(accentColour);
        holder.body.setText(note.getBody());
        holder.body.setTextColor(accentColour);
        holder.noteCard.setBackgroundColor(note.getColour());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        holder.created.setText(String.format("Created %s", dateFormat.format(note.getCreated())));
        holder.created.setTextColor(accentColour);
    }

    @Override
    public int getItemCount() {
        return noteList.size();  // Return the size of the note list
    }

    // ViewHolder class to hold reference to UI components in note_item layout
    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        LinearLayout noteCard;
        TextView title, subtitle, body, created;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteCard = itemView.findViewById(R.id.noteCard);
            title = itemView.findViewById(R.id.noteTitle);
            subtitle = itemView.findViewById(R.id.noteSubtitle);
            body = itemView.findViewById(R.id.noteBody);
            created = itemView.findViewById(R.id.noteCreated);
        }
    }
}
