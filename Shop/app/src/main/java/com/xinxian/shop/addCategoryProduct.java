package com.xinxian.shop;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class addCategoryProduct extends AppCompatActivity {

    private TextView CPText;
    private Button AddCategory;
    private EditText CnameEn,CnameBn;
    String UpperText;

    private DatabaseReference ProductsRef;
    private ProgressBar progressBar;
    private ProgressDialog loadingbar;
    private  String productRandomKey;
    private String saveCurrentDate;
    private String saveCurrentTime;
    private String pId,cpNAME;
    private DatabaseReference PR;
    ImageButton close;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category_product);
        String pName=getIntent().getExtras().getString("Pname");
        pId=getIntent().getExtras().getString("Pid");
        cpNAME=getIntent().getExtras().getString("cpNAME");
        CPText=findViewById(R.id.addCP);
        CPText.setText("Add"+" "+pName);
        ProductsRef= FirebaseDatabase.getInstance().getReference().child(pName.toString());


        progressBar=(ProgressBar)findViewById(R.id.progressId);

        AddCategory=(Button)findViewById(R.id.saveCP);
        CnameEn=(EditText) findViewById(R.id.nameEn);
        CnameBn=(EditText) findViewById(R.id.nameBn);


        loadingbar=new ProgressDialog(this);


        AddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();


            }
        });
        //popup
        close=findViewById(R.id.ib_close);
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.6));
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void CreateAccount() {

        String NameEn=CnameEn.getText().toString();
        String NameBn=CnameBn.getText().toString();



        if(TextUtils.isEmpty(NameEn)){
            Toast.makeText(getApplicationContext(),"Please Insert English Name",Toast.LENGTH_SHORT);
        }
        else if(TextUtils.isEmpty(NameBn)){
            Toast.makeText(getApplicationContext(),"Please Insert Bangla Name",Toast.LENGTH_SHORT);
        }

        else{
            loadingbar.setTitle("Adding Product");
            loadingbar.setMessage("Please Wait..");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();

            ValidatePhoneNumber(NameEn,NameBn);
        }
    }

    private void ValidatePhoneNumber(final String NameEn, final String NameBn) {


        Calendar calendar=Calendar.getInstance();

        SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime=currentTime.format(calendar.getTime());

        productRandomKey=saveCurrentDate +saveCurrentTime;
        PR=ProductsRef.child(productRandomKey);

        ProductsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    HashMap<String,Object> CategoryMAp= new HashMap<>();
                    CategoryMAp.put("nameEn",NameEn);
                    CategoryMAp.put("nameBn",NameBn);
                    CategoryMAp.put("id",productRandomKey);

             if(pId!=null)
                 {
                    ;
                     productRandomKey=cpNAME;
                     CategoryMAp.put("cName",productRandomKey);
                     PR=ProductsRef.child(productRandomKey).child(saveCurrentDate+saveCurrentTime);

                 }
                PR.updateChildren(CategoryMAp)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(),"Added Successfully",Toast.LENGTH_SHORT).show();
                                        loadingbar.dismiss();

                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(),"Check Your Internet Connection",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
