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

import com.xinxian.shop.Model.addCP;
import com.xinxian.shop.ViewHolder.adminCategoryHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddCategory extends AppCompatActivity {

    protected FloatingActionButton fab;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference ProductsRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddCategory.this, addCategoryProduct.class);
                intent.putExtra("Pname","Category");
                startActivity(intent);
            }
        });

        recyclerView=findViewById(R.id.addCategory);
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

        ProductsRef= FirebaseDatabase.getInstance().getReference().child("Category");

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<addCP> options =
                new FirebaseRecyclerOptions.Builder<addCP>().setQuery(ProductsRef, addCP.class)
                        .build();

        FirebaseRecyclerAdapter<addCP, adminCategoryHolder> adapter = new FirebaseRecyclerAdapter
                <addCP, adminCategoryHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull adminCategoryHolder holder, final int position, @NonNull final addCP model) {
                try {


                    holder.CPnAME.setText(model.getNameEn() + "("+model.getNameBn()+")");

                }
                catch(NullPointerException e)

                    {
                        System.out.print("Problem");
                    }

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent=new Intent(AddCategory.this, AddProductNew.class);
                            intent.putExtra("CPid",model.getId());
                            intent.putExtra("CPnAME",model.getNameEn());

                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                           startActivity(intent);

                        }
                    });

                }

            @NonNull
            @Override
            public adminCategoryHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                          int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_category_product, parent, false);
                adminCategoryHolder holder = new adminCategoryHolder(view);
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

        Intent intent=new Intent(AddCategory.this,AdminHome.class);
        startActivity(intent);

    }


    }


