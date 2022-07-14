package com.example.schoolfoodlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceControl;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.schoolfoodlist.fragment.SchoolSearchFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    final static String KEY="9ee8be5c3531463f92125c15895e372f";


    TextView title;
    RecyclerView foodListView;
    ImageView searchBtn;

    //https://open.neis.go.kr/hub/mealServiceDietInfo?KEY=df2ba946955d4d1b8a9b9d767f09e25c&Type=json&pIndex=1&pSize=5&ATPT_OFCDC_SC_CODE=B10&SD_SCHUL_CODE=7091382&MLSV_FROM_YMD=20220702&MLSV_TO_YMD=20220722

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setLayout();
        setEvent();
    }

    private void setLayout(){
        title = findViewById(R.id.title_txt);
        foodListView = findViewById(R.id.food_list_view);
        searchBtn = findViewById(R.id.search_school_btn);
    }

    private void setEvent(){
        searchBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.search_school_btn:
                SchoolSearchFragment schoolSearchFragment = new SchoolSearchFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.root_view, schoolSearchFragment).addToBackStack(null).commitAllowingStateLoss();
                Log.e("yun_log", "is start fragment");
                break;
        }
    }
}