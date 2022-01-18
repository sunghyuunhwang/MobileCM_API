package com.fursys.mobilecm.controllers;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fursys.mobilecm.mapper.CRS0010_M01Mapper;
import com.fursys.mobilecm.mapper.ErpSigongAsMapper;
import com.fursys.mobilecm.service.ApiErpService;
import com.fursys.mobilecm.service.ApiErpSigongAsService;
import com.fursys.mobilecm.utils.ComParamLogger;
import com.fursys.mobilecm.vo.BaseResponse;
import com.fursys.mobilecm.vo.DataResult;
import com.fursys.mobilecm.vo.erp.ERPAddAct;
import com.fursys.mobilecm.vo.erp.ERPAddActDetail;
import com.fursys.mobilecm.vo.erp.ERPAddActList;
import com.fursys.mobilecm.vo.erp.ERPAttachFileList;
import com.fursys.mobilecm.vo.erp.ERPBusinessTrip;
import com.fursys.mobilecm.vo.erp.ERPBusinessTripDetail;
import com.fursys.mobilecm.vo.erp.ERPDeliveryItemList;
import com.fursys.mobilecm.vo.erp.ERPPendencyList;
import com.fursys.mobilecm.vo.erp.ERPScheduleCount;
import com.fursys.mobilecm.vo.erp.ERPTrinfList;
import com.fursys.mobilecm.vo.erp.ERPTtComcd;
import com.fursys.mobilecm.vo.mobile.response.AddActResponse;
import com.fursys.mobilecm.vo.mobile.response.AsReportResponse;
import com.fursys.mobilecm.vo.mobile.response.BusinessTripResponse;
import com.fursys.mobilecm.vo.mobile.response.PendencyDetailListResponse;
import com.fursys.mobilecm.vo.mobile.response.SigongReportResponse;
import com.google.gson.Gson;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.Getter;
import lombok.Setter;

@RestController
@RequestMapping("/v1/api/erp_sigongas")
public class ApiErpSigongAsController {
	
	@Autowired ApiErpSigongAsService apiErpSigongAsService;	
	@Autowired ErpSigongAsMapper erpsigongasMapper;	
	@Autowired private PlatformTransactionManager txManager;
	private SqlSession sql;
	
	Gson gson = new Gson();
	boolean	isDeBug = false;	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@ApiOperation(value = "erp_selectConsumerAmt", notes = "소비자/공장도가 조회")
	@GetMapping("/erp_selectConsumerAmt")  
	public String erp_selectConsumerAmt (
			@ApiParam(value = "PLM_NO", required=true, example = "I202201170027")			
			@RequestParam(name="plm_no", required=true) String plm_no
		) { 	
        
		DataResult dataResult = new DataResult();		
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("plm_no", plm_no);
        
        dataResult = erpsigongasMapper.erp_selectConsumerAmt(params);
        
        if (dataResult == null) {
        	
        } else {
        	if (dataResult.getValue1() == -9999) {
        		dataResult.setData1("");	
        	} else {
        		dataResult.setData1(String.format("%d", dataResult.getValue1()));
        	}
        	if (dataResult.getValue2() == -9999) {
        		dataResult.setData2("");	
        	} else {
        		dataResult.setData2(String.format("%d", dataResult.getValue2()));
        	}        	
        }
        
		return gson.toJson(dataResult);
	}
	
	
	@ApiOperation(value = "erp_deleteAddAct", notes = "추가정산요청취소")
	@GetMapping("/erp_deleteAddAct")
	public String erp_deleteAddAct (
			@RequestParam(name="plm_no", required=true) String plm_no,
			@RequestParam(name="com_ssec", required=true) String com_ssec,
			@RequestParam(name="seq", required=true) String seq,			
			@RequestParam(name="user_id", required=true) String user_id
			
		) {
        
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		int res = 0;
		BaseResponse response = new BaseResponse();
		DataResult dataResult = new DataResult();
		String res_msg = "";
		
		try {
			
			HashMap<String,Object> params = new HashMap<String, Object>();
			params.put("plm_no", plm_no);
			params.put("com_ssec", com_ssec);
			params.put("seq", seq);
			params.put("user_id", user_id);
			
			dataResult = erpsigongasMapper.selectAddActStat(params);
			if (dataResult == null) {
				txManager.rollback(status);
        		response.setResultCode("5001");
        		response.setResultMessage("추가정산 상태체크 실패하였습니다.");
        		return gson.toJson(response);
        	} else {
        		if (!"C83001".equals(dataResult.getData1())) {
        			txManager.rollback(status);
            		response.setResultCode("5001");
            		response.setResultMessage("요청중인 건만 취소처리 할 수 있습니다.");
            		return gson.toJson(response);
        		}
        	}
						
			res = erpsigongasMapper.deleteAddAct(params);       	
			if (res < 1) {    				
				res_msg =  "deleteAddAct 오류 [" + res + "]";
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage(res_msg);
				return gson.toJson(response);
			}

		}
		
		catch (Exception e) {
			txManager.rollback(status);
			System.out.println(e.toString());			
			response.setResultCode("5001");
			response.setResultMessage(e.toString());
			return gson.toJson(response);
		}				
		
		txManager.commit(status);
		response.setResultCode("200");
		System.out.println(response.toString());	
		return gson.toJson(response);        

	}
	
	@ApiOperation(value = "erp_selectAddAct", notes = "추가정산 조회")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "추가정산 조회 실패 !!") })
	@GetMapping("/erp_selectAddAct")
	@RequestMapping(value = "/erp_selectAddAct", method = RequestMethod.GET)
	public String erp_selectAddAct(
			@ApiParam(value = "PLM_NO", required=true, example = "P202112311700")
			@RequestParam(name="plm_no", required=true) String plm_no,
			@ApiParam(value = "COM_SSEC", required=true, example = "C18C")
			@RequestParam(name="com_ssec", required=true) String com_ssec,
			@ApiParam(value = "SEQ", required=true, example = "001")
			@RequestParam(name="seq", required=true) String seq,
			@ApiParam(value = "ATTCH_FILE_ID", required=true, example = "ADDACTP202112311700001")
			@RequestParam(name="attch_file_id", required=true) String attch_file_id,
			@ApiParam(value = "ATTCH_DIV_CD", required=true, example = "C")
			@RequestParam(name="attch_div_cd", required=true) String attch_div_cd
			) {

		AddActResponse response = new AddActResponse();		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("plm_no", plm_no);
		params.put("com_ssec", com_ssec);
		params.put("seq", seq);		
		params.put("attch_file_id", attch_file_id);
        params.put("attch_div_cd", attch_div_cd);

		try {						 
			ERPAddAct addAct = erpsigongasMapper.selectAddAct(params);
			if (addAct == null) {
				addAct = new ERPAddAct(); 
			}			
			ArrayList<ERPAddActDetail> list = erpsigongasMapper.selectAddActDetail(params);		
			ArrayList<ERPAttachFileList> file_list = erpsigongasMapper.selectSigongAttachFileList(params);
	       
			response.setAddAct(addAct);
			response.setList(list);
			response.setFile_list(file_list);
			
			System.out.println(gson.toJson(response));
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		return gson.toJson(response);
	}
	
	@ApiOperation(value = "erp_saveAddAct", notes = "추가정산저장")
	@GetMapping("/erp_saveAddAct")
	public String erp_saveAddAct (
			@RequestParam(name="plm_no", required=true) String plm_no,
			@RequestParam(name="com_ssec", required=true) String com_ssec,
			@RequestParam(name="seq", required=true) String seq,
			@RequestParam(name="com_agsec", required=true) String com_agsec,
			@RequestParam(name="com_brand", required=true) String com_brand,
			@RequestParam(name="trs_sec", required=true) String trs_sec,
			@RequestParam(name="remark", required=true) String remark,			
			@RequestParam(name="user_id", required=true) String user_id,

			@RequestParam(name="dseq_arr", required=true) String dseq_arr,
			@RequestParam(name="tri_icd_arr", required=true) String tri_icd_arr,
			@RequestParam(name="qty_arr", required=true) String qty_arr,
			@RequestParam(name="req_amt_arr", required=true) String req_amt_arr,
			@RequestParam(name="req_sum_amt_arr", required=true) String req_sum_amt_arr,
			@RequestParam(name="tri_type_arr", required=true) String tri_type_arr,
			@RequestParam(name="tri_famt_arr", required=true) String tri_famt_arr,
			@RequestParam(name="tri_drate_arr", required=true) String tri_drate_arr,
			@RequestParam(name="rate_subject_arr", required=true) String rate_subject_arr,
			@RequestParam(name="std_amt_arr", required=true) String std_amt_arr
			
		) {
        
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		int res = 0;
		BaseResponse response = new BaseResponse();
		DataResult dataResult = new DataResult();
		String res_msg = "";		
		
		try {
			
			HashMap<String,Object> params = new HashMap<String, Object>();
			params.put("plm_no", plm_no);
			params.put("com_ssec", com_ssec);
			params.put("seq", seq);
			params.put("com_agsec", com_agsec);
			params.put("com_brand", com_brand);
			params.put("trs_sec", trs_sec);
			params.put("remark", remark);
			params.put("user_id", user_id);
			
			if ("".equals(seq)) {	//신규등록
				dataResult = erpsigongasMapper.selectAddActSeq(params);
				if (dataResult == null) {
					txManager.rollback(status);
	        		response.setResultCode("5001");
	        		response.setResultMessage("추가정산 채번에 실패하였습니다.");
	        		return gson.toJson(response);
	        	}
				
				seq = dataResult.getData1();
				System.out.println(String.format("seq=[%s]", seq));
				
				params.put("seq", seq);
				
				res = erpsigongasMapper.insertAddAct(params);       	
				if (res < 1) {    				
					res_msg =  "insertAddAct 오류 [" + res + "]";
					txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage(res_msg);
					return gson.toJson(response);
				}

				
				String dseq = "";
				String tri_icd[] = tri_icd_arr.split(",");
				String qty[] = qty_arr.split(",");
				String req_amt[] = req_amt_arr.split(",");
				String req_sum_amt[] = req_sum_amt_arr.split(",");
				String tri_type[] = tri_type_arr.split(",");
				String tri_famt[] = tri_famt_arr.split(",");
				String tri_drate[] = tri_drate_arr.split(",");
				String rate_subject[] = rate_subject_arr.split(",");
				String std_amt[] = std_amt_arr.split(",");
				
				for (int i = 0; i<tri_icd.length; i++) {
					params.put("tri_icd", tri_icd[i]);
					params.put("qty", qty[i]);
					params.put("req_amt", req_amt[i]);
					params.put("req_sum_amt", req_sum_amt[i]);
					params.put("tri_type", tri_type[i]);
					params.put("tri_famt", tri_famt[i]);
					params.put("tri_drate", tri_drate[i]);
					params.put("rate_subject", rate_subject[i]);
					params.put("std_amt", std_amt[i]);
					
					System.out.println(String.format("req_amt=[%s]", req_amt[i]));
					
					dataResult = erpsigongasMapper.selectAddActDetailSeq(params);
					if (dataResult == null) {
						txManager.rollback(status);
		        		response.setResultCode("5001");
		        		response.setResultMessage("추가정산상세 채번에 실패하였습니다.");
		        		return gson.toJson(response);
		        	}
					dseq = dataResult.getData1();
					System.out.println(String.format("dseq=[%s]", dseq));
					params.put("dseq", dseq);
					
					res = erpsigongasMapper.insertAddActDetail(params);       	
					if (res < 1) {    				
						res_msg =  "insertAddActDetail 오류 [" + res + "]";
						txManager.rollback(status);
						response.setResultCode("5001");
						response.setResultMessage(res_msg);
						return gson.toJson(response);
					}
				}
			} else {
				res = erpsigongasMapper.updateAddAct(params);       	
				if (res < 1) {    				
					res_msg =  "updateAddAct 오류 [" + res + "]";
					txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage(res_msg);
					return gson.toJson(response);
				}

				String dseq[] = dseq_arr.split(",");
				String tri_icd[] = tri_icd_arr.split(",");
				String qty[] = qty_arr.split(",");
				String req_amt[] = req_amt_arr.split(",");
				String req_sum_amt[] = req_sum_amt_arr.split(",");
				String tri_type[] = tri_type_arr.split(",");
				String tri_famt[] = tri_famt_arr.split(",");
				String tri_drate[] = tri_drate_arr.split(",");
				String rate_subject[] = rate_subject_arr.split(",");
				String std_amt[] = std_amt_arr.split(",");

				for (int i = 0; i<tri_icd.length; i++) {
					params.put("dseq", dseq[i]);
					params.put("tri_icd", tri_icd[i]);
					params.put("qty", qty[i]);
					params.put("req_amt", req_amt[i]);
					params.put("req_sum_amt", req_sum_amt[i]);
					params.put("tri_type", tri_type[i]);
					params.put("tri_famt", tri_famt[i]);
					params.put("tri_drate", tri_drate[i]);
					params.put("rate_subject", rate_subject[i]);
					params.put("std_amt", std_amt[i]);
					
					if ("*".equals(dseq[i])) {
						dataResult = erpsigongasMapper.selectAddActDetailSeq(params);
						if (dataResult == null) {
							txManager.rollback(status);
			        		response.setResultCode("5001");
			        		response.setResultMessage("추가정산상세 채번에 실패하였습니다.");
			        		return gson.toJson(response);
			        	}
						dseq[i] = dataResult.getData1();
						System.out.println(String.format("dseq=[%s]", dseq[i]));
						params.put("dseq", dseq[i]);
						
						res = erpsigongasMapper.insertAddActDetail(params);
						res_msg =  "insertAddActDetail 오류 [" + res + "]";
						
					} else {
						if ("".equals(qty[i])) {
							res = erpsigongasMapper.deleteAddActDetail(params); 
							res_msg =  "deleteAddActDetail 오류 [" + res + "]";
						} else {
							res = erpsigongasMapper.updateAddActDetail(params); 
							res_msg =  "updateAddActDetail 오류 [" + res + "]";
						}
					}
					      	
					if (res < 1) {    				
						txManager.rollback(status);
						response.setResultCode("5001");
						response.setResultMessage(res_msg);
						return gson.toJson(response);
					}
				}
				
			}
			
			response.setResultCount(seq);
			
			//System.out.println(String.format("seq=[%s]", seq));

		}	
		catch (Exception e) {
			txManager.rollback(status);
			System.out.println(e.toString());			
			response.setResultCode("5001");
			response.setResultMessage(e.toString());
			return gson.toJson(response);
		}				
		
		txManager.commit(status);
		response.setResultCode("200");
		//System.out.println(response.toString());	
		return gson.toJson(response);        

	}
	
	@ApiOperation(value = "erp_selectActItemList", notes = "추가정산품목조회")
	@GetMapping("/erp_selectActItemList")
	public String erp_selectActItemList (
			@ApiParam(value = "ORM_NO", required=true, example = "I20211225104501")
			@RequestParam(name="orm_no", required=true) String orm_no,
			@ApiParam(value = "COM_AGSEC", required=true, example = "C02I")
			@RequestParam(name="com_agsec", required=true) String com_agsec,
			@ApiParam(value = "COM_BRAND", required=true, example = "T60I01")
			@RequestParam(name="com_brand", required=true) String com_brand,
			@ApiParam(value = "TRS_SEC", required=true, example = "000054")
			@RequestParam(name="trs_sec", required=true) String trs_sec,
			@ApiParam(value = "TRI_INM", required=false, example = "")
			@RequestParam(name="tri_inm", required=false) String tri_inm
		) {
        
		HashMap<String,Object> params = new HashMap<String, Object>();
		params.put("orm_no", orm_no);
		params.put("com_agsec", com_agsec);
		params.put("com_brand", com_brand);
		params.put("trs_sec", trs_sec);
		if (tri_inm == null) tri_inm = "";
		params.put("tri_inm", tri_inm);
        
		ArrayList<ERPTrinfList> allItems = apiErpSigongAsService.erp_selectActItemList(params);
        
		return gson.toJson(allItems);

	}
	
	@ApiOperation(value = "erp_selectTrsecList", notes = "정산분류조회")
	@GetMapping("/erp_selectTrsecList")  
	public String erp_selectTrsecList(
			@ApiParam(value = "COM_AGSEC", required=true, example = "C02P")
			@RequestParam(name="com_agsec", required=true) String com_agsec,
			@ApiParam(value = "COM_BRAND", required=true, example = "T60P02")
			@RequestParam(name="com_brand", required=true) String com_brand
		) { 
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("com_agsec", com_agsec);
        params.put("com_brand", com_brand);
		ArrayList<ERPTtComcd> allItems = erpsigongasMapper.selectTrsecList(params);
		
		return gson.toJson(allItems);
	}
	
	@ApiOperation(value = "erp_addActList", notes = "추가정산조회")
	@GetMapping("/erp_addActList")
	public String erp_addActList (
			@ApiParam(value = "PLM_NO", required=true, example = "P202112311700")
			@RequestParam(name="plm_no", required=true) String plm_no,
			@ApiParam(value = "COM_SSEC", required=true, example = "C18C")
			@RequestParam(name="com_ssec", required=true) String com_ssec
		) {
        
		HashMap<String,Object> params = new HashMap<String, Object>();
		params.put("plm_no", plm_no);
		params.put("com_ssec", com_ssec);
        
		ArrayList<ERPAddActList> allItems = apiErpSigongAsService.erp_addActList(params);
        
		return gson.toJson(allItems);

	}
	
	@ApiOperation(value = "erp_happyCallKakao", notes = "해피콜전송")
	@GetMapping("/erp_happyCallKakao")
	public String erp_happyCallKakao (
			@ApiParam(value = "PLM_NO", required=true, example = "I202112131451")
			@RequestParam(name="plm_no", required=true) String plm_no,
			@ApiParam(value = "RPT_NO", required=true, example = "I202112131451")
			@RequestParam(name="rpt_no", required=true) String rpt_no,
			@ApiParam(value = "RPT_SEQ", required=true, example = "IC1496")
			@RequestParam(name="rpt_seq", required=true) String rpt_seq,
			@ApiParam(value = "REM_DT", required=true, example = "20211213")
			@RequestParam(name="rem_dt", required=true) String rem_dt,
			@ApiParam(value = "REM_SEQ", required=true, example = "IC1496")
			@RequestParam(name="rem_seq", required=true) String rem_seq,
			@ApiParam(value = "COM_SSEC", required=true, example = "C18C")
			@RequestParam(name="com_ssec", required=true) String com_ssec,
			@ApiParam(value = "COM_AGSEC", required=true, example = "C02I")
			@RequestParam(name="com_agsec", required=true) String com_agsec,
			@ApiParam(value = "COM_BRAND", required=true, example = "T60I01")
			@RequestParam(name="com_brand", required=true) String com_brand,
			@ApiParam(value = "CTM_NM", required=true, example = "홍길동")
			@RequestParam(name="ctm_nm", required=true) String ctm_nm,
			@ApiParam(value = "CTM_HP", required=true, example = "01000000000")
			@RequestParam(name="ctm_hp", required=true) String ctm_hp,
			@ApiParam(value = "STI_CD", required=true, example = "YA521")
			@RequestParam(name="sti_cd", required=true) String sti_cd
		) {
        
		HashMap<String,Object> params = new HashMap<String, Object>();
		params.put("plm_no", plm_no);
		params.put("rpt_no", rpt_no);
		params.put("rpt_seq", rpt_seq);
		params.put("rem_dt", rem_dt);
		params.put("rem_seq", rem_seq);
		params.put("com_ssec", com_ssec);
		params.put("com_agsec", com_agsec);
		params.put("com_brand", com_brand);
		params.put("ctm_nm", ctm_nm);
		params.put("ctm_hp", ctm_hp);
		//params.put("ctm_hp", "010-6689-0755");
		params.put("sti_cd", sti_cd);
		
		BaseResponse response = apiErpSigongAsService.erp_happyCallKakao(params);
		        
		//System.out.println(String.format("response=[%s]", gson.toJson(response)));
		
		return gson.toJson(response);
	}
	
	@ApiOperation(value = "erp_selectAsReport", notes = "서비스내역서")
	@GetMapping("/erp_selectAsReport")
	public String erp_selectAsReport (
			@ApiParam(value = "ORM_NO", required=true, example = "F20211102007201")
			@RequestParam(name="orm_no", required=true) String orm_no
		) {
        
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("orm_no", orm_no);
        
        AsReportResponse response = apiErpSigongAsService.erp_selectAsReport(params);
		        
		return gson.toJson(response);
	}
	
	@ApiOperation(value = "erp_selectSigongReport", notes = "시공내역서")
	@GetMapping("/erp_selectSigongReport")
	public String erp_selectSigongReport (
			@ApiParam(value = "PLM_NO", required=true, example = "I202111260009")
			@RequestParam(name="plm_no", required=true) String plm_no
		) { 	
        
		HashMap<String,Object> params = new HashMap<String, Object>();		
        params.put("plm_no", plm_no);
        
        SigongReportResponse response = apiErpSigongAsService.erp_selectSigongReport(params);
		        
		return gson.toJson(response);
	}
	
	@ApiOperation(value = "erp_selectDeliveryItemList", notes = "출고리스트출력")
	@GetMapping("/erp_selectDeliveryItemList")
	public String erp_selectDeliveryItemList (
			@ApiParam(value = "STI_CD", required=true, example = "YA521")
			@RequestParam(name="sti_cd", required=true) String sti_cd,
			@ApiParam(value = "REM_DT", required=true, example = "20211106")
			@RequestParam(name="rem_dt", required=true) String rem_dt
		) { 	
        
		HashMap<String,Object> params = new HashMap<String, Object>();		
        params.put("sti_cd", sti_cd);
        params.put("rem_dt", rem_dt);
        
        ArrayList<ERPDeliveryItemList> allItems = apiErpSigongAsService.erp_selectDeliveryItemList(params);
		        
		return gson.toJson(allItems);
	}
	
	@ApiOperation(value = "erp_UpdateDropSpot", notes = "하차장소 UPDATE")
	@GetMapping("/erp_UpdateDropSpot")
	@RequestMapping(value = "/erp_UpdateDropSpot", method = RequestMethod.GET)
	public String erp_UpdateDropSpot(
			@ApiParam(value = "COM_SCD", required=true, example = "C16YA")
			@RequestParam(name="com_scd", required=true) String com_scd,
			@ApiParam(value = "STI_CD", required=true, example = "YA551")
			@RequestParam(name="sti_cd", required=true) String sti_cd,
			@ApiParam(value = "PLM_NO", required=true, example = "I202110023382")
			@RequestParam(name="plm_no", required=true) String plm_no,
			@ApiParam(value = "DROP_SPOT", required=true, example = "C77999")
			@RequestParam(name="drop_spot", required=true) String drop_spot,
			@ApiParam(value = "DROP_RMK", required=false, example = "창고옆")
			@RequestParam(name="drop_rmk", required=false) String drop_rmk
			) {
		       
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		int res = 0;
		BaseResponse response = new BaseResponse();
		DataResult dataResult = new DataResult();
		
		try {
			String lgs_stat = "";
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("sti_cd", sti_cd);
			params.put("com_scd", com_scd);
			params.put("plm_no", plm_no);
			params.put("drop_spot", drop_spot);
			
			if ("C77999".equals(drop_spot)) {
				params.put("drop_rmk", drop_rmk);
			} else {
				params.put("drop_rmk", "");
			}
			
			dataResult = erpsigongasMapper.selectLgsStat(params);
    		if (dataResult != null) {
    			lgs_stat = dataResult.getData1();
    		}
    		
    		if (!"".equals(lgs_stat)) {
    			txManager.rollback(status);
        		response.setResultCode("5001");
        		response.setResultMessage("입고상태가 등록되어, 하차장소를 변경할 수 없습니다.");
        		return gson.toJson(response);	
    		}
    		
			res = erpsigongasMapper.updateDropSpot(params);        				
        	if (res < 1){
        		txManager.rollback(status);
        		response.setResultCode("5001");
        		response.setResultMessage("하차장소 변경에 실패하였습니다.");
        		return gson.toJson(response);
        	}
        				
		} catch (Exception e) {
			txManager.rollback(status);
			System.out.println(e.toString());			
			response.setResultCode("5001");
			response.setResultMessage(e.toString());
			return gson.toJson(response);
		}
		
		txManager.commit(status);
		response.setResultCode("200");
		System.out.println(response.toString());	
		return gson.toJson(response);
	}
	
	@ApiOperation(value = "erp_selectPendecyDetailList", notes = "미결상세리스트")
	@GetMapping("/erp_selectPendecyDetailList")  
	public String erp_selectPendecyDetailList (
			@ApiParam(value = "PLM_NO", required=true, example = "P202111010894")
			@RequestParam(name="plm_no", required=true) String plm_no,
			@ApiParam(value = "ATTCH_FILE_ID", required=true, example = "proofF202109060125")
			@RequestParam(name="attch_file_id", required=true) String attch_file_id,
			@ApiParam(value = "ATTCH_DIV_CD", required=true, example = "C")
			@RequestParam(name="attch_div_cd", required=true) String attch_div_cd
		) { 	
        
		HashMap<String,Object> params = new HashMap<String, Object>();		
        params.put("plm_no", plm_no);
        params.put("attch_file_id", attch_file_id);
        params.put("attch_div_cd", attch_div_cd);
		        
        PendencyDetailListResponse response = apiErpSigongAsService.erp_selectPendencyDetailList(params);
        
		return gson.toJson(response);
	}
	
	@ApiOperation(value = "erp_selectPendencyList", notes = "미결리스트")
	@GetMapping("/erp_selectPendencyList")  
	public String erp_selectPendencyList (
			@ApiParam(value = "COM_SCD", required=true, example = "C16YA")
			@RequestParam(name="com_scd", required=true) String com_scd,
			@ApiParam(value = "STI_CD", required=true, example = "YA521")
			@RequestParam(name="sti_cd", required=true) String sti_cd,			
			@ApiParam(value = "FR_DATE", required=true, example = "20211101")
			@RequestParam(name="fr_date", required=true) String fr_date,
			@ApiParam(value = "TO_DATE", required=true, example = "20211101")
			@RequestParam(name="to_date", required=true) String to_date
		) { 	
        
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("com_scd", com_scd);
        params.put("sti_cd", sti_cd);
        params.put("fr_date", fr_date);
        params.put("to_date", to_date);        
        
        ArrayList<ERPPendencyList> allItems = apiErpSigongAsService.erp_selectPendencyList(params);
        
		return gson.toJson(allItems);
	}
	
	@ApiOperation(value = "erp_Test", notes = "TEST")
	@GetMapping("/erp_Test")
	@RequestMapping(value = "/erp_Test", method = RequestMethod.GET)
	public String erp_Test(
			@ApiParam(value = "MOVE_KM", required=true, example = "72")
			@RequestParam(name="move_km", required=true) int move_km
			) {
		       
		int res = 0;
		BaseResponse response = new BaseResponse();
		
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("move_km", move_km);
			
			response = apiErpSigongAsService.erp_sigongDelivery(params);
			
        				
		} catch (Exception e) {
			System.out.println(e.toString());			
			response.setResultCode("5001");
			response.setResultMessage(e.toString());
			return gson.toJson(response);
		}
		
		response.setResultCode("200");
		System.out.println(response.toString());	
		return gson.toJson(response);
	}
	
	@ApiOperation(value = "erp_UpdatePhoneID", notes = "Phone ID UPDATE")
	@GetMapping("/erp_UpdatePhoneID")
	@RequestMapping(value = "/erp_UpdatePhoneID", method = RequestMethod.GET)
	public String erp_UpdatePhoneID(
			@ApiParam(value = "COM_SCD", required=true, example = "C16YA")
			@RequestParam(name="com_scd", required=true) String com_scd,
			@ApiParam(value = "STI_CD", required=true, example = "YA551")
			@RequestParam(name="sti_cd", required=true) String sti_cd,
			@ApiParam(value = "PHONE_ID", required=true, example = "ciOBoXqaQ1qPDcVL5CraXk:APA91bFaBEPkasZlm0L9e2d_C6QYYDj6CTXs6XHT3QlPCiMOee47SE-a_rb0VzQAc_OCsuR0rVzQKNJRZ3DYcUHsVVqs7pfor2OQuc0RcKiOsqvVJc8g7cp3AHDfWcaWKSo6Uv9FCy--")
			@RequestParam(name="phone_id", required=true) String phone_id,
			@ApiParam(value = "VERSION_CODE", required=false, example = "73")
			@RequestParam(name="version_code", required=false) String version_code
			) {
		       
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		int res = 0;
		DataResult dataResult = new DataResult();
		BaseResponse response = new BaseResponse();
		
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("sti_cd", sti_cd);
			params.put("com_scd", com_scd);
			params.put("phone_id", phone_id);
			params.put("version_code", version_code);
							
			String db_phone_id = "", db_version_code = "";
			
			dataResult = erpsigongasMapper.selectMobileCmVersion(params);
    		if (dataResult != null) {
    			db_phone_id = dataResult.getData1();
    			db_version_code = dataResult.getData2();
    		}
    		
    		if (!db_phone_id.equals(phone_id)) {
    			//기존 테이블에 PhoneID가 없을수 있으므로, return check안함
    			res = erpsigongasMapper.deleteUsedPhoneID(params);
    			res = erpsigongasMapper.updatePhoneID(params);
            	if (res < 1){
            		txManager.rollback(status);
            		response.setResultCode("5001");
            		response.setResultMessage("PhoneId 변경에 실패하였습니다.");
            		return gson.toJson(response);
            	}
    		}
			
    		if (!db_version_code.equals(version_code)) {
    			res = erpsigongasMapper.updateMobileCmVersion(params);
            	if (res < 1){
            		txManager.rollback(status);
            		response.setResultCode("5001");
            		response.setResultMessage("MobileCmVersion 변경에 실패하였습니다.");
            		return gson.toJson(response);
            	}
    		}
			
		} catch (Exception e) {
			txManager.rollback(status);
			System.out.println(e.toString());			
			response.setResultCode("5001");
			response.setResultMessage(e.toString());
			return gson.toJson(response);
		}
		
		txManager.commit(status);
		response.setResultCode("200");
		System.out.println(response.toString());	
		return gson.toJson(response);
	}
	
	@ApiOperation(value = "erp_NotifyList", notes = "알림리스트")
	@GetMapping("/erp_NotifyList")  
	public String erp_NotifyList (
			@ApiParam(value = "STI_CD", required=true, example = "YA521")
			@RequestParam(name="sti_cd", required=true) String as_sti_cd
		) { 	
        
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("sti_cd", as_sti_cd);
        
        ArrayList<DataResult> allItems = apiErpSigongAsService.erp_NotifyList(params);
        
		return gson.toJson(allItems);
	}
	
	@ApiOperation(value = "erp_insertSigongOverTimeWork", notes = "일룸 저녁시간 자동정산")
	@GetMapping("/erp_insertSigongOverTimeWork")
	@RequestMapping(value = "/erp_insertSigongOverTimeWork", method = RequestMethod.GET)
	public String erp_insertSigongOverTimeWork(
			@ApiParam(value = "PLM_NO", required=true, example = "P202103090529")
			@RequestParam(name="plm_no", required=true) String as_plm_no,
			@ApiParam(value = "STI_CD", required=true, example = "YA551")
			@RequestParam(name="sti_cd", required=true) String as_sti_cd,
			@ApiParam(value = "COM_SCD", required=true, example = "C16YA")
			@RequestParam(name="com_scd", required=true) String as_com_scd,
			@ApiParam(value = "PLM_CDT", required=true, example = "20210309")
			@RequestParam(name="plm_cdt", required=true) String as_plm_cdt,
			@ApiParam(value = "COM_BRAND", required=true, example = "T60P01")
			@RequestParam(name="com_brand", required=true) String as_com_brand,
			@ApiParam(value = "REM_DT", required=true, example = "20210309")
			@RequestParam(name="rem_dt", required=true) String as_rem_dt,
			@ApiParam(value = "REM_SEQ", required=true, example = "PC0827")
			@RequestParam(name="rem_seq", required=true) String as_rem_seq
			) {
		       
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		int res = 0;
		BaseResponse response = new BaseResponse();
		DataResult dataResult = new DataResult();
		String over_time = "", rem_ftm = "";
		
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();		
			params.put("plm_no", as_plm_no);
			params.put("sti_cd", as_sti_cd);

			dataResult = erpsigongasMapper.selectSigongWorkTimeCheck(params);
    		if (dataResult != null) {
    			over_time = dataResult.getData1();
    		} else {
    			txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("시공시작시간 조회에 오류가 발생했습니다.");
				return gson.toJson(response);
    		}
			
			params.put("rem_dt", as_rem_dt);
			params.put("rem_seq", as_rem_seq);

			dataResult = erpsigongasMapper.selectSigongArrivalTimeCheck(params);
    		if (dataResult != null) {
    			rem_ftm = dataResult.getData1();
    		}
    		
    		//시공시간이 18:00 이후이고, 도착안내시간이 18:00 이후인 경우,
			if ("Y".equals(over_time) && !"".equals(rem_ftm)) {				
				res = erpsigongasMapper.insertSigonWorkTimeOver(params);
				if (res < 1) { 
	    			txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage("insertSigonWorkTimeOver 오류 [" + res + "]");
					return gson.toJson(response);
	    		}
				
				params.put("com_scd", as_com_scd);
				params.put("plm_cdt", as_plm_cdt);
				params.put("com_brand", as_com_brand);        			
			
				res = erpsigongasMapper.insertSigonWorkTimeOverAcc(params);
				if (res < 1) { 
	    			txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage("insertSigonWorkTimeOverAcc 오류 [" + res + "]");
					return gson.toJson(response);
	    		}
				
			}
    					
		} catch (Exception e) {
			txManager.rollback(status);
			System.out.println(e.toString());			
			response.setResultCode("5001");
			response.setResultMessage(e.toString());
			return gson.toJson(response);
		}
		
		txManager.commit(status);
		response.setResultCode("200");
		System.out.println(response.toString());	
		return gson.toJson(response);
	}
	
	@ApiOperation(value = "erp_insertSigongWallFix", notes = "일룸 벽고정 자동정산")
	@GetMapping("/erp_insertSigongWallFix")
	@RequestMapping(value = "/erp_insertSigongWallFix", method = RequestMethod.GET)
	public String erp_insertSigongWallFix(
			@ApiParam(value = "PLM_NO", required=true, example = "P202103090529")
			@RequestParam(name="plm_no", required=true) String as_plm_no,
			@ApiParam(value = "STI_CD", required=true, example = "YA551")
			@RequestParam(name="sti_cd", required=true) String as_sti_cd,
			@ApiParam(value = "COM_SCD", required=true, example = "C16YA")
			@RequestParam(name="com_scd", required=true) String as_com_scd,
			@ApiParam(value = "PLM_CDT", required=true, example = "20210309")
			@RequestParam(name="plm_cdt", required=true) String as_plm_cdt,
			@ApiParam(value = "COM_BRAND", required=true, example = "T60P01")
			@RequestParam(name="com_brand", required=true) String as_com_brand
			) {
		       
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		int res = 0;
		BaseResponse response = new BaseResponse();
		
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();		
			params.put("plm_no", as_plm_no);
			params.put("sti_cd", as_sti_cd);

			res = erpsigongasMapper.insertSigongWallFix(params);
			if (res < 1) { 
    			txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("insertWallFix 오류 [" + res + "]");
				return gson.toJson(response);
    		}
			
			params.put("com_scd", as_com_scd);
			params.put("plm_cdt", as_plm_cdt);
			params.put("com_brand", as_com_brand);        			
			
			res = erpsigongasMapper.insertSigongWallFixAcc(params);
			if (res < 1) { 
    			txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("insertWallFixAcc 오류 [" + res + "]");
				return gson.toJson(response);
    		}
			
		} catch (Exception e) {
			txManager.rollback(status);
			System.out.println(e.toString());			
			response.setResultCode("5001");
			response.setResultMessage(e.toString());
			return gson.toJson(response);
		}
		
		txManager.commit(status);
		response.setResultCode("200");
		System.out.println(response.toString());	
		return gson.toJson(response);
	}
		
	@ApiOperation(value = "erp_FcmSendNotify", notes = "FCM Notify")
	@GetMapping("/erp_FcmSendNotify")
	@RequestMapping(value="/erp_FcmSendNotify",method=RequestMethod.POST)
	public String erp_FcmSendNotify (
			@ApiParam(value = "SEND_FROM_SYSTEM", required=true, example = "MOBILECM")
			@RequestParam(name="send_from_system", required=true) String as_send_from_system,
			@ApiParam(value = "SEND_TO_SYSTEM", required=true, example = "MOBILECM")
			@RequestParam(name="send_to_system", required=true) String as_send_to_system,
			@ApiParam(value = "COM_SCD", required=true, example = "YA521")
			@RequestParam(name="com_scd", required=true) String as_com_scd,
			@ApiParam(value = "TITLE", required=true, example = "This is Title")
			@RequestParam(name="title", required=true) String as_title,
			@ApiParam(value = "MESSAGE", required=true, example = "This is First Message.")
			@RequestParam(name="message", required=true) String as_message,
			@ApiParam(value = "USER_ID", required=true, example = "Ya521")
			@RequestParam(name="user_id", required=true) String as_user_id
		) { 	
        
		BaseResponse response = new BaseResponse();
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("send_from_system", as_send_from_system);
        params.put("send_to_system", as_send_to_system);
        params.put("com_scd", as_com_scd);
        params.put("title", as_title);
        params.put("message", as_message);
        params.put("user_id", as_user_id);
    
		response = apiErpSigongAsService.erp_Fcm_SendNotify(params);

		return gson.toJson(response);
		
	}
	
	@ApiOperation(value = "erp_AttachFileList", notes = "시공,AS첨부파일리스트")
	@GetMapping("/erp_AttachFileList")  
	public String erp_AttachFileList (
			@ApiParam(value = "ATTCH_FILE_ID", required=true, example = "cresultF202109060125")
			@RequestParam(name="attch_file_id", required=true) String as_attch_file_id,
			@ApiParam(value = "ATTCH_DIV_CD", required=true, example = "C")
			@RequestParam(name="attch_div_cd", required=true) String as_attch_div_cd
		) { 	
        
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("attch_file_id", as_attch_file_id);
        params.put("attch_div_cd", as_attch_div_cd);
		
        ArrayList<ERPAttachFileList> allItems = apiErpSigongAsService.erp_AttachFileList(params);
        
		return gson.toJson(allItems);
	}
	
	@ApiOperation(value = "erp_AttachFileDelete", notes = "첨부파일 삭제")
	@GetMapping("/erp_AttachFileDelete")
	@RequestMapping(value = "/erp_AttachFileDelete", method = RequestMethod.GET)
	public String erp_reqCooperationCancel(
			@RequestParam(name = "attch_file_id", required = false) String attch_file_id,	
			@RequestParam(name = "attch_div_cd", required = false) String attch_div_cd,
			@RequestParam(name = "attch_file_snum", required = false) int attch_file_snum
			) {
		       
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		int res = 0;
		BaseResponse response = new BaseResponse();
		
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("attch_file_id", attch_file_id);
			params.put("attch_div_cd", attch_div_cd);
			params.put("attch_file_snum", attch_file_snum);
			
			res = erpsigongasMapper.deleteAttachFile(params);			
			if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("deleteAttachFile 오류 [" + res + "]");
				System.out.println("res=" + res);				
				return gson.toJson(response);
			}
			
		} catch (Exception e) {
			txManager.rollback(status);
			System.out.println(e.toString());			
			response.setResultCode("5001");
			response.setResultMessage(e.toString());
			return gson.toJson(response);
		}
		
		txManager.commit(status);
		response.setResultCode("200");
		System.out.println(response.toString());	
		return gson.toJson(response);
	}
	
}
