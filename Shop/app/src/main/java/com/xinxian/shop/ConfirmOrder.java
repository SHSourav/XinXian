package com.xinxian.shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.xinxian.shop.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmOrder extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    TextView finalName, finalPhone, finalAddress, finalSubtotal, finalDC, finalTotal;
    String dcharge, dchargemv, freeammount, minimum;
    private int subtotal;
   private String t;
    BottomNavigationView bottomNavCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        bottomNavCart = findViewById(R.id.bottomNaviConfirm);
        bottomNavCart.setOnNavigationItemSelectedListener(this);


        finalName = findViewById(R.id.final_username);
        finalPhone = findViewById(R.id.final_mobile);
        finalAddress = findViewById(R.id.final_address);
        finalSubtotal = findViewById(R.id.final_subtotal);
        finalDC = findViewById(R.id.final_emergency);
        finalTotal = findViewById(R.id.final_total);

        finalName.setText(Prevalent.currentOnlineUser.getName());
        finalPhone.setText(Prevalent.currentOnlineUser.getPhone());
        finalAddress.setText(Prevalent.currentOnlineUser.getAddress());
        subtotal = getIntent().getExtras().getInt("subtotal");
        finalSubtotal.setText(String.valueOf(subtotal) + "/=");

        DatabaseReference OtherRef = FirebaseDatabase.getInstance().getReference().child("Others");
        OtherRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    dcharge = dataSnapshot.child("dcharge").getValue().toString();
                    dchargemv = dataSnapshot.child("dchargemv").getValue().toString();
                    freeammount = dataSnapshot.child("freeammount").getValue().toString();
                    minimum = dataSnapshot.child("minimum").getValue().toString();

                    if (Integer.valueOf(subtotal) < Integer.valueOf(minimum)) {
                        finalDC.setText(dchargemv+"/=");
                        finalTotal.setText(String.valueOf(subtotal + Integer.valueOf(dchargemv)) + "/=");
                        t=String.valueOf(subtotal + Integer.valueOf(dchargemv));
                    } else if (Integer.valueOf(subtotal) >= Integer.valueOf(minimum) &&
                            Integer.valueOf(subtotal) < Integer.valueOf(freeammount)) {

                        finalDC.setText(dcharge+"/=");
                        finalTotal.setText(String.valueOf(subtotal + Integer.valueOf(dcharge)) + "/=");
                        t=String.valueOf(subtotal + Integer.valueOf(dcharge));

                    } else {
                        finalDC.setText("0/=");
                        finalTotal.setText(subtotal + "/=");
                        t=String.valueOf(subtotal);
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public boolean onNavigationItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.nav_update_cart:
                Intent intent = new Intent(ConfirmOrder.this, CartActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_confirm_order:



                bottomNavCart.getMenu().removeItem(R.id.nav_confirm_order);
                bottomNavCart.getMenu().removeItem(R.id.nav_update_cart);
                final String saveCurrentTime, saveCurrentDate;
                Calendar calForDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
                saveCurrentDate = currentDate.format(calForDate.getTime());


                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                saveCurrentTime = currentTime.format(calForDate.getTime());

                final DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Orders")
                        .child(Prevalent.currentOnlineUser.getPhone());

                final DatabaseReference HistoryHolder= FirebaseDatabase.getInstance().getReference().child("HistoryHolder")
                        .child(Prevalent.currentOnlineUser.getPhone());

                final HashMap<String, Object> orderMap = new HashMap<>();

                orderMap.put("totalAmount",t);
                orderMap.put("name", Prevalent.currentOnlineUser.getName());
                orderMap.put("phone", Prevalent.currentOnlineUser.getPhone());
                orderMap.put("date", saveCurrentDate);
                orderMap.put("time", saveCurrentTime);
                orderMap.put("address",Prevalent.currentOnlineUser.getAddress());
                orderMap.put("state", "Processing");

                orderRef.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            copyPath(saveCurrentDate,saveCurrentTime);
                            HistoryHolder.child(saveCurrentDate+saveCurrentTime).updateChildren(orderMap);
                            FirebaseDatabase.getInstance().getReference().child("CartList").child("UserView")
                                    .child(Prevalent.currentOnlineUser.getPhone())
                                    .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        FirebaseDatabase.getInstance().getReference().child("CartList").child("AdminView")
                                                .child(Prevalent.currentOnlineUser.getPhone())
                                                .removeValue();
                                        Toast.makeText(ConfirmOrder.this,
                                                "Your order has placed successfully", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(ConfirmOrder.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finishAffinity();

                                    }

                                }
                            });

                        }
                    }
                });

                break;


        }
        return true;


    }

    private void copyPath(String savedate,String savetime) {


        DatabaseReference questionNodes = FirebaseDatabase.getInstance().getReference().child("CartList").child("UserView")
                .child(Prevalent.currentOnlineUser.getPhone()).child("Products");
        final DatabaseReference toUsersQuestions = FirebaseDatabase.getInstance().getReference().child("History")
                .child(Prevalent.currentOnlineUser.getPhone()).child(savedate+savetime);

        questionNodes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot questionCode : dataSnapshot.getChildren()) {
                    String questionCodeKey = questionCode.getKey();
                    String iname = questionCode.child("iname").getValue(String.class);
                    String inumber = questionCode.child("inumber").getValue(String.class);
                    String iprice = questionCode.child("iprice").getValue(String.class);
                    String iquantity = questionCode.child("iquantity").getValue(String.class);
                    toUsersQuestions.child(questionCodeKey).child("iname").setValue(iname);
                    toUsersQuestions.child(questionCodeKey).child("inumber").setValue(inumber);
                    toUsersQuestions.child(questionCodeKey).child("iprice").setValue(iprice);
                    toUsersQuestions.child(questionCodeKey).child("iquantity").setValue(iquantity);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void onBackPressed() {

        Intent intent2=new Intent(ConfirmOrder.this,CartActivity.class);
        startActivity(intent2);
    }
}

