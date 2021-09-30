package com.fursys.mobilecm.vo.erp.apm0020_m01;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class APM0020_P03 {

	public String arrRtn[] = new String[8];
	
	public String strPlmNo = "";
	public String strCcYn = "";
	public String beforeRptNo = "";
	public String beforeRptSeq = "";
	public String strLsYn = "C13N";
	
	public btn_rstReg_onclick btn_rstReg_onclick = new btn_rstReg_onclick();
	
	public class btn_rstReg_onclick {
		public String v_ccstd = "";;
		public String v_ccrmk = "";;
		public String v_rmkTmp = "", v_rmk = "";
		public String v_date = "";
	}
	

}
