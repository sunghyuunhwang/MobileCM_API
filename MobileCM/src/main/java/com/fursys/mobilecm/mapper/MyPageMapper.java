package com.fursys.mobilecm.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.fursys.mobilecm.vo.MyPage;
import com.fursys.mobilecm.vo.erp.ERPItemOrd;
import com.fursys.mobilecm.vo.erp.ERPStiBoard;
import com.fursys.mobilecm.vo.erp.ERPStiBoardFileList;
import com.fursys.mobilecm.vo.erp.ERPStiBoardList;

@Mapper
public interface MyPageMapper {
	
	public ArrayList<ERPStiBoardList> selectStiBoardListByBoardName(HashMap<String,Object> params);
	public ArrayList<ERPStiBoardFileList> selectStiBoardFileList(HashMap<String,Object> params);
	public ERPStiBoard getStiBoardDetail(HashMap<String,Object> params);
	public ArrayList<ERPStiBoardList> selectStiBoardListNext(HashMap<String,Object> params);
	public ArrayList<ERPStiBoardList> selectStiBoardList(HashMap<String,Object> params);
	public ArrayList<ERPStiBoardList> selectStiBoardListRecent(HashMap<String,Object> params);
	public MyPage getMyPage(HashMap<String,Object> params);
	
}
