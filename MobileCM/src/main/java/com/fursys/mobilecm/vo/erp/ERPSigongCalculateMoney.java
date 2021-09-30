package com.fursys.mobilecm.vo.erp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ERPSigongCalculateMoney {	
	private String normal_amt = "0";	/*정상시공*/
	private String req_amt_total = "0"; /*청구금액합계*/
	private String req_amt_1 = "0";		/*매장경유*/
	private String req_amt_2 = "0";		/*타사품목*/
	private String req_amt_3 = "0";		/*분해설치*/
	private String req_amt_4 = "0";		/*샘플회수*/
	private String req_amt_5 = "0";		/*반품회수*/
	private String req_amt_6 = "0";		/*장비용차*/
	private String req_amt_7 = "0";		/*출장비*/	
	private String relative_amt_total = "0";	/*관계사청구금액합계*/ 
	private String relative_amt_1 = "0";		/*관계사(청)*/
	private String relative_amt_2 = "0";		/*제품반입비*/
	private String pay_amt_total = "0";	/*지급금액합계*/
	private String pay_amt_1 = "0";		/*숙식비*/
	private String pay_amt_2 = "0";		/*공수비*/
	private String pay_amt_3 = "0";		/*as지원비*/
	private String pay_amt_4 = "0";		/*원거리*/
	private String pay_amt_5 = "0";		/*장비용차*/
	private String pay_amt_6 = "0";		/*근무지외*/
	private String pay_amt_7 = "0";		/*출장비*/
	private String pay_amt_8 = "0";		/*기타지원*/
	private String pay_amt_9 = "0";		/*폐기물*/
	private String minus_amt_total = "0";/*공제금액합계*/
	private String minus_amt_1 = "0";	/*사공복*/
	private String minus_amt_2 = "0";	/*시공하자*/
	private String minus_amt_3 = "0";	/*용차*/
	private String minus_amt_4 = "0";	/*식대*/
	private String minus_amt_5 = "0";	/*기타*/
	private String share_amt = "0";		/*공동시공분할합계*/
	private String amt_total = "0";		/*전체합계*/	
}


