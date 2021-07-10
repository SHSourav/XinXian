package com.xinxian.shop.ViewHolder;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.xinxian.shop.R;


public class AdditemHolder extends ViewHolder implements View.OnClickListener {


    public TextView Cid,Pid,Iid,Iname,Iquantity,Iuom,Iprice,itemDiscount,itemInStock;
    public ImageView Iimage;

    public ItemClickListener itemClickListener;


    public AdditemHolder(@NonNull View itemView) {
        super(itemView);

        Iimage = itemView.findViewById(R.id.item_image);
        Iname = itemView.findViewById(R.id.item_name);
        Iquantity = itemView.findViewById(R.id.item_quantity);
        Iuom = itemView.findViewById(R.id.item_UOM);
        Iprice = itemView.findViewById(R.id.item_price);
        itemDiscount = itemView.findViewById(R.id.item_discount_price);
        itemInStock = itemView.findViewById(R.id.item_stock_out);



    }



    public void setItemClickListener(ItemClickListener listener)
    {
        this.itemClickListener=listener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onCliclk(v,getAdapterPosition(),false);

    }

}


