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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class InfectionByTotalAPI extends AsyncTask<Void, Void, ArrayList<InfectionByTotal>> {
    private final String url;   //파싱할 url
    String startCreateDt, endCreateDt;  //파싱 시작일과 종료일
    ArrayList<InfectionByTotal> rdata = new ArrayList<InfectionByTotal>();  //해당 날짜 확진자 현황을 담을 ArrayList

    //클래스 생성자
    //url 지정 및 기준날짜 설정(앱 실행 날짜 기준 7일)
    public InfectionByTotalAPI(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);

        //날짜 1주일 설정
        endCreateDt = sdf.format(cal.getTime());
        cal.add(Calendar.DATE,-7);
        startCreateDt = sdf.format(cal.getTime());

        //url 지정
        URL url = null;
        try {
            String ServiceKey = "rl%2B8bqQgAXlgml1MRoJIqGc1YcMKT31NQdmV2graSOPOnxBBdSAAtnKp%2F7XR54yLXVpvKhTnv7UhUw%2FTBjqw9Q%3D%3D";
            StringBuilder urlBuilder = new StringBuilder("http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19InfStateJson"); /*URL*/
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

    //쓰레드 백그라운드 실행 동작
    //해당 날짜 확진자 현황 ArrayList에 저장
    @Override
    protected ArrayList<InfectionByTotal> doInBackground(Void ...voids){
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

            String state_dt = getTagValue("stateDt", eElement); //기준일
            String decide_cnt = getTagValue("decideCnt", eElement); //확진자 수

            rdata.add(new InfectionByTotal(state_dt, decide_cnt));
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
