package com.fursys.mobilecm.vo.tmserp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TMSERPStimemberInfo{
	public String com_scd; /*접수번호*/
	public String sti_cd; /*순번*/
	public String sti_nm; /*대리점명*/
	public Integer total_qty; /*총팀원수*/
	public Integer car_qty; /*총 운영 차량 수*/
}


