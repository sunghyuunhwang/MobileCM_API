package com.fursys.mobilecm.vo.tmserp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TMSERPMigyeolDetailInfo {
	public String plm_no;
	public String com_pldsec;
	public String com_pldsec_nm;
	public String itm_cd;
	public String col_cd;
	public String pld_eqty;
	public String itm_nm;
	public String pld_famt;
	public String pld_cramt; /*예상금액*/
	public String pld_rmk; /*비고*/
	public String com_undsec; /*미결사유*/
	public String com_undsec_nm; 
}
