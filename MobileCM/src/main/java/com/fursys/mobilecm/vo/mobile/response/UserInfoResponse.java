package com.fursys.mobilecm.vo.mobile.response;

import com.fursys.mobilecm.vo.BaseResponse;
import com.fursys.mobilecm.vo.FursysUser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
public class UserInfoResponse extends BaseResponse{
	@Getter @Setter private FursysUser user;
}
