package com.example.schoolfoodlist.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolfoodlist.R;
import com.example.schoolfoodlist.item.FoodListItem;
import com.example.schoolfoodlist.view_holder.FoodListViewHolder;

import java.util.ArrayList;

public class FoodListAdapter extends RecyclerView.Adapter {

    Context mContext;
    ArrayList<FoodListItem> items;

    public FoodListAdapter(Context mContext, ArrayList<FoodListItem> items){
        this.mContext = mContext;
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.food_list_item, parent, false);
        FoodListViewHolder holder = new FoodListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        FoodListViewHolder vh = (FoodListViewHolder) holder;
        vh.day.setText(items.get(position).getfDay());
        vh.menu.setText(items.get(position).getfList());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
