package com.fursys.mobilecm.vo.mobile.response;

import java.util.ArrayList;

import com.fursys.mobilecm.vo.BaseResponse;
import com.fursys.mobilecm.vo.erp.ERPStiBoard;
import com.fursys.mobilecm.vo.erp.ERPStiBoardFileList;

import lombok.Getter;
import lombok.Setter;
public class StiBoardDetailResponse extends BaseResponse{
	@Getter @Setter private ERPStiBoard board;
	@Getter @Setter private ArrayList<ERPStiBoardFileList> filelist;
}
 