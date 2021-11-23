package com.fursys.mobilecm.vo.erp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ERPPendencyList {
	private String com_ssec ; // COM_SSEC
	private String com_brand; //COM_BRAND
	
	private String plm_no;
	private String orm_no;
	private String orm_nm;
	private String sti_nm;
	private String plm_cdt;
	private String pld_rcdt;	/*재일정요청일자*/
	private String drop_spot;	/*하차장소*/
	private String drop_spot_nm;/*하차장소*/
	private String drop_rmk;
	private String lgs_stat;	/*입고상태*/
	private String inp_rmk;		/*입고특이사항*/
	private String com_undsec;  /*구분*/
	private String as_yn;
	private String re_yn;
	private String return_yn;
	private int file_cnt = 0;
	   
}


