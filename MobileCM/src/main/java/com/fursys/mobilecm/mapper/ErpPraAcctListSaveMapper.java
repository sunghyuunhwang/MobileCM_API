package com.fursys.mobilecm.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.fursys.mobilecm.vo.DataResult;
import com.fursys.mobilecm.vo.erp.ERPAsResDtlList;
import com.fursys.mobilecm.vo.erp.apm0020_m01.APM0020_M01;
import com.fursys.mobilecm.vo.erp.apm0020_m01.APM0020_P02;
import com.fursys.mobilecm.vo.erp.apm0020_m01.ds_allset;
import com.fursys.mobilecm.vo.erp.apm0020_m01.ds_btrip;
import com.fursys.mobilecm.vo.erp.apm0020_m01.ds_complmfg;
import com.fursys.mobilecm.vo.erp.apm0020_m01.ds_list1;
import com.fursys.mobilecm.vo.erp.apm0020_m01.ds_list2;
import com.fursys.mobilecm.vo.erp.apm0020_m01.ds_planInfo;
import com.fursys.mobilecm.vo.erp.apm0020_m01.ds_planMst;
import com.fursys.mobilecm.vo.erp.apm0020_m01.ds_result;
import com.fursys.mobilecm.vo.erp.apm0020_m01.ds_rptreq;
import com.fursys.mobilecm.vo.erp.apm0020_m01.ds_tabList1;
import com.fursys.mobilecm.vo.erp.apm0020_m01.ds_uptplandtl;
import com.fursys.mobilecm.vo.erp.crs0010_m01.ds_tcPlanmstList;

@Mapper
public interface ErpPraAcctListSaveMapper {
	
	public int modifyRptEnddt_U(ds_result params);
	public int modifyTcResDtl_I(ds_result params);
	public int modifyTaPlanMst_U(ds_complmfg params);
	public int modifyAllsetPlanDtl_U(HashMap<String,Object> params);
	public int modifyAllsetPlanMst_U(ds_allset params);
	public int modifyTcResMst_U(ds_result params);
	public DataResult executeFaAseqrem(HashMap<String,Object> params);
	public DataResult selectResMstCnt(HashMap<String,Object> params);
	
	public ds_planMst selectRePlanMstInfo(HashMap<String,Object> params);
	public DataResult selectRemCasyn(HashMap<String,Object> params);
	public String selectRemCasyn_(HashMap<String,Object> params);
	
	public ArrayList<ds_list2> selectNotFinishList(HashMap<String,Object> params);
	public DataResult selectNewSeq(HashMap<String,Object> params);
	public int modifyTaPlanDtl_D(HashMap<String,Object> params);
	public int modifyPlmPno_U(HashMap<String,Object> params);
	public int selectInsertPlanDtl_I(HashMap<String,Object> params);
	public int selectInsertPlanMst_I(HashMap<String,Object> params);
	public int modifySeqnoinf(HashMap<String,Object> params);
	public DataResult selectPlmPno(HashMap<String,Object> params);

	public ArrayList<ds_tabList1> selectResDtlList(HashMap<String,Object> params);
	public ds_list1 selectResMstList(HashMap<String,Object> params);
	public ds_planInfo selectPlanInfo(HashMap<String,Object> params);
	public int modifyComUnpsec_U(ds_uptplandtl params);
	public DataResult selectPlmnoCnt(HashMap<String,Object> params);
	public int modifyAsMigeulCancel_U(HashMap<String,Object> params);
	public int modifyAsFinishTime(HashMap<String,Object> params);
	public DataResult selectMaxRcdt(HashMap<String,Object> params);
	public int modifyAllsetRptReq_U(ds_rptreq params);
	public DataResult selectRptAdsec(HashMap<String,Object> params);
	public DataResult selectStiNm(HashMap<String,Object> params);
	public int modifyPlanDtl_U(ds_tabList1 params);
	public int modifyPlanDetailMobStd(HashMap<String,Object> params);
	public DataResult executePraAcctlistSave(HashMap<String,Object> params);
	public int mergeTaPlanDtl(ds_btrip params);
	public int modifyComPlmfg_U(HashMap<String,Object> params);
	public DataResult selectBtripAmt(HashMap<String,Object> params);
	public DataResult selectFaFinishYn(HashMap<String,Object> params);
	public DataResult selectRowyPlmnoCnt(HashMap<String,Object> params);
	public int modifyRowyComRdsec_U(HashMap<String,Object> params);
	public int modifyAsReqRemark(HashMap<String,Object> params);
	public int asLoadingBeforeMinus(HashMap<String,Object> params);
}
