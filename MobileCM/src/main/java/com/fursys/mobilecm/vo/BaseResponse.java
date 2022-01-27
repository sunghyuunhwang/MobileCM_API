package com.fursys.mobilecm.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BaseResponse {
	private String resultCode;
	private String resultCount;
	private String resultMessage;
	private String mappingKey;
	
	private String data1;
	private String data2;
	private String data3;
	private String data4;
	private String data5;
}
