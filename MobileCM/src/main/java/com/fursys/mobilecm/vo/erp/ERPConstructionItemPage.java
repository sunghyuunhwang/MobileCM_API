package com.fursys.mobilecm.vo.erp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ERPConstructionItemPage {
	private String itm_nm ;
	private String itmcd_col ;
	private String setcd_col ;
	private Integer pld_fqty ; 
	private String pld_ssec;
	private Integer pld_sum;
}


