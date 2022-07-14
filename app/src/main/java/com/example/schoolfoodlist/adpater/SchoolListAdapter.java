package com.example.schoolfoodlist.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolfoodlist.R;
import com.example.schoolfoodlist.item.SchoolDataItem;
import com.example.schoolfoodlist.view_holder.SchoolViewHolder;

import java.util.ArrayList;

public class SchoolListAdapter extends RecyclerView.Adapter {

    Context mContext;
    ArrayList<SchoolDataItem> items;

    public SchoolListAdapter(Context mContext, ArrayList<SchoolDataItem> items) {
        this.mContext = mContext;
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.school_data_item, parent, false);
        SchoolViewHolder holder = new SchoolViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SchoolViewHolder vh = (SchoolViewHolder) holder;
        vh.sName.setText(items.get(position).getsName());
        vh.sAddress.setText(items.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
