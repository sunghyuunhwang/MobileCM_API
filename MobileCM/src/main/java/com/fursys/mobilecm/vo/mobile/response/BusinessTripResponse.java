package com.fursys.mobilecm.vo.mobile.response;

import java.util.ArrayList;

import com.fursys.mobilecm.vo.BaseResponse;
import com.fursys.mobilecm.vo.erp.ERPBusinessTrip;
import com.fursys.mobilecm.vo.erp.ERPBusinessTripDetail;

import lombok.Getter;
import lombok.Setter;
public class BusinessTripResponse extends BaseResponse{
	@Getter @Setter private ERPBusinessTrip businesstrip;	
	@Getter @Setter private ArrayList<ERPBusinessTripDetail> list;

}
 