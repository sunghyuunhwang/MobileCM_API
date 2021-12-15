package com.fursys.mobilecm.vo.tmserp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TMSERPStimemberDetailInfo{
	public String stm_no; /*팀원번호*/
	public String sti_cd; /*시공팀코드*/
	public String stm_nm; /*팀원이름*/
	public Integer total_qty; /*총팀원수*/
	public Integer car_qty; /*총 운영 차량 수*/
}


