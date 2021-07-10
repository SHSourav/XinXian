package com.xinxian.shop;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class AddItemDetails extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String Name,Quantity,UOM,Price,saveCurrentDate,saveCurrentTime,item,pID,cID;
    private Button SaveItem;
    private EditText ItemName,ItemQuantity,ItemPrice,ItemDiscount;
    private ImageView ItemImage;
    Spinner ItemUOM;
    private static final int GalleryPick=1;
    private String productRandomKey,downloadImageUrl;
    private StorageReference ProductImagesRef;
    private ProgressDialog loadingbar;
    private DatabaseReference ProductsRef;
    private Uri imageUri;
    ImageButton close;
    private String myUrl = "";
    private String DicountPrice = "",InStock="";
    private String checker = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_details);

        pID=getIntent().getExtras().getString("Pid");
        cID=getIntent().getExtras().getString("Cid");

        ProductImagesRef=FirebaseStorage.getInstance().getReference().child("PImages");
        ProductsRef= FirebaseDatabase.getInstance().getReference().child("Item");

        SaveItem=(Button)findViewById(R.id.saveItem);
        ItemImage=(ImageView)findViewById(R.id.ItemImage);
        ItemName=(EditText)findViewById(R.id.ItemName);
        ItemQuantity=(EditText)findViewById(R.id.ItemQuantity);
        ItemPrice=(EditText)findViewById(R.id.ItemPrice);
        ItemUOM=(Spinner) findViewById(R.id.ItemUoM);
        ItemDiscount=(EditText)findViewById(R.id.ItemDiscountPrice) ;
        loadingbar =new ProgressDialog(this);
        close=findViewById(R.id.ib_close);
        ItemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker="clicked";
                CropImage.activity(imageUri)
                        .start(AddItemDetails.this);
            }
        });



        SaveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductData();
            }
        });



        ItemUOM.setOnItemSelectedListener(this);
        List<String> categories = new ArrayList<String>();
        categories.add("Kg");
        categories.add("gm");
        categories.add("Liter");
        categories.add("ml");
        categories.add("Piece");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, categories);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ItemUOM.setAdapter(dataAdapter);

        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.8));
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            ItemImage.setImageURI(imageUri);
        }


        else{
            Toast.makeText(this,"Error,Try again",Toast.LENGTH_SHORT).show();
            finish();
        }
    }



    private void OpenGallery() {

        Intent galleryIntent=new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GalleryPick);
    }


    private void ValidateProductData() {

        Name=ItemName.getText().toString();
        Quantity=ItemQuantity.getText().toString();
        Price=ItemPrice.getText().toString();
        DicountPrice=ItemDiscount.getText().toString();
        UOM=item;

        if(imageUri==null)
        {
            Toast.makeText(getApplicationContext(),"Image is Mandatory..",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Name))
        {
            Toast.makeText(getApplicationContext(),"Name is Mandatory..",Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(Quantity))
        {
            Toast.makeText(getApplicationContext()," Quantity is Mandatory..",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Price))
        {
            Toast.makeText(getApplicationContext()," Price is Mandatory..",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(UOM))
        {
            Toast.makeText(getApplicationContext()," UOM is Mandatory..",Toast.LENGTH_SHORT).show();
        }
        else{

            StoreProductInformation();
        }
    }

    //For Date and Time

    private void StoreProductInformation() {

        loadingbar.setTitle("Adding New Item");
        loadingbar.setMessage("Please Wait..");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();


        Calendar calendar=Calendar.getInstance();

        SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime=currentTime.format(calendar.getTime());

        productRandomKey=saveCurrentDate +saveCurrentTime;
        final StorageReference filePath=ProductImagesRef.child(imageUri.getLastPathSegment() + productRandomKey + ".jpg");
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

                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void SaveProductInfoToDatabase() {

        HashMap<String,Object> itemMap=new HashMap<>();
        itemMap.put("iid",productRandomKey);
        itemMap.put("iimage",downloadImageUrl);
        itemMap.put("iname",Name);
        itemMap.put("iquantity",Quantity);
        itemMap.put("iprice",Price);
        itemMap.put("idiscount",DicountPrice);
        itemMap.put("iinStock",InStock);
        itemMap.put("iuom",UOM);
        itemMap.put("pid",pID);
        itemMap.put("cid",cID);


        ProductsRef.child(productRandomKey).updateChildren(itemMap).addOnCompleteListener(new OnCompleteListener<Void>() {
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item


            item = parent.getItemAtPosition(position).toString();

    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }



}
