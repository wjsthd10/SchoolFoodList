package com.example.schoolfoodlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.schoolfoodlist.adpater.FoodListAdapter;
import com.example.schoolfoodlist.adpater.SchoolListAdapter;
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

    final static int SEARCH_FINISH = 10001;
    final static int SEARCH_FAILED = 10002;
    final static int SELECT_SCHOOL = 10003;

    TextView title;
    RecyclerView foodListView;
    ImageView searchBtn;
    LinearLayout mainProgress;
    ConstraintLayout rootView;
    ArrayList<FoodListItem> foodListItems = new ArrayList<>();
    AlertDialog schoolListDialog;

    String MLSV_FROM_YMD="20220701";// 날짜 정보 한달치 표시하기
    String MLSV_TO_YMD="20220729";

    final static String schoolAddress = "https://open.neis.go.kr/hub/schoolInfo";
    //https://open.neis.go.kr/hub/mealServiceDietInfo?KEY=df2ba946955d4d1b8a9b9d767f09e25c&Type=json&pIndex=1&pSize=5&ATPT_OFCDC_SC_CODE=B10&SD_SCHUL_CODE=7091382&MLSV_FROM_YMD=20220702&MLSV_TO_YMD=20220722

    ArrayList<SchoolDataItem> schoolDataItems = new ArrayList<>();


    RecyclerView schoolListView;
    SchoolListAdapter schoolAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setLayout();
        setEvent();
    }

    private void setLayout(){
        rootView = findViewById(R.id.root_view);
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
            case R.id.search_school_btn:                ConstraintLayout dialogView = (ConstraintLayout) LayoutInflater.from(this).inflate(R.layout.search_fragment, rootView, false);

                schoolListView = dialogView.findViewById(R.id.school_list_view);
                ImageView schoolSearchBtn = dialogView.findViewById(R.id.school_search_btn);
                EditText schoolNameEditor = dialogView.findViewById(R.id.school_name_edit_view);

                schoolSearchBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mainProgress.setVisibility(View.VISIBLE);
                        String schoolName = schoolNameEditor.getText().toString();
                        if (schoolName.equals("") || schoolName.contains("-") || schoolName.contains(".") || schoolName.contains(",") || schoolName.equals(" ")){
                            mainProgress.setVisibility(View.GONE);
                            return;
                        }
                        String address = schoolAddress
                                +"?KEY="+KEY
                                +"&Type=json&pIndex=1&pSize=5"
                                +"&SCHUL_NM="+schoolName;
                        getSchoolName(address);
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setView(dialogView);
                schoolListDialog = builder.create();
                schoolListDialog.show();

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
//1.난류, 2.우유, 3.메밀, 4.땅콩, 5.대두, 6.밀, 7.고등어, 8.게, 9.새우, 10.돼지고기, 11.복숭아, 12.토마토, 13.아황산염, 14.호두, 15.닭고기, 16.쇠고기, 17.오징어, 18.조개류(굴,전복,홍합 등)
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
                                            String list = foodData.getString("DDISH_NM");

                                            foodListItems.add(new FoodListItem(day,replaceStr(list)));
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

                case SEARCH_FINISH:
                    mainProgress.setVisibility(View.GONE);
                    schoolAdapter = new SchoolListAdapter(MainActivity.this, schoolDataItems, handlerMain);
                    schoolListView.setAdapter(schoolAdapter);
                    schoolAdapter.notifyDataSetChanged();
                    break;
                case SEARCH_FAILED:
                    break;
                case SELECT_SCHOOL:
                    mainProgress.setVisibility(View.GONE);
                    SchoolDataItem items = (SchoolDataItem) msg.obj;
                    Message message = new Message();
                    message.what = CALL_FOOD_LIST;
                    message.obj = items;
                    handlerMain.sendMessage(message);
                    schoolListDialog.dismiss();
                    break;
            }
            return false;
        }
    });

    private String replaceStr(String list){
        String retVal=list;
        retVal = retVal.replace("<br/>", "\n");
        retVal = retVal.replace("(", "\n(");
        if (retVal.contains("18.")) { retVal = retVal.replace("18.","조개류[굴,전복,홍합 등]"); }
        if (retVal.contains("17.")) { retVal = retVal.replace("17.","오징어."); }
        if (retVal.contains("16.")) { retVal = retVal.replace("16.","쇠고기."); }
        if (retVal.contains("15.")) { retVal = retVal.replace("15.","닭고기."); }
        if (retVal.contains("14.")) { retVal = retVal.replace("14.","호두."); }
        if (retVal.contains("13.")) { retVal = retVal.replace("13.","아황산염."); }
        if (retVal.contains("12.")) { retVal = retVal.replace("12.","토마토."); }
        if (retVal.contains("11.")) { retVal = retVal.replace("11.","복숭아."); }
        if (retVal.contains("10.")) { retVal = retVal.replace("10.","돼지고기."); }
        if (retVal.contains("9.")) { retVal = retVal.replace("9.","새우."); }
        if (retVal.contains("8.")) { retVal = retVal.replace("8.","게."); }
        if (retVal.contains("7.")) { retVal = retVal.replace("7.","고등어."); }
        if (retVal.contains("6.")) { retVal = retVal.replace("6.","밀."); }
        if (retVal.contains("5.")) { retVal = retVal.replace("5.","대두."); }
        if (retVal.contains("4.")) { retVal = retVal.replace("4.","땅콩."); }
        if (retVal.contains("3.")) { retVal = retVal.replace("3.","메밀."); }
        if (retVal.contains("2.")) { retVal = retVal.replace("2.","우유."); }
        if (retVal.contains("1.")) { retVal = retVal.replace("1.","난류."); }

        return retVal;
    }

    private void getSchoolName(String address){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
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
                    JSONArray obj2 = obj.getJSONArray("schoolInfo");
                    schoolDataItems.clear();
                    for (int i = 0; i < obj2.length(); i++) {
                        if (obj2.get(i).toString().contains("row")){
                            JSONObject row = obj2.getJSONObject(i);
                            JSONArray rowArr = row.getJSONArray("row");
                            for (int j = 0; j < rowArr.length(); j++) {
                                JSONObject schoolData = rowArr.getJSONObject(j);
                                SchoolDataItem item = new SchoolDataItem();
                                item.setsName(schoolData.getString("SCHUL_NM"));
                                item.setsCode(schoolData.getString("SD_SCHUL_CODE"));
                                item.setAddress(schoolData.getString("ORG_RDNMA"));
                                item.setAtptCode(schoolData.getString("ATPT_OFCDC_SC_CODE"));
                                schoolDataItems.add(item);
                            }
                        }
                    }

                    Message msg = new Message();
                    msg.what=SEARCH_FINISH;
                    handlerMain.sendMessage(msg);

                } catch (MalformedURLException e) {
                    searchFailed();
                    e.printStackTrace();
                } catch (IOException e) {
                    searchFailed();
                    e.printStackTrace();
                } catch (JSONException e) {
                    searchFailed();
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void searchFailed(){
        Message msg = new Message();
        msg.what=SEARCH_FAILED;
        handlerMain.sendMessage(msg);
    }
}