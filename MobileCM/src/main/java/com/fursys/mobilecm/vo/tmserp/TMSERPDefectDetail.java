package com.fursys.mobilecm.vo.tmserp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TMSERPDefectDetail{
	public String rpt_no; /*접수번호*/
	public String rpt_seq; /*순번*/
	public String itm_cd; /*제품코드*/
	public String bmt_item; /*품목코드*/
	public String col_cd; /*접수번호*/
	public String ast_actqty; /*접수번호*/
	public String file_yn; /*접수번호*/
	public String final_file_id;
	public String ast_rtnyn; /*회수여부*/
}


