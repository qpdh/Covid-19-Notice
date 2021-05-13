/////////////////////
// 지역별 감염 현황 //
/////////////////////
// String gubun     시도 명(한글)
// String std_day	기준 일시
// String def_cnt	확진자 수
// String inc_dec	전일 대비 증감 수
//////////////////////////////////////

package kr.ac.kpu.covid_notifier;

public class InfectionByRegion {
    String gubun;
    String std_day;
    String def_cnt;
    String inc_dec;


    InfectionByRegion(String gubun, String std_day, String def_cnt, String inc_dec) {
        this.gubun = gubun;
        this.std_day = std_day;
        this.def_cnt = def_cnt;
        this.inc_dec = inc_dec;
    }
}
