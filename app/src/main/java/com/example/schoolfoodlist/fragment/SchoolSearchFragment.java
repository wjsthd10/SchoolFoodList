package com.example.schoolfoodlist.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolfoodlist.R;
import com.example.schoolfoodlist.adpater.SchoolListAdapter;
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

public class SchoolSearchFragment extends Fragment implements View.OnClickListener {

    final static String KEY="9ee8be5c3531463f92125c15895e372f";
    final static String schoolAddress = "https://open.neis.go.kr/hub/schoolInfo";
    final static int SEARCH_FINISH = 10001;
    final static int SEARCH_FAILED = 10002;
    final static int SELECT_SCHOOL = 10003;
    final static int CALL_FOOD_LIST = 20001;



    //https://open.neis.go.kr/hub/schoolInfo?KEY=df2ba946955d4d1b8a9b9d767f09e25c&Type=json&pIndex=1&pSize=5&SD_SCHUL_CODE=7091382


    EditText schoolNameEditor;
    ImageView searchBtn;
    RecyclerView schoolListView;
    SchoolListAdapter schoolAdapter;
    LinearLayout progressLay;

    ArrayList<SchoolDataItem> schoolDataItems = new ArrayList<>();
    Handler handlerMain;

    public SchoolSearchFragment(Handler handler){
        this.handlerMain = handler;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        schoolListView = view.findViewById(R.id.school_list_view);
        searchBtn = view.findViewById(R.id.school_search_btn);
        schoolNameEditor = view.findViewById(R.id.school_name_edit_view);
        progressLay = view.findViewById(R.id.school_search_progressbar);
        setEvent();

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void setEvent(){
        searchBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.school_search_btn:
                progressLay.setVisibility(View.VISIBLE);
                String schoolName = schoolNameEditor.getText().toString();
                if (schoolName.equals("") || schoolName.contains("-") || schoolName.contains(".") || schoolName.contains(",") || schoolName.equals(" ")){
                    progressLay.setVisibility(View.GONE);
                    break;
                }
                String address = schoolAddress
                        +"?KEY="+KEY
                        +"&Type=json&pIndex=1&pSize=5"
                        +"&SCHUL_NM="+schoolName;
                getSchoolName(address);

                break;
        }
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
                    mHandler.sendMessage(msg);

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
        mHandler.sendMessage(msg);
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what){
                case SEARCH_FINISH:
                    progressLay.setVisibility(View.GONE);
                    schoolAdapter = new SchoolListAdapter(getActivity(), schoolDataItems, mHandler);
                    schoolListView.setAdapter(schoolAdapter);
                    schoolAdapter.notifyDataSetChanged();
                    break;
                case SEARCH_FAILED:
                    progressLay.setVisibility(View.GONE);
                    break;
                case SELECT_SCHOOL:
                    SchoolDataItem items = (SchoolDataItem) message.obj;
                    Message msg = new Message();
                    msg.what = CALL_FOOD_LIST;
                    msg.obj = items;
                    handlerMain.sendMessage(msg);
                    getActivity().getSupportFragmentManager().popBackStack();
                    break;

            }
            return false;
        }
    });
}
