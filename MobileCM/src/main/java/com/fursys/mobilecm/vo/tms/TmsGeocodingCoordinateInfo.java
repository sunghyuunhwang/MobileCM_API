package com.fursys.mobilecm.vo.tms;

import java.util.ArrayList;

import com.fursys.mobilecm.vo.BaseResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TmsGeocodingCoordinateInfo{
	
	private String coordType;
	private String addressFlag;
	private int page;
	private int count;
	private int totalCount;
	private ArrayList<TmsGeocodingCoordinate> coordinate;
	
	
}
