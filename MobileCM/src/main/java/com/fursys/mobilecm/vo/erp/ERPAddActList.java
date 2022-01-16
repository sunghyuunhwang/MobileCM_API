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
public class ERPAddActList {
	
	private String seq;
	private String trs_sec;
	private String trs_snm;
	private String amt;
	private String addact_stat;
}


