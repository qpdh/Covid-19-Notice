package kr.ac.kpu.se2017156030.mapdesign;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.nfc.cardemulation.HostApduService;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

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
    String choice_do = "";
    String choice_se = "";
    Button searchBtn, backBtn;
    Spinner spin1, spin2;
    ArrayAdapter<String> array1, array2;
    ListView hospitalList;

    HospitalInformationAPI data = new HospitalInformationAPI();
    ArrayList<HospitalInformation> lable = new ArrayList<HospitalInformation>();
    AsyncTask<Void, Void, ArrayList<HospitalInformation>> thread = data.execute();

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


        HashMap<String, ArrayList<HospitalInformation>> hospitalInfromationDict = new HashMap<String, ArrayList<HospitalInformation>>();
        TreeMap<String, TreeSet<String>> sgguNmDict = new TreeMap<String, TreeSet<String>>();

        try {
            lable = thread.get();
            for (int i = 0; i < lable.size(); i++) {
                if (sgguNmDict.get(lable.get(i).sidoNm) == null) {
                    sgguNmDict.put(lable.get(i).sidoNm, new TreeSet<String>());
                }
                if (hospitalInfromationDict.get(lable.get(i).sgguNm) == null) {
                    hospitalInfromationDict.put(lable.get(i).sgguNm, new ArrayList<HospitalInformation>());
                }

                TreeSet<String> tmp = sgguNmDict.get(lable.get(i).sidoNm);
                ArrayList<HospitalInformation> tmp2 = hospitalInfromationDict.get(lable.get(i).sgguNm);

                tmp.add(lable.get(i).sgguNm);
                tmp2.add(lable.get(i));
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        array1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, sgguNmDict.keySet().toArray(new String[sgguNmDict.size()]));
        spin1.setAdapter(array1);

        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String sidoNm = array1.getItem(i).toString();
                Log.d("SIDO", sidoNm);
                for (String k : sgguNmDict.get(sidoNm)) {
                    Log.d("SIGUNGU", k);
                }
                ;
                array2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, sgguNmDict.get(sidoNm).toArray(new String[sgguNmDict.get(sidoNm).size()]));
                spin2.setAdapter(array2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        //첫번째 스피너에 8도 배열 추가
//        array1 = ArrayAdapter.createFromResource(this, R.array.spinner_do, android.R.layout.simple_spinner_dropdown_item);
//        array1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spin1.setAdapter(array1);
//
//        //두번째 스피너에 시군구 배열 추가
//        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                switch (array1.getItem(i).toString()) {
//                    case "강원":
//                        choice_do = array1.getItem(i).toString();
//                        array2 = ArrayAdapter.createFromResource(HospitalInformationActivity.this, R.array.spinner_gangwon, android.R.layout.simple_spinner_dropdown_item);
//                        array2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        spin2.setAdapter(array2);
//                        spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                            @Override
//                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                                choice_se = array2.getItem(i).toString();
//                            }
//
//                            @Override
//                            public void onNothingSelected(AdapterView<?> adapterView) {
//                            }
//                        });
//                        break;
//                    case "경기":
//                        choice_do = array1.getItem(i).toString();
//                        array2 = ArrayAdapter.createFromResource(HospitalInformationActivity.this, R.array.spinner_gyeonggi, android.R.layout.simple_spinner_dropdown_item);
//                        array2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        spin2.setAdapter(array2);
//                        spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                            @Override
//                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                                choice_se = array2.getItem(i).toString();
//                            }
//
//                            @Override
//                            public void onNothingSelected(AdapterView<?> adapterView) {
//                            }
//                        });
//                        break;
//                    case "경남":
//                        choice_do = array1.getItem(i).toString();
//                        array2 = ArrayAdapter.createFromResource(HospitalInformationActivity.this, R.array.spinner_gyeongnam, android.R.layout.simple_spinner_dropdown_item);
//                        array2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        spin2.setAdapter(array2);
//                        spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                            @Override
//                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                                choice_se = array2.getItem(i).toString();
//                            }
//
//                            @Override
//                            public void onNothingSelected(AdapterView<?> adapterView) {
//                            }
//                        });
//                        break;
//                    case "경북":
//                        choice_do = array1.getItem(i).toString();
//                        array2 = ArrayAdapter.createFromResource(HospitalInformationActivity.this, R.array.spinner_gyeongbuk, android.R.layout.simple_spinner_dropdown_item);
//                        array2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        spin2.setAdapter(array2);
//                        spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                            @Override
//                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                                choice_se = array2.getItem(i).toString();
//                            }
//
//                            @Override
//                            public void onNothingSelected(AdapterView<?> adapterView) {
//                            }
//                        });
//                        break;
//                    case "광주":
//                        choice_do = array1.getItem(i).toString();
//                        array2 = ArrayAdapter.createFromResource(HospitalInformationActivity.this, R.array.spinner_gwangju, android.R.layout.simple_spinner_dropdown_item);
//                        array2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        spin2.setAdapter(array2);
//                        spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                            @Override
//                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                                choice_se = array2.getItem(i).toString();
//                            }
//
//                            @Override
//                            public void onNothingSelected(AdapterView<?> adapterView) {
//                            }
//                        });
//                        break;
//                    case "대전":
//                        choice_do = array1.getItem(i).toString();
//                        array2 = ArrayAdapter.createFromResource(HospitalInformationActivity.this, R.array.spinner_daejeon, android.R.layout.simple_spinner_dropdown_item);
//                        array2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        spin2.setAdapter(array2);
//                        spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                            @Override
//                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                                choice_se = array2.getItem(i).toString();
//                            }
//
//                            @Override
//                            public void onNothingSelected(AdapterView<?> adapterView) {
//                            }
//                        });
//                        break;
//                    case "부산":
//                        choice_do = array1.getItem(i).toString();
//                        array2 = ArrayAdapter.createFromResource(HospitalInformationActivity.this, R.array.spinner_busan, android.R.layout.simple_spinner_dropdown_item);
//                        array2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        spin2.setAdapter(array2);
//                        spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                            @Override
//                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                                choice_se = array2.getItem(i).toString();
//                            }
//
//                            @Override
//                            public void onNothingSelected(AdapterView<?> adapterView) {
//                            }
//                        });
//                        break;
//                    case "서울":
//                        choice_do = array1.getItem(i).toString();
//                        array2 = ArrayAdapter.createFromResource(HospitalInformationActivity.this, R.array.spinner_seoul, android.R.layout.simple_spinner_dropdown_item);
//                        array2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        spin2.setAdapter(array2);
//                        spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                            @Override
//                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                                choice_se = array2.getItem(i).toString();
//                            }
//
//                            @Override
//                            public void onNothingSelected(AdapterView<?> adapterView) {
//                            }
//                        });
//                        break;
//                    default:
//                        break;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//            }
//        });
        //종료
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //보건소 리스트 불러오기(파일)  나중에 try catch 처리해서 없으면 보건소 없음 표시
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String base = spin2.getSelectedItem().toString();
                String hospitalYadmNmArray[] = new String[hospitalInfromationDict.get(base).size()];
                String hospitalTelNo[] = new String[hospitalInfromationDict.get(base).size()];

                for (int i = 0; i < hospitalInfromationDict.get(base).size(); i++) {
                    hospitalYadmNmArray[i] = hospitalInfromationDict.get(base).get(i).yadmNm;
                    hospitalTelNo[i] = hospitalInfromationDict.get(base).get(i).telno;
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, hospitalYadmNmArray);

                hospitalList.setAdapter(adapter);

                hospitalList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String base2 = hospitalYadmNmArray[i];
                        Toast.makeText(getApplicationContext(), base2, Toast.LENGTH_SHORT).show();

                        Uri uri = Uri.parse("tel:"+hospitalTelNo[i]);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });
            }
        });


    }


}
