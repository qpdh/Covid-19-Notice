package kr.ac.kpu.se2017156030.mapdesign;

import android.app.Activity;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;

public class DailyTotal extends Activity {
    Button backBtn;
    TextView daily_confirmed, daily_increse, date;
    BarChart chart;
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
        
        //BarChart 데이터 삽입
        ArrayList<String> labels = new ArrayList<>();
        labels.add("05.01");
        labels.add("05.02");
        labels.add("05.03");
        labels.add("05.04");
        labels.add("05.05");
        labels.add("05.06");
        labels.add("05.07");
        ArrayList<Integer> values = new ArrayList<>();
        values.add(100);
        values.add(276);
        values.add(350);
        values.add(420);
        values.add(566);
        values.add(648);
        values.add(400);
        BarChartGraph(labels, values);

        //종료
        backBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) { finish(); }
        });
    }
    
    //BarChart 삽입 메소드
    private void BarChartGraph(ArrayList<String> labelList, ArrayList<Integer> valList){
        chart = (BarChart)findViewById(R.id.chart);
        
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //x값 하단에 표시
        xAxis.setTextSize(14);
        xAxis.setSpaceBetweenLabels(1); //라벨 표시 간격 : 1=모두 표시

        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i<7; i++){
            entries.add(new BarEntry((Integer) valList.get(i), i));
        }

        BarDataSet depenses = new BarDataSet(entries, "확진자 추이");
        depenses.setAxisDependency(YAxis.AxisDependency.LEFT);


        ArrayList<String> labels = new ArrayList<>();
        for (int i = 0; i<labelList.size(); i++) {
            labels.add((String) labelList.get(i));
        }

        BarData data = new BarData(labels, depenses);
        data.setValueTextSize(18);
        chart.setData(data);
        chart.animateXY(1000, 1000); //그래프 차오르는 애니메이션
        chart.invalidate();
    }
}
