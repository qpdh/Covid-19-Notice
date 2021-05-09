package kr.ac.kpu.se2017156030.mapdesign;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button dailyBtn, regionBtn, hospitalBtn, newsBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //액션바 숨기기
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //정의
        dailyBtn = (Button)findViewById(R.id.dailyBtn);
        regionBtn = (Button)findViewById(R.id.regionBtn);
        hospitalBtn = (Button)findViewById(R.id.hospitalBtn);
        newsBtn = (Button)findViewById(R.id.newsBtn);

        //일일 확진자 현황 액티비티 실행
        dailyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dailyIntent = new Intent(getApplicationContext(), DailyTotal.class);
                startActivity(dailyIntent);
            }
        });

        //지역별 확진자현황 액티비티 실행
        regionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regionIntent = new Intent(getApplicationContext(), RegionMap.class);
                startActivity(regionIntent);
            }
        });

        //지역 보건소 목록 액티비티 실행
        hospitalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent hospitalIntent = new Intent(getApplicationContext(), HospitalSelect.class);
                startActivity(hospitalIntent);
            }
        });

        //코로나 뉴스 정보 액티비티 실행
        newsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //뉴스정보추가
                Uri uri =Uri.parse("http://ncov.mohw.go.kr/tcmBoardList.do?brdId=&brdGubun=&dataGubun=&ncvContSeq=&contSeq=&board_id=&gubun=");
                Intent intent=new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }
}