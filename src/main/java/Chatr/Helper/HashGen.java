package Chatr.Helper;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashGen {
	public static void main(String[] args) {
		System.out.println(hash("test"));
	}
	
	public static String hash(String toHash) {
		try{
		byte[] msgToByt = toHash.getBytes("UTF-8");
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] hashByte = md.digest(msgToByt);
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < hashByte.length; i++){
			sb.append(Integer.toHexString((hashByte[i] & 0xFF) | 0x100).substring(1,3));
		}
		return sb.toString();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return "Error!";
		
	}
	
}
