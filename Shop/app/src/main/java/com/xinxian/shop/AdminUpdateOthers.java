package com.xinxian.shop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AdminUpdateOthers extends AppCompatActivity  {

    EditText dcharge,dchargemv,minimum,freeammount,ophone,oemail,sharelink;
    private Button btnUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_update_others);

        dcharge=findViewById(R.id.delivery_charge);
        dchargemv=findViewById(R.id.delivery_charge_min);
        minimum=findViewById(R.id.admin_minimum_value);
        freeammount=findViewById(R.id.admin_free_delivery_value);
        ophone=findViewById(R.id.admin_phone);
        oemail=findViewById(R.id.admin_email);
        sharelink=findViewById(R.id.admin_Share_link);
        btnUpdate=findViewById(R.id.update_others);
        DisplayOthersInfo(dcharge,dchargemv,minimum,freeammount,ophone,oemail,sharelink);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                    updateOthersinfo();
                
            }
        });

    }

    private void updateOthersinfo() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Others");

        HashMap<String, Object> userMap = new HashMap<>();
        userMap. put("dcharge", dcharge.getText().toString());
        userMap. put("dchargemv", dchargemv.getText().toString());
        userMap. put("minimum", minimum.getText().toString());
        userMap. put("freeammount", freeammount.getText().toString());
        userMap. put("ophone", ophone.getText().toString());
        userMap. put("oemail", oemail.getText().toString());
        userMap. put("sharelink", sharelink.getText().toString());
        ref.updateChildren(userMap);


        Toast.makeText(AdminUpdateOthers.this, "Information successfully.", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void DisplayOthersInfo(final EditText dcharge, final EditText dchargemv, final EditText minimum,
                                   final EditText freeammount, final EditText ophone, final EditText oemail,final EditText sharelink) {

        DatabaseReference OtherRef= FirebaseDatabase.getInstance().getReference().child("Others");
        OtherRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                        String dcharge1 = dataSnapshot.child("dcharge").getValue().toString();
                        String dchargemv1 = dataSnapshot.child("dchargemv").getValue().toString();
                        String minimum1 = dataSnapshot.child("minimum").getValue().toString();
                        String freeammount1 = dataSnapshot.child("freeammount").getValue().toString();
                        String ophone1 = dataSnapshot.child("ophone").getValue().toString();
                        String oemail1 = dataSnapshot.child("oemail").getValue().toString();
                        String sharelink1 = dataSnapshot.child("sharelink").getValue().toString();

                        dcharge.setText(dcharge1);
                        dchargemv.setText(dchargemv1);
                        minimum.setText(minimum1);
                        freeammount.setText(freeammount1);
                        ophone.setText(ophone1);
                        oemail.setText(oemail1);
                        sharelink.setText(sharelink1);



                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}
