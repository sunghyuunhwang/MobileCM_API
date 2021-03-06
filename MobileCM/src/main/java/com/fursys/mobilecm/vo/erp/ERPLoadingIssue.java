package com.fursys.mobilecm.vo.erp;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ERPLoadingIssue {
			
	private String rem_dt;
	private String rem_seq;
	private String com_ssec;
	private String seq_no;
	private String orm_no;
	private String plm_no;
	private String itm_cd;
	private String itm_nm;
	private int pld_fqty;
	private String col_cd;
	private String loadingissue_std;
	private String loadingissue_std_nm;
	private String loadingissue_remark;
	private String loadingissue_procstd;
	private String loadingissue_procstd_nm;
	private String loadingissue_procremark;
	private String loadingissue_qty;
	

}


