package com.example.newnote;

import android.content.Intent;
import android.os.Bundle;
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
    private Note editable;
    private DatabaseHandler db;
    private TextInputEditText titleEditText;
    private TextInputEditText subtitleEditText;
    private TextInputEditText bodyEditText;
    private long timeStamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_note);

        initializeViews();
        loadExistingNote();
        setupListeners();
        adjustWindowInsets();
    }

    private void initializeViews() {
        db = new DatabaseHandler(NewNote.this);
        titleEditText = findViewById(R.id.title);
        subtitleEditText = findViewById(R.id.subtitle);
        bodyEditText = findViewById(R.id.body);
        timeStamp = System.currentTimeMillis();
        editable = getIntent().getParcelableExtra("NOTE");
    }

    private void loadExistingNote() {
        if (editable != null) {
            titleEditText.setText(editable.getTitle());
            subtitleEditText.setText(editable.getSubtitle());
            bodyEditText.setText(editable.getBody());
            colour = editable.getColour();
            setBackgroundColour(editable.getColour());
        }
    }

    private void setupListeners() {
        setupBackButton();
        setupColourPaletteButton();
        setupDoneButton();
        setupDeleteButton();
    }

    private void setupBackButton() {
        ImageButton newNoteBack = findViewById(R.id.newnoteback);
        newNoteBack.setOnClickListener(v -> {
            Intent i = new Intent(NewNote.this, MainActivity.class);
            startActivity(i);
        });
    }

    private void setupColourPaletteButton() {
        ImageButton colourPalette = findViewById(R.id.colourPalette);
        BottomSheetDialogue colourOptions = new BottomSheetDialogue();
        colourPalette.setOnClickListener(view -> {
            if (!colourOptions.isAdded()) {
                colourOptions.setColourSelectionListener(NewNote.this);
                colourOptions.show(getSupportFragmentManager(), "ModalBottomSheet");
            }
        });
    }

    private void setupDeleteButton() {
        ImageButton delete = findViewById(R.id.delete);
        delete.setOnClickListener(view -> {
            if (editable != null) {
                db.deleteNote(editable.getId());
            }
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish(); // Ensures NewNote activity is finished after deletion
        });
    }

    private void setupDoneButton() {
        Button doneButton = findViewById(R.id.donebutton);
        doneButton.setOnClickListener(v -> saveNote());
    }

    private void saveNote() {
        String titleText = titleEditText.getText().toString();
        String subtitleText = subtitleEditText.getText().toString();
        String bodyText = bodyEditText.getText().toString();

        if (titleText.isBlank()) {
            Toast.makeText(getApplicationContext(), "Please add a note title", Toast.LENGTH_SHORT).show();
            return; // Early exit if title is blank
        }

        if (editable == null) {
            db.newNote(titleText, subtitleText, bodyText, colour, timeStamp);
        } else {
            editable.setTitle(titleText);
            editable.setSubtitle(subtitleText);
            editable.setBody(bodyText);
            editable.setColour(colour);
            db.updateNote(editable);
        }
        finish();
    }

    private void adjustWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onColourSelected(int colour) {
        setBackgroundColour(colour);
        this.colour = colour;
    }

    public void setBackgroundColour(int colour) {
        ConstraintLayout noteLayout = findViewById(R.id.main);
        noteLayout.setBackgroundColor(colour);
    }
}
