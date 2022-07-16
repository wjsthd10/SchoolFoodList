package com.example.schoolfoodlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceControl;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.schoolfoodlist.adpater.FoodListAdapter;
import com.example.schoolfoodlist.fragment.SchoolSearchFragment;
import com.example.schoolfoodlist.item.FoodListItem;
import com.example.schoolfoodlist.item.SchoolDataItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    final static String fAddress = "https://open.neis.go.kr/hub/mealServiceDietInfo?KEY=";
    final static String KEY="9ee8be5c3531463f92125c15895e372f";
    final static int CALL_FOOD_LIST = 20001;
    final static int FOOD_LIST_SET = 20002;

    TextView title;
    RecyclerView foodListView;
    ImageView searchBtn;
    LinearLayout mainProgress;
    ArrayList<FoodListItem> foodListItems = new ArrayList<>();

    String MLSV_FROM_YMD="20220701";// 날짜 정보 한달치 표시하기
    String MLSV_TO_YMD="20220729";

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
        mainProgress = findViewById(R.id.main_progress_lay);
    }

    private void setEvent(){
        searchBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.search_school_btn:
                SchoolSearchFragment schoolSearchFragment = new SchoolSearchFragment(handlerMain);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.root_view, schoolSearchFragment).addToBackStack(null).commitAllowingStateLoss();
                Log.e("yun_log", "is start fragment");
                break;

        }
    }

    public Handler handlerMain = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case CALL_FOOD_LIST:
                    mainProgress.setVisibility(View.VISIBLE);
                    SchoolDataItem schoolDataItem = (SchoolDataItem) msg.obj;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String address = fAddress+KEY
                                        +"&Type=json&pIndex=1&pSize=100" +
                                        "&ATPT_OFCDC_SC_CODE="+schoolDataItem.getAtptCode()
                                        +"&SD_SCHUL_CODE="+schoolDataItem.getsCode()
                                        +"&MLSV_FROM_YMD="+MLSV_FROM_YMD
                                        +"&MLSV_TO_YMD="+MLSV_TO_YMD;

                                URL url = new URL(address);
                                InputStream is = url.openStream();
                                InputStreamReader isr = new InputStreamReader(is);
                                BufferedReader reader = new BufferedReader(isr);

                                StringBuffer buffer = new StringBuffer();
                                String line = reader.readLine();
                                while (line != null) {
                                    buffer.append(line+"\n");
                                    line = reader.readLine();
                                }

                                String jsonData = buffer.toString();
                                JSONObject obj = new JSONObject(jsonData);
                                JSONArray obj2 = obj.getJSONArray("mealServiceDietInfo");
                                foodListItems.clear();
                                for (int i = 0; i < obj2.length(); i++) {
                                    if (obj2.get(i).toString().contains("row")){
                                        JSONObject row = obj2.getJSONObject(i);
                                        JSONArray rowArr = row.getJSONArray("row");
                                        for (int j = 0; j < rowArr.length(); j++) {
                                            JSONObject foodData = rowArr.getJSONObject(j);
                                            String day= foodData.getString("MLSV_FROM_YMD");
                                            String list = foodData.getString("DDISH_NM").replace("<br/>", "\n");
                                            foodListItems.add(new FoodListItem(day,list));
                                        }
                                    }
                                }

                                Message msg = new Message();
                                msg.what=FOOD_LIST_SET;
                                handlerMain.sendMessage(msg);

                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }finally {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mainProgress.setVisibility(View.GONE);
                                    }
                                });
                            }
                        }
                    }).start();
                    break;

                case FOOD_LIST_SET:// 식단표 리스트 보여주기.
                    mainProgress.setVisibility(View.GONE);
                    FoodListAdapter adapter = new FoodListAdapter(MainActivity.this, foodListItems);
                    foodListView.setAdapter(adapter);
                    break;
            }
            return false;
        }
    });




}