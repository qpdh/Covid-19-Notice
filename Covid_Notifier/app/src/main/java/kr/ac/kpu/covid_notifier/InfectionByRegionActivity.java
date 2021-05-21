//////////////////////////////
// 지역별 감염 현황 액티비티 //
/////////////////////////////

package kr.ac.kpu.covid_notifier;

import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
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
    Button syncBtn;
    TextView dateText;
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
        findView();

        // 지역별 감염 현황 값 가져오기
        syncBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    lable = thread.get();
                    dateText.setText(lable.get(0).std_day + " 기준");
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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
    // TextView total_confirmed[];
    // TextView daily_confirmed[];
    private void findView() {
        dateText = (TextView) findViewById(R.id.dateText);
        //정의
        total_confirmed = new TextView[]{
                (TextView) findViewById(R.id.quarantine_confirmed), //검역
                (TextView) findViewById(R.id.jeju_confirmed),       //제주
                (TextView) findViewById(R.id.gyeongnam_confirmed),  //경남
                (TextView) findViewById(R.id.gyeongbuk_confirmed),  //경북
                (TextView) findViewById(R.id.jeonnam_confirmed),    //전남
                (TextView) findViewById(R.id.jeonbuk_confirmed),    //전북
                (TextView) findViewById(R.id.chungnam_confirmed),   //충남
                (TextView) findViewById(R.id.chungbuk_confirmed),   //충북
                (TextView) findViewById(R.id.gangwon_confirmed),    //강원
                (TextView) findViewById(R.id.gyeonggi_confirmed),   //경기
                (TextView) findViewById(R.id.sejong_confirmed),     //세종
                (TextView) findViewById(R.id.ulsan_confirmed),      //울산
                (TextView) findViewById(R.id.daejeon_confirmed),    //대전
                (TextView) findViewById(R.id.gwangju_confirmed),    //광주
                (TextView) findViewById(R.id.incheon_confirmed),    //인천
                (TextView) findViewById(R.id.daegu_confirmed),      //대구
                (TextView) findViewById(R.id.busan_confirmed),      //부산
                (TextView) findViewById(R.id.seoul_confirmed)};     //서울

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
