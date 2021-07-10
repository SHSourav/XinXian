package com.xinxian.shop.ViewHolder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xinxian.shop.AddItem;
import com.xinxian.shop.Model.addP;
import com.xinxian.shop.R;

import java.util.ArrayList;
import java.util.List;

public class adminProductHolder extends RecyclerView.Adapter<adminProductHolder.ViewHolder>{
    LayoutInflater inflater;
    private Context context;
    List<addP> homeProducts;
    private Activity parentActivity;

    private ArrayList<addP> arraylist;

    public adminProductHolder (Context context, List<addP> homeProducts, Activity parentActivity){

        this.inflater = LayoutInflater.from(context);
        this.context=context;
        this.parentActivity=parentActivity;
        this.homeProducts = homeProducts;
        this.arraylist = new ArrayList<addP>();
        this.arraylist.addAll(homeProducts);

    }

    @NonNull

    @Override

    public adminProductHolder.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.add_category_product,parent,false);

        return new adminProductHolder.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final adminProductHolder.ViewHolder holder, final int position) {


     //   holder.CPnAME.setText(homeProducts.get(position).getCpNAme());

        //Start AlertDialog (adapter = new ProductViewHolderAdmin(getApplication(),hProducts,AdminMaintainProducts.this);)
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context, AddItem.class);
                intent.putExtra("CPid",homeProducts.get(position).getId());
              //  intent.putExtra("CPnAME",homeProducts.get(position).getCpNAme());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }

        });


    }



    @Override

    public int getItemCount() {

        return homeProducts.size();

    }



    public  class ViewHolder extends  RecyclerView.ViewHolder {

        TextView CPnAME;





        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            CPnAME = itemView.findViewById(R.id.cp);


        }


    }
}
