package com.example.newnote;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHandler extends SQLiteOpenHelper {

    public DatabaseHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "notes.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE notes (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, subtitle TEXT, body TEXT, colour TEXT, created INTEGER);";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    // Add a note
    public boolean newNote(String title, String subtitle, String body, int colour, long created){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title",title);
        values.put("subtitle", subtitle);
        values.put("body", body);
        values.put("colour", colour);
        values.put("created", created);
        long result = db.insert("notes",null, values);
        Log.d("DatabaseHandler", "Insert result: " + result);
        db.close();
        return result != -1;
    }

    // Get all notes
    public Cursor getAllNotes(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM notes";
        return db.rawQuery(query, null);
    }

    // Search notes
    public Cursor searchNotes(String filter){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM notes WHERE title LIKE ?";
        String[] selectionArgs = new String[]{"%" + filter + "%"};
        return db.rawQuery(query, selectionArgs);
    }








}
