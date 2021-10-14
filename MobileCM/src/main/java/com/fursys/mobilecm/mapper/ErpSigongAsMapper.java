package com.fursys.mobilecm.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.fursys.mobilecm.vo.DataResult;
import com.fursys.mobilecm.vo.erp.ERPAttachFileList;

@Mapper
public interface ErpSigongAsMapper {
	
	public ArrayList<ERPAttachFileList> selectSigongAttachFileList(HashMap<String,Object> params);
	public int deleteAttachFile(HashMap<String,Object> params);
	
}
