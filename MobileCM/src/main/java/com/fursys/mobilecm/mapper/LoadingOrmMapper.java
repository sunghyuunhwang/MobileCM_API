package com.fursys.mobilecm.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.fursys.mobilecm.vo.erp.ERPItemOrd;
	
@Mapper
public interface LoadingOrmMapper {
	public ArrayList<ERPItemOrd> getLoadingOrmList(HashMap<String,Object> params);
	public ArrayList<ERPItemOrd> getLoadingItemList(HashMap<String,Object> params);
}
