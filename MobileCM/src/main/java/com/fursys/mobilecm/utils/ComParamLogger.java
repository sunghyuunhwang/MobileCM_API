package com.fursys.mobilecm.utils;

import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
import javax.servlet.http.HttpServletRequest;
 
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
 
//import org.apache.commons.collections.map.ListOrderedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
//import egovframework.rte.psl.dataaccess.util.EgovMap;
 
//import org.apache.log4j.Logger;
 
public class ComParamLogger {
    
    // protected static final Log log = LogFactory.getLog(ComParamUtil.class);
    // private static final Logger logger = LoggerFactory.getLogger(ComParamUtil.class); //logger로 사용할 경우, 하위 logger들과 충돌 때문에 사용안함
    // private Logger logger = Logger.getLogger(this.getClass()); //현재 클래스의 depth에서 apache.log4j를 사용할 수 없어서 사용 안함
    private static final Logger comParamLogger = LoggerFactory.getLogger(ComParamLogger.class);
    
    /**
     * request의 모든 값을 확인
     * 
     * @param reqClassName
     * @param nowMethod
     * @param request
     * @return
     * @throws Exception
     */
    public static Map<String, Object> requestGetParameterToMap(String reqClassName, String nowMethod, HttpServletRequest request) throws Exception {
        String msg  = "";
        msg = "#    클래스명:[" + reqClassName + "]    " + "메소드명:[" + nowMethod + "]    requestGetParameterToMap    시작    #";
        comParamLogger.debug(msg);
        System.out.println(msg);
        
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        Enumeration<?> params = request.getParameterNames();
        
        int cnt = 0;
        while (params.hasMoreElements()) {
            String keyName = (String) params.nextElement();
            msg = "#    Parameter" + cnt + " # " + keyName + " : [" + request.getParameter(keyName) + "]";
            comParamLogger.debug(msg);
            System.out.println(msg);
            rtnMap.put(keyName, request.getParameter(StringUtil.nullString(keyName)));
            cnt++;
        }
        msg = "#    클래스명:[" + reqClassName + "]    " + "메소드명:[" + nowMethod + "]    requestGetParameterToMap    종료    #";
        comParamLogger.debug(msg);
        System.out.println(msg);
        
        return rtnMap;
    }
    
    /**
     * VO의 모든 값을 확인
     * 
     * @param reqClassName
     * @param nowMethod
     * @param comParamUtilVO
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static Object paramToVO(String reqClassName, String nowMethod, Object comParamUtilVO) throws IllegalArgumentException, IllegalAccessException {
    	String msg  = "public class " + reqClassName + " {";
    	//msg = "#    클래스명:[" + reqClassName + "]    " + "메소드명:[" + nowMethod + "]    " + "VO명:[" + comParamUtilVO + "]        paramToVO    시작    #";
        //comParamLogger.debug(msg);
        System.out.println(msg);
        Object obj = comParamUtilVO;
        
        int i = 0;
        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            String keyName = field.getName();
            Object keyValue = field.get(obj);
            
            if (keyValue == null || ("").equals(keyValue.toString())) {
                keyValue = "";
            }
            
            msg = "#    paramToVO #    VO명:[" + comParamUtilVO + "]    [" + i + "] keyName:[" + keyName + "] # keyValue:[" + keyValue + "]";
            msg ="private String " + keyName + ";";
            //comParamLogger.debug(msg);
            System.out.println(msg);
            
            i++;
        }
        msg = "#    클래스명:[" + reqClassName + "]    " + "메소드명:[" + nowMethod + "]    " + "VO명:[" + comParamUtilVO + "]        paramToVO    종료    #";
        msg = "}";
        
        //comParamLogger.debug(msg);
        System.out.println(msg);
        
        return comParamUtilVO;
    }
    
//    public static List listToMap(List requestList) throws Exception {
//        comParamLogger.debug("### ListUtil 시작---------------------------------------------------------------");
//        List rtnList = new ArrayList();
//        try {
//            Iterator resultIterator = requestList.iterator();
//            int i = 0;
//            while (resultIterator.hasNext()) {
//                ListOrderedMap oderKeyMap = (ListOrderedMap) resultIterator.next();
//                comParamLogger.debug("### ListUtil 1for문 ###" + "Row[" + i + "]의 모든 값 oderKeyMap ==" + oderKeyMap);
//                // String idRd = String.valueOf(oderKeyMap.get("id"));
//                // int id = Integer.parseInt(idRd);
//                // comParamLogger.debug("### ListUtil 1for문 ### id == " + id);
//                EgovMap colMap = new EgovMap();
//                int j = 0;
//                Set key = oderKeyMap.keySet();
//                for (Iterator iterator = key.iterator(); iterator.hasNext();) {
//                    String keyName = (String) iterator.next();
//                    Object valueName = (Object) oderKeyMap.get(keyName);
//                    comParamLogger.debug("### ListUtil 2for문 ###" + "Row[" + i + "]의 colMap[" + j + "] ###" + keyName + " = " + valueName);
//                    colMap.put(keyName, valueName);
//                    j++;
//                }
//                rtnList.add((EgovMap) colMap);
//                i++;
//            }
//        } catch (Exception e) {
//            comParamLogger.debug("### ListUtil Exception : \n" + e);
//        }
//        comParamLogger.debug("### ListUtil 끝---------------------------------------------------------------");
//        return rtnList;
//    }
}

