package com.fursys.mobilecm.vo.erp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ERPStiBoard {
	private String ccd_nm;
	private String board_id;
	private String board_nm;
	private String board_writer;
	private String board_content;
	private String com_scd;
	private String write_dt;
	private String user_cd;
	private String sys_dt;
}
