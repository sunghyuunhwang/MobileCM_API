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
public class ERPPaymentList {
	private String cust_name = "";
	private String ipg_amt = "";
	private String apt_ymd = "";
	private String card_appyno = "";
	private String cancelable = "";
	private String ipg_rmk = "";
}
