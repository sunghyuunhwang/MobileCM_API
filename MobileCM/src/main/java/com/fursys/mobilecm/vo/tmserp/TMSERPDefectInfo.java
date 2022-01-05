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
@ApiModel(value = "하자내역", description = "")
public class TMSERPDefectInfo{
	@ApiModelProperty(value = "접수번호", example="I202109130403")
	public String rpt_no; /*접수번호*/
	@ApiModelProperty(value = "순번", example="01")
	public String rpt_seq; /*순번*/
	@ApiModelProperty(value = "대리점명", example="논현4")
	public String vnd_snm; /*대리점명*/
	@ApiModelProperty(value = "서비스팀코드", example="YA608")
	public String sti_cd; /*서비스팀코드*/
	@ApiModelProperty(value = "서비스팀", example="II08남우준")
	public String sti_nm; /*서비스팀*/
	@ApiModelProperty(value = "ㅍ", example="전자영")
	public String ctm_nm; /*고객명*/
	@ApiModelProperty(value = "서비스 요구내역", example="서비스요구내역ㅇㅇㅇㅇㅇ")
	public String rpt_desc; /*서비스 요구내역*/
	@ApiModelProperty(value = "최종조치일", example="20211005")
	public String rpt_enddt; /*최종조치일*/
	@ApiModelProperty(value = "조치결과 특이사항", example="1.불량(센터하자, 양지센터)\\n")
	public String rpt_astdesc; /*조치결과 특이사항*/
	@ApiModelProperty(value = "접수자", example="한소라")
	public String rpt_usrnm; /*접수자*/
	@ApiModelProperty(value = "의뢰시공팀코드", example="YA045")
	public String rpt_rst_acttm; /*의뢰시공팀코드*/
	@ApiModelProperty(value = "의뢰시공팀", example="(0)시공미결/병행")
	public String rpt_rst_acttm_nm; /*의뢰시공팀*/
	@ApiModelProperty(value = "원시공일", example="20210913")
	public String plm_cdt; /*원시공일*/
	@ApiModelProperty(value = "회수여부", example="Y")
	public String rtnsec; /*회수여부*/
	@ApiModelProperty(value = "접수파일", example="Y")
	public String file_yn; /*접수파일*/
	@ApiModelProperty(value = "시공번호", example="Y")
	public String plm_no; /*시공번호*/
	@ApiModelProperty(value = "접수파일", example="C123456789")
	public String file_id;
}


