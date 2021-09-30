package com.fursys.mobilecm.vo.tms.reponse;

import java.util.ArrayList;

import com.fursys.mobilecm.vo.BaseResponse;
import com.fursys.mobilecm.vo.tms.TmsGeocodingCoordinateInfo;
import com.fursys.mobilecm.vo.tms.TmsTmapError;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TmsGeocodingCoordinateInfoResponse extends BaseResponse{
	
	private TmsGeocodingCoordinateInfo coordinateInfo;
	private TmsTmapError error;
}

