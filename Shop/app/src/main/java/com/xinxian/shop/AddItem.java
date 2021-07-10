package com.xinxian.shop;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xinxian.shop.Model.itemView;
import com.xinxian.shop.ViewHolder.AdditemHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AddItem extends AppCompatActivity {
    TextView APText;
    protected FloatingActionButton fab;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private Query ProductsRef;
    private DatabaseReference ProductsRef2;
    String pID,cID,pNAME;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

         pID=getIntent().getExtras().getString("Pid");
        cID=getIntent().getExtras().getString("Cid");
        pNAME=getIntent().getExtras().getString("PnAME");

        APText=findViewById(R.id.ProductItemName);
        APText.setText(pNAME);

        fab=(FloatingActionButton)findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddItem.this, AddItemDetails.class);
                intent.putExtra("Pid",pID);
                intent.putExtra("Cid",cID);
                startActivity(intent);
            }
        });

        recyclerView=findViewById(R.id.addItem);
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

        ProductsRef= FirebaseDatabase.getInstance().getReference().child("Item").orderByChild("pid").equalTo(pID);
        ProductsRef2= FirebaseDatabase.getInstance().getReference().child("Item");
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<itemView> options =
                new FirebaseRecyclerOptions.Builder<itemView>().setQuery(ProductsRef, itemView.class)
                        .build();

        FirebaseRecyclerAdapter<itemView, AdditemHolder> adapter = new FirebaseRecyclerAdapter
                <itemView, AdditemHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdditemHolder holder, final int position, @NonNull final itemView model) {
                try {


                    Picasso.get().load(model.getIimage()).into(holder.Iimage);
                    holder.Iname.setText(model.getIname());
                    holder.Iprice.setText("৳" + model.getIprice());
                    holder.Iquantity.setText(model.getIquantity());
                    holder.itemInStock.setText(model.getIinStock());
                    holder.Iuom.setText(model.getIuom());
                    if(!model.getIdiscount().equals("")){
                        holder.itemDiscount.setText("৳"+model.getIdiscount());
                        holder.Iprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                    }
                   else {
                        holder.itemDiscount.setText("");
                    }


                }
                catch(NullPointerException e)

                {
                    System.out.print("Problem");
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        CharSequence options[] = new CharSequence[]
                                {
                                        "  Update",
                                        "  Stock Out",
                                        "  Delete"
                                };

                        AlertDialog.Builder builder=new AlertDialog.Builder(AddItem.this);
                        builder.setTitle("Maintain Products:").setIcon(R.drawable.groceyicon);

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                if (i == 0) {
                                    Intent intent=new Intent(AddItem.this, UpdateProducts.class);
                                    intent.putExtra("iID",model.getIid());
                                    startActivity(intent);
                                }

                                if (i == 1)
                                {
                                    String uID=getRef(position).getKey();
                                    ProductsRef2.child(uID);
                                    HashMap<String, Object> stockoutmap = new HashMap<>();
                                    stockoutmap.put("iinStock","Stock Out");
                                    ProductsRef2.child(uID).updateChildren(stockoutmap);
                                }
                                if (i == 2)
                                {
                                    String uID=getRef(position).getKey();
                                    ProductsRef2.child(uID).removeValue();
                                    StorageReference storageReference = FirebaseStorage.getInstance().
                                            getReferenceFromUrl(model.getIimage());
                                    storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // File deleted
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            // Error
                                        }
                                    });
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
            public AdditemHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                          int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_display_layout, parent,
                        false);
                AdditemHolder holder = new AdditemHolder(view);
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

        Intent intent=new Intent(AddItem.this,AddCategory.class);
        startActivity(intent);

    }

}


