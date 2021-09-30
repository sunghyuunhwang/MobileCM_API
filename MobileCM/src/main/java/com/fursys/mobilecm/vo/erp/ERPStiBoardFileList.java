package com.fursys.mobilecm.vo.erp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ERPStiBoardFileList {
	private String attch_file_id;
	private String attch_div_cd;
	private String real_attch_file_name;
	private String attch_file_snum;
}
