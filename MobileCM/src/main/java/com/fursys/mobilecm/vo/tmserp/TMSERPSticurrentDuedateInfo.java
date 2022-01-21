package com.fursys.mobilecm.vo.tmserp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TMSERPSticurrentDuedateInfo{
	public String sti_cd; /*순번*/
	public String sti_nm; /*대리점명*/
	public Integer sti_qtycapa; /*건수카파*/
	public Integer sti_amtcapa; /*금액카파*/
	public Integer sum_currentamt; /*당일시공금액*/
	public Integer sum_currentqty; /*당일 시공건수*/
	public Integer current_dueday; /*현재납기*/	
	public Integer max_dueday; /*max납기*/	
	public Integer total_daycnt; /*당일 시공건수*/	
	public Integer avg_amt; /*평균금액*/		
	public Integer avg_cnt; /*평균건수*/		
	public Integer avg_dueday; /*평균납기*/	
}


