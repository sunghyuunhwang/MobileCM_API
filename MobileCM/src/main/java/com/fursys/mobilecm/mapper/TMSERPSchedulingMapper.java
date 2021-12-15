package com.fursys.mobilecm.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.fursys.mobilecm.vo.tmserp.TMSERPScheduleCount;
import com.fursys.mobilecm.vo.tmserp.TMSERPSigongAsItemList;
import com.fursys.mobilecm.vo.tmserp.TMSERPSigongAsList;
import com.fursys.mobilecm.vo.tmserp.TMSERPStimemberDetailInfo;
import com.fursys.mobilecm.vo.tmserp.TMSERPStimemberInfo;
import com.fursys.mobilecm.vo.tmserp.TMSERPTeamMigyeolRepo;
import com.fursys.mobilecm.vo.tmserp.TMSERPVehicleList;
import com.fursys.mobilecm.vo.tmserp.TMSERPVndBanpum;
import com.fursys.mobilecm.vo.DataResult;
import com.fursys.mobilecm.vo.tms.TmsViaPoints;
import com.fursys.mobilecm.vo.tmserp.TMSERPCenterList;
import com.fursys.mobilecm.vo.tmserp.TMSERPDefectDetail;
import com.fursys.mobilecm.vo.tmserp.TMSERPDefectInfo;
import com.fursys.mobilecm.vo.tmserp.TMSERPFile;
import com.fursys.mobilecm.vo.tmserp.TMSERPKstiList;
import com.fursys.mobilecm.vo.tmserp.TMSERPKsticdAllList;
import com.fursys.mobilecm.vo.tmserp.TMSERPMigyeolDetailInfo;
import com.fursys.mobilecm.vo.tmserp.TMSERPMigyeolInfo;
import com.fursys.mobilecm.vo.tmserp.TMSERPOrderList;
import com.fursys.mobilecm.vo.tmserp.TMSERPResdtl;
import com.fursys.mobilecm.vo.tmserp.TMSERPResmst;

@Mapper
public interface TMSERPSchedulingMapper {
	
	public DataResult getKstiCdYn(HashMap<String,Object> params);
	public DataResult getComRfg(HashMap<String,Object> params);
	public int updateRouteOptimization(HashMap<String,Object> params);
	public DataResult getCenterAddr(HashMap<String,Object> params);
	public ArrayList<TmsViaPoints> selectRouteOptimizationList(HashMap<String,Object> params);
	public ArrayList<TMSERPOrderList> selectOrderListGeocoding(HashMap<String,Object> params);
	public int insertAllocationResult(HashMap<String,Object> params);
	public int insertAllocationResultDetail(HashMap<String,Object> params);
	public int deleteAllocationResult(HashMap<String,Object> params);
	public int deleteAllocationResultDetail(HashMap<String,Object> params);	
	public int updateOrderGeocoding(HashMap<String,Object> params);
	public DataResult selectComCtsec(HashMap<String,Object> params);
	public int modyfyTaRptReqTrsY_U(Map map);
	public int modyfyTaPlanmstComPlmfgC102_2_U(Map map);
	public int modyfyTaPlandtlComRdsecC13W_U(Map map);
	public int modyfyTaPlanmstComPlmfgC102_U(Map map);
	public int modyfyTcPlandtlComRdsecC13W_U(Map map);
	public int modyfyTcPlanmstComPlmfgC102_U(Map map);
	public int modyfyTrSchedule_U(Map map);
	public int modyfyTcToOrddetC090_U(Map map);
	public int modyfyTaRptreqComSprog_U(Map map);
	public List <Map> retrieveTaAstitemCount(Map map);
	public int modyfyTcResdtlComRfgC142_U(Map map);
	public int modyfyTcResmstComRfgC142_U(Map map);
	public List <Map> retrievesTcPlanmstGetPlmRcsec(Map map);
	public List <Map> packagebunbaeproc(Map map);
	public List <Map> retrieveBeabunDataList(Map map);
	public List <Map> select_1(HashMap<String,Object> params);
	public ArrayList<TMSERPOrderList> selectOrderList(HashMap<String,Object> params);
	public ArrayList<TMSERPVehicleList> selectVehicleListTeam(HashMap<String,Object> params);
	public ArrayList<TMSERPVehicleList> selectVehicleList(HashMap<String,Object> params);
	public ArrayList<TMSERPCenterList> selectCenterList(HashMap<String,Object> params);
	public ArrayList<TMSERPCenterList> selectCenterList_as(HashMap<String,Object> params);
	
	public TMSERPScheduleCount selectSigongAverageCount(HashMap<String,Object> params);
	public ArrayList<TMSERPKstiList> selectKstiScheduleList(HashMap<String,Object> params);
	public ArrayList<TMSERPSigongAsList> selectSigongAsList(HashMap<String,Object> params);
	public ArrayList<TMSERPSigongAsItemList> selectSigongItemList(HashMap<String,Object> params);
	public ArrayList<TMSERPSigongAsItemList> selectAsItemList(HashMap<String,Object> params);
	public ArrayList<TMSERPKsticdAllList> selectKsticdAllList(HashMap<String,Object> params);
	public int updateSticdResmst(HashMap<String,Object> params);
	public int updateSticdTcPlanmst(HashMap<String,Object> params);
	public int updateSticdTrSchedule(HashMap<String,Object> params);
	public DataResult getStmHpNo(HashMap<String,Object> params);
	public int updateSticdTaRptReq(HashMap<String,Object> params);
	public int updateSticdTaPlanmst(HashMap<String,Object> params);
	public int updateSticdTaOtpMinf(HashMap<String,Object> params);
	
	//2021-10 모바일 CM 확장
	public ArrayList<TMSERPKstiList> selectKstiTeamSelectList(HashMap<String,Object> params);
	public ArrayList<TMSERPResmst> selectResmstList(HashMap<String,Object> params);
	public ArrayList<TMSERPResdtl> selectSigongResdtlList(HashMap<String,Object> params);
	public ArrayList<TMSERPResdtl> selectAsResdtlList(HashMap<String,Object> params);
	public ArrayList<TMSERPVndBanpum> selectVndBanpumList(HashMap<String,Object> params);
	public ArrayList<TMSERPFile> selectFileList(HashMap<String,Object> params);
	public TMSERPFile searchFileInfo(HashMap<String,Object> params);
	public ArrayList<TMSERPTeamMigyeolRepo> selectMigyeolReportInfo(HashMap<String,Object> params);
	public ArrayList<TMSERPMigyeolInfo> selectMigyeolInfo(HashMap<String,Object> params);
	public ArrayList<TMSERPMigyeolDetailInfo> selectMigyeolDetailInfo(HashMap<String,Object> params);
	public ArrayList<TMSERPDefectInfo> selectDefectInfoList(HashMap<String,Object> params);
	public ArrayList<TMSERPDefectDetail> selectDefectDetail(HashMap<String,Object> params);
	public ArrayList<TMSERPStimemberInfo> selectStimemberInfo(HashMap<String,Object> params);
	public ArrayList<TMSERPStimemberDetailInfo> selectStimemberDetailInfo(HashMap<String,Object> params);
	
}
