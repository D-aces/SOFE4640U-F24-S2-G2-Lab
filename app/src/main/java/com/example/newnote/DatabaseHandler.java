package com.example.newnote;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHandler extends SQLiteOpenHelper {

    public DatabaseHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "notes.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE notes (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, subtitle TEXT, body TEXT, created INTEGER);";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    // Add a note
    public boolean newNote(int id, String title, String subtitle, String body, int created){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title",title);
        values.put("subtitle", subtitle);
        values.put("body", body);
        values.put("created", created);
        long result = db.insert("notes",null, values);
        db.close();
        return result != -1;
    }

    // Get all notes
    public Cursor getAllNotes(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM notes";
        return db.rawQuery(query, null);
    }









}
