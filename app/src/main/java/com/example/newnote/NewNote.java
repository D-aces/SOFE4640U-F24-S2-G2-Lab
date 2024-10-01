package com.example.newnote;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

public class NewNote extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_note);

        ImageButton b = findViewById(R.id.newnoteback);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NewNote.this, MainActivity.class);
                startActivity(i);
            }
        });

        Button done = findViewById(R.id.donebutton);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputEditText title = findViewById(R.id.title);
                TextInputEditText subTitle = findViewById(R.id.subTitle);
                TextInputEditText body = findViewById(R.id.body);

                String a = title.getText().toString();
                String b = subTitle.getText().toString();
                String c = body.getText().toString();
                String d = "color";
                int e = 0;

                DatabaseHandler db = new DatabaseHandler(NewNote.this,null,null,1);
                db.newNote(a, b, c, d,e);



                Intent i = new Intent(NewNote.this, MainActivity.class);
                startActivity(i);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}