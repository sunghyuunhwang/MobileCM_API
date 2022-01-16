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
public class ERPAddActDetail {
    
    private String plm_no;
    private String com_ssec;
    private String seq;
    private String dseq;
    private String tri_icd;
    private String tri_inm;
    private String qty;
    private String req_amt;
    private String req_sum_amt;
    private String tri_type;
    private String tri_famt;
    private String tri_drate;
    private String rate_subject;
    private String std_amt;
}


