package com.example.newnote;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.textfield.TextInputEditText;


public class NewNote extends AppCompatActivity implements ColourSelectionListener {
    private int colour = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_note);

        ImageButton newNoteBack = findViewById(R.id.newnoteback);
        newNoteBack.setOnClickListener(v -> {
            Intent i = new Intent(NewNote.this, MainActivity.class);
            startActivity(i);
        });

        ImageButton colourPalette = findViewById(R.id.colourPalette);

        BottomSheetDialogue colourOptions = new BottomSheetDialogue();
        colourPalette.setOnClickListener(view -> {
            if (!colourOptions.isAdded()) {
                colourOptions.setColourSelectionListener(NewNote.this);
                colourOptions.show(getSupportFragmentManager(), "ModalBottomSheet");
            }
        });

        Button done = findViewById(R.id.donebutton);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputEditText title = findViewById(R.id.title);
                TextInputEditText subTitle = findViewById(R.id.subtitle);
                TextInputEditText body = findViewById(R.id.body);

                String titleText = title.getText().toString();
                String subtitleText = subTitle.getText().toString();
                String bodyText = body.getText().toString();
                long timeStamp = System.currentTimeMillis();

                if(titleText.isBlank()){
                    Toast.makeText(getApplicationContext(), "Please add a note title", Toast.LENGTH_SHORT).show();
                }
                else {
                    DatabaseHandler db = new DatabaseHandler(NewNote.this, null, null, 1);
                    db.newNote(titleText, subtitleText, bodyText, colour, timeStamp);

                    Intent i = new Intent(NewNote.this, MainActivity.class);
                    startActivity(i);
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void onColourSelected(int colour) {
        ConstraintLayout note = findViewById((R.id.main));
        note.setBackgroundColor(colour);
        this.colour = colour;
    }
}