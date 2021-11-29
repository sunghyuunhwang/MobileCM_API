package com.fursys.mobilecm.vo.tmserp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TMSERPVndBanpum{
	public String plm_cdt;
	public String sti_nm;
	public String com_brand_nm;
	public String vnd_nm;
	public String orm_nm;
	public String orm_no;
	public String set_cd;
	public String col_scd;
	public String itm_cd;
	public String col_cd;
	public String ord_qty;
	public String wtp_planqty;
	public String wtp_finish;
	public String wtp_finish_nm;
	public String wtp_entdt; // 반품등록일시
	public String com_rdsec; // 모바일처리상태
	public String com_rdsec_nm;
	public String com_undsec; // 품목별세부상태
	public String com_undsec_nm;
	public String file_yn;
	public String plm_no;
}


