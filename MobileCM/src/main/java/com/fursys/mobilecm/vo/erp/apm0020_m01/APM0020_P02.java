package com.fursys.mobilecm.vo.erp.apm0020_m01;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class APM0020_P02 {
	public String ps1 ="";
	public String strRptNo = "";
	public String strRptSeq = "";
	public String strOrmAdt = "";
	public String strRePlmNo = "";
	public String strPlmNo = "";
	public String strRemDt = "";
	public String strRemSeq = "";
	public String reProcYn = "";		//재시공유무 Flag
	
	public String strComAgsec = "";
	public String v_remCasyn = "", v_agtCd = "", v_ormNm = "", v_ctmZip = "", v_ormPurcst = "";
	public String v_stiCd = "", v_sacCd = "", v_comCtsec = "", v_comScd = "", v_ormNo = "";
		
	public ds_planMst ds_planMst = new ds_planMst();
	public ds_result ds_result = new ds_result();
	public ds_complmfg ds_complmfg = new ds_complmfg();
//	
//	public class ds_planMst {
//		public String rpt_no = "";
//		public String rpt_seq = "";
//		public String ctm_nm = "";
//		public String agt_cd = "";
//		public String ctm_zip = "";
//		public String ctm_cd = "";
//		public String sti_cd = "";
//		public String sac_cd = "";
//		public String com_ctsec = "";
//		public String com_scd = "";
//		public String orm_adt = "";
//		public String com_agsec = "";	
//	}
//	
//	public class ds_result {
//		public String rem_dt = "";
//		public String rem_seq = "";
//		public String plm_no = "";
//		public String orm_no = "";
//		public String prj_cd = "";
//		public String rem_ptm = "";
//		public String rem_ftm = "";
//		public String rem_tmfyn = "";
//		public String rem_ntm = "";
//		public String plm_etm = "";
//		public String plm_fixedt = "";
//		public String com_agsec = "";
//		public String com_ssec = "";
//		public String sti_cd = "";
//		public String com_ctsec = "";
//		public String sac_cd = "";
//		public String orm_amt = "";
//		public String orm_purcst = "";
//		public String orm_nm = "";
//		public String rem_clamt = "";
//		public String com_scd = "";
//		public String stm_no = "";
//		public String agt_cd = "";
//		public String com_rfg = "";
//		public String rem_asno = "";
//		public String rem_rmk = "";
//		public String usr_cd = "";
//		public String sys_dt = "";
//		public String zip_cd = "";
//		public String rem_extyn = "";
//		public String rem_casyn = "";
//		public String rem_csec = "";
//		public String rpt_no = "";
//		public String rpt_seq = "";
//		public String new_plm_no = "";
//		public String new_rem_seq = "";
//		public String pld_rcdt = "";
//	}
//	
//	public class ds_complmfg {
//		public String com_plmfg = "";
//		public String plm_no = "";
//		public String plm_cdt = "";
//		public String orm_rdt = "";	
//		public String usr_id = "";
//	}
	

}
