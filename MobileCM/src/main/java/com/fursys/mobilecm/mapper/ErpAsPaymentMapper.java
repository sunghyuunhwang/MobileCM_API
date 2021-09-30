package com.fursys.mobilecm.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.fursys.mobilecm.vo.DataResult;
import com.fursys.mobilecm.vo.erp.ERPPaymentList;
import com.fursys.mobilecm.vo.erp.ERPSearchTeamList;

@Mapper
public interface ErpAsPaymentMapper {
	
	public DataResult selectPaymentOrderNo(HashMap<String,Object> params); 
	public int asPaymentDetailUpdate(HashMap<String,Object> params);
	public ArrayList<ERPPaymentList> selectPaymentList(HashMap<String,Object> params);
	public DataResult selectPaymentCheck(HashMap<String,Object> params);
	public int asPaymentMasterInsert(HashMap<String,Object> params);
	public int asPaymentDetailInsert(HashMap<String,Object> params);
	public int asPaymentMasterUpdate(HashMap<String,Object> params);
	public DataResult selectPaynemtKakaoYn(HashMap<String,Object> params);
}
