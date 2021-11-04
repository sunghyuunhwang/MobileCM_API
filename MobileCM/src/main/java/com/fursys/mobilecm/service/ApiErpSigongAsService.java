package com.fursys.mobilecm.service;

import java.util.ArrayList;
import java.util.HashMap;

import com.fursys.mobilecm.vo.BaseResponse;
import com.fursys.mobilecm.vo.DataResult;
import com.fursys.mobilecm.vo.erp.ERPAttachFileList;

public interface ApiErpSigongAsService {

	public BaseResponse erp_selectScheduledtFcmNotifyList(HashMap<String, Object> param);
	public ArrayList<DataResult> erp_NotifyList(HashMap<String, Object> param);	
	public BaseResponse erp_Fcm_SendNotify(HashMap<String, Object> param);	
	public ArrayList<ERPAttachFileList> erp_AttachFileList(HashMap<String, Object> param);
	
}
