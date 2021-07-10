package com.xinxian.shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xinxian.shop.Model.itemView;
import com.xinxian.shop.ViewHolder.ProductViewAdapter;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NewUserCategorized extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        BottomNavigationView.OnNavigationItemSelectedListener {


    BottomNavigationView bottomNavigationView;
    SearchView searchView;
    String sharelink1;
    private ProductViewAdapter adapter;
    private List<itemView> exampleList=new ArrayList<>();
    String cid,pid,iid,iimage,iname,iquantity,iuom,iprice,idiscount,iinStock;
    private Query fb;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_home);

        final String cID=getIntent().getExtras().getString("Cid");
        fb=FirebaseDatabase.getInstance().getReference().child("Item").orderByChild("cid").equalTo(cID);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setBackgroundColor(getResources().getColor(R.color.colorDrawer));
        bottomNavigationView=findViewById(R.id.bottomNavigation);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.getMenu().removeItem(R.id.nav_account);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem item = menu.findItem(R.id.nav_cart);
        BadgeDrawable badge = bottomNavigationView.getOrCreateBadge(item.getItemId());







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
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(NewUserCategorized.this);
                adapter = new ProductViewAdapter(exampleList);
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
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem menuItem=menu.findItem(R.id.action_cart);
        searchView=(SearchView) menuItem.getActionView();
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
                Intent intent=new Intent(NewUserCategorized.this,ViewNotification.class);
                intent.putExtra("Pid","");
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_orders) {
            Toast toast=Toast.makeText(this, "  Please Login To View Your Orders  "
                    , Toast.LENGTH_SHORT);
        }  else if (id == R.id.nav_Cooking) {
            Intent intent = new Intent(NewUserCategorized.this, CategorizedItem.class);
            intent.putExtra("Cid","Cooking");
            startActivity(intent);

        }else if (id == R.id.nav_health) {
            Intent intent = new Intent(NewUserCategorized.this, CategorizedItem.class);
            intent.putExtra("Cid","Health Care");
            startActivity(intent);

        }else if (id == R.id.nav_personal) {
            Intent intent = new Intent(NewUserCategorized.this, CategorizedItem.class);
            intent.putExtra("Cid","Personal Care");
            startActivity(intent);

        }else if (id == R.id.nav_dairy) {
            Intent intent = new Intent(NewUserCategorized.this, CategorizedItem.class);
            intent.putExtra("Cid","Dairy");
            startActivity(intent);

        }else if (id == R.id.nav_drinks) {
            Intent intent = new Intent(NewUserCategorized.this, CategorizedItem.class);
            intent.putExtra("Cid","Drinks");
            startActivity(intent);

        }else if (id == R.id.nav_bakery) {
            Intent intent = new Intent(NewUserCategorized.this, CategorizedItem.class);
            intent.putExtra("Cid","Bakery");
            startActivity(intent);

        }else if (id == R.id.nav_homeappliance) {
            Intent intent = new Intent(NewUserCategorized.this, CategorizedItem.class);
            intent.putExtra("Cid","Home Appliances");
            startActivity(intent);

        }else if (id == R.id.nav_fruits) {
            Intent intent = new Intent(NewUserCategorized.this, CategorizedItem.class);
            intent.putExtra("Cid","Fruits & Vegetables");
            startActivity(intent);

        }else if (id == R.id.nav_kids) {
            Intent intent = new Intent(NewUserCategorized.this, CategorizedItem.class);
            intent.putExtra("Cid","Kids");
            startActivity(intent);

        }else if (id == R.id.nav_party) {
            Intent intent = new Intent(NewUserCategorized.this, CategorizedItem.class);
            intent.putExtra("Cid","Party");
            startActivity(intent);

        }else if (id == R.id.nav_fashion) {
            Intent intent = new Intent(NewUserCategorized.this, CategorizedItem.class);
            intent.putExtra("Cid","Fashion");
            startActivity(intent);

        }else if (id == R.id.nav_electronics) {
            Intent intent = new Intent(NewUserCategorized.this, CategorizedItem.class);
            intent.putExtra("Cid","Hardware & Electronics");
            startActivity(intent);

        }
        else if (id == R.id.nav_medicine) {
            Intent intent = new Intent(NewUserCategorized.this, CategorizedItem.class);
            intent.putExtra("Cid","Medicine");
            startActivity(intent);

        } else if (id == R.id.nav_contact) {

            Intent intent=new Intent(NewUserCategorized.this,ContactUs.class);
            startActivity(intent);

        }  else if (id == R.id.nav_share) {

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
            Toast toast=Toast.makeText(this, "  You Are Not Logged In "
                    , Toast.LENGTH_SHORT);

        }


        switch (item.getItemId()){
            case R.id.nav_cart:
                Toast toast=Toast.makeText(this, "  Please Login To Visit Cart  "
                        , Toast.LENGTH_SHORT);
                break;
            case R.id.nav_home:
                Intent intent1=new Intent(NewUserCategorized.this,NewUserHome.class);
                startActivity(intent1);
                break;
            case R.id.nav_login:
                Intent intent2=new Intent(NewUserCategorized.this,LoginActivity.class);
                startActivity(intent2);
                break;



        }

        return true;

    }

    public void onBackPressed() {

        Intent intent2=new Intent(NewUserCategorized.this,NewUserHome.class);
        startActivity(intent2);
    }
}

