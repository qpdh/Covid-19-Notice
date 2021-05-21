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
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class InfectionByRegionAPI extends AsyncTask<Void, Void, ArrayList<InfectionByRegion>> {
    private final String url;   //파싱할 url
    String startCreateDt, endCreateDt;  //파싱 시작일과 종료일
    ArrayList<InfectionByRegion> rdata = new ArrayList<InfectionByRegion>();    //지역별 확진자 현황을 담을 ArrayList
    DecimalFormat dc = new DecimalFormat("###,###,###");    //숫자 자릿 수 표시

    //클래스 생성자
    //url 지정 및 기준날짜 설정(앱 실행 날짜 기준)
    public InfectionByRegionAPI(String today) {
        startCreateDt = today;
        endCreateDt = today;

        //url 지정
        URL url = null;
        try {
            String ServiceKey = "rl%2B8bqQgAXlgml1MRoJIqGc1YcMKT31NQdmV2graSOPOnxBBdSAAtnKp%2F7XR54yLXVpvKhTnv7UhUw%2FTBjqw9Q%3D%3D";
            StringBuilder urlBuilder = new StringBuilder("http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19SidoInfStateJson"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + ServiceKey); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + URLEncoder.encode("-", "UTF-8")); /*공공데이터포털에서 받은 인증키*/
            urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("40", "UTF-8")); /*한 페이지 결과 수*/
            urlBuilder.append("&" + URLEncoder.encode("startCreateDt","UTF-8") + "=" + URLEncoder.encode(startCreateDt, "UTF-8")); /*검색할 생성일 범위의 시작*/
            urlBuilder.append("&" + URLEncoder.encode("endCreateDt","UTF-8") + "=" + URLEncoder.encode(endCreateDt, "UTF-8")); /*검색할 생성일 범위의 종료*/

            url = new URL(urlBuilder.toString());

        } catch (MalformedURLException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.url = url.toString();
    }


    //쓰레드 백그라운드 실행 동작 함수
    //지역별 확진자 현황 ArrayList에 저장
    @Override
    protected ArrayList<InfectionByRegion> doInBackground(Void... voids) {
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

            String gubun = getTagValue("gubun", eElement);  //시도명
            String std_day = getTagValue("stdDay", eElement);   //기준일시
            String def_cnt = dc.format(Integer.parseInt(getTagValue("defCnt", eElement)));  //확진자 수
            String inc_cnt = dc.format(Integer.parseInt(getTagValue("incDec", eElement)));  //전일대비 증감 수

            rdata.add(new InfectionByRegion(gubun, std_day, def_cnt, inc_cnt));
        }
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