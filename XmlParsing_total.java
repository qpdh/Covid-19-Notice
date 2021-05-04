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


public class XmlParsing_total {

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
		String startCreateDt = "20210401"; //tempdate
		String endCreateDt = today;
		
		try {
			
			String ServiceKey = "rl%2B8bqQgAXlgml1MRoJIqGc1YcMKT31NQdmV2graSOPOnxBBdSAAtnKp%2F7XR54yLXVpvKhTnv7UhUw%2FTBjqw9Q%3D%3D";
			String url = "http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19InfStateJson?serviceKey=" 
			+ ServiceKey + "&pageNo=1&numOfRows=1&startCreateDt="+startCreateDt+"&endCreateDt="+endCreateDt;
			
			DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactoty.newDocumentBuilder();
			Document doc = dBuilder.parse(url);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("item");
			DecimalFormat dc = new DecimalFormat("###,###,###,###,###");
			
			for(int temp = 0; temp < nList.getLength(); temp++){		
				Node nNode = nList.item(temp);
				if(nNode.getNodeType() == Node.ELEMENT_NODE){
									
					Element eElement = (Element) nNode;
					System.out.println("######################");
					
					System.out.println("기준일 : " + getTagValue("stateDt", eElement));
					System.out.println("확진자 수 : " + dc.format(Integer.parseInt(getTagValue("decideCnt", eElement))));
					System.out.println("검사진행 수  : " + dc.format(Integer.parseInt(getTagValue("examCnt", eElement))));
					System.out.println("사망자 수 : " + dc.format(Integer.parseInt(getTagValue("deathCnt", eElement))));
					System.out.println("결과 음성 수 : " + dc.format(Integer.parseInt(getTagValue("resutlNegCnt", eElement))));
					System.out.println("누적검사 수  : " + dc.format(Integer.parseInt(getTagValue("accExamCnt", eElement))));
					System.out.println("누적환진률  : " + getTagValue("accDefRate", eElement));
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			}	
	}

}
