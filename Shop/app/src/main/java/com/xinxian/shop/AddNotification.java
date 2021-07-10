package com.xinxian.shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddNotification extends AppCompatActivity {
    ImageView NotificationImage;
    private Button AddNotification,ViewNotification;
    private ProgressDialog loadingbar;
    private StorageReference nImagesRef;
    private DatabaseReference nRef;
    private String checker = "",saveCurrentTime,saveCurrentDate;
    private String productRandomKey,downloadImageUrl;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notification);

        nImagesRef= FirebaseStorage.getInstance().getReference().child("nImages");
        nRef= FirebaseDatabase.getInstance().getReference().child("notification");

        NotificationImage=(ImageView)findViewById(R.id.NotificationImage);
        AddNotification=(Button)findViewById(R.id.AddNotification);
        ViewNotification=(Button)findViewById(R.id.ViewNotification);
        loadingbar =new ProgressDialog(this);

        AddNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checker.equals("clicked")){
                    NotificationSaved();
                }
            }
        });

        ViewNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddNotification.this,ViewNotification.class);
                intent.putExtra("Pid","sourav");
                startActivity(intent);
            }
        });

        NotificationImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                checker = "clicked";

                CropImage.activity(imageUri)
                        .start(AddNotification.this);
            }
        });
    }

    private void NotificationSaved() {

        loadingbar.setTitle("Adding New Item");
        loadingbar.setMessage("Please Wait..");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();


        Calendar calendar=Calendar.getInstance();

        SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime=currentTime.format(calendar.getTime());
         productRandomKey=saveCurrentDate+saveCurrentTime;
        final StorageReference filePath=nImagesRef.child(imageUri.getLastPathSegment() + productRandomKey + ".jpg");
        final UploadTask uploadTask=filePath.putFile(imageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message=e.toString();
                Toast.makeText(getApplicationContext(),"Error.."+message,Toast.LENGTH_SHORT).show();
                loadingbar.dismiss();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(getApplicationContext(),"Image Uploaded Succesfully",Toast.LENGTH_SHORT).show();
                Task<Uri> uriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){

                            throw task.getException();


                        }

                        downloadImageUrl=filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful())
                        {
                            downloadImageUrl=task.getResult().toString();
                            Toast.makeText(getApplicationContext(),"Image Saved To Database Successfully..",
                                    Toast.LENGTH_SHORT).show();

                            SaveNotificationToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void SaveNotificationToDatabase() {

        HashMap<String,Object> itemMap=new HashMap<>();
        itemMap.put("iimage",downloadImageUrl);
        nRef.child(productRandomKey).updateChildren(itemMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(),"Added Successfully..",Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();
                }
                else {
                    loadingbar.dismiss();
                    String message=task.getException().toString();

                    Toast.makeText(getApplicationContext(),"Error"+message,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode== CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            NotificationImage.setImageURI(imageUri);
        }
        else
        {
            Toast.makeText(this, "Error, Try Again.", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(AddNotification.this, AddNotification.class));
            finish();
        }
    }
}