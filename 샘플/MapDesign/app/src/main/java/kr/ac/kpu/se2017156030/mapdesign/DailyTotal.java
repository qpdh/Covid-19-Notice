package kr.ac.kpu.se2017156030.mapdesign;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DailyTotal extends Activity {
    Button backBtn;
    TextView daily_confirmed, daily_increse, date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_total);

        //정의
        backBtn = (Button)findViewById(R.id.dailyBackBtn);
        daily_confirmed = (TextView)findViewById(R.id.total_daily_confirmed);
        daily_increse = (TextView)findViewById(R.id.daily_increse);
        date = (TextView)findViewById(R.id.date);
        
        //확진자 현황 불러오기
        //daily_confirmed = ;  //당일 기준 전체 확진자수
        //daily_increse = ;  //전일대비 증감
        //date = ;  //기준일
        

        //종료
        backBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) { finish(); }
        });
    }
}
