package com.minho.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CryptographyPasswordHash {
	
	public static String computePasswordHash(String aPassword, byte[] aSalt) {
		try {
			String source = aPassword;
			 
			MessageDigest md = null;
			md = MessageDigest.getInstance("SHA-512");
			byte[] salt = aSalt == null ? Cryptography.generateRandomSalt() : aSalt;
			md.update(source.getBytes());
			 
			byte[] hashValue = ByteUtils.copy(md.digest(), salt);
			
			return ByteUtils.toHexString(hashValue);
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}
	
	
	public static boolean verifyPassword(String aPassword, String aPasswordHash) {
		try {
			byte[] hashedPassword = ByteUtils.toBytesFromHexString(aPasswordHash);
			int hashSize = 512/8;
	
			if (hashedPassword.length < hashSize){
				return false;
			}
			byte[] salt = new byte[Cryptography.cMaxSaltSize];
			System.arraycopy(hashedPassword, hashSize, salt, 0, hashedPassword.length - hashSize);
			
			return (aPasswordHash.equals(computePasswordHash(aPassword, salt)));
		} catch(IllegalArgumentException e) {
			return false;
		}
	}	
}
