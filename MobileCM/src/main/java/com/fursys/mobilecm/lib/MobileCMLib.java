package com.fursys.mobilecm.lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import com.fursys.mobilecm.mapper.MobileCMLibMapper;
import com.fursys.mobilecm.vo.BaseResponse;
import com.fursys.mobilecm.vo.DataResult;

public class MobileCMLib {
	
	/**
     * 시공번호 채번
     */
    public String rePlmnoCreate(MobileCMLibMapper libMapper, HashMap<String, Object> params, HashMap<String, Object> outVar) throws Exception {
    	try {
    		DataResult dataResult = new DataResult();
    		int res = 0;
    		
    		String plm_no = (String) params.get("plm_no");
    		String lSzplmNo = "";
    		dataResult = libMapper.selectPlmPno(params);
    		if (dataResult != null) {
    			lSzplmNo = dataResult.getData1();
    		}

    		params.put("lSzplmNo", lSzplmNo);
    		if ("".equals(lSzplmNo) || lSzplmNo == null){ //재일정반영처리가 안된경우
        		//시공번호 채번
        		int iSeqNo = libMapper.modifySeqnoinf(params);
        		if (iSeqNo < 1) { 
        			return "시공번호 생성 시 오류가 발생하였습니다.";
        		}
        		
        		String strSetdt = (String) params.get("seq_setdt");
        		String strAgsec = (String) params.get("com_agsec");
        		
    			// 시공번호 채번    			
        		lSzplmNo = StringUtils.right(strAgsec, 1) + strSetdt + StringUtils.leftPad(String.valueOf(iSeqNo), 4, "0");
    			
        		params.put("lSzplmNo", lSzplmNo);
    			// AS 예정정보mst 생성
        		iSeqNo = libMapper.selectInsertPlanMst_I(params);
    			if (iSeqNo < 1) { 
        			return "시공번호 생성 시 오류가 발생하였습니다2.";
        		}
    			
    			// AS 예정정보dtl 생성
    			res = libMapper.selectInsertPlanDtl_I(params);
    			if (res < 1) {    				
    				return "selectInsertPlanDtl_I 오류 [" + res + "]";
    			}

    			// 이전 시공번호 update
    			res = libMapper.modifyPlmPno_U(params);
    			if (res < 1) {    				
    				return "modifyPlmPno_U 오류 [" + res + "]";
    			}
    		
    		} else {
    			
    			// 재일정반영처리가 된경우
    			res = libMapper.modifyTaPlanDtl_D(params);
    			if (res < 1) {    				
    				return "modifyTaPlanDtl_D 오류 [" + res + "]";
    			}
    			
    			// AS 예정정보dtl 생성
    			res = libMapper.selectInsertPlanDtl_I(params);
    			if (res < 1) {    				
    				return "selectInsertPlanDtl_I 오류 [" + res + "]";
    			}   			
    		}
    		
    		outVar.put("rePlmNo", lSzplmNo);
    		
    		return "";
    	} catch(Exception e){
            return "rePlmnoCreate Exception[" + e.toString() + "]";            
        }    	
    }
    
    public static String makeTmsMsg(BaseResponse response) {
    	return "[코드:" + response.getResultCode() + "],메세지[" + response.getResultMessage() + "]"; 
    }

    /**
     * 암호화 처리
      * @param param 암호화 시킬 값
      * @return String 암호화값
     */
     @SuppressWarnings("static-access")
    public static String makeEncryptValue(String param){
         try{
             //log.debug("ACommonService.makeEncryptValue :: START");
             //log.debug("ACommonService.makeEncryptValue :: check validation");
             
             String base62enc = Base62.encodeToLong(new Long(param).parseLong(param));

             
             //log.debug("ACommonService.makeEncryptValue > " + base62enc);
             //log.debug("ACommonService.makeEncryptValue :: END");
             return base62enc;
         }catch(Exception e){
             //log.debug("ACommonService.makeEncryptValue :: Failed makeEncryptValue");
             e.printStackTrace();
             return "";
         }
     }
     

     /**
      * 복호화 처리
       * @param param 복호화 시킬 값
       * @return String 복호화 값
      */
      public static String makeDecryptValue(String param){
          try{
              //log.debug("ACommonService.makeDecryptValue :: START");
              //log.debug("ACommonService.makeDecryptValue :: check validation");
              
              
              Long base62dec = Base62.decodeToLong(param);
              
              String base62decString = String.valueOf(base62dec);

              
              //log.debug("ACommonService.makeDecryptValue > " + base62decString);
              //log.debug("ACommonService.makeDecryptValue :: END");
              return base62decString;
          }catch(Exception e){
              //log.debug("ACommonService.makeDecryptValue :: Failed makeDecryptValue");
              e.printStackTrace();
              return "";
          }
      }
      
      public static BaseResponse RestCall(String paramUrl,JSONObject jsonObject){
      	BaseResponse res = new BaseResponse();
      	try {
              URL url = new URL(paramUrl);
             
              HttpURLConnection conn = (HttpURLConnection)url.openConnection();
              conn.setRequestMethod("POST");
             // conn.setRequestProperty("X-Auth-Token", API_KEY);            
              conn.setRequestProperty("X-Data-Type", "application/json");
              conn.setRequestProperty("Content-Type", "application/json");
              conn.setDoOutput(true);

              OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(),"UTF-8");
              osw.write(jsonObject.toString());
              osw.flush();
              osw.close();

              BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
              if (conn.getResponseCode() != 200) {
                  System.out.println("Failed: HTTP error code : " + conn.getResponseCode());
              	//throw new RuntimeException("Failed: HTTP error code : " + conn.getResponseCode());
              	res.setResultCode("5001");
              	res.setResultMessage("Failed: HTTP error code : " + conn.getResponseCode());
              } else {
                  System.out.println("발송 성공");
                  res.setResultCode("200");
              	res.setResultMessage("");
              }
              
              String line = null;
              while((line = br.readLine()) != null){
                  System.out.println(line);
              }            
              br.close();            
              conn.disconnect();
              
          } catch (IOException e) {        	
//              System.out.println("RestCall Fail : " + e.getMessage());
//              res.setResultCode("5001");
//          	res.setResultMessage("RestCall Fail : " + e.getMessage());
          	
              System.out.println("발송 성공");
              res.setResultCode("200");
              res.setResultMessage("");
          	
          	return res;
          }
      	
      	return res;
      }	
}
