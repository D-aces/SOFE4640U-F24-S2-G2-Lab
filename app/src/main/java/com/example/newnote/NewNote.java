package com.example.newnote;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;

public class NewNote extends AppCompatActivity implements ColourSelectionListener {
    private int colour = -1;
    private Note editable;
    private DatabaseHandler db;
    private TextInputEditText titleEditText;
    private TextInputEditText subtitleEditText;
    private TextInputEditText bodyEditText;
    private long timeStamp;
    private ImageView imagePreview;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    private ActivityResultLauncher<Uri> takePhoto;
    private Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_note);

        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            // Callback after user selects a media item or closes the photo picker
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: " + uri);
                displaySelectedImage(uri);
            } else {
                Log.d("PhotoPicker", "No media selected");
            }
        });

        takePhoto = registerForActivityResult(new ActivityResultContracts.TakePicture(), isSuccess -> {
            if (isSuccess) {
                Log.d("Camera", "Photo taken successfully: " + photoUri);
                displaySelectedImage(photoUri); // Display the taken photo
            } else {
                Log.d("Camera", "Photo was not taken");
            }
        });

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
        imagePreview = findViewById(R.id.imagePreview);
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
        setupImagePickerButton();
        setupDeleteButton();
        setupDoneButton();
        setupCameraButton();
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

    private void setupImagePickerButton() {
        ImageButton imagePickerButton = findViewById(R.id.imagePicker);
        imagePickerButton.setOnClickListener(v -> {
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });
    }

    private void setupCameraButton() {
        ImageButton cameraButton = findViewById(R.id.cameraSelect);
        cameraButton.setOnClickListener(v -> {
            // Create a file to save the photo
            File photoFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "photo.jpg");
            photoUri = FileProvider.getUriForFile(this, "com.example.newnote" + ".provider", photoFile);

            // Launch the camera
            takePhoto.launch(photoUri);
        });
    }

    private void displaySelectedImage(Uri uri) {
        imagePreview.setImageURI(uri);
        imagePreview.setAdjustViewBounds(true);
        imagePreview.setVisibility(View.VISIBLE);
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
