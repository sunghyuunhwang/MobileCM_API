package com.fursys.mobilecm.vo.erp;

import com.fursys.mobilecm.vo.tms.TmsCenter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ERPSti {
	private String sti_cd;
	private String sti_nm;
	private String com_scd_nm;
	private String com_ctsec;
	private String com_scd;
}
