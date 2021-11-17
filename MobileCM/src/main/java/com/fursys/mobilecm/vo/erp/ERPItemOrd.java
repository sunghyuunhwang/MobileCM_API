package com.fursys.mobilecm.vo.erp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ERPItemOrd {
    private String set_cd;
    private String itm_cd;
    private String col_cd;
    private Integer pld_fqty;
    private String itm_nm;
    private String michul_yn;
    private Integer return_qty;
    private String item_col;
    private String delay_yn;
    private String inpbit;
}
