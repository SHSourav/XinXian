package com.xinxian.shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class UpdateProducts extends AppCompatActivity {

    EditText ProductUOM,ProductName,ProductPrice,ProductQuantity,ProductDiscount;
    ImageView ProductImage;
    private Button UpdateproductButton;
    private Uri imageUri;
    private String myUrl = "",iID,pID;
    private String checker = "";
    private DatabaseReference ProductUpdateRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_products);

        ProductUpdateRef= FirebaseDatabase.getInstance().getReference().child("Item");
       iID=getIntent().getExtras().getString("iID");

        ProductImage=(ImageView)findViewById(R.id.UpdateImage);
        ProductName=(EditText)findViewById(R.id.UpdateProductName);
        ProductPrice=(EditText)findViewById(R.id.UpdateProductPrice);
        ProductUOM=(EditText)findViewById(R.id.UpdateProductUOM);
        ProductQuantity=(EditText)findViewById(R.id.UpdateProductQuantity);
        ProductDiscount=(EditText)findViewById(R.id.UpdateProductDiscount);
        UpdateproductButton=(Button)findViewById(R.id.UpdateProductbutton);

        userInfoDisplay( ProductImage,ProductName,ProductPrice,ProductUOM,ProductQuantity,ProductDiscount);

        UpdateproductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checker.equals("clicked")){
                    userInfoSaved();
                }
                else{
                    updateOnlyUserInfo();
                }
            }
        });

        ProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                checker = "clicked";

                CropImage.activity(imageUri)
                        .start(UpdateProducts.this);
            }
        });

    }


    private void userInfoSaved() {
        if (TextUtils.isEmpty(ProductName.getText().toString()))
        {
            Toast.makeText(this, "Name is mandatory.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(ProductPrice.getText().toString()))
        {
            Toast.makeText(this, "Price is mandatory.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(ProductQuantity.getText().toString()))
        {
            Toast.makeText(this, "Quantity is mandatory.", Toast.LENGTH_SHORT).show();
        }
        else if(checker.equals("clicked"))
        {
            updateOnlyUserInfo();
        }


    }
    private void updateOnlyUserInfo() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Item").child(iID);

        HashMap<String, Object> UpdaterefMAp = new HashMap<>();
        UpdaterefMAp. put("iname", ProductName.getText().toString());
        UpdaterefMAp. put("iprice", ProductPrice.getText().toString());
        UpdaterefMAp. put("iuom", ProductUOM.getText().toString());
        UpdaterefMAp. put("iquantity", ProductQuantity.getText().toString());
        UpdaterefMAp. put("idiscount", ProductDiscount.getText().toString());
        ref.updateChildren(UpdaterefMAp);
        Intent intent=new Intent(UpdateProducts.this, AddItem.class);
        intent.putExtra("Pid",pID);
        Toast.makeText(UpdateProducts.this, "Updated successfully.", Toast.LENGTH_SHORT).show();
        finish();
        startActivity(intent);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode== CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            ProductImage.setImageURI(imageUri);
        }
        else
        {
            Toast.makeText(this, "Error, Try Again.", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(UpdateProducts.this, UpdateProducts.class));
            finish();
        }
    }

    private void userInfoDisplay(final ImageView ProductImage, final EditText ProductName,
                                 final EditText ProductPrice,final EditText ProductUOM, final EditText ProductQuantity,final EditText ProductDiscount) {

        DatabaseReference Updateref= FirebaseDatabase.getInstance().getReference().child("Item").child(iID);
        Updateref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    if (dataSnapshot.child("iimage").exists()) {
                        String image = dataSnapshot.child("iimage").getValue().toString();
                        String name = dataSnapshot.child("iname").getValue().toString();
                        String price = dataSnapshot.child("iprice").getValue().toString();
                        String uom = dataSnapshot.child("iuom").getValue().toString();
                        String quantity = dataSnapshot.child("iquantity").getValue().toString();
                        String discount = dataSnapshot.child("idiscount").getValue().toString();
                        pID=dataSnapshot.child("pid").getValue().toString();

                        Picasso.get().load(image).into(ProductImage);
                        ProductName.setText(name);
                        ProductPrice.setText(price);
                        ProductUOM.setText(uom);
                        ProductQuantity.setText(quantity);
                        ProductDiscount.setText(discount);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
 
}