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
public class TmsRouteOptimization {

	private String reqCoordType = "WGS84GEO";
	private String resCoordType = "WGS84GEO";
	private String startName = "";
	private String startX = "";
	private String startY = "";
	private String startTime = "";
	private String endName = "";
	private String endX = "";
	private String endY = "";
	private String endPoiId = "";
	private String searchOption = "0"; //경로 탐색 옵션 입니다. 0: 교통최적+추천(기본값), 1: 교통최적+무료우선, 2: 교통최적+최소시간, 3: 교통최적+초보, 10: 최단거리+유/무료
	private String carType = "4"; //톨게이트 요금에 대한 차종을 지정합니다, 1: 승용차, 2: 중형승합차, 3: 대형승합차, 4: 대형화물차, 5: 특수화물차
	
	private ArrayList<TmsViaPoints> viaPoints = new ArrayList<TmsViaPoints>();
  
}
