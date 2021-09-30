package com.fursys.mobilecm.vo.tms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TmsViaPoints {
	private String viaPointId;
	private String viaPointName;
	private String viaDetailAddress;
	private String viaX;
	private String viaY;
	private String viaPoiId;
	private String viaTime;
	private String wishStartTime;
	private String wishEndTime;
	private String com_rfg;
	
}
