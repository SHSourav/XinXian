package com.xinxian.shop.ViewHolder;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xinxian.shop.Model.itemView;
import com.xinxian.shop.Prevalent.Prevalent;
import com.xinxian.shop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ExampleAdapter extends
        RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> implements Filterable {
    private List<itemView> exampleList;
    private List<itemView> exampleListFull;

    @Override
    public Filter getFilter() {
        return examplefilter;
    }

    class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView Cid, Pid, Iid, Iname, Iquantity, Iuom, Iprice, itemDiscount, itemInStock;
        public ImageView Iimage,alreadyAdded;

        public ItemClickListener itemClickListener;


        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);

            Iimage = itemView.findViewById(R.id.item_image);
            Iname = itemView.findViewById(R.id.item_name);
            Iquantity = itemView.findViewById(R.id.item_quantity);
            Iuom = itemView.findViewById(R.id.item_UOM);
            Iprice = itemView.findViewById(R.id.item_price);
            itemDiscount = itemView.findViewById(R.id.item_discount_price);
            itemInStock = itemView.findViewById(R.id.item_stock_out);
            alreadyAdded=itemView.findViewById(R.id.item_add_cart_Image);


        }
    }

    public ExampleAdapter(List<itemView> exampleList) {
        this.exampleList = exampleList;
        exampleListFull = new ArrayList<>(exampleList);
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_display_layout, parent, false);
        return new ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ExampleViewHolder holder, final int position) {
        final itemView currentItem = exampleList.get(position);
        Picasso.get().load(currentItem.getIimage()).into(holder.Iimage);
        holder.Iname.setText(currentItem.getIname());
        holder.Iquantity.setText(currentItem.getIquantity());
        holder.Iuom.setText(currentItem.getIuom());
        holder.Iprice.setText("৳" + currentItem.getIprice());
        holder.itemDiscount.setText("৳" + currentItem.getIdiscount());
        holder.itemInStock.setText(currentItem.getIinStock());


        if(!currentItem.getIdiscount().equals("")){

            holder.Iprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

        }
        else{

            holder.itemDiscount.setText("");
        }


        if(!currentItem.getIinStock().equals("")){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast toast=Toast.makeText(v.getContext(), "  Sorry you have selected stockout product  "
                            , Toast.LENGTH_SHORT);
                    View view = toast.getView();
                    view.setBackgroundResource(R.drawable.round_toast);
                    toast.show();
                }
            });

        }


               else {
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {


                                final String saveCurrentTime, saveCurrentDate;
                                Calendar calForDate = Calendar.getInstance();
                                SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
                                saveCurrentDate = currentDate.format(calForDate.getTime());

                                final String mobile = Prevalent.currentOnlineUser.getPhone();


                                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                                saveCurrentTime = currentTime.format(calForDate.getTime());

                                final DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("CartList");


                                            final HashMap<String, Object> cMap = new HashMap<>();
                                            cMap.put("iid", currentItem.getIid());
                                            cMap.put("iimage", currentItem.getIimage());
                                            cMap.put("iname", currentItem.getIname());
                                            cMap.put("iquantity", currentItem.getIquantity() + currentItem.getIuom());
                                            if (currentItem.getIdiscount().equals("")) {
                                                cMap.put("iprice", currentItem.getIprice());
                                            } else {
                                                cMap.put("iprice", currentItem.getIdiscount());
                                            }
                                            cMap.put("itime", saveCurrentTime);
                                            cMap.put("idate", saveCurrentDate);
                                            cMap.put("inumber", "1");




                                            cartRef.child("UserView").child(mobile).child("Products")
                                                    .child(currentItem.getIid()).updateChildren(cMap)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                // historyRef.child(mobile).child(saveCurrentDate).child(currentItem.getIid()).updateChildren(cMap);

                                                                cartRef.child("AdminView").child(mobile)
                                                                        .child("Products").child(currentItem.getIid()).updateChildren(cMap)
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {


                                                                                if (task.isSuccessful()) {

                                                                                    Toast.makeText(v.getContext(), currentItem.getIname() +
                                                                                            " Added To Cart..", Toast.LENGTH_SHORT).show();

                                                                                }
                                                                            }
                                                                        });
                                                            }

                                                        }
                                                    });//

                            }


                        });
                    }


            }


    @Override
    public int getItemCount() {
        return exampleList.size();
    }

    private Filter examplefilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<itemView> filterlist = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {

                filterlist.addAll(exampleListFull);
            } else {
                String pattrn = constraint.toString().toLowerCase().trim();
                for (itemView item : exampleListFull) {
                    if (item.getIname().toLowerCase().contains(pattrn)) {
                        filterlist.add(item);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filterlist;
            return filterResults;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            exampleList.clear();
            exampleList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
