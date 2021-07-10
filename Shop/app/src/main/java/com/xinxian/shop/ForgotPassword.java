package com.xinxian.shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.xinxian.shop.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ForgotPassword extends AppCompatActivity {

    public Button UpdatePass;
    private EditText ForgotPhone,ForgotPassword,SQForgot;
    private ProgressBar progressBar;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        UpdatePass=findViewById(R.id.UpdatePassBtn);
        ForgotPhone=findViewById(R.id.ForgotPhone);
        ForgotPassword=findViewById(R.id.ForgotPassword);
        SQForgot=findViewById(R.id.SQforgot);
        progressBar=findViewById(R.id.forgotprogress);

        loadingbar =new ProgressDialog(this);


        UpdatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdatePass();
            }
        });

    }

    private void UpdatePass() {

        String phone=ForgotPhone.getText().toString();
        String password=ForgotPassword.getText().toString();
        String SQ=SQForgot.getText().toString();

        if(TextUtils.isEmpty(phone)){
            Toast.makeText(getApplicationContext(),"Please Insert Phone Number",Toast.LENGTH_SHORT);
        }

        else if(TextUtils.isEmpty(password)){
            Toast.makeText(getApplicationContext(),"Please Insert Password",Toast.LENGTH_SHORT);
        }
        else if(TextUtils.isEmpty(SQ)){
            Toast.makeText(getApplicationContext(),"Please Insert Security Question's Answer",Toast.LENGTH_SHORT);
        }

        else{
            loadingbar.setTitle("Login Account");
            loadingbar.setMessage("Please Wait..");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();

            AllowAccessToAccount(phone,SQ,password);


        }
    }

    private void AllowAccessToAccount(final String phone, final String sq, final String password) {


        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if((dataSnapshot.child("Users").child(phone).exists())){

                    Users usersData=dataSnapshot.child("Users").child(phone).getValue(Users.class);
                    if(usersData.getPhone().equals(phone)){

                        if(usersData.getSq().equals(sq)){
                            HashMap<String,Object> userdataMap= new HashMap<>();
                            userdataMap.put("password",password);

                            RootRef.child("Users").child(phone).updateChildren(userdataMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(getApplicationContext(),"Password Updated Successfully",Toast.LENGTH_SHORT).show();
                                                loadingbar.dismiss();
                                                Intent intent=new Intent(ForgotPassword.this,LoginActivity.class);
                                                startActivity(intent);

                                            }
                                            else {
                                                Toast.makeText(getApplicationContext(),"Check Your Internet Connection",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });


                        }

                        else {
                            Toast.makeText(getApplicationContext(),"Incorrect Answer",Toast.LENGTH_SHORT).show();
                            loadingbar.dismiss();
                            Toast.makeText(getApplicationContext(),"Please try with correct Answer",Toast.LENGTH_SHORT).show();
                        }

                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Incorrect phone number",Toast.LENGTH_SHORT).show();
                        loadingbar.dismiss();
                        Toast.makeText(getApplicationContext(),"Please try with correct number",Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    Toast.makeText(getApplicationContext(),"This "+phone+" do not Exist",Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();
                    Toast.makeText(getApplicationContext(),"Please try with correct number",Toast.LENGTH_SHORT).show();
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}