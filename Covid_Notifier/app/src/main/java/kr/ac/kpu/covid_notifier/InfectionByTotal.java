///////////////////
// 전체 일일 현황 //
///////////////////
// String state_dt     기준 날짜
// String decide_cnt   기준 시각
////////////////////////////

package kr.ac.kpu.covid_notifier;

public class InfectionByTotal {
    String state_dt;
    String decide_cnt;

    InfectionByTotal(String state_dt, String decide_cnt) {
        this.state_dt = state_dt;
        this.decide_cnt = decide_cnt;
    }
}