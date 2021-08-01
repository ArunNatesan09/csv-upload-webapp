package com.csv.upload.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 
 * @author Arun Natesan
 * 
 * This util file
 *
 */
public class Util {

	private static final Charset UTF_8 = StandardCharsets.UTF_8;
	private static final String PASSWORD = "password";

	/**
	 * MD5 Encript the password
	 * 
	 * @param password
	 * @return
	 */
	public static String encriptPassword(String password) {

		MessageDigest md = null;
		byte[] encriptPwd1 = null;

		if (password != null && !"".equalsIgnoreCase(password)) {
			try {
				md = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				throw new IllegalArgumentException(e);
			}
			encriptPwd1 = md.digest(password.getBytes(UTF_8));
			return encriptPwd1.toString();
		} else {
			return "";
		}

	}

	/**
	 * Find the Password column index
	 * 
	 * @param header
	 * @return
	 */
	public static int passwordIndex(String[] header) {
		for (int i = 0; i < header.length; i++) {
			if (PASSWORD.equalsIgnoreCase(header[i])) {
				return i;
			}
		}
		return -1;
	}
}
