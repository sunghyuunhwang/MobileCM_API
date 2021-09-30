package com.fursys.mobilecm.vo.tms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TmsTmapError {
	private String id;
	private String category;
	private String code;
	private String message;
}
