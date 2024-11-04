package com.example.newnote;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private List<Note> noteList;  // List to hold notes
    private final RecyclerViewInterface recyclerViewInterface;
    public NoteAdapter(List<Note> noteList, RecyclerViewInterface recyclerViewInterface) {
        this.noteList = noteList;
        this.recyclerViewInterface = recyclerViewInterface;
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
        return new NoteViewHolder(view, recyclerViewInterface);
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

        holder.subtitle.setVisibility(note.getSubtitle().isEmpty() ? View.GONE : View.VISIBLE);
        holder.body.setVisibility(note.getBody().isEmpty() ? View.GONE : View.VISIBLE);

        // Set image preview
        String photoPath = note.getPhotopath();
        if (photoPath != null && !photoPath.isEmpty()) {
            File imageFile = new File(photoPath);
            if (imageFile.exists()) {
                Bitmap photo = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                if (photo != null) {
                    holder.noteImage.setImageBitmap(photo);
                    holder.noteImage.setVisibility(View.VISIBLE);
                } else {
                    holder.noteImage.setVisibility(View.GONE);
                }
            } else {
                holder.noteImage.setVisibility(View.GONE);
            }
        } else {
            holder.noteImage.setVisibility(View.GONE);
        }

        holder.noteCardWrapper.setBackgroundColor(note.getColour());

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
        ConstraintLayout noteCardWrapper;
        TextView title, subtitle, body, created;
        ImageView noteImage;

        public NoteViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            noteCardWrapper = itemView.findViewById(R.id.noteCardWrapper);
            title = itemView.findViewById(R.id.noteTitle);
            subtitle = itemView.findViewById(R.id.noteSubtitle);
            body = itemView.findViewById(R.id.noteBody);
            created = itemView.findViewById(R.id.noteCreated);
            noteImage = itemView.findViewById(R.id.noteImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClick(position);
                        }
                    }
                }
            });

        }
    }
}
