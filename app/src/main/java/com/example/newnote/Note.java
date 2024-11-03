package com.example.newnote;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Date;

public class Note implements Parcelable {
    private int id;
    private String title;
    private String subtitle;
    private String body;
    private int colour;
    private Date created;

public Note(int id, String title, String subtitle, String body, int colour, Date created){
    this.id = id;
    this.title = title;
    this.subtitle = subtitle;
    this.body = body;
    this.colour = colour;
    this.created = created;
}

    protected Note(Parcel in) {
        id = in.readInt();
        title = in.readString();
        subtitle = in.readString();
        body = in.readString();
        colour = in.readInt();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public int getId(){
    return id;
    }

    public String getTitle(){
    return title;
    }

    public String getSubtitle(){
        return subtitle;
    }

    public String getBody(){
    return body;
    }

    public int getColour(){return colour;}

    public Date getCreated(){return created; }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setColour(int colour) {
        this.colour = colour;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(subtitle);
        parcel.writeString(body);
        parcel.writeInt(colour);
    }
}
