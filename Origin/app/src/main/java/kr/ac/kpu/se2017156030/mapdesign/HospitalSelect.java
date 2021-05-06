package kr.ac.kpu.se2017156030.mapdesign;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class HospitalSelect extends Activity {
    String choice_do="";
    String choice_se="";
    Button searchBtn, backBtn;
    Spinner spin1, spin2;
    ArrayAdapter<CharSequence> array1, array2;
    ListView hospitalList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hospital_select);

        //정의
        spin1 = (Spinner)findViewById(R.id.spinner);
        spin2 = (Spinner)findViewById(R.id.spinner2);
        searchBtn = (Button)findViewById(R.id.btn_refresh);
        hospitalList = (ListView)findViewById(R.id.hospitalList);
        backBtn = (Button)findViewById(R.id.hospitalBackBtn);

        //첫번째 스피너에 8도 배열 추가
        array1 = ArrayAdapter.createFromResource(this, R.array.spinner_do, android.R.layout.simple_spinner_dropdown_item);
        array1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin1.setAdapter(array1);

        //두번째 스피너에 시군구 배열 추가
        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (array1.getItem(i).toString()) {
                    case "강원":
                        choice_do = array1.getItem(i).toString();
                        array2 = ArrayAdapter.createFromResource(HospitalSelect.this, R.array.spinner_gangwon, android.R.layout.simple_spinner_dropdown_item);
                        array2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin2.setAdapter(array2);
                        spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                choice_se = array2.getItem(i).toString();
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) { }
                        });
                        break;
                    case "경기":
                        choice_do = array1.getItem(i).toString();
                        array2 = ArrayAdapter.createFromResource(HospitalSelect.this, R.array.spinner_gyeonggi, android.R.layout.simple_spinner_dropdown_item);
                        array2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin2.setAdapter(array2);
                        spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                choice_se = array2.getItem(i).toString();
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) { }
                        });
                        break;
                    case "경남":
                        choice_do = array1.getItem(i).toString();
                        array2 = ArrayAdapter.createFromResource(HospitalSelect.this, R.array.spinner_gyeongnam, android.R.layout.simple_spinner_dropdown_item);
                        array2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin2.setAdapter(array2);
                        spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                choice_se = array2.getItem(i).toString();
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) { }
                        });
                        break;
                    case "경북":
                        choice_do = array1.getItem(i).toString();
                        array2 = ArrayAdapter.createFromResource(HospitalSelect.this, R.array.spinner_gyeongbuk, android.R.layout.simple_spinner_dropdown_item);
                        array2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin2.setAdapter(array2);
                        spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                choice_se = array2.getItem(i).toString();
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) { }
                        });
                        break;
                    case "광주":
                        choice_do = array1.getItem(i).toString();
                        array2 = ArrayAdapter.createFromResource(HospitalSelect.this, R.array.spinner_gwangju, android.R.layout.simple_spinner_dropdown_item);
                        array2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin2.setAdapter(array2);
                        spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                choice_se = array2.getItem(i).toString();
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) { }
                        });
                        break;
                    case "대전":
                        choice_do = array1.getItem(i).toString();
                        array2 = ArrayAdapter.createFromResource(HospitalSelect.this, R.array.spinner_daejeon, android.R.layout.simple_spinner_dropdown_item);
                        array2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin2.setAdapter(array2);
                        spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                choice_se = array2.getItem(i).toString();
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) { }
                        });
                        break;
                    case "부산":
                        choice_do = array1.getItem(i).toString();
                        array2 = ArrayAdapter.createFromResource(HospitalSelect.this, R.array.spinner_busan, android.R.layout.simple_spinner_dropdown_item);
                        array2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin2.setAdapter(array2);
                        spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                choice_se = array2.getItem(i).toString();
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) { }
                        });
                        break;
                    case "서울":
                        choice_do = array1.getItem(i).toString();
                        array2 = ArrayAdapter.createFromResource(HospitalSelect.this, R.array.spinner_seoul, android.R.layout.simple_spinner_dropdown_item);
                        array2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin2.setAdapter(array2);
                        spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                choice_se = array2.getItem(i).toString();
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) { }
                        });
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        //종료
        backBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) { finish(); }
        });

        //보건소 리스트 불러오기(파일)  나중에 try catch 처리해서 없으면 보건소 없음 표시
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HospitalSelect.this, choice_do + " "+ choice_se, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
