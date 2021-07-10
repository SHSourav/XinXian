package com.xinxian.shop.ViewHolder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.xinxian.shop.R;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;


public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
       public TextView cartIname, cartIprice,  cartIquantity;
       public ImageView cartIimage;
      public  ImageButton CatrIdelete;
      public  ElegantNumberButton eCartBtn;
    public ItemClickListener itemClickListener;



        public CartViewHolder(@NonNull View itemView) {

            super(itemView);

            cartIname = itemView.findViewById(R.id.cart_product_name);
            cartIprice = itemView.findViewById(R.id.cart_product_price);
            cartIquantity = itemView.findViewById(R.id.cart_product_quantity);
            cartIimage = itemView.findViewById(R.id.cart_product_image);
            eCartBtn=itemView.findViewById(R.id.cart_number_btn);


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


