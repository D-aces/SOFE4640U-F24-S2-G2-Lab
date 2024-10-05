package com.example.newnote;

import android.R.color;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

        Button done = findViewById(R.id.button2);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NewNote.this, MainActivity.class);
                startActivity(i);
            }
        });

        ImageButton colourPalette = findViewById(R.id.colourPalette);
        colourPalette.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                BottomSheetDialogue colourOptions = new BottomSheetDialogue();
                colourOptions.show(getSupportFragmentManager(),"ModalBottomSheet");
            }

        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}