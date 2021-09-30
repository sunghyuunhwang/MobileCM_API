package com.fursys.mobilecm.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.security.crypto.password.PasswordEncoder;

public class FursysPasswordEncoder implements PasswordEncoder{

	@Override
	public String encode(CharSequence rawPassword) {
		String encPw = "";
		
		try {
			encPw = encrypt(rawPassword.toString());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return encPw;
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		System.out.println(encode(rawPassword));
		System.out.println(encodedPassword);
		return encodedPassword.equals(encode(rawPassword));
	}
	
	public String encrypt(String input) throws NoSuchAlgorithmException {
        String output = "";
        StringBuffer sb = new StringBuffer();
     
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(input.getBytes());
     
        byte[] msgb = md.digest();
     
        for (int i = 0; i < msgb.length; i++) {
            byte temp = msgb[i];
            String str = Integer.toHexString(temp & 0xFF);
            while (str.length() < 2) {
                str = "0" + str;
            }
            str = str.substring(str.length() - 2);
            sb.append(str);
     
        }
        output = sb.toString();
     
        return output.toUpperCase();
    }
}
