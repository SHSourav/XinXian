package com.xinxian.shop;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xinxian.shop.Model.AdminOrders;
import com.xinxian.shop.Prevalent.Prevalent;
import com.xinxian.shop.ViewHolder.MyOrderHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyOrders extends AppCompatActivity {

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference HistoryHolderRef;
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

        HistoryHolderRef= FirebaseDatabase.getInstance().getReference().child("HistoryHolder")
                .child(Prevalent.currentOnlineUser.getPhone());

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrders> options =
                new FirebaseRecyclerOptions.Builder<AdminOrders>().setQuery(HistoryHolderRef, AdminOrders.class)
                        .build();

        FirebaseRecyclerAdapter<AdminOrders, MyOrderHolder> adapter = new FirebaseRecyclerAdapter
                <AdminOrders, MyOrderHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyOrderHolder holder, final int position,
                                            @NonNull final AdminOrders model) {
                try {


                    holder.UserOrderDate.setText(model.getDate());
                    holder.UserOrderTime.setText(model.getTime());
                    holder.UserOrderState.setText(model.getState());
                    holder.UserORderTotal.setText("৳"+model.getTotalAmount());
                }
                catch(NullPointerException e)

                {
                    System.out.print("Problem");
                }


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String uID=model.getDate()+model.getTime();
                        Intent intent=new Intent(MyOrders.this,MyOrderList.class);
                        intent.putExtra("uid",uID);
                        startActivity(intent);

                    }
                });

            }

            @NonNull
            @Override
            public MyOrderHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                           int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_order_history_layout, parent,
                        false);
                MyOrderHolder holder = new MyOrderHolder(view);
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

    public void onBackPressed() {

        Intent intent2=new Intent(MyOrders.this,MainActivity.class);
        startActivity(intent2);
    }

}


