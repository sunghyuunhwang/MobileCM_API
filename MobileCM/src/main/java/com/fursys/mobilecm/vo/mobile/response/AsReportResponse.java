package com.fursys.mobilecm.vo.mobile.response;

import java.util.ArrayList;

import com.fursys.mobilecm.vo.BaseResponse;
import com.fursys.mobilecm.vo.erp.ERPAsItemReport;
import com.fursys.mobilecm.vo.erp.ERPAsReport;

import lombok.Getter;
import lombok.Setter;
public class AsReportResponse extends BaseResponse{
	@Getter @Setter private ERPAsReport as;	
	@Getter @Setter private ArrayList<ERPAsItemReport> list;

}
 