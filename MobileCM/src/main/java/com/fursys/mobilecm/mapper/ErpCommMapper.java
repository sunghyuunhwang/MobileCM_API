package com.fursys.mobilecm.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.fursys.mobilecm.vo.erp.ERPCenterList;
import com.fursys.mobilecm.vo.erp.ERPSearchTeamList;
import com.fursys.mobilecm.vo.erp.ERPTeamList;
import com.fursys.mobilecm.vo.erp.ERPTtComcd;

@Mapper
public interface ErpCommMapper {
		
	public ArrayList<ERPSearchTeamList> erp_searchTeamList(HashMap<String,Object> params);
	public ArrayList<ERPTeamList> erp_selectTeamList(HashMap<String,Object> params);
	public ArrayList<ERPTeamList> erp_selectTeamListAll(HashMap<String,Object> params);
	public ArrayList<ERPCenterList> erp_selectCenterList(HashMap<String,Object> params);
	public ArrayList<ERPTtComcd> erp_selectTtComcd(HashMap<String,Object> params);  
  
}
