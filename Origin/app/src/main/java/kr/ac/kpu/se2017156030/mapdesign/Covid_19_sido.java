package kr.ac.kpu.se2017156030.mapdesign;

import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Covid_19_sido extends AsyncTask<Void, Void, ArrayList<InfectionByRegion>> {
    private final String url;
    Date date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
    String today = sdf.format(date);
    String startCreateDt = today;
    String endCreateDt = today;
    ArrayList<InfectionByRegion> rdata = new ArrayList<InfectionByRegion>();

    // 클래스 생성자 : URL 정리 초기화
    public Covid_19_sido() {

        URL url = null;
        try {
            String ServiceKey = "rl%2B8bqQgAXlgml1MRoJIqGc1YcMKT31NQdmV2graSOPOnxBBdSAAtnKp%2F7XR54yLXVpvKhTnv7UhUw%2FTBjqw9Q%3D%3D";
            String url2 = "http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19SidoInfStateJson?serviceKey="
                    + ServiceKey + "&pageNo=1&numOfRows=40&startCreateDt=" + startCreateDt + "&endCreateDt="+ endCreateDt;

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
    protected ArrayList<InfectionByRegion> doInBackground(Void ...voids) {
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

            String gubun = getTagValue("gubun", eElement);
            String def_cnt = getTagValue("defCnt", eElement);
            String inc_cnt = getTagValue("incDec", eElement);
            String std_day = getTagValue("stdDay", eElement);

            rdata.add(new InfectionByRegion(gubun, def_cnt, inc_cnt, std_day));
        }
        return rdata;
    }

    @Override
    protected void onPostExecute(ArrayList<InfectionByRegion> result) {
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