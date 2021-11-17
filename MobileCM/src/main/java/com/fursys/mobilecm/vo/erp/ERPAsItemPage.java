package com.fursys.mobilecm.vo.erp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ERPAsItemPage {
	
	private String bmt_nm ;
	private String bmtcd_col ;
	private Integer pld_fqty ; 
	private Integer total_cost ;
}


