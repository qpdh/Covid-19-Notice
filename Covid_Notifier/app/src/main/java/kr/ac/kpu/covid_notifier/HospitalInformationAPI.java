package kr.ac.kpu.covid_notifier;

import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class HospitalInformationAPI extends AsyncTask<Void, Void, ArrayList<ArrayList<HospitalInformation>>> {
    private final String url;   //파싱할 url
    ArrayList<ArrayList<HospitalInformation>> rdata = new ArrayList<ArrayList<HospitalInformation>>();  //각 기관별 ArrayList를 저장할 ArrayList
    ArrayList<HospitalInformation> CA0 = new ArrayList<HospitalInformation>();  //코드 A0에 해당하는 기관의 정보를 저장하는 ArrayList
    ArrayList<HospitalInformation> C97 = new ArrayList<HospitalInformation>();  //코드 97에 해당하는 기관의 정보를 저장하는 ArrayList
    ArrayList<HospitalInformation> C99 = new ArrayList<HospitalInformation>();  //코드 99에 해당하는 기관의 정보를 저장하는 ArrayList

    //클래스 생성자
    //url 지정 및 기관 리스트 조회
    public HospitalInformationAPI() {
        URL url = null;
        try {
            String ServiceKey = "rl%2B8bqQgAXlgml1MRoJIqGc1YcMKT31NQdmV2graSOPOnxBBdSAAtnKp%2F7XR54yLXVpvKhTnv7UhUw%2FTBjqw9Q%3D%3D";
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B551182/pubReliefHospService/getpubReliefHospList"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + ServiceKey); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + URLEncoder.encode("-", "UTF-8")); /*공공데이터포털에서 받은 인증키*/
            urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("1200", "UTF-8")); /*한 페이지 결과 수*/

            url = new URL(urlBuilder.toString());

        } catch (MalformedURLException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.url = url.toString();
    }

    //쓰레드 백그라운드 실행 동작 함수
    //기관 종류에 해당하는 지역별 기관 각 ArrayList에 저장
    @Override
    protected ArrayList<ArrayList<HospitalInformation>> doInBackground(Void ... voids) {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        Document doc = null;

        //xml 파싱
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(url);
            doc.getDocumentElement().normalize();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("API", "IOException");
        }

        NodeList nList = doc.getElementsByTagName("item");

        //파싱한 데이터 저장
        for (int i = 0; i < nList.getLength(); i++) {
            Element eElement = (Element) nList.item(i);

            String sidoNm = getTagValue("sidoNm", eElement);    //시도명
            String sgguNm = getTagValue("sgguNm", eElement);    //시군구명
            sgguNm = sgguNm.split(" ")[0];                    //시군구 앞단위만 자름
            String code = getTagValue("spclAdmTyCd", eElement); //기관 코드 A0 : 국민안심병원/ 97 : 코로나검사 실시기관/ 99 : 코로나 선별진료소 운영기관
            String telno = getTagValue("telno", eElement);      //전화번호
            String yadmNm = getTagValue("yadmNm", eElement);    //기관명

            switch (code){
                case "A0":
                    CA0.add(new HospitalInformation(sidoNm, sgguNm, telno, yadmNm));
                    break;
                case "97":
                    C97.add(new HospitalInformation(sidoNm, sgguNm, telno, yadmNm));
                    break;
                case "99":
                    C99.add(new HospitalInformation(sidoNm, sgguNm, telno, yadmNm));
                    break;
            }
        }
        rdata.add(CA0);
        rdata.add(C97);
        rdata.add(C99);
        return rdata;
    }

    //xml에서 tag값에 해당하는 데이터를 파싱하는 함수
    private static String getTagValue(String tag, Element eElement) {
        try {
            String result = eElement.getElementsByTagName(tag).item(0).getTextContent();
            return result;
        } catch (NullPointerException e) {
            return "";
        } catch (Exception e) {
            return "";
        }
    }
}