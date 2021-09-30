package com.fursys.mobilecm.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Coordinate {
	private String matchFlag;
	private String lat;
	private String lon;
	private String latEntr;
	private String lonEntr;
	private String city_do;
	private String gu_gun;
	private String eup_myun;
	private String legalDong;
	private String adminDong;
	private String ri;
	private String bunji;
	private String buildingName;
	private String buildingDong;
	private String newMatchFlag;
	private String newLat;
	private String newLon;
	private String newLatEntr;
	private String newLonEntr;
	private String newRoadName;
	private String newBuildingIndex;
	private String newBuildingName;
	private String newBuildingCateName;
	private String newBuildingDong;
	private String zipcode;
	private String remainder;
	private String fullAddr;
}
