package com.xinxian.shop;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.Calendar;
import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    private TextView txtSignUp,selectbd;
    private Button signUpbtn;
    private EditText SignUpName,SignUpEmail,SignUpPassword,SignUpPhone,SignUpAddress;
    ImageView calendar;
    private static final String TAG = "SignupActivity";
    private ProgressBar progressBar;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private ProgressDialog loadingbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        progressBar=(ProgressBar)findViewById(R.id.progressId);
        txtSignUp=(TextView)  findViewById(R.id.AlreadyRegister);
        signUpbtn=findViewById(R.id.btnLogin2);
        SignUpName=(EditText)findViewById(R.id.SignUpName);
        SignUpEmail=(EditText) findViewById(R.id.SignUpEmail);
        SignUpPhone=(EditText) findViewById(R.id.SignUpPhone);
        SignUpAddress=(EditText)findViewById(R.id.SignUpAddress);
        selectbd=(TextView)findViewById(R.id.selectbd);
        calendar=(ImageView)findViewById(R.id.calendar);


        SignUpPassword=(EditText) findViewById(R.id.SignUpPass);

        loadingbar=new ProgressDialog(this);
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        SignupActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }

        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                selectbd.setText(date);
            }
        };
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(intent);

            }
        });


        signUpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();


            }
        });
    }

    private void CreateAccount() {

        String name=SignUpName.getText().toString();
        String email=SignUpEmail.getText().toString();
        String phone=SignUpPhone.getText().toString();
        String password=SignUpPassword.getText().toString();
        String address=SignUpAddress.getText().toString();
        String SQ= selectbd.getText().toString();



        if(TextUtils.isEmpty(name)){
            Toast.makeText(getApplicationContext(),"Please Insert Name",Toast.LENGTH_SHORT);
        }
        else if(TextUtils.isEmpty(email)){
            Toast.makeText(getApplicationContext(),"Please Insert Email",Toast.LENGTH_SHORT);
        }

        else if(TextUtils.isEmpty(phone)){
            Toast.makeText(getApplicationContext(),"Please Insert Phone Number",Toast.LENGTH_SHORT);
        }

        else if(TextUtils.isEmpty(password)){
            Toast.makeText(getApplicationContext(),"Please Insert Password",Toast.LENGTH_SHORT);
        }

        else if(TextUtils.isEmpty(address)){
            Toast.makeText(getApplicationContext(),"Please Insert Address",Toast.LENGTH_SHORT);
        }


        else if(!email.matches(emailPattern))
        {
            Toast.makeText(getApplicationContext(),"Invalid email address", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(SQ))
        {
            Toast.makeText(getApplicationContext(),"Please Enter Security Question's Answer", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingbar.setTitle("Create Account");
            loadingbar.setMessage("Please Wait..");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();

            ValidatePhoneNumber(name,phone,email,password,address,SQ);
        }
    }

    private void ValidatePhoneNumber(final String name, final String phone, final String email, final String password,
                                     final String address,final String SQ) {



        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(!(dataSnapshot.child("Users").child(phone).exists())){
                    HashMap<String,Object> userdataMap= new HashMap<>();
                    userdataMap.put("name",name);
                    userdataMap.put("phone",phone);
                    userdataMap.put("email",email);
                    userdataMap.put("password",password);
                    userdataMap.put("address",address);
                    userdataMap.put("sq",SQ);


                    RootRef.child("Users").child(phone).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(),"Congratulations..",Toast.LENGTH_SHORT).show();
                                        loadingbar.dismiss();
                                        Intent intent=new Intent(SignupActivity.this,LoginActivity.class);
                                        startActivity(intent);

                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(),"Check Your Internet Connection",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else {
                    Toast.makeText(getApplicationContext(),"This"+phone+"Already Exist",Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();
                    Toast.makeText(getApplicationContext(),"Please try whith another number",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void onBackPressed() {

        Intent intent2=new Intent(SignupActivity.this,LoginActivity.class);
        startActivity(intent2);
    }
}
