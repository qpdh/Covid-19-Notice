////////////////////////////
// 전체 일일 현황 액티비티 //
///////////////////////////

package kr.ac.kpu.covid_notifier;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class InfectionByTotalActivity extends Activity {
    Button backBtn;
    TextView daily_confirmed, daily_increse, dateset;
    BarChart chart;

    InfectionByTotalAPI data = new InfectionByTotalAPI();
    ArrayList<InfectionByTotal> lable = new ArrayList<InfectionByTotal>();
    AsyncTask<Void, Void, ArrayList<InfectionByTotal>> thread = data.execute();
    DecimalFormat dc = new DecimalFormat("###,###,###");    //숫자 자릿 수 표시

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infection_by_total);

        //정의
        backBtn = (Button) findViewById(R.id.dailyBackBtn);
        daily_confirmed = (TextView) findViewById(R.id.total_daily_confirmed);
        daily_increse = (TextView) findViewById(R.id.daily_increse);
        dateset = (TextView) findViewById(R.id.datetoday);

        ArrayList<String> labels = new ArrayList<>();
        ArrayList<Integer> values = new ArrayList<>();

        // API로부터 데이터 가져오기
        try {
            lable = thread.get();
            daily_confirmed.setText(dc.format(Integer.parseInt((lable.get(0).decide_cnt))));
            daily_increse.setText(dc.format(Integer.parseInt(lable.get(0).decide_cnt) - Integer.parseInt(lable.get(1).decide_cnt)));
            dateset.setText("기준일 : " + lable.get(0).state_dt);

            for (int i = lable.size() - 1; i > 0; i--) {
                SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
                SimpleDateFormat newDtFormat = new SimpleDateFormat("M.d");
                String date = newDtFormat.format(dtFormat.parse(lable.get(i).state_dt));

                labels.add(date);
                values.add(Integer.parseInt(lable.get(i - 1).decide_cnt) - Integer.parseInt(lable.get(i).decide_cnt));
            }
            BarChartGraph(labels, values);
        } catch (Exception e) {}


        //종료
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //BarChart 삽입 메소드
    private void BarChartGraph(ArrayList<String> labelList, ArrayList<Integer> valList) {
        chart = (BarChart) findViewById(R.id.chart);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //x값 하단에 표시
        xAxis.setTextSize(14);
        xAxis.setSpaceBetweenLabels(1); //라벨 표시 간격 : 1=모두 표시

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setAxisMinValue(0);

        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < labelList.size(); i++) {
            entries.add(new BarEntry((Integer) valList.get(i), i));
        }

        BarDataSet depenses = new BarDataSet(entries, "");
        depenses.setAxisDependency(YAxis.AxisDependency.LEFT);


        ArrayList<String> labels = new ArrayList<>();
        for (int i = 0; i < labelList.size(); i++) {
            labels.add((String) labelList.get(i));
        }

        BarData data = new BarData(labels, depenses);
        data.setValueTextSize(18);
        chart.setData(data);
        chart.animateXY(1000, 1000); //그래프 차오르는 애니메이션
        chart.invalidate();
    }
}