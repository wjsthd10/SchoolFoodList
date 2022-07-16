package com.example.schoolfoodlist.view_holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolfoodlist.R;

public class FoodListViewHolder extends RecyclerView.ViewHolder {

    public TextView day, menu;

    public FoodListViewHolder(@NonNull View itemView) {
        super(itemView);
        day = itemView.findViewById(R.id.food_day);
        menu = itemView.findViewById(R.id.food_menu);
    }
}
