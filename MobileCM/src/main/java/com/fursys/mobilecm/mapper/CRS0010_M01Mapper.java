package com.fursys.mobilecm.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.fursys.mobilecm.vo.DataResult;
import com.fursys.mobilecm.vo.erp.ERPBusinessTrip;
import com.fursys.mobilecm.vo.erp.ERPBusinessTripDetail;
import com.fursys.mobilecm.vo.erp.ERPPlanDtList;
import com.fursys.mobilecm.vo.erp.ERPPushMessage;
import com.fursys.mobilecm.vo.erp.crs0010_m01.ds_tcPlandtlList1;
import com.fursys.mobilecm.vo.erp.crs0010_m01.ds_tcPlandtlList2;
import com.fursys.mobilecm.vo.erp.crs0010_m01.ds_tcPlandtlList3;
import com.fursys.mobilecm.vo.erp.crs0010_m01.ds_tcPlanmstList;

@Mapper
public interface CRS0010_M01Mapper {
	
	
	public int insertWallFixAcc(HashMap<String,Object> params);
	public int insertWallFix(HashMap<String,Object> params);
	public ArrayList<ERPPushMessage> selectPhoneID(HashMap<String,Object> params);
	public int updatePhoneID(HashMap<String,Object> params);
	public int insertNotify(HashMap<String,Object> params);
	public DataResult selectIsEmpty(HashMap<String,Object> params);
	public int updateAsResultRemark(HashMap<String,Object> params);
	public int updateAsResult(HashMap<String,Object> params);
	public DataResult getSignDt(HashMap<String,Object> params);
	public int updateSignDt(HashMap<String,Object> params);
	public ArrayList<DataResult> selectTcSignContent(HashMap<String,Object> params);
	public int insertTcStiReq(Map map);
	public int modyfyTcPlandtlSuspense_U(Map map);
	public int modyfyTcActlist_D(Map map);
	public int TcplanmstAllCalculate(Map map);
	public List <Map> retrievesTcActinf_0000(Map map);
	public List <Map> retrieveTcPlandtlEtcSum(Map map);
	public List <Map> retrieveTcPlandtlPldsecSum(Map map);
	public List <Map> retrievesTcActinf_C096(Map map);
	public List <Map> retrievesC096RelayAmt(Map map);
	public List <Map> retrievesC096List(Map map);
	public List <Map> retrievesC092PassList(Map map);
	public List <Map> retrievesC092RelayAmt(Map map);
	public List <Map> retrievesTcActinf_C092(Map map);
	public int modyfyTcActlistP_I(Map map);
	public List <Map> retrievesC090RelayAmt(Map map);
	public int modyfyTcActlistD_I(Map map);	
	public List <Map> retrievesTcActinf_C090(Map map);
	public List <Map> retrieveC02I_Sum(Map map);
	public List <Map> retrievePldsecCountList(Map map);
	public DataResult retrieveTcPlanmstComPlmfg(HashMap<String,Object> params);
	public DataResult getActCnt(HashMap<String,Object> params);
	public int modyfyTcPlanMstComple(Map params);
	public List<Map> retrievesTcPlandtlCompleChk(Map params);
	public int modyfyTcplanmstAllComplete(Map params);
	public ds_tcPlanmstList retrievesTcPlanmstList(HashMap<String,Object> params);	
	public ArrayList<ds_tcPlandtlList1> retrieveTcPlandtlList(HashMap<String,Object> params);
	public ArrayList<ds_tcPlandtlList1> retrievesTcPlandtlList_1(HashMap<String,Object> params);
	public ArrayList<ds_tcPlandtlList2> retrievesTcPlandtlList_2(HashMap<String,Object> params);
	public ArrayList<ds_tcPlandtlList3> retrievesTcPlandtlList_3(HashMap<String,Object> params);
	
	
}
