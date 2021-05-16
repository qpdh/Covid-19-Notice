/////////////////////
// 코로나19병원정보 //
/////////////////////
// String sidoNm    시도 명
// String sgguNm     시군구 명
// String telno      전화번호
// String yadmNm     병원 이름
//////////////////////////////////////

package kr.ac.kpu.covid_notifier;

public class HospitalInformation {
    String sidoNm;
    String sgguNm;
    String telno;
    String yadmNm;

    public HospitalInformation(String sidoNm, String sgguNm, String telno, String yadmNm) {
        this.sidoNm = sidoNm;
        this.sgguNm = sgguNm;
        this.telno = telno;
        this.yadmNm = yadmNm;
    }

    public String toString() {
        return this.yadmNm;
    }
}
