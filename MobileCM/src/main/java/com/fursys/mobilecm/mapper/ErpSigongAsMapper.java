package com.fursys.mobilecm.mapper;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import com.fursys.mobilecm.vo.DataResult;
import com.fursys.mobilecm.vo.erp.ERPAttachFileList;
import com.fursys.mobilecm.vo.erp.ERPFcmNotify;
import com.fursys.mobilecm.vo.erp.ERPPushMessage;

@Mapper
public interface ErpSigongAsMapper {
	
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
	public int updatePhoneID(HashMap<String,Object> params);
	public int insertNotify(HashMap<String,Object> params);
	public ArrayList<ERPAttachFileList> selectSigongAttachFileList(HashMap<String,Object> params);
	public int deleteAttachFile(HashMap<String,Object> params);
	
}
