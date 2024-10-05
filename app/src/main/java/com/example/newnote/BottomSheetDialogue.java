package com.example.newnote;

import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetDialogue extends BottomSheetDialogFragment {

    private String selectedColour;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
    ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_layout, container, false);

        ImageButton close = v.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        ImageButton pinkButton = v.findViewById(R.id.PinkButton);
        ImageButton blueButton = v.findViewById(R.id.BlueButton);
        ImageButton greenButton = v.findViewById(R.id.GreenButton);
        ImageButton turquoiseButton = v.findViewById(R.id.TurquoiseButton);
        ImageButton yellowButton = v.findViewById(R.id.YellowButton);

        setColorButtonListener(pinkButton);
        setColorButtonListener(blueButton);
        setColorButtonListener(greenButton);
        setColorButtonListener(turquoiseButton);
        setColorButtonListener(yellowButton);
        return v;
    }

    private void setColorButtonListener(ImageButton button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetButtonIcons();
                button.setImageResource(R.drawable.baseline_check_circle_24);
                selectedColour = String.valueOf(button.getColorFilter());
                System.out.println(selectedColour);
            }
        });
    }

    // Reset icons for all buttons
    private void resetButtonIcons() {
        View v = getView();
        if (v != null) {
            ((ImageButton) v.findViewById(R.id.PinkButton)).setImageResource(R.drawable.baseline_circle_24);
            ((ImageButton) v.findViewById(R.id.BlueButton)).setImageResource(R.drawable.baseline_circle_24);
            ((ImageButton) v.findViewById(R.id.GreenButton)).setImageResource(R.drawable.baseline_circle_24);
            ((ImageButton) v.findViewById(R.id.TurquoiseButton)).setImageResource(R.drawable.baseline_circle_24);
            ((ImageButton) v.findViewById(R.id.YellowButton)).setImageResource(R.drawable.baseline_circle_24);
        }
    }
}
