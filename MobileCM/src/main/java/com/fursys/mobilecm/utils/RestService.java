package com.fursys.mobilecm.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

import com.fursys.mobilecm.vo.BaseResponse;

public class RestService {
	public interface RestServiceCallBack {
		void onResult(String result);
	}
	
	public static void get(String strUrl,RestServiceCallBack restServiceCallBack) {
		try {
			URL url = new URL(strUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection(); 
			con.setConnectTimeout(5000); //������ ����Ǵ� Timeout �ð� ����
			con.setReadTimeout(50000); // InputStream �о� ���� Timeout �ð� ����
//			con.addRequestProperty("x-api-key", RestTestCommon.API_KEY); //key�� ����

			con.setRequestMethod("GET");
                       con.setDoOutput(false); 

			StringBuilder sb = new StringBuilder();
			if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
				BufferedReader br = new BufferedReader(
						new InputStreamReader(con.getInputStream(), "utf-8"));
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line).append("\n");
				}
				br.close();
				restServiceCallBack.onResult(sb.toString());
			} else {
				restServiceCallBack.onResult(con.getResponseMessage());
			}

		} catch (Exception e) {
			restServiceCallBack.onResult(e.toString());
		}
	}
	
	public static void post(String strUrl, String jsonMessage, RestServiceCallBack restServiceCallBack) {
		try {
			URL url = new URL(strUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setConnectTimeout(5000); //������ ����Ǵ� Timeout �ð� ����
			con.setReadTimeout(50000); // InputStream �о� ���� Timeout �ð� ����
//			con.addRequestProperty("x-api-key", RestTestCommon.API_KEY); //key�� ����

			con.setRequestMethod("POST");

                                     //json���� message�� �����ϰ��� �� �� 
			con.setRequestProperty("Content-Type", "application/json");
			con.setDoInput(true);
			con.setDoOutput(true); //POST �����͸� OutputStream���� �Ѱ� �ְڴٴ� ���� 
			con.setUseCaches(false);
			con.setDefaultUseCaches(false);

			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
			wr.write(jsonMessage); //json ������ message ���� 
			wr.flush();

			StringBuilder sb = new StringBuilder();
			if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
				//Stream�� ó������� �ϴ� �������� ����.
				BufferedReader br = new BufferedReader(
						new InputStreamReader(con.getInputStream(), "utf-8"));
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line).append("\n");
				}
				br.close();
				restServiceCallBack.onResult(sb.toString());
			} else {
				System.out.println(con.getResponseMessage());
				restServiceCallBack.onResult(con.getResponseMessage());
			}
		} catch (Exception e){
			restServiceCallBack.onResult(e.toString());
		}
	}
	
	public static void postAppKey(String strUrl, String jsonMessage, String appKey, RestServiceCallBack restServiceCallBack) {
		try {
			URL url = new URL(strUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setConnectTimeout(5000); //������ ����Ǵ� Timeout �ð� ����
			con.setReadTimeout(50000); // InputStream �о� ���� Timeout �ð� ����
			con.addRequestProperty("appKey", appKey); //key�� ����

			//con.setRequestProperty("Content-Type", "application/json; utf-8");
			con.setRequestProperty("Accept", "application/json");
			con.setRequestProperty("User-Agent", "Mozilla/5.0"); // https를 호출시 user-agent 필요
			con.setRequestProperty("charset", "UTF-8");

			con.setRequestMethod("POST");

                                     //json���� message�� �����ϰ��� �� �� 
			con.setRequestProperty("Content-Type", "application/json");
			
			con.setDoInput(true);
			con.setDoOutput(true); //POST �����͸� OutputStream���� �Ѱ� �ְڴٴ� ���� 
			con.setUseCaches(false);
			con.setDefaultUseCaches(false);

			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
			wr.write(jsonMessage); //json ������ message ���� 
			wr.flush();

			StringBuilder sb = new StringBuilder();
			if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
				//Stream�� ó������� �ϴ� �������� ����.
				BufferedReader br = new BufferedReader(
						new InputStreamReader(con.getInputStream(), "utf-8"));
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line).append("\n");
				}
				br.close();
				restServiceCallBack.onResult(sb.toString());
			} else {
				System.out.println("ResponseCode:" + con.getResponseCode() + ", ResponseMessage:" +  con.getResponseMessage());
				restServiceCallBack.onResult(con.getResponseMessage());
			}
		} catch (Exception e){
			restServiceCallBack.onResult(e.toString());
		}
	}
	
	public static String sendREST(String sendUrl, String jsonValue) throws IllegalStateException {
		String inputLine = null;
		StringBuffer outResult = new StringBuffer();
		
		try {
			URL url = new URL(sendUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept-Charset", "UTF-8");
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(5000);
			
			//POST일 경우
			//OutputStream os = conn.getOutputStream();
			//os.write(jsonValue.getBytes("UTF-8"));
			//os.flush();			
			
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			while ((inputLine = in.readLine()) != null) {
				outResult.append((inputLine));
			}
			
			conn.disconnect();
			
		} catch(Exception e) {
			
			e.printStackTrace();
		}
		
		return outResult.toString();
	}
	
	public static BaseResponse getHttpsConnection(String urlstr, String json) {
		HttpsURLConnection conn = null;
		BaseResponse result = new BaseResponse();  
		try {
			URL url = new URL(urlstr);
			conn = (HttpsURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("User-Agent", "Mozilla/5.0"); // https를 호출시 user-agent 필요
			conn.setRequestProperty("charset", "UTF-8");
			conn.setDoOutput(true);
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(10000);
			conn.setHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});
			
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(null, new TrustManager[]{
					new javax.net.ssl.X509TrustManager() {
						@Override
						public X509Certificate[] getAcceptedIssuers() {
							// TODO Auto-generated method stub
							return null;
						}
						@Override
						public void checkClientTrusted(X509Certificate[] arg0,
								String arg1) throws CertificateException {
							// TODO Auto-generated method stub							
						}
						@Override
						public void checkServerTrusted(X509Certificate[] arg0,
								String arg1) throws CertificateException {
							// TODO Auto-generated method stub
						}
					}
			}, null);
			conn.setSSLSocketFactory(context.getSocketFactory());
			
			System.out.println("url=" + urlstr);
			
			conn.connect();
			conn.setInstanceFollowRedirects(true);
			
			System.out.println("json=" + json);
			
			OutputStream wrl = conn.getOutputStream();
			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
			
			wrl.write(json.getBytes("UTF-8"));
			wrl.flush();
			wrl.close();
			
			final int responseCode = conn.getResponseCode();
			if(responseCode == 200) { // 정상 호출
				InputStream in = conn.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
				final StringBuffer buffer = new StringBuffer();
				String line = null;
				while((line = reader.readLine()) != null) {
					buffer.append(line);
				}
				
				reader.close();
				result.setResultCode("200");
				result.setResultMessage(buffer.toString());
				
				System.out.println("result=" + buffer.toString());
			} else {
				result.setResultCode("5001");
				result.setResultMessage("https 호출시 오류가 발생하였습니다.");
			}
		} catch(Exception e) {
			//e.printStackTrace();
			result.setResultCode("5001");
			result.setResultMessage(e.toString());
		} finally {
			if (conn != null){
				conn.disconnect();
			}
			return result;
		}
		
	}
}
