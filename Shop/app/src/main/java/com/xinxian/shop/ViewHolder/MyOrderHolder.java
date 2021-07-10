package com.xinxian.shop.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xinxian.shop.R;


public class MyOrderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView UserOrderDate, UserOrderTime,UserOrderState,UserORderTotal;

    public ItemClickListener itemClickListener;



    public MyOrderHolder(@NonNull View itemView) {

        super(itemView);

        UserOrderDate = itemView.findViewById(R.id.order_number);
        UserOrderTime = itemView.findViewById(R.id.order_date);
        UserOrderState = itemView.findViewById(R.id.order_state);
        UserORderTotal= itemView.findViewById(R.id.order_total);


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




