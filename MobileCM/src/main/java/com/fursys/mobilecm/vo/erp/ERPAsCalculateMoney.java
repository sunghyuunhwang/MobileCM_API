package com.fursys.mobilecm.vo.erp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ERPAsCalculateMoney {
	
	private String base_amt = "0";  /*기본용역비*/
	private String gun_amt = "0";   /*건수정액비*/
	private String etc_amt = "0";   /*기타용역비*/
	private String etc_amt_1 = "0"; /*식대(지)*/
	private String etc_amt_2 = "0"; /*식대(청)*/
	private String etc_amt_3 = "0"; /*출장비*/
	private String etc_amt_4 = "0"; /*난지역*/
	private String etc_amt_5 = "0"; /*as하자*/
	private String etc_amt_6 = "0"; /*년차휴가*/
	private String etc_amt_7 = "0"; /*as조치품*/
	private String etc_amt_8 = "0"; /*기타비용(지)*/
	private String etc_amt_9 = "0"; /*기타비용(청)*/
	private String amt_total = "0"; /*전체합계*/
}


