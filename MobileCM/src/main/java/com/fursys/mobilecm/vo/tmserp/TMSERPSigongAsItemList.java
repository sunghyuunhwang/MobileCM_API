package com.fursys.mobilecm.vo.tmserp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TMSERPSigongAsItemList {
	
	private String com_pldsec;
	private String itmcd_col;
	private Integer ord_amt;
	private Integer pld_fqty;
	private String itm_nm;
	private Integer pld_sum;
	
}


