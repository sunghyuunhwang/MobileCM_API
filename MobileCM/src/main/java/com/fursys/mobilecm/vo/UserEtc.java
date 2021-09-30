package com.fursys.mobilecm.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserEtc {
	private String com_scd;
	private String sti_cd;
	private String k_sti_cd;
	private String tmap_appkey;
	private String usr_id;
}
