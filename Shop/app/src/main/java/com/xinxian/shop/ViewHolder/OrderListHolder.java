package com.xinxian.shop.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.xinxian.shop.R;

public class OrderListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView OrderListName, OrderListPrice,  OrderListQuantity,OrderListNumber;

        public ItemClickListener itemClickListener;



        public OrderListHolder(@NonNull View itemView) {

            super(itemView);

            OrderListName = itemView.findViewById(R.id.order_list_name);
            OrderListPrice = itemView.findViewById(R.id.order_list_price);
            OrderListQuantity = itemView.findViewById(R.id.order_list_quantity);


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



