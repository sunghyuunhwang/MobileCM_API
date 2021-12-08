package com.fursys.mobilecm.vo.mobile.response;

import java.util.ArrayList;

import com.fursys.mobilecm.vo.BaseResponse;
import com.fursys.mobilecm.vo.erp.ERPBusinessTrip;
import com.fursys.mobilecm.vo.erp.ERPBusinessTripDetail;
import com.fursys.mobilecm.vo.erp.ERPSigongItemReport;
import com.fursys.mobilecm.vo.erp.ERPSigongReport;

import lombok.Getter;
import lombok.Setter;
public class SigongReportResponse extends BaseResponse{
	@Getter @Setter private ERPSigongReport sigong;	
	@Getter @Setter private ArrayList<ERPSigongItemReport> list;

}
 