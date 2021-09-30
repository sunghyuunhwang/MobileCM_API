package com.fursys.mobilecm.vo.tms;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TmsZone extends TmsZoneBase {
	private String zipcodeData;
	private String updateDate;
	private String seq;
	private String adminData;
}