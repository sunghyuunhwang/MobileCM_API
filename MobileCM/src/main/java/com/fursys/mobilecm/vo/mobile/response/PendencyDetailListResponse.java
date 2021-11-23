package com.fursys.mobilecm.vo.mobile.response;

import java.util.ArrayList;

import com.fursys.mobilecm.vo.BaseResponse;
import com.fursys.mobilecm.vo.DataResult;
import com.fursys.mobilecm.vo.erp.ERPAttachFileList;
import com.fursys.mobilecm.vo.erp.ERPBusinessTrip;
import com.fursys.mobilecm.vo.erp.ERPBusinessTripDetail;
import com.fursys.mobilecm.vo.erp.ERPConstructionItemPage;

import lombok.Getter;
import lombok.Setter;
public class PendencyDetailListResponse extends BaseResponse{

	@Getter @Setter private DataResult dataResult;
	@Getter @Setter private ArrayList<ERPAttachFileList> file_list;
	@Getter @Setter private ArrayList<ERPConstructionItemPage> item_list;

}
 