import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class XmlParsing_city {

	private static String getTagValue(String tag, Element eElement) {
		try{
	        String result = eElement.getElementsByTagName(tag).item(0).getTextContent();
	        return result;
	    } catch(NullPointerException e){
	        return "NullPointer Error";
	    } catch(Exception e){
	        return "Other Error";
	    }
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
		String today = sdf.format(date);
		String startCreateDt = today;
		String endCreateDt = today;
		
		try {
			
			String ServiceKey = "rl%2B8bqQgAXlgml1MRoJIqGc1YcMKT31NQdmV2graSOPOnxBBdSAAtnKp%2F7XR54yLXVpvKhTnv7UhUw%2FTBjqw9Q%3D%3D";
			String url = "http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19SidoInfStateJson?serviceKey=" 
			+ ServiceKey + "&pageNo=1&numOfRows=40&startCreateDt=" + startCreateDt + "&endCreateDt="+ endCreateDt;
			
			DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactoty.newDocumentBuilder();
			Document doc = dBuilder.parse(url);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("item");
			Node nNode = nList.item(0);
			Element eElement_date = (Element) nNode;
			DecimalFormat dc = new DecimalFormat("###,###,###,###");
			

			System.out.println("기준일 : " + getTagValue("stdDay", eElement_date));
			
			for(int temp = 1; temp < nList.getLength(); temp++){		
				nNode = nList.item(temp);
				if(nNode.getNodeType() == Node.ELEMENT_NODE){
									
					Element eElement = (Element) nNode;
					System.out.println("######################");
					System.out.println(getTagValue("gubun", eElement));
					System.out.println("누적 확진자 수  : " + dc.format(Integer.parseInt(getTagValue("defCnt", eElement))));
					System.out.println("사망자 수 : " + dc.format(Integer.parseInt(getTagValue("deathCnt", eElement))));
					System.out.println("전일대비 증감 수 : " + dc.format(Integer.parseInt(getTagValue("incDec", eElement))));
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			}	
	}

}
