package com.fursys.mobilecm.vo.tmserp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TMSERPAllMigyeolRepo {
	public int com_unpsec_a; /*AS건수*/
	public int com_unpsec_e; /*반품건수*/
	public int com_unpsec_c; /*내부부적합건수*/
	public int com_unpsec_r; /*연기건수*/
	public int tot_cnt; /*총 건수*/
	public int migyeol_cnt; /*미결 건수*/
	public int comp_cnt; /*완결 건수*/
	public int comp_per;
	public int migyeol_per;
	public int com_unpsec_a_per;
    public int com_unpsec_e_per;
    public int com_unpsec_c_per;
    public int com_unpsec_r_per;
}
