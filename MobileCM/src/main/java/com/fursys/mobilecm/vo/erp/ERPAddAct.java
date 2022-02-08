package com.fursys.mobilecm.vo.erp;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ERPAddAct {

	private String plm_no = "";
    private String com_ssec = "";
    private String seq = "";
    private String com_agsec = "";
    private String com_brand = "";
    private String trs_sec = "";
    private String trs_snm = "";
    private String addact_reqdt = "";
    private String addact_stat = "";
    private String reject_reason = "";
    
    private String orm_no = "";
    private String remark = "";
}


