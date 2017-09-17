package com.minho.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Cryptography {
	
	
	public static final int cMaxSaltSize = 8;


	public static byte[] generateRandomSalt() throws NoSuchAlgorithmException
	{
		int saltSize = cMaxSaltSize;

		SecureRandom csprng = null;
		csprng = SecureRandom.getInstance("SHA1PRNG");
		
		byte[] salt = new byte[saltSize];
		csprng.nextBytes(salt);

		return salt;
	}

	
}
