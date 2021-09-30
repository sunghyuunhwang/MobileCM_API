package com.fursys.mobilecm.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.fursys.mobilecm.vo.DataResult;

@Mapper
public interface MobileCMLibMapper {
	
	
	public int modifyTaPlanDtl_D(HashMap<String,Object> params);
	public int modifyPlmPno_U(HashMap<String,Object> params);
	public int selectInsertPlanDtl_I(HashMap<String,Object> params);
	public int selectInsertPlanMst_I(HashMap<String,Object> params);
	public int modifySeqnoinf(HashMap<String,Object> params);
	public DataResult selectPlmPno(HashMap<String,Object> params);

}
