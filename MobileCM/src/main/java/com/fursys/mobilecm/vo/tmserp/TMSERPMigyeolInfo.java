package com.fursys.mobilecm.vo.tmserp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TMSERPMigyeolInfo {
	public String plm_no;
	public String orm_no;
	public String com_brand;
	public String com_brand_nm;
	public String agt_cd;
	public String sti_cd;
	public String plm_cdt;
	public String orm_nm;
	public String sti_nm;
	public String vnd_nm;
	public String vnd_cd;
	public String unpsec_a_yn;
	public String unpsec_e_yn;
	public String unpsec_r_yn;
	public String unpsec_c_yn;
	public String file_yn;
	public String mob_remark;
}
