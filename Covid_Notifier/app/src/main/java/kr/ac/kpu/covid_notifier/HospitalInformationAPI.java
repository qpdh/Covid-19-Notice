package kr.ac.kpu.covid_notifier;

import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class HospitalInformationAPI extends AsyncTask<Void, Void, ArrayList<ArrayList<HospitalInformation>>> {
    private final String url;

    ArrayList<ArrayList<HospitalInformation>> rdata = new ArrayList<ArrayList<HospitalInformation>>();
    ArrayList<HospitalInformation> CA0 = new ArrayList<HospitalInformation>();
    ArrayList<HospitalInformation> C97 = new ArrayList<HospitalInformation>();
    ArrayList<HospitalInformation> C99 = new ArrayList<HospitalInformation>();

    // 클래스 생성자 : URL 정리 초기화
    public HospitalInformationAPI() {
        URL url = null;
        try {
            String ServiceKey = "re%2B4ZbFs0erT%2Bg5bW2VLp2lNnYogTrEt0R7QKSYaDpZh4g1hbkj8kgaNSL7JTXaXugynM9f8TSqjeODqtP9Dow%3D%3D";
            String url2 = "http://apis.data.go.kr/B551182/pubReliefHospService/getpubReliefHospList?serviceKey="
                    + ServiceKey + "&pageNo=1&numOfRows=1200" ;

            url = new URL(url2);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        this.url = url.toString();
    }


    // 쓰레드 동작
    // 다큐먼트 만들기
    // doc 반환
    @Override
    protected ArrayList<ArrayList<HospitalInformation>> doInBackground(Void ... voids) {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        Document doc = null;

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
        //Document doc = dBuilder.parse(url.toString());

        NodeList nList = doc.getElementsByTagName("item");

        for (int i = 0; i < nList.getLength(); i++) {
            Element eElement = (Element) nList.item(i);

            String sidoNm = getTagValue("sidoNm", eElement);
            String sgguNm = getTagValue("sgguNm", eElement);
            sgguNm = sgguNm.split(" ")[0];
            String code = getTagValue("spclAdmTyCd", eElement);
            String telno = getTagValue("telno", eElement);
            String yadmNm = getTagValue("yadmNm", eElement);

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

    @Override
    protected void onPostExecute(ArrayList<ArrayList<HospitalInformation>> result) {
        super.onPostExecute(result);
    }

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