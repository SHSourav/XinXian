package com.xinxian.shop;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;


public class AdminHome extends AppCompatActivity {

    private ImageView AddProductAdmin;
    GridLayout mainGrid;
    private PendingIntent pendingIntent;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);


        AddProductAdmin=(ImageView) findViewById(R.id.addProduct);
        mainGrid=(GridLayout)findViewById(R.id.mainGrid);
        action(mainGrid);


        final DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() != 0) {
                      notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationBuilder = new NotificationCompat.Builder(AdminHome.this)
                            .setContentTitle("New Order Arrive").setSmallIcon(R.mipmap.ic_launcher).setContentIntent(pendingIntent)
                            .setContentText("Please place the order");
                    //add sound
                    Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    notificationBuilder.setSound(uri);
                    //vibrate
                    long[] v1 = {200, 500};
                    notificationBuilder.setVibrate(v1);
                    notificationManager.notify(1, notificationBuilder.build());


                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException(); // Don't ignore errors
            }
        });
       /*AddProductAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });*/
    }





    private void action(GridLayout mainGrid) {
        for(int i=0;i<mainGrid.getChildCount();i++)
        {
            CardView cardView=(CardView)mainGrid.getChildAt(i);
            final int f=i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(f==0){
                        Intent intent=new Intent(AdminHome.this, AddCategory.class);
                        startActivity(intent);
                    }

                    if(f==1){
                        Intent intent=new Intent(AdminHome.this, AdminUpdateOthers.class);
                        startActivity(intent);
                    }

                    if(f==2){
                        Intent intent=new Intent(AdminHome.this,AdminViewOrders.class);
                        startActivity(intent);
                    }

                    if(f==3){
                        Intent intent=new Intent(AdminHome.this,StockOutProducts.class);
                        startActivity(intent);
                    }

                    if(f==4){
                        Intent intent=new Intent(AdminHome.this,AddNotification.class);
                        startActivity(intent);
                    }
                    if(f==5){
                        Paper.book().destroy();
                        Intent intent=new Intent(AdminHome.this,LoginActivity.class);
                        startActivity(intent);
                    }

                }
            });
        }
    }

    private void createNotification()
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            CharSequence charSequence="New Order Arrived";
            String description="Please confirm Order";
            int importance=NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel=new NotificationChannel("notifylemubit",charSequence,importance);
            channel.setDescription(description);

            NotificationManager notificationManager=getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
    }
    }

    public void onBackPressed() {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AdminHome.this);
        builder.setTitle(Html.fromHtml("<font color='#00b894'>XinXian</font>"));
        builder.setIcon(R.mipmap.icon_round);
        builder.setMessage("Do you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        android.app.AlertDialog alert = builder.create();
        alert.show();

    }

}
