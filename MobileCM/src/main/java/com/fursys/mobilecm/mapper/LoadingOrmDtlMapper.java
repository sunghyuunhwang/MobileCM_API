package com.fursys.mobilecm.mapper;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.fursys.mobilecm.vo.erp.ERPItemOrd;
	
@Mapper
public interface LoadingOrmDtlMapper {
	public ArrayList<ERPItemOrd> getLoadingOrmDtlList(HashMap<String,Object> params);
	public ArrayList<ERPItemOrd> getLoadingAsDtlList(HashMap<String,Object> params);
}
