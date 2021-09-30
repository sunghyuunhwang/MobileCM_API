package com.fursys.mobilecm.vo.mobile.response;


import com.fursys.mobilecm.vo.BaseResponse;
import com.fursys.mobilecm.vo.erp.ERPSigonResult;

import lombok.Getter;
import lombok.Setter;
public class SigongResultResponse extends BaseResponse{
	@Getter @Setter private ERPSigonResult SigonResult;
}
 