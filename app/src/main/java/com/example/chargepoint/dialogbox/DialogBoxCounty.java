package com.example.chargepoint.dialogbox;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import com.example.chargepoint.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DialogBoxCounty extends AppCompatDialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Sort by county");
        String [] county = getContext().getResources().getStringArray(R.array.county);
        builder.setItems(county, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String c = county[which];
                Toast toast =  Toast.makeText(getContext(), "County of " + c + " selected.", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        return builder.create();
    }


}
