package com.fursys.mobilecm.mapper;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import com.fursys.mobilecm.vo.erp.ERPMigeulAverage;
import com.fursys.mobilecm.vo.erp.ERPMigeulDetailList;
import com.fursys.mobilecm.vo.erp.ERPMigeulList;

@Mapper
public interface MigeulListMapper {
	
	public ArrayList<ERPMigeulList> selectMigeulList(HashMap<String,Object> params); 
	public ArrayList<ERPMigeulDetailList> selectMigeulSigongDetailList(HashMap<String,Object> params); 
	public ArrayList<ERPMigeulDetailList> selectMigeulAsDetailList(HashMap<String,Object> params); 
	public ERPMigeulAverage selectMigeulAverageCount(HashMap<String,Object> params);
}
