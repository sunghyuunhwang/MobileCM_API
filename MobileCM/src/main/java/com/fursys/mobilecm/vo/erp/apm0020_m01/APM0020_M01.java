package com.fursys.mobilecm.vo.erp.apm0020_m01;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class APM0020_M01 {
	
	public String as_com_unsec = "";
	public String v_rtnStiNm = "";
	public int v_rtnAmt = 0;
	public String v_com_unsec = "";
	public String beforeRptNo = "";
	public String beforeRptSeq = "";
	public String comUnmsec = "";

	public fn_finishyn fn_finishyn = new fn_finishyn();
	public fn_setRmfg fn_setRmfg = new fn_setRmfg();

	public btnReASP btnReASP = new btnReASP();
	public btn_selectRecProc_onclick btn_selectRecProc_onclick = new btn_selectRecProc_onclick();
	public btnSaveSP btnSaveSP = new btnSaveSP();
	public btn_allset_onclick btn_allset_onclick = new btn_allset_onclick();

	public ds_callbackUpt ds_callbackUpt = new ds_callbackUpt();
	public ds_planInfo ds_planInfo = new ds_planInfo();
	public ds_uptplandtl ds_uptplandtl = new ds_uptplandtl();
	public ArrayList<ds_list2> ds_list2 = new ArrayList<ds_list2>();
	public ds_allset ds_allset = new ds_allset();
	public ds_rptreq ds_rptreq = new ds_rptreq();
	public ds_btrip ds_btrip = new ds_btrip();
	public ArrayList<ds_tabList1> ds_tabList1 = new ArrayList<ds_tabList1>();
	public ds_list1 ds_list1 = new ds_list1();
	
	public void ds_uptplandtl_reset() {
		ds_uptplandtl = new ds_uptplandtl();
	}
	
	public class btnReASP {
		public String v_pldRmk = "";		
		public String v_plmNo	= "";
	}
	
	public class fn_finishyn {
		public String v_rtnStr = "";
		public int lln = 0, llw = 0, lly = 0;
	}
	
	public class fn_setRmfg {
		public String v_comRmfg = "", v_plmNo = "";
		public int nRow = 0;
		public int llRow = 0;
	}
	
	public class btn_selectRecProc_onclick {
		public int nCnt = 0, nRow = 0, nllyn = 0;
		public String v_ormNo = "", v_plmNo = "", v_maxRcdt = "", v_rePlmNo = "";//재시공번호
		public String v_pldRmk = "", v_rptNo = "", v_rptSeq = "", v_comAgsec = "";
		public String v_comRdsec = "", v_comUnpsec = "", v_pldRcdt = "";
		public String v_remDt = "", v_remSeq = "";
	}
	
	public class btn_allset_onclick {
		public int nChk = 0, nCnt = 0;
		public String v_comPldsec = "", v_plmNo = "", v_comRmfg = "", v_ccyn = "", v_comSprog = "", v_comScd = "", v_stiCd = "";
	}
	
	public class btnSaveSP {
		public String v_yn = "", v_rmk = "", v_rmk1 = "", v_comUnpsec = "", v_rptNo = "", v_rptSeq = "", v_stiNm = "", v_stiCd = "", v_comScd = "";
		public String v_plmNo = "", v_comAgesec = "";
		public int nRow = 0;
	}
	
	
//	public class ds_callbackUpt {
//		public String plm_no = "";
//		public String re_plm_no = "";
//	}
//	
//	public class ds_planInfo {		
//		public String rpt_no = "";
//		public String rpt_seq = "";
//	}
//	
//	public class ds_uptplandtl {		
//		public String plm_no = "";
//		public String com_unpsec = "";
//		public String pld_rmk = "";
//		public String pld_rcdt = "";
//		public String usr_id = "";
//	}
//	
//	public class ds_list2 {
//		public String bmt_item = "";
//		public String col_cd = "";
//		public String pld_famt = "";
//		public String pld_eqty = "";
//		public String pld_cramt = "";
//		public String plm_no = "";
//		public String com_pldsec = "";
//		public String ord_sseq = "";
//		public String usr_id = "";
//		public String sys_dt = "";
//		public String itm_nm = "";
//		public String com_rdsec = "";
//		public String com_undsec = "";
//		public String com_unpsec = "";
//		public String pld_rcdt = "";
//		public String pld_fqty = "";
//		public String pld_cfamt = "";
//		public String chk = "";
//		public String pld_rmk = "";
//		public String com_undsec1 = "";
//		public String com_undsec2 = "";
//		public String bmt_nm = "";
//	}
//	
//	public class ds_allset {
//		public String plm_no = "";
//		public String com_plmfg = "";
//		public String com_unmsec = "";
//		public String com_rmfg = "";
//		public String usr_id = "";
//	}
//	
//	public class ds_rptreq {
//		public String rpt_no = "";
//		public String rpt_seq = "";
//		public String rpt_adesc = "";
//		public String ls_yn = "";
//		public String usr_id = "";
//	}
//	
//	public class ds_btrip {
//		public String plm_no = "";
//		public String com_pldsec = "";
//		public String ord_sseq = "";
//		public int pld_fqty = 0;
//		public int pld_famt = 0;
//		public String com_acd = "";
//		public String com_svnd = "";
//		public String com_vfsec = "";
//		public String com_vtsec = "";
//		public String com_rasec = "";
//		public String com_agsec = "";
//		public String pld_cfamt = "";
//		public String usr_id = "";
//	}
//	
//	public class ds_list1 {
//		public String chk = "";
//		public String sigong_y = "";
//		public String rem_dt = "";
//		public String rpt_no = "";
//		public String rpt_seq = "";
//		public String com_atsec = "";
//		public String rem_ftm = "";
//		public String plm_ftm = "";
//		public String vnd_nm = "";
//		public String vnd_telno = "";
//		public String orm_nm = "";
//		public String file_yn = "";
//		public String cc = "";
//		public String sign_yn = "";
//		public String ctm_nm = "";
//		public String ctm_addr = "";
//		public String sti_cd = "";
//		public String com_sprog = "";
//		public String com_rmfg = "";
//		public String com_plmfg = "";
//		public String plm_rmk = "";
//		public String nofin_yn = "";
//		public String mobile2 = "";
//		public String mobile3 = "";
//		public String rem_csec = "";
//		public String plm_no = "";
//		public String notfinish_yn = "";
//		public String re = "";
//		public String refund = "";
//		public String rem_seq = "";
//		public String file_id = "";
//		public String com_unpsec = "";
//		public String pld_rcdt = "";
//		public String plm_ftm_gubun = "";
//		public String com_scd = "";
//		public String mob = "";
//		public String plm_cdt = "";
//		public String ctm_hp = "";	
//		
//		public String com_agsec = "";
//		public String com_unmsec = "";
//	}
//	
//	public class ds_tabList1 {
//		public String bmt_item = "";
//		public String col_cd = "";
//		public String pld_famt = "";
//		public String pld_eqty = "";
//		public String pld_cramt = "";
//		public String com_pldsec = "";
//		public String ord_sseq = "";
//		public String bmt_nm = "";
//		public String plm_no = "";
//		public String com_rdsec = "";
//		public String com_undsec = "";
//		public String usr_id = "";
//		public String sys_dt = "";
//		public String com_unpsec = "";
//		public String pld_rcdt = "";
//		public int pld_fqty = 0;
//		public String column16 = "";
//		public int pld_cfamt = 0;
//		public String chk = "";
//		public String pld_rmk = "";
//		public String com_undsec1 = "";
//		public String com_undsec2 = "";
//		public String amt = "";
//	}


}
