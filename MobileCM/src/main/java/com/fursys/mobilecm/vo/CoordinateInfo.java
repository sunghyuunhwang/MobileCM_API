package com.fursys.mobilecm.vo;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CoordinateInfo{
	private String coordType;
	private String addressFlag;
	private int page;
	private int count;
	private int totalCount;
	private ArrayList<Coordinate> coordinate;
}
