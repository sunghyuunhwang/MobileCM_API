package com.fursys.mobilecm.vo.mobile.response;

import java.util.ArrayList;

import com.fursys.mobilecm.vo.BaseResponse;
import com.fursys.mobilecm.vo.MyPage;
import com.fursys.mobilecm.vo.erp.ERPStiBoardList;

import lombok.Getter;
import lombok.Setter;
public class MyPageResponse extends BaseResponse{
	@Getter @Setter private MyPage mypage;
	@Getter @Setter private ArrayList<ERPStiBoardList> list;
}
 