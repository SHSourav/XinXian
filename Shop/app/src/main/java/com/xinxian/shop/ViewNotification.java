package com.xinxian.shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xinxian.shop.Model.itemView;
import com.xinxian.shop.ViewHolder.NotificationViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ViewNotification extends AppCompatActivity {

        private RecyclerView productList;
        RecyclerView.LayoutManager layoutManager;
        private String myName;
        private DatabaseReference cartListRef;
        private String userID="";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_admin_view_orders);

           myName=getIntent().getExtras().getString("Pid");

            productList=findViewById(R.id.recycler_admin_view_orders);
            productList.setHasFixedSize(true);
            layoutManager=new LinearLayoutManager(this);
            productList.setLayoutManager(layoutManager);

            cartListRef= FirebaseDatabase.getInstance().getReference().child("notification");
        }

        protected void onStart(){
            super.onStart();


            FirebaseRecyclerOptions<itemView> options=new FirebaseRecyclerOptions.Builder<itemView>()
                    .setQuery(cartListRef,itemView.class)
                    .build();

            FirebaseRecyclerAdapter<itemView, NotificationViewHolder> adapter=new FirebaseRecyclerAdapter<itemView, NotificationViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull NotificationViewHolder holder, final int position, @NonNull final itemView model) {


                    Picasso.get().load(model.getIimage()).into(holder.NotificationImageView);
                    if(!myName.equals("")){
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                CharSequence options[] = new CharSequence[]
                                        {
                                                "  Delete"
                                        };

                                AlertDialog.Builder builder=new AlertDialog.Builder(ViewNotification.this);
                                builder.setTitle("Delete Notification:").setIcon(R.drawable.groceyicon);

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i)
                                    {
                                        if (i == 0) {
                                            String uID=getRef(position).getKey();
                                            cartListRef.child(uID).removeValue();
                                            StorageReference storageReference = FirebaseStorage.getInstance().
                                                    getReferenceFromUrl(model.getIimage());
                                            storageReference.delete();
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


                }

                @NonNull
                @Override
                public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                    View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout
                            .activity_view_notification, viewGroup, false);

                    NotificationViewHolder holder=new NotificationViewHolder(view);
                    return holder;
                }
            };

            productList.setAdapter(adapter);
            adapter.startListening();
        }
}