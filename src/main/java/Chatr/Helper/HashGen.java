package Chatr.Helper;


import java.security.MessageDigest;


public class HashGen {

	/**
	 * This method gives either an ID in plaintext, or the ID as an MD5 hash.
	 * @param clear True if you want the ID in plaintext, false when you want it as hash.
	 * @return The ID as plaintext or as hash.
	 */
	public static String getID(boolean clear){
		String nano = String.valueOf(System.nanoTime());
		if(clear){
			return nano;
		}else{
			return hash(nano);
		}
		
	}
	
	/**
	 * This methot converts any String into an MD5 hash.
	 * @param toHash String to be hashed.
	 * @return The MD5 hash as String.
	 */
	public static String hash(String toHash) {
		try {
			byte[] msgToByt = toHash.getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] hashByte = md.digest(msgToByt);
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < hashByte.length; i++) {
				sb.append(Integer.toHexString((hashByte[i] & 0xFF) | 0x100).substring(1, 3));
			}
			return sb.toString();
		} catch (Exception e) {
			System.err.println("Error! Couldn't hash the String!");
		}

		return "";

	}

}
