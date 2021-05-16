//////////////////////////////
// 코로나19병원정보 액티비티 //
/////////////////////////////

package kr.ac.kpu.covid_notifier;

import android.app.Activity;
import android.content.Intent;

import android.net.Uri;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;

public class HospitalInformationActivity extends Activity {
    // Log.d 용 태그
    private static String TAG = "HospitalInformationActivity";

    Button searchBtn, backBtn;
    Spinner spin1, spin2;
    ArrayAdapter<String> array1, array2;
    ListView hospitalList;
    RadioButton rbA0;
    RadioGroup radioGroupHospital;

    HashMap<HashSet<String>, ArrayList<HospitalInformation>> hospitalInformationDict;

    TreeMap<String, TreeSet<String>> sgguNmDict;

    ArrayList<ArrayList<HospitalInformation>> lable = new ArrayList<ArrayList<HospitalInformation>>();
    HospitalInformationAPI data = new HospitalInformationAPI();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hostpital_information);

        // 뷰 가져오기
        spin1 = (Spinner) findViewById(R.id.spinner);
        spin2 = (Spinner) findViewById(R.id.spinner2);
        searchBtn = (Button) findViewById(R.id.btn_refresh);
        hospitalList = (ListView) findViewById(R.id.hospitalList);
        backBtn = (Button) findViewById(R.id.hospitalBackBtn);
        rbA0 = (RadioButton) findViewById(R.id.HA0);
        radioGroupHospital = (RadioGroup) findViewById(R.id.radioGroupHospital);

        // 스피너 너비 조정
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spin1);

            popupWindow.setHeight(600);

            popupWindow = (android.widget.ListPopupWindow) popup.get(spin2);
            popupWindow.setHeight(400);

        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
        }


        // 데이터 가져오기 쓰레드 생성
        AsyncTask<Void, Void, ArrayList<ArrayList<HospitalInformation>>> thread = data.execute();

        try {
            lable = thread.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 리스너 추가하는 메소드

        setActionListeners();

        // A0 선택되고 시작
        rbA0.setChecked(true);

    }

    // 리스너 추가하는 메소드
    // radioGroupHospital
    // searchBtn
    // backBtn
    private void setActionListeners() {
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

                    if(spin1Item.equals("시/도")){
                        Toast.makeText(getApplicationContext(),"시/도 를 선택해주세요.",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(spin2Item.equals("시/군/구")){
                        Toast.makeText(getApplicationContext(),"시/군/구 를 선택해주세요.",Toast.LENGTH_SHORT).show();
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

                } catch (Exception e) {}

            }
        });

        // 뒤로가기 버튼
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        array1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item);
        array1.add("시/도");
        array1.addAll(sgguNmDict.keySet().toArray(new String[sgguNmDict.size()]));
        //array1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, sgguNmDict.keySet().toArray(new String[sgguNmDict.size()]));

        spin1.setAdapter(array1);

        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    String sidoNm = array1.getItem(i);
                    Log.d(TAG, sidoNm);
                    for (String k : sgguNmDict.get(sidoNm)) {
                        Log.d(TAG, k);
                    }

                    array2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item);
                    array2.add("시/군/구");
                    array2.addAll(sgguNmDict.get(sidoNm).toArray(new String[sgguNmDict.get(sidoNm).size()]));
                    //array2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, sgguNmDict.get(sidoNm).toArray(new String[sgguNmDict.get(sidoNm).size()]));
                    spin2.setAdapter(array2);
                } else {
                    array2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item);
                    array2.add("시/군/구");
                    spin2.setAdapter(array2);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }
}
