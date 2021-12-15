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
	public String com_pos; /*직책*/
	public String stm_addr; /*주소*/
	public String stm_hp; /*HP*/
	public String stm_zdt; /*입사일*/
	public String stm_zip; /*우편번호*/
	public String stm_mdt; /*차량번호*/
}


