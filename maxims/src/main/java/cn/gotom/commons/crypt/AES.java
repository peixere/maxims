package cn.gotom.commons.crypt;

import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import cn.gotom.commons.utils.TextUtils;

public class AES {
	private static final String ALGO = "AES";

	public static String encrypt(String data, String keyStr) throws Exception {
		Key key = new SecretKeySpec(keyStr.getBytes(), ALGO);
		Cipher c = Cipher.getInstance(ALGO);
		c.init(Cipher.ENCRYPT_MODE, key);
		byte[] encVal = c.doFinal(TextUtils.toBytes(data));
		String encryptedValue = Base64.getEncoder().encodeToString(encVal);
		return encryptedValue;
	}

	public static String decrypt(String base64Str, String keyStr) throws Exception {
		Key key = new SecretKeySpec(keyStr.getBytes(), ALGO);
		Cipher c = Cipher.getInstance(ALGO);
		c.init(Cipher.DECRYPT_MODE, key);
		byte[] encVal = c.doFinal(Base64.getDecoder().decode(base64Str));
		return TextUtils.toString(encVal);
	}

}
