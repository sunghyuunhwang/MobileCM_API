package com.fursys.mobilecm.vo.erp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ERPStiSituationInfoList {
	
	private int total_cnt = 0;
	private int finish_cnt = 0;
	private int notfinish_cnt = 0;
	private String plm_cdt = "";
	private String sti_cd = "";
	private String sti_nm = "";
	private String arrivaltm_yn = "";
	private String arrivaltm_sendyn = "";
	private String stm_hp = "";
	private String com_scd = "";
}


