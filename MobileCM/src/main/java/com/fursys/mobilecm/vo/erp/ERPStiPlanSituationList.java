package com.fursys.mobilecm.vo.erp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ERPStiPlanSituationList {
	
	private String mob_starttm = "";
	private String mob_endtm = "";
	private String work_time = "";
	private String orm_nm = "";
	private String mob_status = "";
	private String arrivaltm_yn = "";
	private String arrivaltm_sendyn = "";
}


