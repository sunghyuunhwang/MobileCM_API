package com.fursys.mobilecm.vo.tmserp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TMSERPFile {
	public String attch_div_cd;
	public String attch_file_id;
	public String attch_file_snum;
	public String virtual_attch_file_name;
	public String real_attch_file_name;
	public String file_kind;
	public String attch_file_path;
	public String attch_file_size;
}


