package com.fursys.mobilecm.utils;

import java.util.HashMap;
import java.util.Map;

import com.fursys.mobilecm.vo.BaseResponse;
import com.fursys.mobilecm.vo.mobile.response.SpcResponse;
import com.fursys.mobilecm.vo.tms.reponse.TmsRouteOptimizationResponse;
import com.google.gson.Gson;
import com.mainpay.sdk.net.HttpSendTemplate;
import com.mainpay.sdk.utils.ParseUtils;

public class SpcAPI {
	
	public class KeyInResultData {
	    public String mbrNo = "";				//	가맹점번호
	    public String mbrRefNo = "";			//	가맹점주문번호
	    public String refNo = "";				//	거래번호
	    public String tranDate = "";			//	거래일자 (YYMMDD)
	    public String tranTime = "";			//	거래시간 (HHMMSS)
	    public String goodsName = "";			//	상품명
	    public String amount = "";				//	결제금액
	    public String taxAmount = "";			//	부가세 (Default : 0)
	    public String feeAmount = "";			//	봉사료 (Default : 0)
	    public String installment = "";			//	할부개월 (0 ~ 24)
	    public String customerName = "";		//	구매자명
	    public String customerTelNo = "";		//	구매자연락처
	    public String applNo = "";				//	카드사 승인번호
	    public String cardNo = "";				//	카드번호.
	    public String issueCompanyNo = "";		//	카드발급사코드 (공통코드표 참고)
	    public String issueCompanyName = "";	//	발급사명
	    public String issueCardName = "";		//	발급사 카드명
	    public String acqCompanyNo = "";		//	카드매입사코드 (공통코드표 참고)
	    public String acqCompanyName = "";		//	매입사명
	    public String payType = "";				//	결제타입
	}

	public class KeyInResult {
	    public String resultCode = "";			//	응답코드 '200' 이면 성공, 이외는 실패
	    public String resultMessage = "";		//	응답메시지
	    public KeyInResultData data = new KeyInResultData(); 
	}

	public class KeyInCancelResultData {
	    public String mbrNo = "";				//	가맹점번호
	    public String mbrRefNo = "";			//	가맹점주문번호
	    public String refNo = "";				//	거래번호
	    public String tranDate = "";			//	거래일자 (YYMMDD)
	    public String tranTime = "";			//	거래시간 (HHMMSS)
	    public String goodsName = "";			//	상품명
	    public String amount = "";				//	결제금액
	    public String taxAmount = "";			//	부가세 (Default : 0)
	    public String feeAmount = "";			//	봉사료 (Default : 0)
	    public String installment = "";			//	할부개월 (0 ~ 24)
	    public String customerName = "";		//	구매자명
	    public String customerTelNo = "";		//	구매자연락처
	    public String applNo = "";				//	카드사 승인번호
	    public String cardNo = "";				//	카드번호.
	    public String issueCompanyNo = "";		//	카드발급사코드 (공통코드표 참고)
	    public String issueCompanyName = "";	//	발급사명
	    public String issueCardName = "";		//	발급사 카드명
	    public String acqCompanyNo = "";		//	카드매입사코드 (공통코드표 참고)
	    public String acqCompanyName = "";		//	매입사명
	    public String payType = "";				//	결제타입
	}

	public class KeyInCancelResult {
	    public String resultCode = "";			//	응답코드 '200' 이면 성공, 이외는 실패
	    public String resultMessage = "";		//	응답메시지
	    public KeyInCancelResultData data = new KeyInCancelResultData(); 
	}		
	
//	static String API_BASE = "https://dev-relay.mainpay.co.kr";				// TEST API URL
//	static String mbrNo = "100011";											// 테스트 가맹점번호
//	static String apiKey = "U1FVQVJFLTEwMDAxMTIwMTgwNDA2MDkyNTMyMTA1MjM0";		// 테스트 apiKey
	
	static String API_BASE = "https://relay.mainpay.co.kr";					// REAL API URL
	static String mbrNo = "105361";											// REAL 가맹점번호
	static String apiKey = "U1FVQVJFLTEwNTM2MTIwMjAxMjA4MDk0MjUyNDMwMzc2";	// REAL apiKey	
		
	public static SpcResponse card(String goodsName, String customerName, String cardno, String expd, String installment, String amount, String orderno) {
		SpcResponse res = new SpcResponse();
		String responseJson = "";
		String apiUrl = API_BASE + "/v1/api/payments/payment/card-keyin/trans";				// 신용카드 KEY-IN 승인 요청 API
		Map responseMap = null;
		String resultCode = "";
		String resultMessage = "";

		try {
			/* Request 파라미터 map 생성 */ 
			Map<String, String> parameters = new HashMap<String, String>();
			
			/*=================================================================================================
				요청 파라미터
		      =================================================================================================*/
			parameters.put("mbrNo", mbrNo);											// SPC Networks에서 부여한 가맹점번호 (상점 ID)
			parameters.put("mbrRefNo", orderno);									// 가맹점에서 나름대로 정한 중복되지 않는 주문번호
			//parameters.put("orgRefNo", "1102C1359004");							// 취소대상 승인거래번호
			//parameters.put("orgTranDate", "201102");								// 취소대상 승인거래의 거래일자
			parameters.put("cardNo", cardno);										// 카드번호
			parameters.put("expd", expd);											// 유효기간 (YYMM)
			parameters.put("amount", amount);										// 결제금액(공급가+부가세) (#주의#)페이지에서 전달 받은 값을 그대로 사용할 경우 금액위변조 시도가 가능합니다. DB에서 조회한 값을 사용 바랍니다.
			parameters.put("taxAmt", "0");											// 부가세 (Default : 0)
			parameters.put("feeAmt", "0");											// 봉사료 (Default : 0)
			parameters.put("installment", installment);								// 할부개월
			parameters.put("payType", "Keyin");									// 결제타입 (※ 고정 : Keyin)
			parameters.put("goodsName", goodsName);									// 상품명
			parameters.put("customerName", customerName);							// 구매자성함
			parameters.put("customerTelNo", "");									// 구매자연락처
			parameters.put("clientType", "Online");									// 클라이언트 타입 ※ 고정 (Online 또는 pos)
			parameters.put("keyinAuthType", "K");									// 키인인가구분 (K : 비인증, O : 구인증)
			parameters.put("authType", "0");										// 인증타입 (0 : 생년월일, 1 : 사업자번호) ※ 카드사 특약(구인증) 필요
			//parameters.put("regNo", "");											// 구매자 생년월일 6자리(YYMMDD) 또는 사업자번호 10자리 ※ 카드사 특약(구인증) 필요
			//parameters.put("passwd", "");											// 비밀번호 앞 두자리 ※ 카드사 특약(구인증) 필요
			/* timestamp
			  Java 버전은 생략(라이브러리에서 자동 생성됨)
			*/		
			/* signature
				Java 버전은 생략(라이브러리에서 자동 생성됨)
			*/
			//System.out.println("parameters:" + parameters.toString());
	
			/*=================================================================================================
		       API 호출 
		      =================================================================================================*/
			
			responseJson = HttpSendTemplate.post(apiUrl, parameters, apiKey);
			
			responseMap = ParseUtils.fromJson(responseJson, Map.class);
			resultCode = (String) responseMap.get("resultCode");
			resultMessage = (String) responseMap.get("resultMessage");
	
			if( ! "200".equals(resultCode)) {	// API 호출 실패		
				System.out.println(responseJson);
				res.setResultCode("5001");
				res.setResultMessage("code:" + resultCode + ", message:" + resultMessage);
				return res;
			}
			
			/*=================================================================================================
		       RESPONSE 데이터 추출 및 저장 (상점상황에 따라 DB에 저장해도 무방)
		      =================================================================================================*/
			Map dataMap = (Map)responseMap.get("data");
			res.setRefNo((String) dataMap.get("refNo"));
			System.out.println("123123123123");
			System.out.println((String) dataMap.get("refNo"));
			res.setTranDate((String) dataMap.get("tranDate"));
			res.setTranTime((String) dataMap.get("tranTime"));			
			res.setIssueCardName((String) dataMap.get("issueCardName"));
			if (cardno.length() == 16) {
				res.setCardno(cardno.substring(0,4) + "-****-****-" + cardno.substring(12,16));
			} else if (cardno.length() == 15) {
				res.setCardno(cardno.substring(0,4) + "-****-****-" + cardno.substring(12,15));
			} 
			
		} catch (Exception e) {
			System.out.println(responseJson);
			res.setResultCode("5001");
			res.setResultMessage(e.toString());
			return res;			
		}
		// JSON TYPE RESPONSE
		//response.setContentType("application/json");
		//System.out.println(responseJson);
		res.setResultCode("200");
		res.setResultMessage("");
		
		return res; 

	}
	
	public static SpcResponse cancel(String orgRefNo, String orgTranDate, String amount, String goodsName, String orderno) {
		SpcResponse res = new SpcResponse();
		String responseJson = "";
		String apiUrl = API_BASE + "/v1/api/payments/payment/card-keyin/cancel";				// 신용카드 KEY-IN 취소 요청 API
		Map responseMap = null;
		String resultCode = "";
		String resultMessage = "";

		try {
			/* Request 파라미터 map 생성 */ 
			Map<String, String> parameters = new HashMap<String, String>();
			
			/*=================================================================================================
				요청 파라미터
		      =================================================================================================*/
			parameters.put("mbrNo", mbrNo);											// SPC Networks에서 부여한 가맹점번호 (상점 ID)
			parameters.put("mbrRefNo", orderno);									// 가맹점에서 나름대로 정한 중복되지 않는 주문번호
			parameters.put("orgRefNo", orgRefNo);							// 취소대상 승인거래번호
			parameters.put("orgTranDate", orgTranDate);								// 취소대상 승인거래의 거래일자
			parameters.put("amount", amount);										// 결제금액(공급가+부가세) (#주의#)페이지에서 전달 받은 값을 그대로 사용할 경우 금액위변조 시도가 가능합니다. DB에서 조회한 값을 사용 바랍니다.
			parameters.put("taxAmt", "0");											// 부가세 (Default : 0)
			parameters.put("feeAmt", "0");											// 봉사료 (Default : 0)
			parameters.put("installment", "0");										// 할부개월
			//parameters.put("payType", "Keyin");									// 결제타입 (※ 고정 : Keyin)
			parameters.put("goodsName", goodsName);									// 상품명
			//parameters.put("customerName", customerName);							// 구매자성함
			parameters.put("customerTelNo", "");									// 구매자연락처
			parameters.put("clientType", "Online");									// 클라이언트 타입 ※ 고정 (Online 또는 pos)
			parameters.put("keyinAuthType", "K");									// 키인인가구분 (K : 비인증, O : 구인증)
			parameters.put("authType", "0");										// 인증타입 (0 : 생년월일, 1 : 사업자번호) ※ 카드사 특약(구인증) 필요
			//parameters.put("regNo", "");											// 구매자 생년월일 6자리(YYMMDD) 또는 사업자번호 10자리 ※ 카드사 특약(구인증) 필요
			//parameters.put("passwd", "");											// 비밀번호 앞 두자리 ※ 카드사 특약(구인증) 필요
			/* timestamp
			  Java 버전은 생략(라이브러리에서 자동 생성됨)
			*/		
			/* signature
				Java 버전은 생략(라이브러리에서 자동 생성됨)
			*/
			//System.out.println("parameters:" + parameters.toString());
	
			/*=================================================================================================
		       API 호출 
		      =================================================================================================*/
			
			responseJson = HttpSendTemplate.post(apiUrl, parameters, apiKey);
			
			responseMap = ParseUtils.fromJson(responseJson, Map.class);
			resultCode = (String) responseMap.get("resultCode");
			resultMessage = (String) responseMap.get("resultMessage");
	
			if( ! "200".equals(resultCode)) {	// API 호출 실패		
				System.out.println(responseJson);
				res.setResultCode("5001");
				res.setResultMessage("code:" + resultCode + ", message:" + resultMessage);
				return res;
			}
			
			/*=================================================================================================
		       RESPONSE 데이터 추출 및 저장 (상점상황에 따라 DB에 저장해도 무방)
		      =================================================================================================*/
			Map dataMap = (Map)responseMap.get("data");
			res.setRefNo((String) dataMap.get("refNo"));
			res.setTranDate((String) dataMap.get("tranDate"));
			res.setTranTime((String) dataMap.get("tranTime"));
			res.setIssueCardName((String) dataMap.get("issueCardName"));
			
		} catch (Exception e) {
			System.out.println(responseJson);
			res.setResultCode("5001");
			res.setResultMessage(e.toString());
			return res;			
		}
		// JSON TYPE RESPONSE
		//response.setContentType("application/json");
		//System.out.println(responseJson);
		res.setResultCode("200");
		res.setResultMessage("");
		
		return res; 
		
		
	}
	
}
