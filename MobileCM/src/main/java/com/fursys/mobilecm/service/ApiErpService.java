package com.fursys.mobilecm.service;

import java.util.ArrayList;
import java.util.HashMap;

import com.fursys.mobilecm.vo.BaseResponse;
import com.fursys.mobilecm.vo.DataResult;
import com.fursys.mobilecm.vo.erp.ERPAsCalculateMoney;
import com.fursys.mobilecm.vo.erp.ERPAttachFileList;
import com.fursys.mobilecm.vo.erp.ERPSigongCalculateMoney;
import com.fursys.mobilecm.vo.erp.ERPSigongCalculateMoneyTeam;
import com.fursys.mobilecm.vo.erp.apm0020_m01.ds_tabList1;
import com.fursys.mobilecm.vo.mobile.response.AsResultResponse;
import com.fursys.mobilecm.vo.mobile.response.SigongResultResponse;

public interface ApiErpService {
		
	public SigongResultResponse erp_SiGongMigeulResultSave(HashMap<String,Object> param);
	public BaseResponse erp_requestGoGoVan(HashMap<String, Object> param);
	public ERPAsCalculateMoney erp_selectAsCalculateMoney(HashMap<String, Object> param);
	public ArrayList<ERPSigongCalculateMoneyTeam> erp_selectSigongCalculateMoneyTeam(HashMap<String, Object> param);
	
	public ERPSigongCalculateMoney erp_sigongCalculateMoney(HashMap<String, Object> param);
	public BaseResponse erp_AsResultSave(HashMap<String, Object> param);
	public BaseResponse erp_insertTcStiReq(HashMap<String, Object> param);
	public SigongResultResponse erp_SigonResultMigeulReason(HashMap<String, Object> param);
	//public AsResultResponse erp_AsCompleteSave(HashMap<String, Object> param);
	public SigongResultResponse erp_SiGongAccSave(HashMap<String,Object> param);
	public SigongResultResponse erp_SiGongResultSave(HashMap<String,Object> param);
	public ArrayList<ds_tabList1> selectAsDtlList(HashMap<String,Object> param);	
	public AsResultResponse erp_AsResultSaveRe(HashMap<String,Object> param);	
	
}
