package com.fursys.mobilecm.vo.tmserp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ApiModel(value = "시공팀원정보", description = "")
public class TMSERPStimemberDetailInfo{
	@ApiModelProperty(value = "서비스센터", example="C16YA")
	public String com_scd; /*서비스센터*/
	@ApiModelProperty(value = "팀원번호", example="01")
	public String stm_no; /*팀원번호*/
	@ApiModelProperty(value = "시공팀코드", example="YA521")
	public String sti_cd; /*시공팀코드*/
	@ApiModelProperty(value = "팀원이름", example="황성현팀")
	public String stm_nm; /*팀원이름*/
	@ApiModelProperty(value = "직책", example="C112")
	public String com_pos; /*직책*/
	@ApiModelProperty(value = "주소", example="서울서울서울")
	public String stm_addr; /*주소*/
	@ApiModelProperty(value = "HP", example="010-0000-0000")
	public String stm_hp; /*HP*/
	@ApiModelProperty(value = "입사일", example="20211125")
	public String stm_jdt; /*입사일*/
	@ApiModelProperty(value = "우편번호", example="05353")
	public String stm_zip; /*우편번호*/
	@ApiModelProperty(value = "차량번호", example="123가4567")
	public String stm_mdt; /*차량번호*/
}


