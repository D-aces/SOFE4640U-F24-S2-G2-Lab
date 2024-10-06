package com.example.newnote;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.content.res.ColorStateList;
import androidx.annotation.Nullable;

public class BottomSheetDialogue extends BottomSheetDialogFragment {
        private ImageButton lastSelectedButton;
        private ColourSelectionListener colourSelectionListener;
        private int tintColour = -1;

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.bottom_sheet_layout, container, false);

            ImageButton pinkButton = v.findViewById(R.id.PinkButton);
            ImageButton blueButton = v.findViewById(R.id.BlueButton);
            ImageButton greenButton = v.findViewById(R.id.GreenButton);
            ImageButton turquoiseButton = v.findViewById(R.id.TurquoiseButton);
            ImageButton yellowButton = v.findViewById(R.id.YellowButton);

            setColourButtonListener(pinkButton);
            setColourButtonListener(blueButton);
            setColourButtonListener(greenButton);
            setColourButtonListener(turquoiseButton);
            setColourButtonListener(yellowButton);

            ImageButton close = v.findViewById(R.id.close);
            close.setOnClickListener(view -> dismiss());

            return v;
        }

        private void setColourButtonListener(ImageButton button) {
            button.setOnClickListener(view -> {
                if (lastSelectedButton != null && lastSelectedButton != button) {
                    lastSelectedButton.setImageResource(R.drawable.baseline_circle_24);
                }

                button.setImageResource(R.drawable.baseline_check_circle_24);
                lastSelectedButton = button;

                ColorStateList tintColourList = button.getImageTintList();
                if (tintColourList != null) {
                    tintColour = tintColourList.getDefaultColor();
                    System.out.println(tintColour);
                }
                if (colourSelectionListener != null) {
                    colourSelectionListener.onColourSelected(tintColour);  // Pass the selected color
                }
            });
        }
    public void setColourSelectionListener(ColourSelectionListener listener) {
        this.colourSelectionListener = listener;
    }
    }

