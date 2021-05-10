package kr.ac.kpu.se2017156030.mapdesign;

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

    public String toString(){
        return this.yadmNm;
    }
}
