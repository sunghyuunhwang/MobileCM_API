package com.fursys.mobilecm.vo.erp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ERPStiBoardList {
	private String ccd_nm;
	private String board_id;
	private String board_nm;
	private String board_writer;
	private String write_dt;
}
