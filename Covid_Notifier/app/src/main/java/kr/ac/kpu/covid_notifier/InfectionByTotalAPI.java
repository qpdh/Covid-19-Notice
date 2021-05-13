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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class InfectionByTotalAPI extends AsyncTask<Void, Void, ArrayList<InfectionByTotal>> {
    private final String url;

    ArrayList<InfectionByTotal> rdata = new ArrayList<InfectionByTotal>();

    public InfectionByTotalAPI(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);

        String endCreateDt = sdf.format(cal.getTime());

        cal.add(Calendar.DATE,-7);
        String startCreateDt = sdf.format(cal.getTime());

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
    protected ArrayList<InfectionByTotal> doInBackground(Void ...voids){
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

            rdata.add(new InfectionByTotal(state_dt, decide_cnt));
        }
        return rdata;
    }

    @Override
    protected void onPostExecute(ArrayList<InfectionByTotal> result) {
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
