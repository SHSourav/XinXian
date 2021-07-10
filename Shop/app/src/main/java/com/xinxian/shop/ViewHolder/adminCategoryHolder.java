package com.xinxian.shop.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.xinxian.shop.R;


public class adminCategoryHolder extends ViewHolder implements View.OnClickListener {


    public TextView CPnAME;

    public ItemClickListener itemClickListener;


    public adminCategoryHolder(@NonNull View itemView) {
        super(itemView);

        CPnAME = itemView.findViewById(R.id.cp);


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


