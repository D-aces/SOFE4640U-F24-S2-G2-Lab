package com.example.newnote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "notes.db";

    public DatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE notes (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, subtitle TEXT, body TEXT, colour INTEGER, created INTEGER);";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion < 3) {
            String alterTableStatement = "ALTER TABLE notes ADD COLUMN photopath TEXT";
            db.execSQL(alterTableStatement);
        }
    }

    // Add a new note to the database
    public boolean newNote(String title, String subtitle, String body, int colour, long created, String photopath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("subtitle", subtitle);
        values.put("body", body);
        values.put("colour", colour);
        values.put("created", created);
        values.put("photopath", photopath);
        long result = db.insert("notes", null, values);
        Log.d("DatabaseHandler", "Insert result: " + result);
        db.close();
        return result != -1;
    }

    // Fetch all notes from the database and return as a List of Note objects
    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM notes", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String subtitle = cursor.getString(2);
                String body = cursor.getString(3);
                int colour = cursor.getInt(4);
                long created = cursor.getLong(5);
                String photopath = cursor.getString(6);
                notes.add(new Note(id, title, subtitle, body, colour, new Date(created), photopath));
            } while (cursor.moveToNext());
        }
        cursor.close();  // Always close the cursor
        return notes;
    }

    // For later
    public Note getNote(int noteId) {
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.rawQuery("SELECT * FROM notes WHERE id = ?", new String[]{String.valueOf(noteId)})) {
            if (cursor.moveToFirst()) {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String subtitle = cursor.getString(2);
                String body = cursor.getString(3);
                int colour = cursor.getInt(4);
                long created = cursor.getLong(5);
                String photopath = cursor.getString(6);
                Note note = new Note(id, title, subtitle, body, colour, new Date(created), photopath);
                cursor.close();
                return note;
            }
        }
        return null;
    }

    public boolean updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", note.getTitle());
        contentValues.put("subtitle", note.getSubtitle());
        contentValues.put("body", note.getBody());
        contentValues.put("colour", note.getColour());
        contentValues.put("photopath", note.getPhotopath());

        // Update the note based on its ID
        int rowsAffected = db.update("notes", contentValues, "id = ?", new String[]{String.valueOf(note.getId())});
        return rowsAffected > 0; // Returns true if at least one row was updated
    }


    public boolean deleteNote(int noteId){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.delete("notes", "id = ?", new String[]{String.valueOf(noteId)}) > 0;
    }

    // Search for notes by title and return a List of Note objects
    public List<Note> searchNotes(String filter) {
        List<Note> notes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM notes WHERE title LIKE ?", new String[]{"%" + filter + "%"});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String subtitle = cursor.getString(2);
                String body = cursor.getString(3);
                int colour = cursor.getInt(4);
                long created = cursor.getLong(5);
                String photopath = cursor.getString(6);
                notes.add(new Note(id, title, subtitle, body, colour, new Date(created), photopath));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return notes;
    }
}
