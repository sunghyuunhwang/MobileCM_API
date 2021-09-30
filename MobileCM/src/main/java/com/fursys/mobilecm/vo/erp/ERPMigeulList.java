package com.fursys.mobilecm.vo.erp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ERPMigeulList {
	private String com_ssec ; // COM_SSEC
	private String com_brand; //COM_BRAND
	private String plm_no  ;
	private String plm_cdt ;
	private String orm_nm ;
	private String as_yn;
	private String re_yn;
	private String return_yn;
	private String addas_yn;
	private String ascancel_yn;
}


