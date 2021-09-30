package com.fursys.mobilecm.vo.erp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ERPAsFileList {
	private String file_type ;
	private String attch_file_id ;
	private String attch_file_nm ;
	private String file_knd ; 
	private String attch_div_cd;
	private String attch_file_snum;
	private String download_url;
	private String seq;
	private String grp_id;
	private String img_id;
	private int img_cnt; 
}


