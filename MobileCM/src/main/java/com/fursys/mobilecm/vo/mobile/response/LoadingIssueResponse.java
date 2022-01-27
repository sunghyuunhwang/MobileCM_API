package com.fursys.mobilecm.vo.mobile.response;

import java.util.ArrayList;

import com.fursys.mobilecm.vo.BaseResponse;
import com.fursys.mobilecm.vo.erp.ERPAttachFileList;
import com.fursys.mobilecm.vo.erp.ERPLoadingIssue;

import lombok.Getter;
import lombok.Setter;
public class LoadingIssueResponse extends BaseResponse{
	@Getter @Setter private ERPLoadingIssue loadingIssue;	
	@Getter @Setter private ArrayList<ERPAttachFileList> file_list;

}
 