package com.example.newnote;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface {

    private DatabaseHandler dbHandler;
    private NoteAdapter notesAdapter;
    private List<Note> noteList;  // Current list of notes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize the DatabaseHandler and RecyclerView
        dbHandler = new DatabaseHandler(this);
        RecyclerView listNotes = findViewById(R.id.notes);

        // Set up RecyclerView
        listNotes.setLayoutManager(new LinearLayoutManager(this));

        // Load all notes from the database initially
        noteList = dbHandler.getAllNotes();
        notesAdapter = new NoteAdapter(noteList, this);
        listNotes.setAdapter(notesAdapter);

        // Set up Floating Action Button to add a new note
        FloatingActionButton b = findViewById(R.id.floatingActionButton2);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, NewNote.class);
                startActivity(i);
            }
        });

        // Set up SearchView functionality
        SearchView searchNote = findViewById(R.id.searchNote);
        searchNote.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.isEmpty()) {
                    // If the search query is empty, reload all notes from the database
                    noteList = dbHandler.getAllNotes();
                } else {
                    // Query the database for filtered results
                    noteList = dbHandler.searchNotes(s);
                }
                notesAdapter.updateNoteList(noteList); // Update the RecyclerView with the new list
                return true;
            }
        });

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume() { // on resume for returning to notes list
        super.onResume();
        SearchView searchNote = findViewById(R.id.searchNote);
        String filter = searchNote.getQuery().toString();
        if (filter.trim().isEmpty()) {
            noteList = dbHandler.getAllNotes();
        } else {
            noteList = dbHandler.searchNotes(filter);
        }
        notesAdapter.updateNoteList(noteList); // update the list
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHandler.close();
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, NewNote.class);
        intent.putExtra("NOTE", noteList.get(position));
        startActivity(intent);
    }
}
