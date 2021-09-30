package com.fursys.mobilecm.vo.erp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ERPSigongCalculateMoneyTeam {
	private String sti_nm = "";				/*시공팀명 */
	private String year_amt = "0";			/*년 누적금액*/
	private String prev_month_amt = "0";	/*전월 누적금액*/
	private String month_amt = "0";			/*월 누적금액*/
	private String change_rate_amt = "0";	/*변동률*/
}


