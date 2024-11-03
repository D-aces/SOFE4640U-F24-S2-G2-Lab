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

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "notes.db";

    public DatabaseHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE notes (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, subtitle TEXT, body TEXT, colour INTEGER, created INTEGER);";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Handle upgrades if necessary
    }

    // Add a new note to the database
    public boolean newNote(String title, String subtitle, String body, int colour, long created) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("subtitle", subtitle);
        values.put("body", body);
        values.put("colour", colour);
        values.put("created", created);
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
                notes.add(new Note(id, title, subtitle, body, colour, new Date(created)));
            } while (cursor.moveToNext());
        }
        cursor.close();  // Always close the cursor
        return notes;
    }

    // Search for notes by id and return a Note Object
    // TODO: Use this in the NewNote Activity to pass the note object
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
                Note note = new Note(id, title, subtitle, body, colour, new Date(created));
                cursor.close();
                return note;
            }
        }
        return null;
    }

    // TODO: Use this in the NewNote Activity
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
                notes.add(new Note(id, title, subtitle, body, colour, new Date(created)));
            } while (cursor.moveToNext());
        }
        cursor.close();  // Always close the cursor
        return notes;
    }
}
