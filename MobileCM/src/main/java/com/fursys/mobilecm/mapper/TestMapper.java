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
import com.fursys.mobilecm.vo.erp.ERPTestVo;
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
public interface TestMapper {
	
	public ArrayList<ERPTestVo> test(HashMap<String,Object> params); 

}
