package com.xinxian.shop.ViewHolder;


import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.xinxian.shop.R;

public class AdminViewOrderHolder extends ViewHolder implements View.OnClickListener{


        public TextView OVAName,OVATotal,OVATime,OVAAddress,OVANumber;
        public Button orderCall,orderViewList;
        public ItemClickListener itemClickListener;



        public  AdminViewOrderHolder(@NonNull View itemView) {

            super(itemView);

            OVAName= itemView.findViewById(R.id.admin_order_view_name);
            OVATotal = itemView.findViewById(R.id.admin_order_view_total);
            OVATime = itemView.findViewById(R.id.admin_order_view_date);
            OVAAddress= itemView.findViewById(R.id.admin_order_view_address);
            OVANumber = itemView.findViewById(R.id.admin_order_view_number);
            orderCall = itemView.findViewById(R.id.admin_order_view_call);
            orderViewList=itemView.findViewById(R.id.admin_order_view_listbtn);




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
