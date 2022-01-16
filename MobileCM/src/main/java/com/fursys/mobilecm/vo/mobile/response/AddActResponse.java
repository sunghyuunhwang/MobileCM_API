package com.fursys.mobilecm.vo.mobile.response;

import java.util.ArrayList;

import com.fursys.mobilecm.vo.BaseResponse;
import com.fursys.mobilecm.vo.erp.ERPAddAct;
import com.fursys.mobilecm.vo.erp.ERPAddActDetail;
import com.fursys.mobilecm.vo.erp.ERPAttachFileList;

import lombok.Getter;
import lombok.Setter;
public class AddActResponse extends BaseResponse{
	@Getter @Setter private ERPAddAct addAct;	
	@Getter @Setter private ArrayList<ERPAddActDetail> list;
	@Getter @Setter private ArrayList<ERPAttachFileList> file_list;

}
 