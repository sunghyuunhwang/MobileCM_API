package com.fursys.mobilecm.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.fursys.mobilecm.vo.DataResult;
import com.fursys.mobilecm.vo.erp.ERPSigongAttachFileList;

@Mapper
public interface ErpSigongAsMapper {
	
	public ArrayList<ERPSigongAttachFileList> selectSigongAttachFileList(HashMap<String,Object> params);
	
}
