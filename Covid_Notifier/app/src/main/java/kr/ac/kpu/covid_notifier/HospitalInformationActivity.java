//////////////////////////////
// 코로나19병원정보 액티비티 //
/////////////////////////////

package kr.ac.kpu.covid_notifier;

import android.content.Intent;

import android.graphics.drawable.ColorDrawable;
import android.net.Uri;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;

public class HospitalInformationActivity extends AppCompatActivity {
    private static String TAG = "HospitalInformationActivity";    // Log.d 용 태그

    Button searchBtn;
    Spinner spin1, spin2;
    ArrayAdapter<String> arraySido, arraySigun;
    ListView hospitalList;
    RadioButton rbA0;
    RadioGroup radioGroupHospital;
    TextView source;

    HashMap<HashSet<String>, ArrayList<HospitalInformation>> hospitalInformationDict;

    TreeMap<String, TreeSet<String>> sgguNmDict;

    ArrayList<ArrayList<HospitalInformation>> lable = new ArrayList<ArrayList<HospitalInformation>>();
    HospitalInformationAPI data = new HospitalInformationAPI();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Hospital);
        setContentView(R.layout.activity_hostpital_information);

        //액션바 띄우기, 스타일 수정
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(0xFF46B7BD));
        actionBar.setDisplayHomeAsUpEnabled(true);

        // 뷰 가져오기
        findView();

        // 데이터 가져오기 쓰레드 생성
        AsyncTask<Void, Void, ArrayList<ArrayList<HospitalInformation>>> thread = data.execute();

        try {
            lable = thread.get();
        } catch (Exception e) {
        }

        // 리스너 추가하는 메소드
        setListener();

        // A0 선택되고 시작
        rbA0.setChecked(true);

    }

    // 뷰 가져오는 메소드
    private void findView() {
        spin1 = (Spinner) findViewById(R.id.spinner);
        spin2 = (Spinner) findViewById(R.id.spinner2);
        searchBtn = (Button) findViewById(R.id.btn_refresh);
        hospitalList = (ListView) findViewById(R.id.hospitalList);
        rbA0 = (RadioButton) findViewById(R.id.HA0);
        radioGroupHospital = (RadioGroup) findViewById(R.id.radioGroupHospital);
        source = (TextView) findViewById(R.id.source);
    }

    // 리스너 추가하는 메소드
    // radioGroupHospital
    // searchBtn
    // backBtn
    private void setListener() {
        // 라디오 그룹 버튼이 변경되면 데이터 불러오기
        radioGroupHospital.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Log.d(TAG, "radioGroupHospital onCheckedChanged 호출");

                switch (i) {
                    case R.id.HA0:
                        divide(0);
                        spinner();
                        hospitalList.setAdapter(null);
                        break;

                    case R.id.H97:
                        divide(1);
                        spinner();
                        hospitalList.setAdapter(null);
                        break;

                    case R.id.H99:
                        divide(2);
                        spinner();
                        hospitalList.setAdapter(null);
                        break;
                }
            }
        });

        // 검색버튼 누를 시
        // 보건소 리스트 불러오기
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // spin1Item : 시/도 선택 스피너
                    // spin2Item : 시/군/구 선택 스피너
                    String spin1Item = spin1.getSelectedItem().toString();
                    String spin2Item = spin2.getSelectedItem().toString();

                    if (spin1Item.equals("시/도")) {
                        Toast.makeText(getApplicationContext(), "시/도 를 선택해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (spin2Item.equals("시/군/구")) {
                        Toast.makeText(getApplicationContext(), "시/군/구 를 선택해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    HashSet<String> iterSet = new HashSet<String>();
                    iterSet.add(spin1Item);
                    iterSet.add(spin2Item);

                    // 시/도, 시/군/구에 맞는 병원 이름과 전화번호 리스트화
                    // hospitalYadmNmArray  : 병원 이름 리스트
                    // hospitalTelNo        : 병원 전화번호 리스트
                    String hospitalYadmNmArray[] = new String[hospitalInformationDict.get(iterSet).size()];
                    String hospitalTelNo[] = new String[hospitalInformationDict.get(iterSet).size()];

                    // 리스트에 데이터 삽입
                    for (int i = 0; i < hospitalInformationDict.get(iterSet).size(); i++) {
                        hospitalYadmNmArray[i] = hospitalInformationDict.get(iterSet).get(i).yadmNm;
                        hospitalTelNo[i] = hospitalInformationDict.get(iterSet).get(i).telno;
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, hospitalYadmNmArray);

                    hospitalList.setAdapter(adapter);

                    // 병원 목록을 클릭하면 전화 연결
                    hospitalList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String base2 = hospitalYadmNmArray[i];
                            Toast.makeText(getApplicationContext(), base2, Toast.LENGTH_SHORT).show();

                            Uri uri = Uri.parse("tel:" + hospitalTelNo[i]);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }
                    });

                } catch (Exception e) {
                }

            }
        });

        //소스 링크 열기
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

    // 라디오 버튼에서 선택된 A0, 99, 97
    // 데이터 딕셔너리화
    private void divide(int k) {
        // key : (시도, 시군구) value : (HospitalInformation 리스트)
        hospitalInformationDict = new HashMap<HashSet<String>, ArrayList<HospitalInformation>>();

        // key : (시도) value : (시군구)
        sgguNmDict = new TreeMap<String, TreeSet<String>>();

        for (int j = 0; j < lable.get(k).size(); j++) {
            HospitalInformation iter = lable.get(k).get(j);

            // iterSet : (시도, 시군구) 세트
            HashSet<String> iterSet = new HashSet<String>();
            iterSet.add(iter.sidoNm);
            iterSet.add(iter.sgguNm);

            // 넣으려는 데이터 값이 비어있으면 생성해주기
            if (sgguNmDict.get(iter.sidoNm) == null) {
                sgguNmDict.put(iter.sidoNm, new TreeSet<String>());
            }

            if (hospitalInformationDict.get(iterSet) == null) {
                hospitalInformationDict.put(iterSet, new ArrayList<HospitalInformation>());
            }

            hospitalInformationDict.get(iterSet).add(iter);
            sgguNmDict.get(iter.sidoNm).add(iter.sgguNm);

        }
    }

    // 스피너 목록 설정하는 메소드
    private void spinner() {
        arraySido = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        arraySido.add("시/도");
        arraySido.addAll(sgguNmDict.keySet().toArray(new String[sgguNmDict.size()]));

        spin1.setAdapter(arraySido);

        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    String sidoNm = arraySido.getItem(i);
                    Log.d(TAG, sidoNm);
                    for (String k : sgguNmDict.get(sidoNm)) {
                        Log.d(TAG, k);
                    }

                    arraySigun = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item);
                    arraySigun.add("시/군/구");
                    arraySigun.addAll(sgguNmDict.get(sidoNm).toArray(new String[sgguNmDict.get(sidoNm).size()]));
                    spin2.setAdapter(arraySigun);
                } else {
                    arraySigun = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item);
                    arraySigun.add("시/군/구");
                    spin2.setAdapter(arraySigun);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
}
