package com.fursys.mobilecm.vo.erp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ERPConstructionItemResultPage {
	
	private String gubun_nm ;
	private String set_cd ;
	private String col_scd ;
	private String itm_cd ;
	private String itm_nm ;
	private String col_cd ;
	private Integer pld_eqty;
	private String com_rdsec;
	private String plm_no;
	private String trs_sec_2;
	private String com_pldsec;
	private String ord_sseq;
	private String ord_iseq;
	private String orm_gubbun;
	
}


