package com.fursys.mobilecm.vo.tmserp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TMSERPStiPerformInfo{
	public String rem_dt;
	public String com_scd;
	public String sti_cd;
	public String sti_nm;
	public Integer total_happycall_cnt;
	public Integer total_happycall_score;
	public Integer happycall_average;
	public Integer total_sigong_cnt;
	public Integer inconsistent_cnt;
	public Integer inconsistent_average;
	public Integer orm_amt;
	public Integer constcst_sum;
	public Integer sigonghaja_cnt;
	public Integer sigonghaja_average;
	public Integer claim_cnt;
	public Integer service_cnt;
	
/**	
	public String sti_cd;
	public String sti_nm;
	public Integer sti_qtycapa; 
	public Integer sti_amtcapa; 
	public Integer sti_currentamt; 
	public Integer sti_currentqty; 
	public Integer current_dueday;
	public Integer max_dueday;	
	public Integer total_daycnt;
	public Integer avg_amt;
	public Integer avg_cnt;
	public Integer avg_dueday;	
	
*/
}


