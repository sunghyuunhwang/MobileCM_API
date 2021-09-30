package com.fursys.mobilecm.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.fursys.mobilecm.vo.DataResult;
import com.fursys.mobilecm.vo.erp.ERPAsCalculateMoney;
import com.fursys.mobilecm.vo.erp.ERPBusinessTrip;
import com.fursys.mobilecm.vo.erp.ERPBusinessTripDetail;
import com.fursys.mobilecm.vo.erp.ERPGoGoVanWayPoint;
import com.fursys.mobilecm.vo.erp.ERPPaymentList;
import com.fursys.mobilecm.vo.erp.ERPScheduleCount;
import com.fursys.mobilecm.vo.erp.ERPSigongCalculateMoney;
import com.fursys.mobilecm.vo.erp.ERPSigongCalculateMoneyTeam;

@Mapper
public interface ErpCalculateMoneyMapper {
	
	public int updateLogispot(HashMap<String,Object> params);
	public int updateGoGoVan(HashMap<String,Object> params);
	public ArrayList<ERPGoGoVanWayPoint> selectWayPointList(HashMap<String,Object> params);
	public ERPScheduleCount selectBusinessTripScheduleCount(HashMap<String,Object> params);
	public ArrayList<ERPBusinessTripDetail> selectBusinessTripDetail(HashMap<String,Object> params);
	public ERPBusinessTrip selectBusinessTrip(HashMap<String,Object> params);
	public int insertBusinessTrip(HashMap<String,Object> params);
	public int updateBusinessTrip(HashMap<String,Object> params);

	public ERPAsCalculateMoney selectAsCalculateMoney(HashMap<String,Object> params);
	public ArrayList<ERPSigongCalculateMoneyTeam> selectSigongCalculateMoneyTeam(HashMap<String,Object> params);
	public DataResult selectCalculateMoneyDate(HashMap<String,Object> params);
	public ERPSigongCalculateMoney selectSigongCalculateMoney(HashMap<String,Object> params);

}
