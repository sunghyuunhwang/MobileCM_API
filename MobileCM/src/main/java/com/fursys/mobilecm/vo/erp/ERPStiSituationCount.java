package com.fursys.mobilecm.vo.erp;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ERPStiSituationCount {
	
	private int total_cnt = 0;
	private int finish_cnt = 0;
	private int notfinish_cnt = 0;
	
}


