package com.fursys.mobilecm.vo.tms;

import com.fursys.mobilecm.vo.BaseResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TmsCenter{
	private String centerId;
	private String updateDate;
	private String address;
	private String latitude;
	private String longitude;
	private String seq;
	private String centerName;
}
