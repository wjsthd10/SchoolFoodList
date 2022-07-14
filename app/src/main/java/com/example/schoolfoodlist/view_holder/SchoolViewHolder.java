package com.example.schoolfoodlist.view_holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolfoodlist.R;

public class SchoolViewHolder extends RecyclerView.ViewHolder {

    public TextView sName, sAddress;

    public SchoolViewHolder(@NonNull View itemView) {
        super(itemView);
        sName = itemView.findViewById(R.id.school_name);
        sAddress = itemView.findViewById(R.id.school_address);
    }
}
