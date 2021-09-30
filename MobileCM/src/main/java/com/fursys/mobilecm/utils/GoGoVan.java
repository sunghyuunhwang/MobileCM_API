package com.fursys.mobilecm.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.fursys.mobilecm.vo.erp.ERPGoGoVan;
import com.fursys.mobilecm.vo.mobile.response.GoGoVanResponse;
import com.google.gson.Gson;

public class GoGoVan {

	public static GoGoVanResponse sumit(ERPGoGoVan gogovan) {
		GoGoVanResponse res = new GoGoVanResponse();
		String responseJson = "";
		Gson gson = new Gson();
		
		String apiUrl = "https://stg-api-business.gogovan.co.kr/api/order/submit";
		apiUrl = "http://business-staging.gogovan.co.kr/api/order/submit";
        boolean a2b = false;
        int branchCode = 3000000;
        String apikey = "C23EF4AC99493790DD9E44F7F328F05114788DF97FCA57707893199ADD7EBA15";
        
        gogovan.setUser_code("300000104");
        String data = gson.toJson(gogovan);
        try {
            Map<String, String> properties = new HashMap<String, String>();
            properties.put("Content-Type", "application/json; charset=UTF-8");
            properties.put("type", "json");
            properties.put("data-type", "json");
            properties.put("a2b", String.valueOf(a2b));
            properties.put("code", String.valueOf(branchCode));
            properties.put("apikey", apikey);
            String result = download(apiUrl, data, "POST", properties, "UTF-8");
            System.out.println(result);
            
            //////////////////////////////
            //
            // result 응답형식에 따라 처리루틴 필요
            //
            /////////////////////////////
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
		res.setResultCode("200");
		res.setResultMessage("");
        		
		return res; 

	}

    public static String download(String textUrl, String parameters, String method, Map<String, String> requestProperties, String encoding) {
        try {
            if (method.equalsIgnoreCase("GET"))
            textUrl += "?" + parameters;
            URL url = new URL(textUrl);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            if (requestProperties != null) {
                for (Map.Entry<String, String> entry : requestProperties.entrySet()) {
                    con.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            if (con instanceof HttpsURLConnection) {
                HttpsURLConnection httpscon = (HttpsURLConnection) con;
                httpscon.setHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
                SSLContext context = SSLContext.getInstance("TLS");
                context.init(null, new TrustManager[] { new X509TrustManager() {
                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
                    public X509Certificate[] getAcceptedIssuers() { return null; }
                }}, null);
                httpscon.setSSLSocketFactory(context.getSocketFactory());
            }
            con.setUseCaches(false);
            con.setRequestMethod(method);
            con.setDoOutput(true);
            con.setDoInput(true);
            if (method.equalsIgnoreCase("POST")) {
                OutputStream outStream = con.getOutputStream();
                if (parameters != null && parameters.length() > 0)
                    outStream.write(parameters.getBytes(encoding));
                outStream.flush();
                outStream.close();
            }
            InputStream content = (InputStream)con.getInputStream();
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] outbuffer = new byte[1024];
            int length;
            while ((length = content.read(outbuffer)) != -1) {
                result.write(outbuffer, 0, length);
            }
            return result.toString(encoding);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
	
}
