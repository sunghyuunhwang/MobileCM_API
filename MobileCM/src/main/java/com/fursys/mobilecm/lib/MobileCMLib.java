package com.fursys.mobilecm.lib;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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

}
