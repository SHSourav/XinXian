package com.xinxian.shop.ViewHolder;

import android.content.Context;
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
import com.xinxian.shop.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductViewAdapter extends
        RecyclerView.Adapter<ProductViewAdapter.ProductViewHolder> implements Filterable {
    private List<itemView> exampleList;
    private List<itemView> exampleListFull;


    @Override
    public Filter getFilter() {
        return examplefilter;
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        public TextView Cid, Pid, Iid, Iname, Iquantity, Iuom, Iprice, itemDiscount, itemInStock;
        public ImageView Iimage;

        public ItemClickListener itemClickListener;


        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            Iimage = itemView.findViewById(R.id.item_image);
            Iname = itemView.findViewById(R.id.item_name);
            Iquantity = itemView.findViewById(R.id.item_quantity);
            Iuom = itemView.findViewById(R.id.item_UOM);
            Iprice = itemView.findViewById(R.id.item_price);
           itemDiscount = itemView.findViewById(R.id.item_discount_price);
            itemInStock = itemView.findViewById(R.id.item_stock_out);


        }
    }

    public ProductViewAdapter(List<itemView> exampleList) {
        this.exampleList = exampleList;
        exampleListFull = new ArrayList<>(exampleList);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_display_layout, parent, false);
        return new ProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductViewHolder holder, final int position) {
        final itemView currentItem = exampleList.get(position);
        Picasso.get().load(currentItem.getIimage()).into(holder.Iimage);
        holder.Iname.setText(currentItem.getIname());
        holder.Iquantity.setText(currentItem.getIquantity());
        holder.Iuom.setText(currentItem.getIuom());
        holder.Iprice.setText("৳" + currentItem.getIprice());
        holder.itemInStock.setText(currentItem.getIinStock());
        if(!currentItem.getIdiscount().equals("")){

            holder.Iprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            holder.itemDiscount.setText("৳" + currentItem.getIdiscount());

        }
        else{
           //holder.itemDiscount.setText("");
        }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    Toast.makeText(v.getContext(), "Please Login To Add Product", Toast.LENGTH_SHORT).show();

                }


            });

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
