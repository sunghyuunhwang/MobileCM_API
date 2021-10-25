package com.fursys.mobilecm.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fursys.mobilecm.vo.erp.ERPPushMessage;

public class FcmMessage {
	public static boolean Send(ERPPushMessage vo)throws Exception{
        String apiKey = "AAAAruMucqk:APA91bEtsHarn7pESbZO5YXLj16KlRI9-ANmuA2ZOqvo7pGpabAiBiWAgBKoMqJuW7cSlXKXVll7dzuHEKBITUczBG99tx9dbjMaTPrrBXupZazzhziMr_q4YL5satevoBSRd7hlrjsW"; //"파이어 베이스 서버 API키";
        URL url = new URL("https://fcm.googleapis.com/fcm/send");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "key=" + apiKey);
        conn.setDoOutput(true);
        
        String input = "{\"data\" : {\"" +  vo.getCommand() +  "\" : \"" + vo.getMessage() + "\"}, \"to\":\"" + vo.getToken() + "\"}";

        OutputStream os = conn.getOutputStream();
        
        // 서버에서 날려서 한글 깨지는 사람은 아래처럼  UTF-8로 인코딩해서 날려주자
        os.write(input.getBytes("UTF-8"));
        os.flush();
        os.close();

        int responseCode = conn.getResponseCode();
        
        if (responseCode == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            //System.out.println(response.toString());
        	try {
        		
				/*
				 * org.json.JSONObject jsonObject = new
				 * org.json.JSONObject(response.toString()); String success =
				 * jsonObject.get("success").toString(); org.json.JSONArray jsonArray =
				 * (org.json.JSONArray) jsonObject.get("results"); for(int i = 0; i <
				 * jsonArray.length(); i++ ){ org.json.JSONObject jo = (org.json.JSONObject)
				 * jsonArray.get(i); if ("0".equals(success)) { vo.error =
				 * jo.get("error").toString(); } else { vo.messageid =
				 * jo.get("message_id").toString(); } System.out.println("success : " +
				 * success); System.out.println("error : " + vo.error);
				 * System.out.println("messageid : " + vo.messageid); }
				 */
        	} catch (Exception e) {
        		System.out.println(e.toString());
        		return false;
    	}
    }
    return true;
}
}
