package com.fursys.mobilecm.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ORMMapper {
	public List<Map<String, Object>> getORMList(HashMap<String,Object> params);  
}
