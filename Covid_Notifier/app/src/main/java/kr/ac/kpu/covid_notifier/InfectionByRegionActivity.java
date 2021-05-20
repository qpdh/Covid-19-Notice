//////////////////////////////
// 지역별 감염 현황 액티비티 //
/////////////////////////////

package kr.ac.kpu.covid_notifier;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class InfectionByRegionActivity extends AppCompatActivity {
    LinearLayout mapview;
    ImageView map;
    Button backBtn, syncBtn;
    TextView dateText;
    LinearLayout layout_list[];
    TextView total_confirmed[];
    TextView daily_confirmed[];

    Date date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
    String today = sdf.format(date);

    InfectionByRegionAPI data = new InfectionByRegionAPI(today);
    ArrayList<InfectionByRegion> lable = new ArrayList<InfectionByRegion>();
    AsyncTask<Void, Void, ArrayList<InfectionByRegion>> thread = data.execute();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Region);
        setContentView(R.layout.activity_infection_by_region);

        //액션바 띄우기, 스타일 수정
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(0xFF4A78BE));
        actionBar.setDisplayHomeAsUpEnabled(true);

        // 뷰 ID 가져오기
        setViewID();

        //스마트폰의 높이, 너비 받아오기
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int getheight = displayMetrics.heightPixels;
        int getwidth = displayMetrics.widthPixels;

        //좌표값 조정
        float x, y;
        for (int i = 0; i < 18; i++) {
            x = layout_list[i].getX();
            y = layout_list[i].getY();
            layout_list[i].setX((float) (x * (getwidth / 1080)));
            layout_list[i].setY((float) (y * (getwidth / 1920)));
        }

        // 지역별 감염 현황 값 가져오기
        syncBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    lable = thread.get();
                    dateText.setText(lable.get(0).std_day);
                    for (int k = 0; k < data.rdata.size(); k++) {
                        total_confirmed[k].setText(lable.get(k).def_cnt);
                        daily_confirmed[k].setText('+' + lable.get(k).inc_dec);
                    }

                } catch (Exception e) {

                }
            }
        });

        //최소 1회 강제 sync
        syncBtn.performClick();
    }

    //종료
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                super.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // 다음 뷰의 Id를 가져옴
    // LinearLayout mapview;
    // ImageView map;
    // Button backBtn, syncBtn;
    // TextView dateText;
    // LinearLayout layout_list[];
    // TextView total_confirmed[];
    // TextView daily_confirmed[];
    private void setViewID() {
        dateText = (TextView) findViewById(R.id.dateText);
        //정의
        layout_list = new LinearLayout[]{
                (LinearLayout) findViewById(R.id.quarantine_layout), //검역
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

        total_confirmed = new TextView[]{
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

        daily_confirmed = new TextView[]{
                (TextView) findViewById(R.id.quarantine_daily_confirmed),
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
        syncBtn = (Button) findViewById(R.id.syncBtn);
        map = (ImageView) findViewById(R.id.map);
    }
}
