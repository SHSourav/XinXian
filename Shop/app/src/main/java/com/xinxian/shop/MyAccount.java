package com.xinxian.shop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xinxian.shop.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import io.paperdb.Paper;

public class MyAccount extends AppCompatActivity {

    EditText MAname,MAemail,MApassword,MAaddress,MAsq;
    TextView MAphone;
    String oldPhone,oldPass;
    private Button MAbtnUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        Paper.init(this);

        MAname=findViewById(R.id.myAccountName);
        MAphone=findViewById(R.id.myAccountPhone);
        MAemail=findViewById(R.id.myAccountEmail);
        MApassword=findViewById(R.id.myAccountPass);
        MAaddress=findViewById(R.id.myAccountAddress);
        MAbtnUpdate=findViewById(R.id.myAccountUpdateBtn);
        MAsq=findViewById(R.id.myAccountSQ);
        DisplayOthersInfo(MAname,MAphone,MAemail,MAaddress,MApassword,MAsq);


        MAbtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateOthersinfo();

            }
        });

    }

    private void updateOthersinfo() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(Prevalent.currentOnlineUser.getPhone());

        HashMap<String, Object> userMap = new HashMap<>();
        userMap. put("name", MAname.getText().toString());
        userMap. put("phone", MAphone.getText().toString());
        userMap. put("email", MAemail.getText().toString());
        userMap. put("address", MAaddress.getText().toString());
        userMap. put("password", MApassword.getText().toString());
        userMap. put("sq", MAsq.getText().toString());
        ref.updateChildren(userMap);

        Prevalent.currentOnlineUser.setName(MAname.getText().toString());
        Prevalent.currentOnlineUser.setAddress(MAaddress.getText().toString());

        if(oldPass!=MApassword.getText().toString()){
            Paper.book().write(Prevalent.UserPasswordKey,MApassword.getText().toString());

        }


        Toast.makeText(MyAccount.this, "Information Updated Successfully.", Toast.LENGTH_SHORT).show();
        Intent intent2=new Intent(MyAccount.this,MainActivity.class);
        startActivity(intent2);
        finish();
    }

    private void DisplayOthersInfo(final EditText MAname, final TextView MAphone, final EditText MAemail,
                                   final EditText MAaddress, final EditText MApassword,final EditText sq) {

        DatabaseReference OtherRef= FirebaseDatabase.getInstance().getReference().child("Users")
                .child(Prevalent.currentOnlineUser.getPhone());
        OtherRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    String MAname1 = dataSnapshot.child("name").getValue().toString();
                    String MAphone1 = dataSnapshot.child("phone").getValue().toString();
                    String MAemail1 = dataSnapshot.child("email").getValue().toString();
                    String MAaddress1 = dataSnapshot.child("address").getValue().toString();
                    String MApassword1 = dataSnapshot.child("password").getValue().toString();
                    String sq1 = dataSnapshot.child("sq").getValue().toString();

                    oldPhone=MAphone1;
                    oldPass=MApassword1;

                    MAname.setText(MAname1);
                    MAphone.setText(MAphone1);
                    MAemail.setText(MAemail1);
                    MAaddress.setText(MAaddress1);
                    MApassword.setText(MApassword1);
                    sq.setText(sq1);


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void onBackPressed() {

        Intent intent2=new Intent(MyAccount.this,MainActivity.class);
        startActivity(intent2);
    }


}
