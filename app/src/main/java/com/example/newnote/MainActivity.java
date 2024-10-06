package com.example.newnote;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private DatabaseHandler dbHandler;
    private RecyclerView listNotes;
    private NoteAdapter notesAdapter;
    private List<Note> noteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize the DatabaseHandler and RecyclerView
        dbHandler = new DatabaseHandler(this, null, null, 1);
        listNotes = findViewById(R.id.notes);

        // Set up RecyclerView
        listNotes.setLayoutManager(new LinearLayoutManager(this));
        noteList = getAllNotesFromDB();
        notesAdapter = new NoteAdapter(noteList);
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

        SearchView searchNote = findViewById(R.id.searchNote);
        searchNote.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                dbHandler.searchNotes(s);
                // TODO: Add update to NoteAdapter here
                return false;
            }
        });


        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


    }

    // Method to get all notes from the database
    private List<Note> getAllNotesFromDB() {
        List<Note> notes = new ArrayList<>();
        Cursor cursor = dbHandler.getAllNotes();

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String subtitle = cursor.getString(2);
                String body = cursor.getString(3);
                int colour = cursor.getInt(4);
                Date created = new Date (cursor.getLong(5));
                notes.add(new Note(id, title, subtitle, body, colour, created));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return notes;
    }
}
