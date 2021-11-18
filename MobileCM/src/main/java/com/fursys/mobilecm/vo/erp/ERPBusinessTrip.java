package com.fursys.mobilecm.vo.erp;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ERPBusinessTrip {
	private String move_km = "";
	private String move_amt = "";
	private String toll_fee = "";	
	private String proc_status = "";
	private String total_count = "";
	private String total_move = "";
	private String real_movekm = "";
}


