package com.example.schoolfoodlist.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolfoodlist.R;

public class SchoolSearchFragment extends Fragment implements View.OnClickListener {

    Context mContext;
    EditText schoolNameEditor;
    ImageView searchBtn;
    RecyclerView schoolListView;

    public SchoolSearchFragment(Context mContext) {
        this.mContext = mContext;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        schoolListView = view.findViewById(R.id.school_list_view);
        searchBtn = view.findViewById(R.id.school_search_btn);
        schoolNameEditor = view.findViewById(R.id.school_name_edit_view);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.school_search_btn:
                break;
        }
    }
}
