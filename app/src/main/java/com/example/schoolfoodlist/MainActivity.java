package com.example.schoolfoodlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    final static String KEY="9ee8be5c3531463f92125c15895e372f";

    TextView title;
    RecyclerView foodListView;
    ImageView searchBtn;

    //https://open.neis.go.kr/hub/schoolInfo?KEY=df2ba946955d4d1b8a9b9d767f09e25c&Type=json&pIndex=1&pSize=5&SD_SCHUL_CODE=7091382
    //https://open.neis.go.kr/hub/mealServiceDietInfo?KEY=df2ba946955d4d1b8a9b9d767f09e25c&Type=json&pIndex=1&pSize=5&ATPT_OFCDC_SC_CODE=B10&SD_SCHUL_CODE=7091382&MLSV_FROM_YMD=20220702&MLSV_TO_YMD=20220722
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setLayout();
    }

    private void setLayout(){
        title = findViewById(R.id.title_txt);
        foodListView = findViewById(R.id.food_list_view);
        searchBtn = findViewById(R.id.search_school_btn);
    }
}