package com.xinxian.shop;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.xinxian.shop.Model.Cart;
import com.xinxian.shop.Prevalent.Prevalent;
import com.xinxian.shop.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.HashMap;
import java.util.Map;


public class CartActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    public static Map<String,Integer> itemPriceMap=null;
    public static Map<String,Integer> itemCountMap;
    TextView CartText;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    public int TotalCartPrice=0,itemCount=0;
    BottomNavigationView bottomNavCart;
    private int totalPrice=0;
    String indprice,enumber= String.valueOf(1);
     private int []a=new int[0];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


      CartText=findViewById(R.id.total_price);


        recyclerView=findViewById(R.id.cart_list);
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

        bottomNavCart=findViewById(R.id.bottomNaviCart);
        bottomNavCart.setOnNavigationItemSelectedListener(this);

        final DatabaseReference historyRef = FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(Prevalent.currentOnlineUser.getPhone());

        historyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                 bottomNavCart.getMenu().removeItem(R.id.nav_checkout);
                }
                else {
                    bottomNavCart.getMenu().removeItem(R.id.nav_processing);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    protected void onStart(){
        super.onStart();
       // CheckOrderState();

        final DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("CartList");
        final DatabaseReference historyRef = FirebaseDatabase.getInstance().getReference().child("History");

        final FirebaseRecyclerOptions<Cart>options=new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartRef.child("UserView").child(Prevalent.currentOnlineUser
                        .getPhone()).child("Products"),Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder>adapter=
                new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final CartViewHolder holder, int position, @NonNull final Cart model) {
                        if(CartActivity.itemPriceMap == null || CartActivity.itemCountMap == null){
                            CartActivity.itemPriceMap = new HashMap<>();
                            CartActivity.itemCountMap = new HashMap<>();
                        }
                        cartRef.child("UserView").child(Prevalent.currentOnlineUser
                                .getPhone()).child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snap: dataSnapshot.getChildren()) {
                                    itemCount+=snap.getChildrenCount();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }});
                        holder.cartIname.setText(model.getIname());
                        holder.cartIprice.setText(model.getIprice());
                        holder.cartIquantity.setText(model.getIquantity());
                        Picasso.get().load(model.getIimage()).into(holder.cartIimage);
                        holder.eCartBtn.setNumber(model.getInumber());

                       // String indprice= (String) holder.cartIprice.getText();
                        if(CartActivity.itemPriceMap.get(model.getIid()) == null){
                            CartActivity.itemPriceMap.put(model.getIid(),Integer.parseInt(model.getIprice()));
                            CartActivity.itemCountMap.put(model.getIid(),Integer.parseInt(model.getInumber()));
                        }else{
                            CartActivity.itemCountMap.put(model.getIid(),Integer.parseInt(model.getInumber()));
                        }

                        //For Total Price Count
                        holder.eCartBtn.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                            @Override
                            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                                final HashMap<String, Object> cMap = new HashMap<>();
                                cMap.put("inumber", holder.eCartBtn.getNumber());
                                cartRef.child("UserView").child(Prevalent.currentOnlineUser.getPhone()).child("Products")
                                        .child(model.getIid()).updateChildren(cMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){

                                                    cartRef.child("AdminView").child(Prevalent.currentOnlineUser.getPhone())
                                      .child("Products").child(model.getIid()).updateChildren(cMap)
                                                 .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task)
                                                                {

                                                                }
                                                    });
                                                }

                                            }
                                        });
                            }
                        });




                        int indProductPrice=(int)(((Integer.valueOf(model.getIprice())))*Integer.valueOf(model.getInumber()));
                     holder.cartIprice.setText(Integer.toString(indProductPrice));


                        // TotalCartPrice=TotalCartPrice-a[position];

                        //TotalCartPrice=TotalCartPrice+Integer.valueOf(model.getIprice());
                     totalPrice = 0;

                        if(CartActivity.itemPriceMap != null &&  CartActivity.itemCountMap != null){
                            for(String key : CartActivity.itemPriceMap.keySet()){
                                if(CartActivity.itemPriceMap.get(key) == null || CartActivity.itemCountMap.get(key) == null){
                                    continue;
                                }
                                totalPrice += CartActivity.itemPriceMap.get(key) * CartActivity.itemCountMap.get(key);


                            }
                        }
                        CartText.setText(("Total Price= " + String.valueOf(totalPrice)) +"৳");
                       // a[position]=indProductPrice;



                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {




                                CharSequence options[] = new CharSequence[]
                                        {
                                                "  Remove"
                                        };

                                AlertDialog.Builder builder=new AlertDialog.Builder(CartActivity.this);
                                builder.setTitle("Remove Items:").setIcon(R.drawable.groceyicon);

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i)
                                    {
                                        if (i == 0) {
                                            CartActivity.itemPriceMap.put(model.getIid(),null);
                                            CartActivity.itemCountMap.put(model.getIid(),null);
                                             totalPrice = 0;

                                            if(CartActivity.itemPriceMap != null &&  CartActivity.itemCountMap != null){
                                                for(String key : CartActivity.itemPriceMap.keySet()){
                                                    if(CartActivity.itemPriceMap.get(key) == null || CartActivity.itemCountMap.get(key) == null){
                                                        continue;
                                                    }
                                                    totalPrice += CartActivity.itemPriceMap.get(key) * CartActivity.itemCountMap.get(key);
                                                }
                                            }
                                            CartText.setText(("Total Price= " + String.valueOf(totalPrice)) +"৳");

                                            cartRef.child("UserView").child(Prevalent.currentOnlineUser.getPhone())
                                                    .child("Products").child(model.getIid())
                                                    .removeValue();
                                            cartRef.child("AdminView").child(Prevalent.currentOnlineUser.getPhone())
                                                    .child("Products").child(model.getIid())
                                                    .removeValue();
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
                    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout
                                .cart_item_layout, viewGroup, false);

                        CartViewHolder holder=new CartViewHolder(view);
                        return holder;
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public boolean onNavigationItemSelected(MenuItem item) {


        switch (item.getItemId()){
            case R.id.nav_home:
                Intent intent=new Intent(CartActivity.this,MainActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_checkout:

                            if (totalPrice!=0){
                                Intent intent2=new Intent(CartActivity.this,ConfirmOrder.class);
                                intent2.putExtra("subtotal",totalPrice);
                                CartActivity.itemPriceMap.clear();
                                CartActivity.itemCountMap.clear();
                                startActivity(intent2);

                            }
                            break;
                            

        }

        return true;


    }


    public void onBackPressed() {

        Intent intent2=new Intent(CartActivity.this,MainActivity.class);
        startActivity(intent2);
    }


}


