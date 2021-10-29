package com.fursys.mobilecm.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.fursys.mobilecm.vo.erp.ERPScheduleList;
import com.fursys.mobilecm.vo.erp.ERPSigongSearchDetailInfo;
import com.fursys.mobilecm.vo.erp.ERPSigongSearchInfo;
import com.fursys.mobilecm.vo.erp.ERPSigongStmList;
import com.fursys.mobilecm.vo.erp.ERPSti;
import com.fursys.mobilecm.vo.erp.ERPStiReqTimeSendList;
import com.fursys.mobilecm.vo.erp.ERPStmInfo;
import com.fursys.mobilecm.vo.erp.ERPGubbunInfo;
import com.fursys.mobilecm.vo.erp.ERPNetKmAllowance;
import com.fursys.mobilecm.vo.erp.ERPConstructionMainPage;
import com.fursys.mobilecm.vo.erp.ERPCooperationList;
import com.fursys.mobilecm.vo.erp.ERPReReserveInfo;
import com.fursys.mobilecm.vo.erp.ERPReqCooperationList;
import com.fursys.mobilecm.vo.erp.ERPResultFinishynCheck;
import com.fursys.mobilecm.vo.DataResult;
import com.fursys.mobilecm.vo.erp.ERPAgtInfo;
import com.fursys.mobilecm.vo.erp.ERPAsFileList;
import com.fursys.mobilecm.vo.erp.ERPAsItemPage;
import com.fursys.mobilecm.vo.erp.ERPAsMainPage;
import com.fursys.mobilecm.vo.erp.ERPConstructionItemPage;
import com.fursys.mobilecm.vo.erp.ERPConstructionItemResultPage;
import com.fursys.mobilecm.vo.erp.ERPScheduleCount;

@Mapper
public interface ScheduleMainListMapper {
	
	public ArrayList<ERPSti> selectStiList(HashMap<String,Object> params); 
	public ArrayList<ERPSti> selectKStiList(HashMap<String,Object> params);
	public ArrayList<ERPScheduleList> selectScheduleMainList(HashMap<String,Object> params); 
	public ERPConstructionMainPage selectSigongMainPage(HashMap<String,Object> params); 
	public ArrayList<ERPConstructionItemPage> selectSigongItemPage(HashMap<String,Object> params); 
	public ArrayList<ERPConstructionItemPage> selectSigongGitaItemPage(HashMap<String,Object> params);
	public ERPAsMainPage selectAsMainPage(HashMap<String,Object> params);
	public ArrayList<ERPAsItemPage> selectAsItemPage(HashMap<String,Object> params);
	public ArrayList<ERPAsFileList> selectAsFileList(HashMap<String,Object> params);
	public ArrayList<ERPAsFileList> selectSigongFileList(HashMap<String,Object> params);
	public ArrayList<ERPAsFileList> selectSigongFileListCad(HashMap<String,Object> params);
	public ArrayList<ERPAsFileList> selectSigongFileListIos(HashMap<String,Object> params);
	public ERPScheduleCount selectScheduleCount(HashMap<String,Object> params);
	public ERPResultFinishynCheck selectFinishYnCheckAs(HashMap<String,Object> params); 
	public ERPResultFinishynCheck selectFinishYnCheckSigong(HashMap<String,Object> params);
	public int updateAddAsStartTime(HashMap<String,Object> params);
	public int updateAddSigongStartTime(HashMap<String,Object> params);
	public int updateAddAsEndTime(HashMap<String,Object> params);
	public int updateAddSigongEndTime(HashMap<String,Object> params);
	public ArrayList<ERPReReserveInfo> selectSigongReReserveInfo(HashMap<String,Object> params);
	public ArrayList<ERPReReserveInfo> selectAsReReserveInfo(HashMap<String,Object> params);
	public ERPGubbunInfo selectGubbunYnInfo(HashMap<String,Object> params);
	public ArrayList<ERPConstructionItemResultPage> selectSigongItemResultPage(HashMap<String,Object> params); 
	public int sigongBanpumAgtRequestInsert(HashMap<String,Object> params);
	public DataResult getNowTime();
	public int insertNightTimeJungsan(HashMap<String,Object> params);
	public int updateStiReqTimeSort(HashMap<String,Object> params);
	public int updateStiReqTime(HashMap<String,Object> params);
	public int updateStiReqTimeSend(HashMap<String,Object> params);		
	public ArrayList<ERPStiReqTimeSendList> selectStiReqTimeSendListCheck(HashMap<String,Object> params);
	public ArrayList<ERPStiReqTimeSendList> selectStiReqTimeSendList(HashMap<String,Object> params);
	public DataResult selectKakaotalkMessage(HashMap<String,Object> params);
	public ArrayList<ERPStmInfo> selectStmInfoList(HashMap<String,Object> params);
	public ArrayList<ERPAgtInfo> selectAgtInfoList(HashMap<String,Object> params);
	public ArrayList<ERPSigongSearchInfo> selectSigongSearchInfo(HashMap<String,Object> params);
	public ArrayList<ERPSigongSearchInfo> selectSigongSearchInfoByItem(HashMap<String,Object> params);
	public ArrayList<ERPSigongSearchDetailInfo > selectSigongSearchDetailInfo(HashMap<String,Object> params);
	public ArrayList<ERPSigongSearchDetailInfo > selectAsSearchDetailInfo(HashMap<String,Object> params);
	public ERPNetKmAllowance selectNetKmAllowance(HashMap<String,Object> params);
	public ArrayList<ERPSigongStmList> selectSelectStmList(HashMap<String,Object> params);
	public int updateStmNoResmst(HashMap<String,Object> params);
	public int updateStmNoTcPlanmst(HashMap<String,Object> params);	
	public ArrayList<ERPCooperationList> selectSelectCooperationList(HashMap<String,Object> params);
	public int insertCooperationRequestMaster(HashMap<String,Object> params);
	public int insertCooperationRequestDetail(HashMap<String,Object> params);
	public ArrayList<ERPReqCooperationList> selectReqCooperationList(HashMap<String,Object> params);
	public ArrayList<ERPReqCooperationList> selectReqDetailCooperationList(HashMap<String,Object> params);
	public int updateReqCooperationCancel(HashMap<String,Object> params);
	public ArrayList<ERPReqCooperationList> selectApplyCooperationList(HashMap<String,Object> params);
	public int updateReqCooperationApply(HashMap<String,Object> params);
	public int insertCooperationOriginalJungsan(HashMap<String,Object> params);
	public int insertCooperationOriginalJungsanDetail(HashMap<String,Object> params);
	public int insertCooperationReqJungsan(HashMap<String,Object> params);
	public int updateReqCooperationJungsan(HashMap<String,Object> params);
	public DataResult selectMasterDeleteYn(HashMap<String,Object> params);
	public int updateReqCooperationMasterCancel(HashMap<String,Object> params);
	public DataResult selectCooperationMasterYn(HashMap<String,Object> params);
	public ArrayList<ERPAsFileList> selectSigongFileListCad_new(HashMap<String,Object> params);
	public DataResult selectSigongFileListCadYn(Map params);
	public DataResult getSigongBrand(Map params);
	public DataResult getSigongOrmNm(Map params);
	public DataResult getSigongOrmHdphone(Map params);
	public DataResult getSigongSofaYn(Map params);
	public DataResult getSigongAgtCd(Map params);
	public DataResult searchIosFileYn(Map params);
	
	
	
	
}
