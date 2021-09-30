package com.fursys.mobilecm.config;

import org.springframework.beans.factory.annotation.Value;

public class ServerInfo {
	public static String SERVER_URL = "http://220.118.27.34:8082";

	public static String LOCALHOST = "http://localhost:8082";
	
	public static String AS_ATTACHED_FILE_PATH = "/ERP_FILE/ERP/A/";
	public static String SIGONG_ATTACHED_FILE_PATH = "/ERP_FILE/ERP/C/";
	public static String AS_SIGN_FILE_PATH = "/ERP_FILE/ERP/ASIGN/";
	public static String SIGONG_SIGN_FILE_PATH = "/ERP_FILE/ERP/CSIGN/";
	
	//public static String SIGN_FILE_PATH = "/data/www/mobilecm/_sign/";
	
	//http://220.118.27.34:8082/_sign/Mobilecm_Sign_20201014_144651506.jpg
}
