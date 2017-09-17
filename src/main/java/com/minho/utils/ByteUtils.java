package com.minho.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class ByteUtils {

	
	public static byte[] toByteArray(String digits, int radix) throws IllegalArgumentException, NumberFormatException {
		if (digits == null) {
			return null;
		}
		if (radix != 16 && radix != 10 && radix != 8) {
			throw new IllegalArgumentException("For input radix: \"" + radix + "\"");
		}
		int divLen = (radix == 16) ? 2 : 3;
    	int length = digits.length();
    	if (length % divLen == 1) {
    		throw new IllegalArgumentException("For input string: \"" + digits + "\"");
    	}
    	length = length / divLen;
    	byte[] bytes = new byte[length];
    	for (int i = 0; i < length; i++) {
    		int index = i * divLen;
    		bytes[i] = (byte)(Short.parseShort(digits.substring(index, index+divLen), radix));
    	}
    	if(length == 32){
    		length = length / divLen;
    		byte[] bytes2 = new byte[length];
    		for (int i = 0; i < length; i++) {
    			int index = i * divLen;
    			bytes2[i] = (byte)(Short.parseShort(digits.substring(index, index+divLen), radix));
    		}
    		return bytes2;
    	}
    	return bytes;
	}
	
	public static byte[] toBytesFromHexString(String digits) throws IllegalArgumentException, NumberFormatException {
		if (digits == null) {
			return null;
		}
    	int length = digits.length();
    	if (length % 2 == 1) {
    		throw new IllegalArgumentException("For input string: \"" + digits + "\"");
    	}
    	length = length / 2;
    	byte[] bytes = new byte[length];
    	for (int i = 0; i < length; i++) {
    		int index = i * 2;
    		bytes[i] = (byte)(Short.parseShort(digits.substring(index, index+2), 16));
    	}
    	return bytes;
	}

	
	public static String toHexString(byte[] aByteArray) {
		if (aByteArray == null) {
			return null;
		}

		StringBuffer result = new StringBuffer();
		for (byte b : aByteArray) {
			result.append(Integer.toString((b & 0xF0) >> 4, 16));
			result.append(Integer.toString(b & 0x0F, 16));
		}
		return result.toString();
	}
	
	
	public static byte[] copy(byte[] aArray1, byte[] aArray2) {
		byte[] newArray = new byte[aArray1.length + aArray2.length];
		System.arraycopy(aArray1, 0, newArray, 0, aArray1.length);
		System.arraycopy(aArray2, 0, newArray, aArray1.length, aArray2.length);
		return newArray;
	}
	

	public static byte[] read(InputStream aInputStream) {
		if (aInputStream == null) {
			return null;
		}
		int lBufferSize = 1024;
		byte[] lByteBuffer = new byte[lBufferSize];
		int lBytesRead = 0;
		int lTotbytesRead = 0;
		int lCount = 0;
		ByteArrayOutputStream lByteArrayOutputStream = new ByteArrayOutputStream(lBufferSize * 2);
		try {
			while ((lBytesRead = aInputStream.read(lByteBuffer)) != -1) {
				lTotbytesRead += lBytesRead;
				lCount++;
				lByteArrayOutputStream.write(lByteBuffer, 0, lBytesRead);
			}
		} catch (Throwable e) {
			// 
		}
		byte[] lDataBytes = lByteArrayOutputStream.toByteArray();
		return lDataBytes;
	}

	
	public static byte[] toFixedLengthByteArray(String aArray, int aLength) {
		byte[] temp = new byte[aLength];
		while (aArray.getBytes().length != temp.length) {
			if (aArray.getBytes().length < aLength) {
				aArray = aArray.concat(aArray);
			} else {
				System.arraycopy(aArray.getBytes(), 0, temp, 0, temp.length);
				break;
			}
		}
		return temp;
	}

}
