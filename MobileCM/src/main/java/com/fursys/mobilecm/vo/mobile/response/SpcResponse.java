package com.fursys.mobilecm.vo.mobile.response;

import java.util.ArrayList;

import com.fursys.mobilecm.vo.BaseResponse;
import com.fursys.mobilecm.vo.tms.TmsCenter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SpcResponse extends BaseResponse{
	private String	refNo = "";
	private String	tranDate = "";
	private String	tranTime = "";
	private String	issueCardName = "";
	private String	cardno = "";
}
 