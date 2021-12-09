package com.fursys.mobilecm.vo.tmserp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TMSERPDefectInfo{
	public String rpt_no; /*접수번호*/
	public String rpt_seq; /*순번*/
	public String vnd_snm; /*대리점명*/
	public String sti_cd; /*서비스팀코드*/
	public String sti_nm; /*서비스팀*/
	public String ctm_nm; /*고객명*/
	public String rpt_desc; /*서비스 요구내역*/
	public String rpt_enddt; /*최종조치일*/
	public String rpt_astdesc; /*조치결과 특이사항*/
	public String rpt_usrnm; /*접수자*/
	public String rpt_rst_acttm; /*의뢰시공팀코드*/
	public String rpt_rst_acttm_nm; /*의뢰시공팀*/
	public String plm_cdt; /*원시공일*/
	public String rtnsec; /*회수여부*/
	public String file_yn; /*접수파일*/
	public String opinion; /*이의제기*/
}


