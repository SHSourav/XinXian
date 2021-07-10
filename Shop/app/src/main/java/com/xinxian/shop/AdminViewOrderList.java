package com.xinxian.shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xinxian.shop.Model.Cart;
import com.xinxian.shop.ViewHolder.OrderListHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminViewOrderList extends AppCompatActivity {

    private RecyclerView productList;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference cartListRef;
    private String userID="",usertime="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_orders);
        userID=getIntent().getStringExtra("uid");
        usertime=getIntent().getStringExtra("utime");
        productList=findViewById(R.id.recycler_admin_view_orders);
        productList.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        productList.setLayoutManager(layoutManager);

        cartListRef= FirebaseDatabase.getInstance().getReference().child("History").child(userID).child(usertime);
    }

    protected void onStart(){
        super.onStart();


        FirebaseRecyclerOptions<Cart> options=new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef,Cart.class)
                .build();

        FirebaseRecyclerAdapter<Cart, OrderListHolder> adapter=new FirebaseRecyclerAdapter<Cart, OrderListHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderListHolder holder, int position, @NonNull Cart model) {

                int a=Integer.valueOf(model.getIprice());
                int b=Integer.valueOf(model.getInumber());
                int c=a*b;
                holder.OrderListName.setText(model.getIname());
                holder.OrderListPrice.setText(String.valueOf(c)+" ৳");
                holder.OrderListQuantity.setText(model.getInumber()+" x "+model.getIquantity());


            }

            @NonNull
            @Override
            public OrderListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout
                        .activity_order_list, viewGroup, false);

                OrderListHolder holder=new OrderListHolder(view);
                return holder;
            }
        };

        productList.setAdapter(adapter);
        adapter.startListening();
    }
}