package com.fursys.mobilecm.utils;

import java.io.FileInputStream;
import java.lang.reflect.Array;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@SuppressWarnings("unchecked")
public class StringUtil {
	private StringUtil() {}
	//public static PropertyService propertiesService;
	
	public static boolean isEmpty(Object obj){
        if( obj instanceof String ) return obj==null || "".equals(obj.toString().trim());
        else if( obj instanceof List ) return obj==null || ((List)obj).isEmpty();
        else if( obj instanceof Map ) return obj==null || ((Map)obj).isEmpty();
        else if( obj instanceof Object[] ) return obj==null || Array.getLength(obj)==0;
        else return obj==null;
    }
     
    public static boolean isNotEmpty(String s){
        return !isEmpty(s);
    }
    
	public static String getToday() {
        Calendar today = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        return sdf.format(today.getTime());
    }
	
	public static Boolean notEmpty(Object obj) {
        return !empty(obj);
    }

	public static Boolean empty(Object obj) {
        if (obj instanceof String) return obj == null || "".equals(obj.toString().trim());
        else if (obj instanceof List) return obj == null || ((List<?>) obj).isEmpty();
        else if (obj instanceof Map) return obj == null || ((Map<?, ?>) obj).isEmpty();
        else if (obj instanceof Object[]) return obj == null || Array.getLength(obj) == 0;
        else return obj == null;
    }
	
	public static String nullString(String val) { 
        String res = "";
 
        if (val == null)
            return "";
        if (val == "null")
            return "";
        try {
            res = val.trim();
        } catch (Exception ex) {
            res = "";
        }
        return res;
    }

	public static List getIbatisIterate(String context, String name, String delim){
		List templist = new ArrayList();
		String[] tempLine = context.split(delim);
		for(int j=0;j<tempLine.length;j++){
			templist.add(tempLine[j]);
		}
		return templist;
	}
	 
    /**
     * @param value
     * @return ' ==> ''
     *
     */
    public static String escape(String value)
    {
        value = replace(value, "'", "''");

        return value;
    }
    
    public static String replace(String str, String pattern, String replace)
    {
        int s = 0;
        int e = 0;
        int patternLength = pattern.length();
        StringBuffer buf = new StringBuffer();

        while ((e = str.indexOf(pattern, s)) >= 0)
        {
            buf.append(str.substring(s, e));
            buf.append(replace);
            s = e + patternLength;
        }

        buf.append(str.substring(s));
        return buf.toString();
    }

    /**
     * Number Comma Replace
     * @param str
     * @return 
     */
    public static String getComma(String str) {
		String sj = str;

		try {
			if (str == null)
				return null;
			if (str.trim().equals(""))
				return "";
			if (str.indexOf(",") != -1)
				return str;
			if (sj.startsWith("0")) {
				for (int i = 0; i < str.length(); i++) {
					sj = str.substring(i);
					if (!sj.startsWith("0"))
						break;
				}
			}
			if (str.equals("0"))
				return "0";
			else {
				double nu = Double.parseDouble(sj);
				NumberFormat nf = NumberFormat.getInstance();
				String no = nf.format(nu);
				return no;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return str;
		}
	}
    
    public static String convertHtmlBr(String comments) {
    	StringBuffer buffer = new StringBuffer();
    	
    	if (comments != null) {
    		
    		//comments.replace(System.getProperty("line.separator"), "<br>");
    		
    		
			int lg = comments.length();
			
			
			for (int i = 0; i < lg; ++i) {
				String temp = comments.substring(i, i + 1);
				
				if ((int)temp.charAt(0) ==10){
					buffer.append("<br>");					
				}
				if (temp.equals(System.getProperty("line.separator"))){
				
					buffer.append("<br>");
				}				
				
				if ("\r".equals(temp)) {
					temp = comments.substring(++i, i + 1);
					
					if ("\n".equals(temp)){
						buffer.append("<br>\r");
					}else{
						buffer.append("\r");
					}
				}
	
				buffer.append(temp);
			}
    	}
		return buffer.toString();
	}
    
	public static String nvl(String value) {
		return (value == null ? "" : value.trim());
	}    

    /**
     * 객체가 null인지 확인하고 null인 경우 "" 로 바꾸는 메서드
     *
     * @param object
     *            원본 객체
     * @return resultVal 문자열
     */
    public static String isNullToString(Object object) {
        String string = "";

        if (object != null) {
            string = object.toString().trim();
        }

        return string;
    }	
    

    /**
     * Object 타입도 null 체크 가능하도록 기능 추가 구현
     *
     * @param str
     *            Object
     * @return
     */
    public static boolean isNull(Object str) {

        return isNull(object2string(str));
    }    
    /**
     * @param str
     * @param defaultValue
     * @return
     */
    public static String null2string(String str, String defaultValue) {

        if (isNull(str)) {
            return defaultValue;
        }

        return str;
    }    
    public static String null2string(Object str, String defaultValue) {

        return null2string(object2string(str), defaultValue);
    }    
    private static String object2string(Object str) {

        String checkString;

        try {
            checkString = str.toString();
        } catch (Exception e) {

            checkString = null;
        }
        return checkString;

    }    
    
    
//    public static String getProperty(String str) {
//
//        String checkString;
//        try {
//        // 프로퍼티 객체 생성
//        Properties props = new Properties();
//        Resource resource = new ClassPathResource("/egovframework/egovProps/globals.properties");        
//        // 프로퍼티 파일 스트림에 담기
//        FileInputStream fis = new FileInputStream(resource.getFile());
//         
//        // 프로퍼티 파일 로딩
//        props.load(new java.io.BufferedInputStream(fis));         
//         
//        // 항목 읽기
//        checkString = props.getProperty(str) ;
//
//        } catch (Exception e) {
//        	e.printStackTrace();
//            checkString = null;
//        }
//        return checkString;
//
//    }
    
	/**
	 * 랜덤수 문자열을 구한다
	 * @return
	 */
	public static String randomNumber(int len) {
	    String number = "";
		
	    for(int i=0; i<len; i++) {
			int intNum = (int)(Math.random() * 10);
			number += Integer.toString(intNum);
		}
		
		return number;
    }
	//자바 이메일 유효성 체크
	public static boolean isEmail(String email) {
        if (email==null) return false;
        boolean b = Pattern.matches("[\\w\\~\\-\\.]+@[\\w\\~\\-]+(\\.[\\w\\~\\-]+)+",email.trim());
        return b;
    }
    
}