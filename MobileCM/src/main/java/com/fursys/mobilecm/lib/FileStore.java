package com.fursys.mobilecm.lib;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.multipart.MultipartFile;

import com.fursys.mobilecm.mapper.TMSERPSchedulingMapper;
import com.fursys.mobilecm.vo.tmserp.TMSERPFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FileStore {
	
	@Value("${file.upload.directory}")
    private String basePath;
	
	@Autowired private PlatformTransactionManager txManager;
	@Autowired private TMSERPSchedulingMapper tmserpScheduling;
	
	public List<TMSERPFile> storeFiles(String attch_div_cd, String attch_file_id, List<MultipartFile> multipartFiles) throws Exception {
		
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		List<TMSERPFile> storeFileResult = new ArrayList<>();
		
		try {
			if(multipartFiles.isEmpty()) {
				txManager.rollback(status);
				return null;				
			}
			
			if(attch_file_id.isEmpty()) {	
				attch_file_id = tmserpScheduling.getAttchFileId(attch_div_cd);
				log.debug("attch_file_id : {}", attch_file_id);
			}
		
			for(MultipartFile multipartFile : multipartFiles) {
				if(!multipartFile.isEmpty()) {
					storeFileResult.add(storeFile(multipartFile, attch_file_id, attch_div_cd));
				}
			}
						
		} catch(Exception e){
			txManager.rollback(status);
			return null;
		}
		
		txManager.commit(status);
		return storeFileResult;
	}
	
	private TMSERPFile storeFile(MultipartFile multipartFile, String attch_file_id, String attch_div_cd) throws Exception {

		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		TMSERPFile file = new TMSERPFile();
		int res = 0;
		
		try {
			if (multipartFile.isEmpty()) {
				txManager.rollback(status);
				return null;
			}
			
			String real_attch_file_name = multipartFile.getOriginalFilename();
			String atchFilePath = makeNewFileName(real_attch_file_name, attch_div_cd);
			String atchFilePathOnly = "";
			String vtAtchFileNm = "";
			if(!atchFilePath.isEmpty()) {
				atchFilePathOnly = makeAtchFilePathOnly(atchFilePath);
				vtAtchFileNm = makevtAtchFileNm(atchFilePath);
				log.debug("atchFilePathOnly : {}", atchFilePathOnly);
				log.debug("vtAtchFileNm : {}", vtAtchFileNm);
			}
			
			if(!real_attch_file_name.isEmpty()) multipartFile.transferTo(new File(getFullPath(atchFilePath)));
			
			file.setAttch_div_cd(attch_div_cd);
			file.setAttch_file_id(attch_file_id);
			file.setAttch_file_path(atchFilePathOnly);
			file.setReal_attch_file_name(real_attch_file_name);
			file.setAttch_file_size(Long.toString(multipartFile.getSize()));
			file.setReal_attch_file_name(real_attch_file_name);
			file.setVirtual_attch_file_name(vtAtchFileNm);
			res = tmserpScheduling.insertFile(file);
			
			if(res < 1) {
	    		txManager.rollback(status);
	    		return null;			
			}
			
		} catch(Exception e) {
			txManager.rollback(status);
			return null;
		}
		
		txManager.commit(status);
		return file;
	}
	
	private String makevtAtchFileNm(String atchFilePath) {
		if (atchFilePath.indexOf("/") > -1) {
			return atchFilePath.substring(atchFilePath.lastIndexOf("/") + 1, atchFilePath.length());						
		} else {
			return atchFilePath.substring(atchFilePath.lastIndexOf("\\") + 1, atchFilePath.length());						
		}	
	}

	private String makeAtchFilePathOnly(String atchFilePath) {
		if (atchFilePath.indexOf("/") > -1) {
			return atchFilePath.substring(0, atchFilePath.lastIndexOf("/"));
		} else {
			return atchFilePath.substring(0, atchFilePath.lastIndexOf("\\"));
		}			
	}

	private String getFullPath(String virtual_attch_file_name) {
		return basePath + virtual_attch_file_name;
	}
	
	private String makeNewFileName(final String originFileName, final String strAttchDivCd) throws Exception{
		try{
			String fileExt;
			final NumberFormat numberformat = new DecimalFormat("000");
			
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS"); //SSS가 밀리세컨드 표시
	        Calendar calendar = Calendar.getInstance();
			String today =  dateFormat.format(calendar.getTime());
	
			if (originFileName.indexOf(".") > -1) {
				fileExt = originFileName.substring(originFileName.lastIndexOf(".") + 1);
			} else {
				fileExt = "";
			}
	
			String filePath = makeNewFolderName(originFileName, strAttchDivCd);
			if ("".equals(filePath)) {
				System.out.println("file path unrecognized");
				return "";
			}
	
			int serial = 1;
			String tempFileName;
			while (true) {
				tempFileName = today + numberformat.format(serial) + ("".equals(fileExt) ? "" : ("." + fileExt));
				File checkFile = new File(basePath + "/" + filePath + "/" + tempFileName);
				if (checkFile.exists()) {
					serial++;
				} else {
					break;
				}
			}
			return filePath + "/" + tempFileName;
		}catch(Exception e){
			throw new Exception();
		}
	}	
	
	private String makeNewFolderName(final String fileName, final String strAttchDivCd) throws Exception {
		try{
		String filePath = "";

		if ("".equals(strAttchDivCd)) {
			return "";
		}
		filePath = "/Scheduling" +strAttchDivCd+"/"+ getShortDateString();
		File checkFolder = new File(basePath + "/" + filePath);
		if (!checkFolder.exists() || checkFolder.isFile()) {
			checkFolder.mkdirs();
		}

		return filePath;
		
		}catch(Exception e){
			throw new Exception();
        }			
	}
	
	private String getShortDateString() {
		java.text.SimpleDateFormat formatter =
            new java.text.SimpleDateFormat ("yyyyMMdd", java.util.Locale.KOREA);
		return formatter.format(new java.util.Date());
	}
}
