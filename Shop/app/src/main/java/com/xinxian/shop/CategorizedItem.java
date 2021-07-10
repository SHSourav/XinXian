package com.xinxian.shop;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.xinxian.shop.Model.itemView;
import com.xinxian.shop.Prevalent.Prevalent;
import com.xinxian.shop.ViewHolder.ExampleAdapter;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;


public class CategorizedItem extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        BottomNavigationView.OnNavigationItemSelectedListener {


    BottomNavigationView bottomNavigationView;
    private TextView upName,upAddress,upPhone;
    private ExampleAdapter adapter;
    String sharelink1;
    private Query fb;
    private List<itemView> exampleList=new ArrayList<>();
    String cid,pid,iid,iimage,iname,iquantity,iuom,iprice,idiscount,iinStock;

    DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("CartList").child("UserView")
            .child(Prevalent.currentOnlineUser.getPhone()).child("Products");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String cID=getIntent().getExtras().getString("Cid");
        fb=FirebaseDatabase.getInstance().getReference().child("Item").orderByChild("cid").equalTo(cID);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setBackgroundColor(getResources().getColor(R.color.colorDrawer));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        bottomNavigationView=findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.getMenu().removeItem(R.id.nav_login);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem item = menu.findItem(R.id.nav_cart);
        final BadgeDrawable badge = bottomNavigationView.getOrCreateBadge(item.getItemId());
        cartRef.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count = (int) dataSnapshot.getChildrenCount();
                int i=count;
                badge.setNumber(i);
                if(i==0)
                    badge.clearNumber();
            }
            public void onCancelled(DatabaseError databaseError) { }
        });



        //header_user_information
        upName = headerView.findViewById(R.id.user_profile_name);
        upAddress=headerView.findViewById(R.id.user_profile_address);
        upPhone=headerView.findViewById(R.id.user_profile_number);

        upName.setText(Prevalent.currentOnlineUser.getName());
        upPhone.setText(Prevalent.currentOnlineUser.getPhone());
        upAddress.setText(Prevalent.currentOnlineUser.getAddress());

        // fillExampleList();



    }
    public void onStart() {
        super.onStart();

        fb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                exampleList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    cid=ds.child("cid").getValue().toString();
                    pid=ds.child("pid").getValue().toString();
                    iid = ds.child("iid").getValue().toString();
                    iimage = ds.child("iimage").getValue().toString();
                    iname = ds.child("iname").getValue().toString();
                    iquantity = ds.child("iquantity").getValue().toString();
                    iinStock = ds.child("iinStock").getValue().toString();
                    idiscount= ds.child("idiscount").getValue().toString();
                    iuom= ds.child("iuom").getValue().toString();
                    iprice= ds.child("iprice").getValue().toString();
                    itemView item=new itemView(cid,pid,iid,iimage,iname,iquantity,iuom,iprice,idiscount,iinStock);
                    exampleList.add(item);
                }

                RecyclerView recyclerView = findViewById(R.id.recycler_menu);
                recyclerView.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CategorizedItem.this);
                adapter = new ExampleAdapter(exampleList);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem menuItem=menu.findItem(R.id.action_cart);
        SearchView searchView=(SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search Here");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                String text = newText;
                adapter.getFilter().filter(text);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.action_notification:
                Intent intent=new Intent(CategorizedItem.this,ViewNotification.class);
                intent.putExtra("Pid","");
                startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_orders) {
            Intent intent=new Intent(CategorizedItem.this,MyOrders.class);
            startActivity(intent);
        } else if (id == R.id.nav_Cooking) {
            Intent intent = new Intent(CategorizedItem.this, CategorizedItem.class);
            intent.putExtra("Cid","Cooking");
            startActivity(intent);

        }else if (id == R.id.nav_health) {
            Intent intent = new Intent(CategorizedItem.this, CategorizedItem.class);
            intent.putExtra("Cid","Health Care");
            startActivity(intent);

        }else if (id == R.id.nav_personal) {
            Intent intent = new Intent(CategorizedItem.this, CategorizedItem.class);
            intent.putExtra("Cid","Personal Care");
            startActivity(intent);

        }else if (id == R.id.nav_dairy) {
            Intent intent = new Intent(CategorizedItem.this, CategorizedItem.class);
            intent.putExtra("Cid","Dairy");
            startActivity(intent);

        }else if (id == R.id.nav_drinks) {
            Intent intent = new Intent(CategorizedItem.this, CategorizedItem.class);
            intent.putExtra("Cid","Drinks");
            startActivity(intent);

        }else if (id == R.id.nav_bakery) {
            Intent intent = new Intent(CategorizedItem.this, CategorizedItem.class);
            intent.putExtra("Cid","Bakery");
            startActivity(intent);

        }else if (id == R.id.nav_homeappliance) {
            Intent intent = new Intent(CategorizedItem.this, CategorizedItem.class);
            intent.putExtra("Cid","Home Appliances");
            startActivity(intent);

        }else if (id == R.id.nav_fruits) {
            Intent intent = new Intent(CategorizedItem.this, CategorizedItem.class);
            intent.putExtra("Cid","Fruits & Vegetables");
            startActivity(intent);

        }else if (id == R.id.nav_kids) {
            Intent intent = new Intent(CategorizedItem.this, CategorizedItem.class);
            intent.putExtra("Cid","Kids");
            startActivity(intent);

        }else if (id == R.id.nav_party) {
            Intent intent = new Intent(CategorizedItem.this, CategorizedItem.class);
            intent.putExtra("Cid","Party");
            startActivity(intent);

        }else if (id == R.id.nav_fashion) {
            Intent intent = new Intent(CategorizedItem.this, CategorizedItem.class);
            intent.putExtra("Cid","Fashion");
            startActivity(intent);

        }else if (id == R.id.nav_electronics) {
            Intent intent = new Intent(CategorizedItem.this, CategorizedItem.class);
            intent.putExtra("Cid","Hardware & Electronics");
            startActivity(intent);

        }
        else if (id == R.id.nav_medicine) {
            Intent intent = new Intent(CategorizedItem.this, CategorizedItem.class);
            intent.putExtra("Cid","Medicine");
            startActivity(intent);

        } else if (id == R.id.nav_contact) {

            Intent intent=new Intent(CategorizedItem.this,ContactUs.class);
            startActivity(intent);

        } else if (id == R.id.nav_share) {

            DatabaseReference OtherRef= FirebaseDatabase.getInstance().getReference().child("Others");
            OtherRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists()){

                        sharelink1 = dataSnapshot.child("sharelink").getValue().toString();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            Intent Share=new Intent(Intent.ACTION_SEND);
            Share.setType("text/plain");
            String body=sharelink1;
            String subject="Please Share The App:";

            Share.putExtra(Intent.EXTRA_TEXT,body);
            Share.putExtra(Intent.EXTRA_SUBJECT,subject);

            startActivity(Share);


        }  else if (id == R.id.nav_logout) {
            Paper.book().destroy();
            Intent intent=new Intent(CategorizedItem.this,NewUserHome.class);
            startActivity(intent);

        }



        switch (item.getItemId()){
            case R.id.nav_cart:
                Intent intent=new Intent(CategorizedItem.this, CartActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_home:
                Intent intent1=new Intent(CategorizedItem.this,MainActivity.class);
                startActivity(intent1);
                break;
            case R.id.nav_account:
                Intent intent2=new Intent(CategorizedItem.this,MyAccount.class);
                startActivity(intent2);
                break;



        }

        return true;


    }
    public void onBackPressed() {

        Intent intent=new Intent(CategorizedItem.this,MainActivity.class);
        startActivity(intent);

    }

}
