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
@ApiModel(value = "하자내역상세", description = "")
public class TMSERPDefectDetail{
	@ApiModelProperty(value = "접수번호", example="I202109130403")
	public String rpt_no; /*접수번호*/
	@ApiModelProperty(value = "순번", example="01")
	public String rpt_seq; /*순번*/
	@ApiModelProperty(value = "제품코드", example="HYC8024V")
	public String itm_cd; /*제품코드*/
	@ApiModelProperty(value = "품목코드", example="M0XHYC8024V3")
	public String bmt_item; /*품목코드*/
	@ApiModelProperty(value = "품목색상", example="OS")
	public String col_cd; /*품목색상*/
	@ApiModelProperty(value = "조치수량", example="1")
	public String ast_actqty; /*조치수량*/
	@ApiModelProperty(value = "파일여부", example="Y")
	public String file_yn; /*파일여부*/
	@ApiModelProperty(value = "파일id", example="")
	public String final_file_id;
	@ApiModelProperty(value = "회수여부", example="Y")
	public String ast_rtnyn; /*회수여부*/
	@ApiModelProperty(value = "이의제기", example="이의제기내용입니다.")
	public String opinion; /*이의제기*/
}


