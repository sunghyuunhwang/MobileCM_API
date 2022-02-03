package com.fursys.mobilecm.vo.erp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ERPItemOrdSummary {
	private String orm_nm;
	private String orm_gaddr;
	private String plm_ftmnm;
	private String loading_yn;
	private String loadingissue_yn;
    
}
