package kr.ac.kpu.covid_notifier;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.nfc.cardemulation.HostApduService;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;

public class HospitalInformationActivity extends Activity {
    private static String TAG = "check";

    Button searchBtn, backBtn;
    Spinner spin1, spin2;
    ArrayAdapter<String> array1, array2;
    ListView hospitalList;
    RadioButton rbA0;
    RadioGroup radioGroupHospital;

    HashMap<HashSet<String>, ArrayList<HospitalInformation>> hospitalInformationDict;

    //  HashMap<String, ArrayList<HospitalInformation>> hospitalInfromationDict;
    TreeMap<String, TreeSet<String>> sgguNmDict;

    ArrayList<ArrayList<HospitalInformation>> lable = new ArrayList<ArrayList<HospitalInformation>>();
    HospitalInformationAPI data = new HospitalInformationAPI();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hostpital_information);

        //정의
        spin1 = (Spinner) findViewById(R.id.spinner);
        spin2 = (Spinner) findViewById(R.id.spinner2);
        searchBtn = (Button) findViewById(R.id.btn_refresh);
        hospitalList = (ListView) findViewById(R.id.hospitalList);
        backBtn = (Button) findViewById(R.id.hospitalBackBtn);
        rbA0 = (RadioButton) findViewById(R.id.HA0);
//        rb97 = (RadioButton) findViewById(R.id.H97);
//        rb99 = (RadioButton) findViewById(R.id.H99);
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


        // 데이터 가져오기
        AsyncTask<Void, Void, ArrayList<ArrayList<HospitalInformation>>> thread = data.execute();

        try {
            lable = thread.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 라디오 그룹 버튼이 변경되면 데이터 불러오기
        radioGroupHospital.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.HA0:
                        divide(0);
                        spinner();
                        searchBtn.performClick();
                        break;

                    case R.id.H97:
                        divide(1);
                        spinner();
                        searchBtn.performClick();
                        break;

                    case R.id.H99:
                        divide(2);
                        spinner();
                        searchBtn.performClick();
                        break;
                }
            }
        });

        //보건소 리스트 불러오기
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String spin1Item = spin1.getSelectedItem().toString();
                    String spin2Item = spin2.getSelectedItem().toString();

                    HashSet<String> iterSet = new HashSet<String>();
                    iterSet.add(spin1Item);
                    iterSet.add(spin2Item);

                    String hospitalYadmNmArray[] = new String[hospitalInformationDict.get(iterSet).size()];
                    Log.d("SEARCHBTN", Integer.toString(hospitalYadmNmArray.length));
                    String hospitalTelNo[] = new String[hospitalInformationDict.get(iterSet).size()];

                    for (int i = 0; i < hospitalInformationDict.get(iterSet).size(); i++) {
                        hospitalYadmNmArray[i] = hospitalInformationDict.get(iterSet).get(i).yadmNm;
                        hospitalTelNo[i] = hospitalInformationDict.get(iterSet).get(i).telno;
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, hospitalYadmNmArray);

                    hospitalList.setAdapter(adapter);

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

        //종료
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        // A0 선택되고 시작
        rbA0.setChecked(true);

    }

    private void divide(int k) {
//        hospitalInfromationDict = new HashMap<String, ArrayList<HospitalInformation>>();

        hospitalInformationDict = new HashMap<HashSet<String>, ArrayList<HospitalInformation>>();

        sgguNmDict = new TreeMap<String, TreeSet<String>>();

        for (int j = 0; j < lable.get(k).size(); j++) {
            HospitalInformation iter = lable.get(k).get(j);

            // (시도, 시군구) 세트
            HashSet<String> iterSet = new HashSet<String>();
            iterSet.add(iter.sidoNm);
            iterSet.add(iter.sgguNm);

            if (sgguNmDict.get(iter.sidoNm) == null) {
                sgguNmDict.put(iter.sidoNm, new TreeSet<String>());
            }

            if (hospitalInformationDict.get(iterSet) == null) {
                hospitalInformationDict.put(iterSet, new ArrayList<HospitalInformation>());
            }

//            if (hospitalInfromationDict.get(iter.sgguNm) == null) {
//                hospitalInfromationDict.put(iter.sgguNm, new ArrayList<HospitalInformation>());
//            }

            hospitalInformationDict.get(iterSet).add(iter);
            sgguNmDict.get(iter.sidoNm).add(iter.sgguNm);

//            // 스피너에 넣을 TreeSet (시군구)
//            TreeSet<String> tmp = sgguNmDict.get(iter.sidoNm);
//            ArrayList<HospitalInformation> tmp2 = hospitalInfromationDict.get(iter.sgguNm);
//            tmp.add(lable.get(k).get(j).sgguNm);
//            tmp2.add(lable.get(k).get(j));
        }
    }

    private void spinner() {
        array1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, sgguNmDict.keySet().toArray(new String[sgguNmDict.size()]));
        spin1.setAdapter(array1);

        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String sidoNm = array1.getItem(i);
                Log.d(TAG, sidoNm);
                for (String k : sgguNmDict.get(sidoNm)) {
                    Log.d(TAG, k);
                }
                ;
                array2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, sgguNmDict.get(sidoNm).toArray(new String[sgguNmDict.get(sidoNm).size()]));
                spin2.setAdapter(array2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
}
