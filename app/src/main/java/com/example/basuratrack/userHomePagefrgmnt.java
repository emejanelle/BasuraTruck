package com.example.basuratrack;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;


public class userHomePagefrgmnt extends Fragment {

    Button bookNowButton;
    Dialog dialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_home_pagefrgmnt, container, false);

        bookNowButton = view.findViewById(R.id.book_now_button);
        bookNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(requireContext(), "Book waste to be collected", Toast.LENGTH_SHORT).show();
                onBookNowButtonClicked();
            }
        });

        return view;
    }

    private void onBookNowButtonClicked() {
//        FragmentManager fragmentManager = getChildFragmentManager();
            dialog = new Dialog(requireContext());
            dialog.setContentView(R.layout.userbooknow_onclick);
            dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimationReport;
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            Button bookNowCancel = dialog.findViewById(R.id.buttonBookNowCancel);
            bookNowCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    dialog.dismiss();

                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                    builder.setTitle("Cancel Transaction");
                    builder.setMessage("Are you sure you want to cancel this transaction?");
                    builder.setPositiveButton("Yes", (alertDialog, which) -> {

                        Toast.makeText(requireContext(), "Transaction cancelled", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    });
                    builder.setNegativeButton("No", (dialogInterface, which) -> {
                        dialogInterface.dismiss();
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });

            LinearLayout solidWasteLT = dialog.findViewById(R.id.bookSolidWaste);
            solidWasteLT.setOnClickListener(view -> {
                Intent intent = new Intent(requireContext(), bookSolidWaste.class);
//                intent.putExtra();
                startActivity(intent);
            });
//
        Log.d("userHomePagefrgmnt","Dialog opened successfully");
        dialog.show();
    }
}