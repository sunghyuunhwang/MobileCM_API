package com.fursys.mobilecm.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.fursys.mobilecm.vo.DataResult;

@Mapper
public interface ErpAsAddInfoInsertMapper {
	
	public int insertSiGongAddSignFileInfo(HashMap<String,Object> params);
	public int insertSiGongAddFileInfo(HashMap<String,Object> params);
	
	public int insertAsAddSignFileInfo(HashMap<String,Object> params);
	
	
	public DataResult searchToday(HashMap<String,Object> params);
	public DataResult searchMaxSeq(HashMap<String,Object> params);
	public int selInsertRptReq(HashMap<String,Object> params);
	public int updateAddAsMobileStatus(HashMap<String,Object> params);
	public int updateAddEndTimeStatus(HashMap<String,Object> params);
	public DataResult sp_get_filekey(HashMap<String,Object> params);
	public DataResult selectAsFileID(HashMap<String,Object> params);
	public int updateAsFileId(HashMap<String,Object> params);
	public int updateAsAddFileId(HashMap<String,Object> params);
	public int insertAsAddFileInfo(HashMap<String,Object> params);
}
