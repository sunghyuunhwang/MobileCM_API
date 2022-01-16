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
public class ERPTrinfList {
	private String trs_sec;
	private String tri_icd;
	private String tri_inm;
	private String tri_type;
	private String rate_subject;
	private double tri_drate;
	private int tri_amt;
	private int std_amt;
}


