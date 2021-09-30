package com.fursys.mobilecm.vo.erp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ERPReReserveInfo {
	private String rem_dt; //일자
	private String sti_nm ; //팀명
	private String stm_hp ; //HP
	private String re_std ; //재일정사유
}


