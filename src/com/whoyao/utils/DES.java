package com.whoyao.utils;

import android.annotation.SuppressLint;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.whoyao.AppException;
import com.whoyao.Const;
import com.whoyao.Const.Encryption;

public class DES {
	/** 加密 */
	public static final int ENCODE = 0;
	/** 解密 */
	public static final int DECODE = 1;

	private static Key key;
	private static String keyString = Encryption.DES_KEY;
	private static Cipher cipher;
//	static {
//		getInstance(keyString);
//	}


	@Deprecated
	public static String authcode(String content, int operation, String key) {

		String encontent = null;
		if (operation == ENCODE) {
			encontent = encrypt(content, key);
		} else if (operation == DECODE) {
			encontent = decrypt(content, key);
		}
		return encontent;
	}
	


	/**
	 * 进行base64加密操作
	 * @param text
	 * @param keyString
	 * @return String
	 */
	public static String encrypt(String text, String keyString) {
		String body = null;

		try {
			getInstance(keyString);
			byte[] b = encrypt(text.getBytes("UTF8"));
			body = new String(Base64.encodeBase64(b));
		} catch (Exception e) {

		}
		return body;
	}

	public static byte[] encrypt(byte b[]) throws InvalidKeyException,
			BadPaddingException, IllegalBlockSizeException,
			IllegalStateException {
		byte byteFina[] = null;
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byteFina = cipher.doFinal(b);
		return byteFina;
	}

	/**
	 * 进行base64进行解密
	 * @param content
	 * @param keyString
	 * @return String
	 */
	public static String decrypt(String content, String keyString) {
		String body = null;
		try {
			getInstance(keyString);
			byte[] b = Base64.decodeBase64(content.getBytes());
			body = new String(decrypt(b), "UTF8");
		} catch (Exception e) {
			AppException.handle(e);
		}
		return body;
	}

	public static byte[] decrypt(byte b[]) throws InvalidKeyException,
			BadPaddingException, IllegalBlockSizeException,
			IllegalStateException {
		byte byteFina[] = null;
		cipher.init(Cipher.DECRYPT_MODE, key);
		byteFina = cipher.doFinal(b);
		return byteFina;
	}

	/**
	 * 初始化Key和Cipher
	 * @param keyStr
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 */
	public static void getInstance(String keyStr) throws NoSuchPaddingException, NoSuchAlgorithmException {
		keyStr = keyStr.toUpperCase();
		boolean b1 = null == DES.key;
		boolean b2 = !keyString.equals(keyStr);
		if (null == DES.key || !keyString.equals(keyStr)){
			keyString = keyStr;
			byte keyByte[] = getKeyByStr(keyString);
			DES.key = new SecretKeySpec(keyByte, "DES");
		}
		if (null == DES.cipher){
			DES.cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		}
	}
	
	@SuppressLint("UseValueOf")
	public static byte[] getKeyByStr(String str) {
		byte bRet[] = new byte[str.length() / 2];
		for (int i = 0; i < str.length() / 2; i++) {
			Integer itg = new Integer(16 * getChrInt(str.charAt(2 * i))
					+ getChrInt(str.charAt(2 * i + 1)));
			bRet[i] = itg.byteValue();
		}
		return bRet;
	}

	@SuppressLint("UseValueOf")
	private static int getChrInt(char chr) {
		int iRet = 0;
		switch (chr) {
		case '0':
			iRet = 0;
			break;
		case '1':
			iRet = 1;
			break;
		case '2':
			iRet = 2;
			break;
		case '3':
			iRet = 3;
			break;
		case '4':
			iRet = 4;
			break;
		case '5':
			iRet = 5;
			break;
		case '6':
			iRet = 6;
			break;
		case '7':
			iRet = 7;
			break;
		case '8':
			iRet = 8;
			break;
		case '9':
			iRet = 9;
			break;
		case 'a':
		case 'A':
			iRet = 10;
			break;
		case 'b':
		case 'B':
			iRet = 11;
			break;
		case 'c':
		case 'C':
			iRet = 12;
			break;
		case 'd':
		case 'D':
			iRet = 13;
			break;
		case 'e':
		case 'E':
			iRet = 14;
			break;
		case 'f':
		case 'F':
			iRet = 15;
			break;
		default:
			break;
		}
		return iRet;
	}

}