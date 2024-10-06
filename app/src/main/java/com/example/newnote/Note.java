package com.example.newnote;
import java.util.Date;

public class Note {
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

}
