package com.fursys.mobilecm.vo;

import com.fursys.mobilecm.vo.FursysUser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FursysUser {
	private String usr_cd;
	private String usr_nm;
	private String com_cmp;
	private String vnd_cd;
	private String vnd_nm;
	private String grp_cd;
	private String user_email;
	private String usr_pwd;
	private String usr_rip;
	private String usr_logindt;
	private String menu_grp;
	private String usr_pno;
	private String cnt;
	private String pwd_chgdt;
	private String partners_agr_yn;
	private String partners_agr_dt;
	private String com_scd;
	private String sti_cd;
	private String k_sti_cd;	
	private String db; // REAL:운영, TEST:테스트
	private String tmapkey="";
	private String com_stsec=""; 
	private String cmjungsan_pwd = "";
}
