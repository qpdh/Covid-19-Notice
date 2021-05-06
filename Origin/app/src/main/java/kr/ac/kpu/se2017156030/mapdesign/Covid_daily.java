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

public class Covid_daily extends AsyncTask<Void, Void, ArrayList<InfectionBydaily>> {
    private final String url;
    Date date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
    String today = sdf.format(date);
    String startCreateDt;
    String endCreateDt = today;

    ArrayList<InfectionBydaily> rdata = new ArrayList<InfectionBydaily>();

    public Covid_daily(String wantday){
        startCreateDt = wantday;
        URL url = null;
        try {
            String ServiceKey = "rl%2B8bqQgAXlgml1MRoJIqGc1YcMKT31NQdmV2graSOPOnxBBdSAAtnKp%2F7XR54yLXVpvKhTnv7UhUw%2FTBjqw9Q%3D%3D";
            String url2 = "http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19InfStateJson?serviceKey="
                    + ServiceKey + "&pageNo=1&numOfRows=40&startCreateDt=" + startCreateDt + "&endCreateDt="+ endCreateDt;

            url = new URL(url2);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        this.url = url.toString();
    }
    @Override
    protected ArrayList<InfectionBydaily> doInBackground(Void ...voids){
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

            String state_dt = getTagValue("stateDt", eElement);
            String decide_cnt = getTagValue("decideCnt", eElement);

            rdata.add(new InfectionBydaily(state_dt, decide_cnt));
        }
        return rdata;
    }

    @Override
    protected void onPostExecute(ArrayList<InfectionBydaily> result) {
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
