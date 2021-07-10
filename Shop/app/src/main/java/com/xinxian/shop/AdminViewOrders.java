package com.xinxian.shop;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xinxian.shop.Model.AdminOrders;
import com.xinxian.shop.ViewHolder.AdminViewOrderHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AdminViewOrders extends AppCompatActivity {

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference OrderRef,HistoryHolderRef,CartRef,HistoryRef;
    private static final int REQUEST_CALL = 1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_orders);




        recyclerView=findViewById(R.id.recycler_admin_view_orders);
        try
        {
            recyclerView.setHasFixedSize(true);
            layoutManager=new LinearLayoutManager(this);

            recyclerView.setLayoutManager(layoutManager);
        }
        catch(NullPointerException e)
        {
            System.out.print("Problem");
        }

        OrderRef= FirebaseDatabase.getInstance().getReference().child("Orders");
        CartRef= FirebaseDatabase.getInstance().getReference().child("CartList").child("AdminView");
        HistoryHolderRef=FirebaseDatabase.getInstance().getReference().child("HistoryHolder");


    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrders> options =
                new FirebaseRecyclerOptions.Builder<AdminOrders>().setQuery(OrderRef, AdminOrders.class)
                        .build();

        FirebaseRecyclerAdapter<AdminOrders, AdminViewOrderHolder> adapter = new FirebaseRecyclerAdapter
                <AdminOrders, AdminViewOrderHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminViewOrderHolder holder, final int position,
                                            @NonNull final AdminOrders model) {
                try {


                    holder.OVAName.setText(model.getName());
                    holder.OVATotal.setText(model.getTotalAmount());
                    holder.OVAAddress.setText(model.getAddress());
                    holder.OVANumber.setText(model.getPhone());
                    holder.OVATime.setText(model.getTime());
                    if(model.getState()=="Confirmed")
                    {
                        holder.OVAName.setTextColor(ContextCompat.getColor(AdminViewOrders.this, R.color.red));
                        holder.OVATotal.setTextColor(ContextCompat.getColor(AdminViewOrders.this, R.color.red));
                        holder.OVAAddress.setTextColor(ContextCompat.getColor(AdminViewOrders.this, R.color.red));
                        holder.OVANumber.setTextColor(ContextCompat.getColor(AdminViewOrders.this, R.color.red));
                        holder.OVATime.setTextColor(ContextCompat.getColor(AdminViewOrders.this, R.color.red));
                    }



                }
                catch(NullPointerException e)

                {
                    System.out.print("Problem");
                }

                holder.orderViewList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String uID=getRef(position).getKey();
                        Intent intent=new Intent(AdminViewOrders.this,AdminViewOrderList.class);
                        intent.putExtra("uid",uID);
                        intent.putExtra("utime",model.getDate()+model.getTime());
                        startActivity(intent);
                    }
                });
                holder.orderCall.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View v) {
                        makePhoneCall();
                    }

                    private void makePhoneCall()  {
                        String number = model.getPhone();
                        if (number.trim().length() > 0) {

                            if (ContextCompat.checkSelfPermission(AdminViewOrders.this,
                                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(AdminViewOrders.this,
                                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                            } else {
                                String dial = "tel:" + number;
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse(dial));
                                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(callIntent);
                            }

                        }
                    }

                    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                        if (requestCode == REQUEST_CALL) {
                            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                                makePhoneCall();
                            } else {
                                Toast.makeText(AdminViewOrders.this, "Permission DENIED", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        CharSequence options[] = new CharSequence[]
                                {
                                        "  Confirm",
                                        "  Cancel",
                                        "  Shipped",
                                        "  Delivered"

                                };

                        AlertDialog.Builder builder=new AlertDialog.Builder(AdminViewOrders.this);
                        builder.setTitle("Maintain Products:").setIcon(R.drawable.groceyicon);

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                if (i == 0) {
                                    String uID=getRef(position).getKey();
                                    OrderRef.child(uID);
                                    HashMap<String, Object> stockoutmap = new HashMap<>();
                                    stockoutmap.put("state","Confirmed");

                                    HistoryHolderRef.child(model.getPhone()).child(model.getDate()+model.getTime()).updateChildren(stockoutmap);
                                    OrderRef.child(uID).updateChildren(stockoutmap);
                                }

                                if (i == 1)
                                {
                                    String uID=getRef(position).getKey();
                                    OrderRef.child(uID);
                                    HashMap<String, Object> stockoutmap = new HashMap<>();
                                    stockoutmap.put("state","Cancel");

                                    HistoryHolderRef.child(model.getPhone()).child(model.getDate()+model.getTime()).updateChildren(stockoutmap);
                                    OrderRef.child(getRef(position).getKey()).removeValue();

                                }
                                if(i==2){
                                    String uID=getRef(position).getKey();
                                    OrderRef.child(uID);
                                    HashMap<String, Object> stockoutmap = new HashMap<>();
                                    stockoutmap.put("state","Shipped");
                                    HistoryHolderRef.child(model.getPhone()).child(model.getDate()+model.getTime()).updateChildren(stockoutmap);

                                }

                                if(i==3){
                                    String uID=getRef(position).getKey();
                                    OrderRef.child(uID);
                                    HashMap<String, Object> stockoutmap = new HashMap<>();
                                    stockoutmap.put("state","Delivered");
                                    HistoryHolderRef.child(model.getPhone()).child(model.getDate()+model.getTime()).updateChildren(stockoutmap);
                                    OrderRef.child(uID).removeValue();
                                }


                            }

                        });

                        builder.setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {

                                    }
                                });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();


                    }
                });

            }

            @NonNull
            @Override
            public AdminViewOrderHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                    int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_order_view_list, parent,
                        false);
                AdminViewOrderHolder holder = new AdminViewOrderHolder(view);
                return holder;
            }
        };

        try {
            recyclerView.setAdapter(adapter);
            adapter.startListening();
        } catch (
                NullPointerException e) {
            System.out.print("Problem");
        }

    }




}


