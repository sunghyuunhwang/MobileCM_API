package com.fursys.mobilecm.vo.erp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ERPAsItemReport {
	private String bmt_item = "";
	private String col_cd = "";
	private String bmt_nm = "";
	private String com_rpftyp = "";
	private int ast_actqty = 0;
	private String com_spycod = "";
	private int bmt_samt = 0;
	private String vnd_cd = "";
	private String defect_gubun = "";
	private String defect_typ = "";
	private String mob_useyn = "";
	private int mob_notuseqty = 0;
	private String mob_remark = "";
	
}


