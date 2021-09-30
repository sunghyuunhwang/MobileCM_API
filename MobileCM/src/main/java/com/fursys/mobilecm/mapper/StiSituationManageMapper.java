package com.fursys.mobilecm.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.fursys.mobilecm.vo.erp.ERPStiPlanSituationList;
import com.fursys.mobilecm.vo.erp.ERPStiSituationCount;
import com.fursys.mobilecm.vo.erp.ERPStiSituationInfoList;

@Mapper
public interface StiSituationManageMapper {
	
	public ERPStiSituationCount selectSigongAverageCount(HashMap<String,Object> params);
	public ArrayList<ERPStiSituationInfoList> selectStiSituationInfoList(HashMap<String,Object> params);
	public ArrayList<ERPStiPlanSituationList> selectStiPlanSituationInfoList(HashMap<String,Object> params);
}
