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
public class ERPOrdLdList {
	private String com_ssec;
	private String com_brand;
	private String com_agsec;
	private String plm_no;
	private String orm_no;
	private String orm_nm;
	private String michul_yn;
	private int itm_qty;
	private int itm_sum;
	private String return_yn;
	private int arrival_seq;
	private String plm_ftmnm;
	private String loading_yn;
	private String loadingissue_yn;
}
