package com.fursys.mobilecm.vo.erp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ERPManualSearchList {

	private String manual_id;
	private String manual_std;
	private String manual_name;
	private String manual_content;
	private String search_tag;
	private String usr_cd;
	private String sys_dt;
	
}


