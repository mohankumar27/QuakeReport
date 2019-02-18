package com.example.mohankumars.textrecognizer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ImageView textImage;
    TextView detectText;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        textImage =  (ImageView) findViewById(R.id.textImage);
        detectText = (TextView) findViewById(R.id.viewText);
    }

     public void pickImage(View v){
        detectText.setText("");
        Intent pickImageFromGallary = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(pickImageFromGallary,1);
     }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            Uri uri = data.getData();
            CropImage.activity(uri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), result.getUri());
                    textImage.setImageBitmap(bitmap); // display cropped image in imageView
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this,"Crop Error",Toast.LENGTH_LONG);
                Log.i("ImageCropError: ", error.toString());
            }
        }
    }

    public void detectTextFromImage(View v){
         if(bitmap == null){
             Toast.makeText(getApplicationContext(),"Image Cannot be detected",Toast.LENGTH_LONG);
         }else {
             final FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap);
             FirebaseVisionTextRecognizer firebaseVisionTextRecognizer = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
             firebaseVisionTextRecognizer.processImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                 @Override
                 public void onSuccess(FirebaseVisionText firebaseVisionText) {
                     processText(firebaseVisionText);
                 }
             }).addOnFailureListener(new OnFailureListener() {
                 @Override
                 public void onFailure(@NonNull Exception e) {
                     Toast.makeText(getApplicationContext(),"Unable to Detect Text",Toast.LENGTH_LONG); // when no image is recognized
                 }
             });
         }
    }

    private void processText(FirebaseVisionText firebaseVisionText) {
        List<FirebaseVisionText.TextBlock> block = firebaseVisionText.getTextBlocks();
        if(block.size()==0){
            Toast.makeText(getApplicationContext(),"No Text in Image",Toast.LENGTH_LONG); // when no text is detected
        }else{
            for(FirebaseVisionText.TextBlock blocks:firebaseVisionText.getTextBlocks()){
                String text = blocks.getText();
                detectText.append(text+"\n"); // display detected text in textview
            }
        }
    }
}
