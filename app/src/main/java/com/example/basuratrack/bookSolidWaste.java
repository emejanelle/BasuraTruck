package com.example.basuratrack;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class bookSolidWaste extends AppCompatActivity{
    private Bitmap capturedImageBitmap;
    private EditText nameInput, contactInput, locationInput;

    private FrameLayout fLayout;
    private TextView headerTitle;
    private LinearLayout locPinDet;
    private GestureDetector gestureDetector;
    private ActivityResultLauncher<Intent> imageCaptureLauncher;

    ImageView btnAddImage, addedImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_solid_waste);

        headerTitle = findViewById(R.id.customToolbar).findViewById(R.id.BookNowHeaderTitle);
        locPinDet = findViewById(R.id.customToolbar).findViewById(R.id.locationPinDet);

       fLayout = findViewById(R.id.frameLayout2);
       showPickUpLocDet();

        // Initialize the gesture detector
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                // Detect swipe gesture
                if (velocityX < 0) { // Left swipe detected
                    showConfirmExitDialog("swipe");
                    return true;
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });

    }

    private void showPickUpLocDet() {
        fLayout.removeAllViews();
//        LayoutInflater inflater = LayoutInflater.from(this);
        View pickUpLoc = getLayoutInflater().inflate(R.layout.activity_pick_up_loc_det, null);

        fLayout.addView(pickUpLoc);
        
        Button nextBtn = findViewById(R.id.btnBookNowNext);
        nextBtn.setOnClickListener(v -> activityWastePUDet());
    }

    private void activityWastePUDet() {
        fLayout.removeAllViews();
        View pickUpLoc = getLayoutInflater().inflate(R.layout.activity_waste_pudetails, null);

        fLayout.addView(pickUpLoc);
        updateHeaderTitle("WASTE PICK-UP DETAILS");
        locPinDet.setVisibility(View.GONE);

        setupReturnToPrevButton(pickUpLoc);

        Button btnQuoteBooking = findViewById(R.id.btnBookSWasteQuote);
        btnQuoteBooking.setOnClickListener(view -> {
            String name = nameInput.getText().toString();
            String contact = contactInput.getText().toString();
            String location = locationInput.getText().toString();
            String status = "Pending";
            String transactionID = UUID.randomUUID().toString(); // generate unique ID

            // Pass data to new activity
            Intent intent = new Intent(this, UserTransaction.class);
            intent.putExtra("name", name);
            intent.putExtra("contact", contact);
            intent.putExtra("location", location);
            intent.putExtra("status", status);
            intent.putExtra("transactionID", transactionID);

            // Pass image as byte array
            if (capturedImageBitmap != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                capturedImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                intent.putExtra("imageBitmap", byteArray);
            }

            startActivity(intent);
        });

        btnAddImage = findViewById(R.id.addImage);

        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 101);

                addedImg.setVisibility(View.VISIBLE);
            }
        });

        addedImg = findViewById(R.id.IVaddedImage);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 101);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            capturedImageBitmap = (Bitmap) extras.get("data");
            addedImg.setImageBitmap(capturedImageBitmap);
            addedImg.setVisibility(View.VISIBLE);
        }
    }

    private void updateHeaderTitle(String title) {
        if (headerTitle != null){
            headerTitle.setText(title);
        }
    }
    private void showConfirmExitDialog(String action) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cancel Progress");

        // Handle different actions with an if-else statement
        if ("swipe".equals(action)) {
            builder.setMessage("You swiped to return. Are you sure? Your data won't be saved.");
        } else if ("return".equals(action)) {
            builder.setMessage("You pressed the back button. Are you sure? Your data won't be saved.");
        } else if ("button".equals(action)) {
            builder.setMessage("You clicked the return button. Are you sure? Your data won't be saved.");
        }

        builder.setPositiveButton("Yes", (dialog, id) -> super.onBackPressed());
        builder.setNegativeButton("No", (dialog, id) -> dialog.dismiss());
        builder.create().show();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
            // Pass the touch event to the gesture detector
            gestureDetector.onTouchEvent(event);
            return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        // Handle back button press
        super.onBackPressed();
        showConfirmExitDialog("return");
    }

    public void setupReturnToPrevButton(View parentView) {
        Button returnToPrevButton = parentView.findViewById(R.id.returnToPrev);
        if (returnToPrevButton != null) {
            returnToPrevButton.setOnClickListener(v -> {
                showConfirmExitDialog("button");
                // Debug log to verify
                Toast.makeText(this, "Return button clicked!", Toast.LENGTH_SHORT).show();
            });
        } else {
            // Debug log for error tracking
            System.out.println("Button not found!");
        }

    }

}
