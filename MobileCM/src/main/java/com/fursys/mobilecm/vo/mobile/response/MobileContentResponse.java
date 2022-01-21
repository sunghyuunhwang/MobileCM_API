package com.fursys.mobilecm.vo.mobile.response;

import java.util.ArrayList;

import com.fursys.mobilecm.vo.BaseResponse;
import com.fursys.mobilecm.vo.erp.ERPAttachFileList;
import com.fursys.mobilecm.vo.erp.ERPMobileContent;

import lombok.Getter;
import lombok.Setter;
public class MobileContentResponse extends BaseResponse{
	@Getter @Setter private ERPMobileContent mobileContent;	
	@Getter @Setter private ArrayList<ERPAttachFileList> file_list;

}
 