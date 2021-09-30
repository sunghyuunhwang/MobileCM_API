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
public class ERPSigongSearchDetailInfo {
	
	private String itmcd_col = "";
	private String itm_nm = "";
	private String com_pldsec = "";
	private Integer qty = 0;
	private Integer amt = 0;
}


