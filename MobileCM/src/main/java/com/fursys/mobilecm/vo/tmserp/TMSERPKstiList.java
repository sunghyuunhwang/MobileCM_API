package com.fursys.mobilecm.vo.tmserp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TMSERPKstiList {
	
	private String rem_dt;
	private String com_scd;
	private String sti_cd;
	private String sti_nm;
	private Integer orm_qty ;
	private Integer orm_amt ;
	private Integer constcst_sum;
	private Integer real_cnt;
	private Integer as_count;
}


