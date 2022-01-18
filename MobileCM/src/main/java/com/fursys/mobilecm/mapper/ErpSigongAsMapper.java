package com.fursys.mobilecm.mapper;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import com.fursys.mobilecm.vo.DataResult;
import com.fursys.mobilecm.vo.erp.ERPAddAct;
import com.fursys.mobilecm.vo.erp.ERPAddActDetail;
import com.fursys.mobilecm.vo.erp.ERPAddActList;
import com.fursys.mobilecm.vo.erp.ERPAsItemReport;
import com.fursys.mobilecm.vo.erp.ERPAsReport;
import com.fursys.mobilecm.vo.erp.ERPAttachFileList;
import com.fursys.mobilecm.vo.erp.ERPConstructionItemPage;
import com.fursys.mobilecm.vo.erp.ERPDeliveryItemList;
import com.fursys.mobilecm.vo.erp.ERPFcmNotify;
import com.fursys.mobilecm.vo.erp.ERPHappyCall;
import com.fursys.mobilecm.vo.erp.ERPPendencyList;
import com.fursys.mobilecm.vo.erp.ERPPushMessage;
import com.fursys.mobilecm.vo.erp.ERPSigongItemReport;
import com.fursys.mobilecm.vo.erp.ERPSigongReport;
import com.fursys.mobilecm.vo.erp.ERPTrinfList;
import com.fursys.mobilecm.vo.erp.ERPTtComcd;

@Mapper
public interface ErpSigongAsMapper {
	
	
	
	public DataResult selectAddActStat(HashMap<String,Object> params);
	public DataResult erp_selectConsumerAmt(HashMap<String,Object> params);
	public int deleteAddAct(HashMap<String,Object> params);
	public int deleteAddActDetailAll(HashMap<String,Object> params);
	public int deleteAddActDetail(HashMap<String,Object> params);
	public int updateAddAct(HashMap<String,Object> params);
	public int updateAddActDetail(HashMap<String,Object> params);
	
	public ERPAddAct selectAddAct(HashMap<String,Object> params);
	public ArrayList<ERPAddActDetail> selectAddActDetail(HashMap<String,Object> params);
	
	public int insertAddAct(HashMap<String,Object> params);
	public int insertAddActDetail(HashMap<String,Object> params);
	public int insertAddActDetailMulti(HashMap<String,Object> params);
	public DataResult selectAddActDetailSeq(HashMap<String,Object> params);
	public DataResult selectAddActSeq(HashMap<String,Object> params);
	public ArrayList<ERPTrinfList> selectActItemList(HashMap<String,Object> params);
	public ArrayList<ERPTtComcd> selectTrsecList(HashMap<String,Object> params);
	public ArrayList<ERPAddActList> selectAddActList(HashMap<String,Object> params);
	public int selectNotSendCtm(String params);
	public DataResult selectHappyCallSendCheck(HashMap<String,Object> params);
	public int insertHappyCallAnswer(HashMap<String,Object> params);
	public int insertHappyCallDetail(HashMap<String,Object> params);
	public int insertHappyCallMaster(HashMap<String,Object> params);
	public int updateHappyCallCount(HashMap<String,Object> params);
	
	public ERPHappyCall selectHappyCallMessage(HashMap<String,Object> params);
	public int erp_sigongDelivery(HashMap<String,Object> params);
	public ERPAsReport erp_selectAsReport(HashMap<String,Object> params);
	public ArrayList<ERPAsItemReport> erp_selectAsItemReport(HashMap<String,Object> params);
	public ERPSigongReport erp_selectSigongReport(HashMap<String,Object> params);
	public ArrayList<ERPSigongItemReport> erp_selectSigongItemReport(HashMap<String,Object> params);
	public ArrayList<ERPDeliveryItemList> erp_selectDeliveryItemList(HashMap<String,Object> params);
	public DataResult selectLgsStat(HashMap<String,Object> params);
	public int updateDropSpot(HashMap<String,Object> params);
	public ArrayList<ERPConstructionItemPage> selectPendencyItemList(HashMap<String,Object> params);
	public ArrayList<ERPPendencyList> selectPendencyList(HashMap<String,Object> params);
	public int finishScheduleHistory(HashMap<String,Object> params);
	public int startScheduleHistory(HashMap<String,Object> params);
	public ArrayList<ERPFcmNotify> selectScheduledtFcmNotifyList(HashMap<String,Object> params);
	public ArrayList<DataResult> selectNotifyList(HashMap<String,Object> params);	
	public int insertSigonWorkTimeOverAcc(HashMap<String,Object> params);
	public int insertSigonWorkTimeOver(HashMap<String,Object> params);
	public DataResult selectSigongArrivalTimeCheck(HashMap<String,Object> params);
	public DataResult selectSigongWorkTimeCheck(HashMap<String,Object> params);
	public int insertSigongWallFixAcc(HashMap<String,Object> params);
	public int insertSigongWallFix(HashMap<String,Object> params);
	public ArrayList<ERPPushMessage> selectPhoneID(HashMap<String,Object> params);
	public int deleteUsedPhoneID(HashMap<String,Object> params);
	public DataResult selectNotifyGetDate(HashMap<String, Object> param);
	public DataResult selectMobileCmVersion(HashMap<String, Object> param);
	public int updateMobileCmVersion(HashMap<String,Object> params);
	public int updatePhoneID(HashMap<String,Object> params);
	public int insertNotify(HashMap<String,Object> params);
	public ArrayList<ERPAttachFileList> selectSigongAttachFileList(HashMap<String,Object> params);
	public int deleteAttachFile(HashMap<String,Object> params);
	
}
