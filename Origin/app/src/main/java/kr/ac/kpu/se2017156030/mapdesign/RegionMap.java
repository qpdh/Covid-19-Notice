package kr.ac.kpu.se2017156030.mapdesign;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class RegionMap extends Activity {
    LinearLayout mapview;
    ImageView map;
    Button backBtn, syncBtn;
    Covid_19_sido data = new Covid_19_sido();
    ArrayList<InfectionByRegion> lable = new ArrayList<InfectionByRegion>();
    AsyncTask<Void, Void, ArrayList<InfectionByRegion>> thread = data.execute();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.region_map);

        TextView dateText = (TextView)findViewById(R.id.dateText);
        //정의
        LinearLayout layout_list[] = {
                (LinearLayout)findViewById(R.id.quarantine_layout), //검역
                (LinearLayout) findViewById(R.id.jeju_layout),      //제주
                (LinearLayout) findViewById(R.id.gyeongnam_layout), //경남
                (LinearLayout) findViewById(R.id.gyeongbuk_layout), //경북
                (LinearLayout) findViewById(R.id.jeonnam_layout),   //전남
                (LinearLayout) findViewById(R.id.jeonbuk_layout),   //전북
                (LinearLayout) findViewById(R.id.chungnam_layout),  //충남
                (LinearLayout) findViewById(R.id.chungbuk_layout),  //충북
                (LinearLayout) findViewById(R.id.gangwon_layout),   //강원
                (LinearLayout) findViewById(R.id.gyeonggi_layout),  //경기
                (LinearLayout) findViewById(R.id.sejong_layout),    //세종
                (LinearLayout) findViewById(R.id.ulsan_layout),     //울산
                (LinearLayout) findViewById(R.id.daejeon_layout),   //대전
                (LinearLayout) findViewById(R.id.gwangju_layout),   //광주
                (LinearLayout) findViewById(R.id.incheon_layout),   //인천
                (LinearLayout) findViewById(R.id.daegu_layout),     //대구
                (LinearLayout) findViewById(R.id.busan_layout),     //부산
                (LinearLayout) findViewById(R.id.seoul_layout)};    //서울
        TextView total_confirmed[] = {
                (TextView) findViewById(R.id.quarantine_confirmed),
                (TextView) findViewById(R.id.jeju_confirmed),
                (TextView) findViewById(R.id.gyeongnam_confirmed),
                (TextView) findViewById(R.id.gyeongbuk_confirmed),
                (TextView) findViewById(R.id.jeonnam_confirmed),
                (TextView) findViewById(R.id.jeonbuk_confirmed),
                (TextView) findViewById(R.id.chungnam_confirmed),
                (TextView) findViewById(R.id.chungbuk_confirmed),
                (TextView) findViewById(R.id.gangwon_confirmed),
                (TextView) findViewById(R.id.gyeonggi_confirmed),
                (TextView) findViewById(R.id.sejong_confirmed),
                (TextView) findViewById(R.id.ulsan_confirmed),
                (TextView) findViewById(R.id.daejeon_confirmed),
                (TextView) findViewById(R.id.gwangju_confirmed),
                (TextView) findViewById(R.id.incheon_confirmed),
                (TextView) findViewById(R.id.daegu_confirmed),
                (TextView) findViewById(R.id.busan_confirmed),
                (TextView) findViewById(R.id.seoul_confirmed)};
        TextView daily_confirmed[] = {
                (TextView)findViewById(R.id.quarantine_daily_confirmed),
                (TextView) findViewById(R.id.jeju_daily_confirmed),
                (TextView) findViewById(R.id.gyeongnam_daily_confirmed),
                (TextView) findViewById(R.id.gyeongbuk_daily_confirmed),
                (TextView) findViewById(R.id.jeonnam_daily_confirmed),
                (TextView) findViewById(R.id.jeonbuk_daily_confirmed),
                (TextView) findViewById(R.id.chungnam_daily_confirmed),
                (TextView) findViewById(R.id.chungbuk_daily_confirmed),
                (TextView) findViewById(R.id.gangwon_daily_confirmed),
                (TextView) findViewById(R.id.gyeonggi_daily_confirmed),
                (TextView) findViewById(R.id.sejong_daily_confirmed),
                (TextView) findViewById(R.id.wulsan_daily_confirmed),
                (TextView) findViewById(R.id.daejeon_daily_confirmed),
                (TextView) findViewById(R.id.gwangju_daily_confirmed),
                (TextView) findViewById(R.id.incheon_daily_confirmed),
                (TextView) findViewById(R.id.daegu_daily_confirmed),
                (TextView) findViewById(R.id.busan_daily_confirmed),
                (TextView) findViewById(R.id.seoul_daily_confirmed)};
        mapview = (LinearLayout) findViewById(R.id.frame);
        backBtn = (Button)findViewById(R.id.regionBackBtn);
        syncBtn = (Button)findViewById(R.id.syncBtn);
        map = (ImageView)findViewById(R.id.map);


        //스마트폰의 높이, 너비 받아오기
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int getheight = displayMetrics.heightPixels;
        int getwidth = displayMetrics.widthPixels;
        
        //좌표값 조정
        float x, y;
        for(int i = 0; i<18; i++){
            x = layout_list[i].getX();
            y = layout_list[i].getY();
            layout_list[i].setX((float) (x*(getwidth/1080)));
            layout_list[i].setY((float) (y*(getwidth/1920)));
        }

        //값 가져오기
        syncBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    lable = thread.get();
                    dateText.setText(lable.get(0).stdDay);
                    for (int k = 0; k < data.rdata.size(); k++) {
                        total_confirmed[k].setText(lable.get(k).def_cnt);
                        daily_confirmed[k].setText('+'+ lable.get(k).inc_dec);
                    }

                } catch (Exception e) {

                }
            }
        });

        //최소 1회 강제 sync
        syncBtn.performClick();

        //종료
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
