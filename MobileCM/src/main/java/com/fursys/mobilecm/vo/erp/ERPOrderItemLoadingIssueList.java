package com.fursys.mobilecm.vo.erp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ERPOrderItemLoadingIssueList {
	
    private String orm_no;
    private String orm_nm;
    private String loadingissue_std;
    private String loadingissue_std_nm;
    private String loadingissue_procstd;
    private String loadingissue_procstd_nm;
    private String rem_dt;
    private String rem_seq;
    private String com_ssec;
    private int seq_no; 
}
