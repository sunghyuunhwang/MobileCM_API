package com.fursys.mobilecm.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface STIMapper {	
	public List<Map<String, Object>> getSTIList();  
	public List<Map<String, Object>> getZoneList();  
}
