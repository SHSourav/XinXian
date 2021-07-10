package com.xinxian.shop.ViewHolder;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xinxian.shop.R;

public class NotificationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ImageView NotificationImageView;

    public ItemClickListener itemClickListener;



    public NotificationViewHolder(@NonNull View itemView) {

        super(itemView);

        NotificationImageView=itemView.findViewById(R.id.NotificationImageView);


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
